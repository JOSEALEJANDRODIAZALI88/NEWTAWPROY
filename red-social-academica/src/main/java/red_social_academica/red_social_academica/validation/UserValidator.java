package red_social_academica.red_social_academica.validation;

import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.dto.user.UserCreateDTO;
import red_social_academica.red_social_academica.repository.UserRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validarCreacion(UserCreateDTO user) {
        validarEmailUnico(user.getEmail());
        validarUsernameUnico(user.getUsername());
        validarRuUnico(user.getRu());
        validarDominioEmail(user.getEmail());
        validarNombre(user.getName());
        validarApellido(user.getLastName());
    }

    public void validarEmailUnico(String email) {
        if (userRepository.existsByEmailAndActivoTrue(email)) {
            throw new BusinessException("Ya existe un usuario activo con este email");
        }
    }

    public void validarUsernameUnico(String username) {
        if (userRepository.existsByUsernameAndActivoTrue(username)) {
            throw new BusinessException("Ya existe un usuario activo con este nombre de usuario");
        }
    }

    public void validarRuUnico(String ru) {
        if (userRepository.existsByRuAndActivoTrue(ru)) {
            throw new BusinessException("Ya existe un usuario activo con este Registro Universitario");
        }
    }

    public void validarDominioEmail(String email) {
        String dominio = email.substring(email.indexOf('@') + 1);
        List<String> dominiosBloqueados = Arrays.asList("spam.com", "bloqueado.edu", "correo.com");
        if (dominiosBloqueados.contains(dominio)) {
            throw new BusinessException("El dominio del correo no está permitido");
        }
    }

    public void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new BusinessException("El nombre no puede estar vacío");
        }
    }

    public void validarApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new BusinessException("El apellido no puede estar vacío");
        }
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String mensaje) {
            super(mensaje);
        }
    }
}
