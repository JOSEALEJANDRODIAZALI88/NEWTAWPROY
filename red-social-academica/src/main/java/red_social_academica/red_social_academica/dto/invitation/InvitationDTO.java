package red_social_academica.red_social_academica.dto.invitation;

import io.swagger.v3.oas.annotations.media.Schema;
//import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

/**
 * DTO que representa una invitación de amistad entre dos usuarios.
 * Incluye información básica del remitente y destinatario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una invitación de amistad")
public class InvitationDTO implements Serializable {

    @Schema(description = "ID único de la invitación", example = "200", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del usuario que envía la invitación", example = "10")
    private Long senderId;

    @Schema(description = "Nombre del remitente", example = "Carlos Ramírez")
    private String senderName;

    @Schema(description = "ID del usuario que recibe la invitación", example = "25")
    private Long receiverId;

    @Schema(description = "Nombre del destinatario", example = "Ana Gómez")
    private String receiverName;
}