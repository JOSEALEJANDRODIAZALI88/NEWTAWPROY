package red_social_academica.red_social_academica.dto.notification;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO que representa una notificación enviada a un usuario.
 * Incluye el mensaje, estado de lectura, fecha de creación y destino del recurso relacionado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una notificación en el sistema")
public class NotificationDTO implements Serializable {

    @Schema(description = "Identificador único de la notificación", example = "301", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Mensaje de la notificación que se muestra al usuario", example = "Juan comentó en tu publicación.")
    @NotBlank(message = "El mensaje no puede estar vacío")
    @Size(max = 500, message = "El mensaje no debe superar los 500 caracteres")
    private String message;

    @Schema(description = "Indica si la notificación ha sido leída", example = "false")
    private boolean read;

    @Schema(description = "Fecha de creación de la notificación", example = "2025-05-24T21:10:00Z", accessMode = Schema.AccessMode.READ_ONLY)
    private Date createdAt;

    @Schema(description = "URL o ruta interna del recurso asociado a la notificación", example = "/posts/101")
    @Size(max = 255, message = "La URL destino no debe superar los 255 caracteres")
    private String targetUrl;

    @Schema(description = "ID del usuario que recibe la notificación", example = "15")
    private Long recipientId;

    @Schema(description = "Nombre del destinatario", example = "Ana Gómez")
    private String recipientName;
}