package red_social_academica.red_social_academica.service.impl;

import red_social_academica.red_social_academica.dto.user.*;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IUserService;
import red_social_academica.red_social_academica.validation.UserValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    @Override
    public UserDTO crearUsuario(UserCreateDTO dto) {
        userValidator.validarCreacion(dto);

        User user = mapFromCreateDTO(dto);
        user.setFechaAlta(LocalDate.now());
        user.setUsuarioAlta("admin");
        user.setActivo(true);

        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO actualizarPerfil(String username, UserUpdateDTO dto) {
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));

        updateEntityFromUpdateDTO(dto, user);
        user.setFechaModificacion(LocalDate.now());
        user.setUsuarioModificacion("admin");

        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserDTO obtenerPorUsername(String username) {
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o inactivo"));
        return convertToDTO(user);
    }

    @Override
    public Page<UserDTO> obtenerPorRol(String role, Pageable pageable) {
        return userRepository.findAllByRoleAndActivoTrue(role, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public Page<UserDTO> buscarPorNombreYCorreo(String texto, String role, Pageable pageable) {
        return userRepository.searchByEmailAndNameByRole(texto, role, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<UserDTO> obtenerAmigos(String username) {
        return userRepository.getFriendsOf(username).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserDTO> obtenerAmigos(String username, Pageable pageable) {
        return userRepository.getFriendsOf(username, pageable)
                .map(this::convertToDTO);
    }

    @Override
    public UserDTO eliminarUsuario(String username) {
        User user = userRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado o ya eliminado"));

        user.setActivo(false);
        user.setFechaBaja(LocalDate.now());
        user.setUsuarioBaja("admin");
        user.setMotivoBaja("solicitud del usuario");

        return convertToDTO(userRepository.save(user));
    }

    @Override
    public boolean existePorUsername(String username) {
        return userRepository.findByUsernameAndActivoTrue(username).isPresent();
    }

    @Override
    public boolean existePorEmail(String email) {
        return userRepository.findByEmailAndActivoTrue(email).isPresent();
    }

    // --------- Mapeos ---------

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .lastName(user.getLastName())
                .bio(user.getBio())
                .career(user.getCareer())
                .profilePictureUrl(user.getProfilePictureUrl())
                .birthdate(user.getBirthdate())
                .fechaAlta(user.getFechaAlta())
                .fechaModificacion(user.getFechaModificacion())
                .fechaBaja(user.getFechaBaja())
                .motivoBaja(user.getMotivoBaja())
                .activo(user.isActivo())
                .build();
    }

    private User mapFromCreateDTO(UserCreateDTO dto) {
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .career(dto.getCareer())
                .bio(dto.getBio())
                .birthdate(dto.getBirthdate())
                .profilePictureUrl(dto.getProfilePictureUrl())
                .build();
    }

    private void updateEntityFromUpdateDTO(UserUpdateDTO dto, User user) {
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setBio(dto.getBio());
        user.setCareer(dto.getCareer());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
    }
}