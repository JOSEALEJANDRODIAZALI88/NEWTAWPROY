package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.comment.CommentDTO;
import red_social_academica.red_social_academica.dto.comment.CommentUpdateDTO;
import red_social_academica.red_social_academica.service.ICommentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/comments")
@Tag(name = "Admin - Comentarios", description = "Gestión de comentarios como administrador")
public class CommentAdminController {

    private final ICommentService commentService;

    @Autowired
    public CommentAdminController(ICommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Actualizar cualquier comentario como admin")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> actualizarComentarioComoAdmin(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateDTO dto) {
        CommentDTO actualizado = commentService.actualizarComentarioComoAdmin(commentId, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cualquier comentario como admin")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentDTO> eliminarComentarioComoAdmin(
            @PathVariable Long commentId,
            @RequestParam String motivo) {
        CommentDTO eliminado = commentService.eliminarComentarioComoAdmin(commentId, motivo);
        return ResponseEntity.ok(eliminado);
    }
}
