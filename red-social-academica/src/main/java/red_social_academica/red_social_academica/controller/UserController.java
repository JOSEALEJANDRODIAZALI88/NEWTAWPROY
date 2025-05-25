package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Operaciones para la gestión de usuarios")
public class UserController {

    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Registrar un nuevo usuario")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    @PostMapping
    public ResponseEntity<UserDTO> crearUsuario(
            @Valid @RequestBody UserCreateDTO userCreateDTO) {
        logger.info("[USUARIO] Creando usuario: {}", userCreateDTO.getUsername());
        UserDTO nuevoUsuario = userService.crearUsuario(userCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @Operation(summary = "Actualizar perfil del usuario autenticado")
    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> actualizarPerfil(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        logger.info("[USUARIO] Actualizando perfil de: {}", username);
        UserDTO actualizado = userService.actualizarPerfil(username, userUpdateDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Obtener perfil de un usuario")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> obtenerPorUsername(@PathVariable String username) {
        logger.info("[USUARIO] Consultando perfil de: {}", username);
        UserDTO usuario = userService.obtenerPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Eliminar lógicamente un usuario")
    @PutMapping("/{username}/baja")
    public ResponseEntity<UserDTO> eliminarUsuario(@PathVariable String username) {
        logger.info("[USUARIO] Dando de baja usuario: {}", username);
        UserDTO eliminado = userService.eliminarUsuario(username);
        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Listar usuarios por rol")
    @GetMapping("/rol/{role}")
    public ResponseEntity<Page<UserDTO>> obtenerPorRol(
            @PathVariable String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usuarios = userService.obtenerPorRol(role, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios por nombre o email")
    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> buscarPorNombreYCorreo(
            @RequestParam String texto,
            @RequestParam String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> resultado = userService.buscarPorNombreYCorreo(texto, role, pageable);
        return ResponseEntity.ok(resultado);
    }

    @Operation(summary = "Obtener lista de amigos de un usuario")
    @GetMapping("/{username}/amigos")
    public ResponseEntity<List<UserDTO>> obtenerAmigos(@PathVariable String username) {
        List<UserDTO> amigos = userService.obtenerAmigos(username);
        return ResponseEntity.ok(amigos);
    }

    @Operation(summary = "Obtener lista paginada de amigos")
    @GetMapping("/{username}/amigos/page")
    public ResponseEntity<Page<UserDTO>> obtenerAmigosPaginados(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> amigos = userService.obtenerAmigos(username, pageable);
        return ResponseEntity.ok(amigos);
    }
}
