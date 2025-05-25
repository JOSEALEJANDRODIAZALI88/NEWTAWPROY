package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.post.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPostService {

    PostDTO crearPost(String username, PostCreateDTO postCreateDTO);

    PostDTO actualizarPost(Long postId, PostUpdateDTO postUpdateDTO);

    void eliminarPost(Long postId);

    PostDTO obtenerPorId(Long postId);

    Page<PostDTO> obtenerPostsDeUsuario(String username, Pageable pageable);

    Page<PostDTO> buscarPostsPorTexto(String texto, Pageable pageable);
}