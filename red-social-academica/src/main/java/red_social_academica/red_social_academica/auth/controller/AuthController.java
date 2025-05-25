package red_social_academica.red_social_academica.auth.controller;

import red_social_academica.red_social_academica.auth.dto.AuthDTO.*;
import red_social_academica.red_social_academica.auth.model.Role;
import red_social_academica.red_social_academica.auth.model.Role.NombreRol;
import red_social_academica.red_social_academica.auth.repository.RoleRepository;
import red_social_academica.red_social_academica.auth.security.JwtUtils;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las operaciones de autenticación y registro de usuarios.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * Autenticación de usuario.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getNombre().name()) // Ej: "ROLE_ADMIN"
                .collect(Collectors.toSet());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        ));
    }

    /**
     * Registro de usuario.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username en uso."));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email ya registrado."));
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());
        user.setLastName(signUpRequest.getLastName());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByNombre(NombreRol.ROLE_PUBLIC)
                    .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
            roles.add(defaultRole);
        } else {
            for (String role : strRoles) {
                NombreRol nombreRol = switch (role.toLowerCase()) {
                    case "admin" -> NombreRol.ROLE_ADMIN;
                    default -> NombreRol.ROLE_PUBLIC;
                };
                Role rol = roleRepository.findByNombre(nombreRol)
                        .orElseThrow(() -> new RuntimeException("Error: Rol no encontrado."));
                roles.add(rol);
            }
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado correctamente"));
    }

    /**
     * Devuelve información de la sesión actual.
     */
    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = (String) auth.getPrincipal();  // ✅ CAMBIO HECHO AQUÍ
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Error: Usuario no encontrado."));

            Set<String> roles = user.getRoles().stream()
                    .map(r -> r.getNombre().name())
                    .collect(Collectors.toSet());

            return ResponseEntity.ok(new JwtResponse(
                    null, // no nuevo token
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    roles
            ));
        }
        return ResponseEntity.ok(new MessageResponse("No hay sesión activa"));
    }

    /**
     * Cierra la sesión del usuario actual (frontend local).
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Sesión cerrada"));
    }
}
