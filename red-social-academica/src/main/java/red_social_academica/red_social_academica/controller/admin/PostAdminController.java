package red_social_academica.red_social_academica.controller.admin;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.service.IPostService;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/posts")
@Tag(name = "Publicaciones (Admin)", description = "Gestión de publicaciones como administrador")
public class PostAdminController {

    private final IPostService postService;

    @Autowired
    public PostAdminController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Actualizar cualquier publicación como administrador")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> actualizarPostComoAdmin(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDTO dto) {
        PostDTO actualizado = postService.actualizarPostComoAdmin(postId, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Operation(summary = "Eliminar cualquier publicación como administrador")
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostDTO> eliminarPostComoAdmin(
            @PathVariable Long postId,
            @RequestParam String motivo) {
        PostDTO eliminado = postService.eliminarPostComoAdmin(postId, motivo);
        return ResponseEntity.ok(eliminado);
    }

    @Operation(summary = "Obtener detalles de una publicación activa por ID")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> obtenerPostPorId(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.obtenerPorId(postId));
    }
}

