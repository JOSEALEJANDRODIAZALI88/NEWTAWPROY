package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.service.IPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Publicaciones", description = "Operaciones sobre publicaciones académicas")
public class PostController {

    private final IPostService postService;

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Crear una nueva publicación")
    @ApiResponse(responseCode = "201", description = "Publicación creada exitosamente")
    @PostMapping("/{username}")
    public ResponseEntity<PostDTO> crearPost(
            @PathVariable String username,
            @Valid @RequestBody PostCreateDTO postCreateDTO) {
        PostDTO nueva = postService.crearPost(username, postCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una publicación")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> actualizarPost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        PostDTO actualizada = postService.actualizarPost(postId, postUpdateDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar lógicamente una publicación")
    @PutMapping("/{postId}/baja")
    public ResponseEntity<PostDTO> eliminarPost(
            @PathVariable Long postId,
            @RequestParam String motivo) {
        PostDTO eliminada = postService.eliminarPost(postId, motivo);
        return ResponseEntity.ok(eliminada);
    }

    @Operation(summary = "Obtener detalles de una publicación activa")
    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> obtenerPostPorId(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.obtenerPorId(postId));
    }

    @Operation(summary = "Listar publicaciones activas de un usuario")
    @GetMapping("/usuario/{username}")
    public ResponseEntity<Page<PostDTO>> obtenerPostsDeUsuario(
            @PathVariable String username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.obtenerPostsDeUsuario(username, pageable));
    }

    @Operation(summary = "Buscar publicaciones activas por texto")
    @GetMapping("/buscar")
    public ResponseEntity<Page<PostDTO>> buscarPorTexto(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.buscarPostsPorTexto(texto, pageable));
    }

    @Operation(summary = "Obtener las 10 publicaciones más recientes")
    @GetMapping("/recientes")
    public ResponseEntity<List<PostDTO>> obtenerTop10Recientes() {
        return ResponseEntity.ok(postService.obtenerTop10PublicacionesRecientes());
    }
}