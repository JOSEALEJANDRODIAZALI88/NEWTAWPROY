package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/usuarios")
@Tag(name = "Usuarios (Admin)", description = "Operaciones administrativas sobre usuarios")
public class UserAdminController {

    private final IUserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserAdminController.class);

    @Autowired
    public UserAdminController(IUserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Listar todos los usuarios (activos e inactivos)")
    @GetMapping("/listar")
    public ResponseEntity<Page<UserDTO>> listarTodosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("[ADMIN][USUARIO] Listando todos los usuarios (activos e inactivos)");
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usuarios = userService.listarTodosUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Crear un nuevo usuario desde el panel de administrador")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente por el administrador")
    @PostMapping("/crear")
    public ResponseEntity<UserDTO> crearUsuarioComoAdmin(@Valid @RequestBody UserCreateAdminDTO userCreateAdminDTO) {
        logger.info("[ADMIN][USUARIO] Creando nuevo usuario con rol: {}", userCreateAdminDTO.getRol());
        UserDTO creado = userService.crearUsuarioComoAdmin(userCreateAdminDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @Operation(summary = "Obtener perfil de cualquier usuario por username")
    @GetMapping("/{username}")
    public ResponseEntity<UserDTO> obtenerPorUsername(@PathVariable String username) {
        logger.info("[ADMIN][USUARIO] Consultando perfil: {}", username);
        UserDTO usuario = userService.obtenerPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Actualizar datos de un usuario como administrador")
    @PutMapping("/{username}")
    public ResponseEntity<UserDTO> actualizarUsuarioComoAdmin(
            @PathVariable String username,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
        logger.info("[ADMIN][USUARIO] Actualizando usuario: {}", username);
        UserDTO actualizado = userService.actualizarUsuarioComoAdmin(username, userUpdateDTO);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Dar de baja a un usuario como administrador")
    @PutMapping("/{username}/baja")
    public ResponseEntity<UserDTO> eliminarUsuarioComoAdmin(@PathVariable String username) {
        logger.info("[ADMIN][USUARIO] Dando de baja a usuario: {}", username);
        UserDTO eliminado = userService.eliminarUsuarioComoAdmin(username);
        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Listar usuarios por rol")
    @GetMapping("/rol/{role}")
    public ResponseEntity<Page<UserDTO>> obtenerPorRol(
            @PathVariable String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("[ADMIN][USUARIO] Listando usuarios por rol: {}", role);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> usuarios = userService.obtenerPorRol(role, pageable);
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Buscar usuarios por nombre o email")
    @GetMapping("/buscar")
    public ResponseEntity<Page<UserDTO>> buscarPorNombreYCorreo(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("[ADMIN][USUARIO] Buscando usuarios con texto='{}', rol='{}'", texto);
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> resultados = userService.buscarPorNombreYCorreo(texto, pageable);
        return ResponseEntity.ok(resultados);
    }
}
