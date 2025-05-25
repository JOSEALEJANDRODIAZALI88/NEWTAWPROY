package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.comment.*;
import red_social_academica.red_social_academica.service.ICommentService;
import static red_social_academica.red_social_academica.auth.security.AuthUtils.getCurrentUsername;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comentarios", description = "Operaciones públicas sobre comentarios")
public class CommentController {

    private final ICommentService commentService;

    @Autowired
    public CommentController(ICommentService commentService) {
        this.commentService = commentService;
    }

    // === Crear ===
    @Operation(summary = "Crear un nuevo comentario")
    @PostMapping
    public ResponseEntity<CommentDTO> crearComentario(@Valid @RequestBody CommentCreateDTO dto) {
        String username = getCurrentUsername();
        CommentDTO nuevo = commentService.crearComentario(username, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    // === Leer ===
    @Operation(summary = "Obtener comentarios activos de un post")
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> obtenerComentariosDePost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.obtenerComentariosDePost(postId));
    }

    @Operation(summary = "Obtener comentarios activos de un usuario (sin paginación)")
    @GetMapping("/usuario/{username}")
    public ResponseEntity<List<CommentDTO>> obtenerComentariosDeUsuario(@PathVariable String username) {
        return ResponseEntity.ok(commentService.obtenerComentariosDeUsuario(username));
    }

    @Operation(summary = "Obtener comentarios activos de un usuario (paginado)")
    @GetMapping("/usuario/{username}/paginado")
    public ResponseEntity<Page<CommentDTO>> obtenerComentariosDeUsuarioPaginado(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commentService.obtenerComentariosDeUsuario(username, pageable));
    }

    // === Actualizar ===
    @Operation(summary = "Actualizar tu propio comentario")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> actualizarComentario(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO dto) {
        return ResponseEntity.ok(commentService.actualizarComentarioPropio(commentId, dto));
    }

    // === Eliminar ===
    @Operation(summary = "Eliminar lógicamente tu comentario")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDTO> eliminarComentario(
            @PathVariable Long commentId,
            @RequestParam String motivo) {
        return ResponseEntity.ok(commentService.eliminarComentarioPropio(commentId, motivo));
    }
}
