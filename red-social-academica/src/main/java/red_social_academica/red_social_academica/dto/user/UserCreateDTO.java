package red_social_academica.red_social_academica.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

/**
 * DTO utilizado para el registro de nuevos usuarios en la red social académica.
 * Incluye validaciones para asegurar integridad y formato correcto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para registrar un nuevo usuario")
public class UserCreateDTO implements Serializable {

    @Schema(description = "Nombre del usuario", example = "Ana")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Gómez")
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String lastName;

    @Schema(description = "Nombre de usuario único", example = "ana_gomez")
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 30, message = "El nombre de usuario debe tener entre 3 y 30 caracteres")
    @Column(unique = true, nullable = false)
    private String username;

    @Schema(description = "Correo electrónico del usuario", example = "ana.gomez@universidad.edu")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Schema(description = "Contraseña para el acceso", example = "segura123")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    @Schema(description = "Confirmación de contraseña", example = "segura123")
    @NotBlank(message = "Debes confirmar tu contraseña")
    private String passwordConfirm;
}