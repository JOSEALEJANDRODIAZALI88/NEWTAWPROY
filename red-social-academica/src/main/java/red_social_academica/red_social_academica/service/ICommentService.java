package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.comment.*;

import java.util.List;

public interface ICommentService {

    CommentDTO crearComentario(String username, CommentCreateDTO commentCreateDTO);

    List<CommentDTO> obtenerComentariosDePost(Long postId);

    List<CommentDTO> obtenerComentariosDeUsuario(String username);

    void eliminarComentario(Long commentId);
}