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
import static red_social_academica.red_social_academica.auth.security.AuthUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Publicaciones", description = "Operaciones públicas sobre publicaciones académicas")
public class PostController {

    private final IPostService postService;

    @Autowired
    public PostController(IPostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Crear una nueva publicación (usuario autenticado)")
    @PostMapping
    public ResponseEntity<PostDTO> crearPost(
            @Valid @RequestBody PostCreateDTO postCreateDTO) {
        String username = getCurrentUsername();
        PostDTO nueva = postService.crearPost(username, postCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una publicación propia")
    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> actualizarPost(
            @PathVariable Long postId,
            @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        PostDTO actualizada = postService.actualizarPostPropio(postId, postUpdateDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar lógicamente una publicación propia")
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostDTO> eliminarPost(
            @PathVariable Long postId,
            @RequestParam String motivo) {
        PostDTO eliminada = postService.eliminarPostPropio(postId, motivo);
        return ResponseEntity.ok(eliminada);
    }

    @Operation(summary = "Obtener detalles de una publicación activa por ID")
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

    @Operation(summary = "Buscar publicaciones activas por texto en título o contenido")
    @GetMapping("/buscar")
    public ResponseEntity<Page<PostDTO>> buscarPorTexto(
            @RequestParam String texto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(postService.buscarPostsPorTexto(texto, pageable));
    }

    @Operation(summary = "Obtener las 10 publicaciones activas más recientes")
    @GetMapping("/recientes")
    public ResponseEntity<List<PostDTO>> obtenerTop10Recientes() {
        return ResponseEntity.ok(postService.obtenerTop10PublicacionesRecientes());
    }
}
