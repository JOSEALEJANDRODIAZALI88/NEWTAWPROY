package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.comment.*;
import red_social_academica.red_social_academica.model.Comment;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.CommentRepository;
import red_social_academica.red_social_academica.repository.PostRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.ICommentService;
import red_social_academica.red_social_academica.validation.CommentValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentValidator commentValidator;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
            UserRepository userRepository, CommentValidator commentValidator) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentValidator = commentValidator;
    }

    @Override
    @Transactional
    public CommentDTO crearComentario(String username, CommentCreateDTO dto) {
        commentValidator.validarCreacion(dto);

        User author = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        Post post = postRepository.findByIdAndActivoTrue(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));

        Comment comment = mapFromCreateDTO(dto, author, post);

        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentDTO actualizarComentario(Long commentId, String username, CommentUpdateDTO dto) {
        Comment comment = commentRepository.findByIdAndActivoTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado o inactivo"));

        boolean esAdmin = isAdmin();
        if (!esAdmin && !comment.getAuthor().getUsername().equals(username)) {
            throw new SecurityException("No puedes editar este comentario");
        }

        updateEntityFromUpdateDTO(dto, comment);
        comment.setFechaModificacion(LocalDate.now());
        comment.setUsuarioModificacion(username);

        return convertToDTO(commentRepository.save(comment));
    }

    @Override
    public List<CommentDTO> obtenerComentariosDePost(Long postId) {
        return commentRepository.findByPostIdAndActivoTrueOrderByCreatedAtAsc(postId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<CommentDTO> obtenerComentariosDeUsuario(String username) {
        return commentRepository.findByAuthorUsernameAndActivoTrue(username).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public Page<CommentDTO> obtenerComentariosDeUsuario(String username, Pageable pageable) {
        return commentRepository.findByAuthorUsernameAndActivoTrue(username, pageable)
                .map(this::convertToDTO);
    }

    @Override
    @Transactional
    public CommentDTO eliminarComentario(Long commentId, String motivoBaja, String username, String role) {
        Comment comment = commentRepository.findByIdAndActivoTrue(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado o ya eliminado"));

        boolean esAdmin = "ROLE_ADMIN".equalsIgnoreCase(role);
        boolean esAutor = comment.getAuthor().getUsername().equals(username);

        if (!esAdmin && !esAutor) {
            throw new SecurityException("No tienes permisos para eliminar este comentario");
        }

        comment.setActivo(false);
        comment.setFechaBaja(LocalDate.now());
        comment.setUsuarioBaja(username);
        comment.setMotivoBaja(motivoBaja);

        return convertToDTO(commentRepository.save(comment));
    }

    // --------- Mapeos ---------

    private CommentDTO convertToDTO(Comment comment) {
        String fullName = comment.getAuthor().getName() + " " + comment.getAuthor().getLastName();
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorFullName(fullName)
                .postId(comment.getPost().getId())
                .fechaAlta(comment.getFechaAlta())
                .fechaModificacion(comment.getFechaModificacion())
                .fechaBaja(comment.getFechaBaja())
                .motivoBaja(comment.getMotivoBaja())
                .activo(comment.isActivo())
                .build();
    }
    // --------- Mapeos DTO a Entidad ---------

    private Comment mapFromCreateDTO(CommentCreateDTO dto, User author, Post post) {
        return Comment.builder()
                .content(dto.getContent())
                .createdAt(new Date())
                .fechaAlta(LocalDate.now())
                .usuarioAlta(author.getUsername())
                .activo(true)
                .author(author)
                .post(post)
                .build();
    }

    private void updateEntityFromUpdateDTO(CommentUpdateDTO dto, Comment comment) {
        comment.setContent(dto.getContent());
        comment.setFechaModificacion(LocalDate.now());
        comment.setUsuarioModificacion("admin");
    }

    private boolean isAdmin() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

}
