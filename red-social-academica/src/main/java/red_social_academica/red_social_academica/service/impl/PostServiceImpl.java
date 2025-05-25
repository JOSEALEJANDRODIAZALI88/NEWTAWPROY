package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.post.*;
import red_social_academica.red_social_academica.model.Post;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.PostRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IPostService;
import red_social_academica.red_social_academica.validation.PostValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class PostServiceImpl implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostValidator postValidator;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PostDTO crearPost(String username, PostCreateDTO dto) {
        postValidator.validarCreacion(dto);
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        Post post = mapFromCreateDTO(dto, user);
        return convertToDTO(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDTO actualizarPost(Long postId, PostUpdateDTO dto) {
        postValidator.validarActualizacion(dto);
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));

        updateEntityFromUpdateDTO(dto, post);
        post.setFechaModificacion(LocalDate.now());
        post.setUsuarioModificacion("admin");

        return convertToDTO(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostDTO eliminarPost(Long postId, String motivoBaja) {
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o ya eliminada"));

        post.setActivo(false);
        post.setFechaBaja(LocalDate.now());
        post.setUsuarioBaja("admin");
        post.setMotivoBaja(motivoBaja);

        return convertToDTO(postRepository.save(post));
    }

    @Override
    public PostDTO obtenerPorId(Long postId) {
        Post post = postRepository.findByIdAndActivoTrue(postId)
                .orElseThrow(() -> new RuntimeException("Publicacion no encontrada o inactiva"));
        return convertToDTO(post);
    }

    @Override
    public Page<PostDTO> obtenerPostsDeUsuario(String username, Pageable pageable) {
        return postRepository.findByUserUsernameAndActivoTrue(username, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<PostDTO> buscarPostsPorTexto(String texto, Pageable pageable) {
        return postRepository.searchByTitleOrText(texto, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<PostDTO> obtenerTop10PublicacionesRecientes() {
        return postRepository.findTop10ByActivoTrueOrderByDateDesc().stream()
                .map(this::convertToDTO)
                .toList();
    }

    // --------- Mapeos ---------

    private PostDTO convertToDTO(Post post) {
        String fullName = post.getUser().getName() + " " + post.getUser().getLastName();
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .text(post.getText())
                .imageUrl(post.getImageUrl())
                .resourceUrl(post.getResourceUrl())
                .eventDate(post.getEventDate())
                .date(post.getDate())
                .userId(post.getUser().getId())
                .authorFullName(fullName)
                .fechaAlta(post.getFechaAlta())
                .fechaModificacion(post.getFechaModificacion())
                .fechaBaja(post.getFechaBaja())
                .motivoBaja(post.getMotivoBaja())
                .activo(post.isActivo())
                .build();
    }

    private Post mapFromCreateDTO(PostCreateDTO dto, User user) {
        return Post.builder()
                .title(dto.getTitle())
                .text(dto.getText())
                .imageUrl(dto.getImageUrl())
                .resourceUrl(dto.getResourceUrl())
                .eventDate(dto.getEventDate())
                .date(new Date())
                .fechaAlta(LocalDate.now())
                .usuarioAlta(user.getUsername())
                .activo(true)
                .user(user)
                .build();
    }

    private void updateEntityFromUpdateDTO(PostUpdateDTO dto, Post post) {
        if (dto.getTitle() != null)
            post.setTitle(dto.getTitle());
        if (dto.getText() != null)
            post.setText(dto.getText());
        if (dto.getImageUrl() != null)
            post.setImageUrl(dto.getImageUrl());
        if (dto.getResourceUrl() != null)
            post.setResourceUrl(dto.getResourceUrl());
        if (dto.getEventDate() != null)
            post.setEventDate(dto.getEventDate());
    }
}