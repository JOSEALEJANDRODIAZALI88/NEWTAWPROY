package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.comment.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {

    // === Crear ===

    /**
     * Crea un nuevo comentario en una publicación activa.
     * @param username autor del comentario
     * @param commentCreateDTO contenido del comentario y post destino
     * @return DTO del comentario creado
     */
    CommentDTO crearComentario(String username, CommentCreateDTO commentCreateDTO);

    // === Leer ===

    /**
     * Obtiene los comentarios activos de una publicación (ordenados por fecha).
     * @param postId ID de la publicación
     * @return Lista de comentarios activos
     */
    List<CommentDTO> obtenerComentariosDePost(Long postId);

    /**
     * Obtiene los comentarios activos realizados por un usuario (sin paginar).
     * @param username nombre de usuario
     * @return Lista de comentarios activos del usuario
     */
    List<CommentDTO> obtenerComentariosDeUsuario(String username);

    /**
     * Obtiene los comentarios activos realizados por un usuario (paginados).
     * @param username nombre del autor
     * @param pageable configuración de paginación
     * @return Página de comentarios activos
     */
    Page<CommentDTO> obtenerComentariosDeUsuario(String username, Pageable pageable);

    // === Actualizar ===

    /**
     * Actualiza el contenido de un comentario si es del autor o el usuario es admin.
     * @param commentId ID del comentario
     * @param username usuario solicitante
     * @param commentUpdateDTO contenido actualizado
     * @return Comentario actualizado
     */
    CommentDTO actualizarComentario(Long commentId, String username, CommentUpdateDTO commentUpdateDTO);

    // === Eliminar ===

    /**
     * Elimina lógicamente un comentario (auditoría).
     * @param commentId ID del comentario a eliminar
     * @param motivoBaja razón de eliminación
     * @param username nombre del usuarioq ue esta eliminando
     * @param role role del usuario que esta eliminando
     * @return Comentario eliminado
     */
    CommentDTO eliminarComentario(Long commentId, String motivoBaja, String username, String role);
}