package red_social_academica.red_social_academica.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import red_social_academica.red_social_academica.dto.invitation.InvitationCreateDTO;
import red_social_academica.red_social_academica.dto.invitation.InvitationDTO;
import red_social_academica.red_social_academica.model.Invitation;
import red_social_academica.red_social_academica.model.User;
import red_social_academica.red_social_academica.repository.InvitationRepository;
import red_social_academica.red_social_academica.repository.UserRepository;
import red_social_academica.red_social_academica.service.IInvitationService;
import red_social_academica.red_social_academica.validation.InvitationValidator;

@Service
public class InvitationServiceImpl implements IInvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvitationValidator invitationValidator;

    @Override
    @Transactional
    public InvitationDTO enviarInvitacion(String senderUsername, InvitationCreateDTO dto) {
        invitationValidator.validarEnvio(senderUsername, dto.getReceiverUsername());

        User sender = userRepository.findByUsernameAndActivoTrue(senderUsername)
                .orElseThrow(() -> new RuntimeException("Remitente no encontrado o inactivo"));

        User receiver = userRepository.findByUsernameAndActivoTrue(dto.getReceiverUsername())
                .orElseThrow(() -> new RuntimeException("Destinatario no encontrado o inactivo"));

        if (invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue(senderUsername,
                dto.getReceiverUsername())) {
            throw new IllegalStateException("Ya existe una invitacion activa entre estos usuarios");
        }

        Invitation invitation = Invitation.builder()
                .sender(sender)
                .receiver(receiver)
                .fechaAlta(LocalDate.now())
                .usuarioAlta(senderUsername)
                .activo(true)
                .build();

        return convertToDTO(invitationRepository.save(invitation));
    }

    @Override
    @Transactional
    public InvitationDTO aceptarInvitacion(Long invitationId, String receiverUsername) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .filter(Invitation::isActivo)
                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada o inactiva"));

        if (!invitation.getReceiver().getUsername().equals(receiverUsername)) {
            throw new SecurityException("No tienes permiso para aceptar esta invitacion");
        }

        invitation.accept();
        invitation.setActivo(false);
        invitation.setFechaBaja(LocalDate.now());
        invitation.setUsuarioBaja(receiverUsername);
        invitation.setMotivoBaja("aceptada");

        return convertToDTO(invitationRepository.save(invitation));
    }

    @Override
    @Transactional
    public InvitationDTO rechazarInvitacion(Long invitationId, String username) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .filter(Invitation::isActivo)
                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada o inactiva"));

        if (!invitation.esDelUsuario(username)) {
            throw new SecurityException("No tienes permiso para rechazar esta invitacion");
        }

        invitation.setActivo(false);
        invitation.setFechaBaja(LocalDate.now());
        invitation.setUsuarioBaja(username);
        invitation.setMotivoBaja("rechazada");

        return convertToDTO(invitationRepository.save(invitation));
    }

    @Override
    public List<InvitationDTO> obtenerInvitacionesEnviadas(String username) {
        return invitationRepository.findBySenderUsernameAndActivoTrue(username).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<InvitationDTO> obtenerInvitacionesRecibidas(String username) {
        return invitationRepository.findByReceiverUsernameAndActivoTrue(username).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<InvitationDTO> obtenerTodasInvitacionesActivas() {
        return invitationRepository.findAllByActivoTrue().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    @Transactional
    public InvitationDTO cancelarInvitacion(Long invitationId, String senderUsername) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .filter(Invitation::isActivo)
                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada o inactiva"));

        if (!invitation.getSender().getUsername().equals(senderUsername)) {
            throw new SecurityException("No tienes permiso para cancelar esta invitacion");
        }

        invitation.setActivo(false);
        invitation.setFechaBaja(LocalDate.now());
        invitation.setUsuarioBaja(senderUsername);
        invitation.setMotivoBaja("cancelada por el remitente");

        return convertToDTO(invitationRepository.save(invitation));
    }

    @Override
    public List<InvitationDTO> obtenerInvitacionesPendientesRecibidas(String username) {
        return invitationRepository.findByReceiverUsernameAndActivoTrue(username).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public InvitationDTO obtenerDetalleInvitacion(Long invitationId, String username, String role) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitacion no encontrada"));

        boolean esAdmin = "ROLE_ADMIN".equalsIgnoreCase(role);
        boolean esParticipante = invitation.getSender().getUsername().equals(username)
                || invitation.getReceiver().getUsername().equals(username);

        if (!esAdmin && !esParticipante) {
            throw new SecurityException("No tienes permisos para ver esta invitacion");
        }

        return convertToDTO(invitation);
    }

    @Override
    public boolean yaExisteInvitacion(String senderUsername, String receiverUsername) {
        return invitationRepository.existsBySenderUsernameAndReceiverUsernameAndActivoTrue(senderUsername,
                receiverUsername);
    }

    // ======================= MAPEOS =======================

    private InvitationDTO convertToDTO(Invitation inv) {
        return InvitationDTO.builder()
                .id(inv.getId())
                .senderId(inv.getSender().getId())
                .senderName(inv.getSender().getName() + " " + inv.getSender().getLastName())
                .receiverId(inv.getReceiver().getId())
                .receiverName(inv.getReceiver().getName() + " " + inv.getReceiver().getLastName())
                .fechaAlta(inv.getFechaAlta())
                .fechaModificacion(inv.getFechaModificacion())
                .fechaBaja(inv.getFechaBaja())
                .motivoBaja(inv.getMotivoBaja())
                .activo(inv.isActivo())
                .build();
    }

}
