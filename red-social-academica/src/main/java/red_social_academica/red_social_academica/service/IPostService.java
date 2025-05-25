package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.post.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz para operaciones sobre publicaciones en la red social académica.
 * Aplica lógica de negocio con eliminación lógica y control de auditoría.
 */
public interface IPostService {

    /**
     * Crea una nueva publicación para un usuario activo.
     * 
     * @param username      nombre de usuario autor del post.
     * @param postCreateDTO datos de la nueva publicación.
     * @return DTO de la publicación creada.
     */
    PostDTO crearPost(String username, PostCreateDTO postCreateDTO);

    /**
     * Actualiza una publicación existente si está activa.
     * 
     * @param postId        identificador del post.
     * @param postUpdateDTO nuevos datos del post.
     * @return DTO del post actualizado.
     */
    PostDTO actualizarPost(Long postId, PostUpdateDTO postUpdateDTO);

    /**
     * Elimina lógicamente un post (marcar como inactivo con auditoría).
     * 
     * @param postId     identificador del post.
     * @param motivoBaja motivo de la eliminación.
     * @return DTO del post eliminado.
     */
    PostDTO eliminarPost(Long postId, String motivoBaja);

    /**
     * Obtiene una publicación activa por ID.
     * 
     * @param postId identificador del post.
     * @return DTO del post si está activo.
     */
    PostDTO obtenerPorId(Long postId);

    /**
     * Lista publicaciones activas de un usuario.
     * 
     * @param username nombre de usuario.
     * @param pageable información de paginación.
     * @return Página de publicaciones activas del usuario.
     */
    Page<PostDTO> obtenerPostsDeUsuario(String username, Pageable pageable);

    /**
     * Busca publicaciones activas por coincidencia en texto o título.
     * 
     * @param texto    texto a buscar.
     * @param pageable paginación.
     * @return Página de resultados de búsqueda.
     */
    Page<PostDTO> buscarPostsPorTexto(String texto, Pageable pageable);

    /**
     * Obtiene las 10 publicaciones más recientes activas.
     * 
     * @return Lista de las publicaciones más recientes.
     */
    List<PostDTO> obtenerTop10PublicacionesRecientes();

}