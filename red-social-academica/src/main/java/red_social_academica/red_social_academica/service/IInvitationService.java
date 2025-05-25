package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.invitation.*;

import java.util.List;

public interface IInvitationService {

    InvitationDTO enviarInvitacion(String senderUsername, InvitationCreateDTO dto);

    List<InvitationDTO> obtenerInvitacionesRecibidas(String username);

    List<InvitationDTO> obtenerInvitacionesEnviadas(String username);

    void aceptarInvitacion(Long invitationId);

    void rechazarInvitacion(Long invitationId);

    boolean yaExisteInvitacion(String senderUsername, String receiverUsername);
}