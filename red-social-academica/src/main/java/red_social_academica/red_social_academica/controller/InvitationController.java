package red_social_academica.red_social_academica.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import red_social_academica.red_social_academica.dto.invitation.*;
import red_social_academica.red_social_academica.service.IInvitationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/invitaciones")
@Tag(name = "Invitaciones", description = "Gestión de solicitudes de amistad entre usuarios")
public class InvitationController {

    private final IInvitationService invitationService;

    @Autowired
    public InvitationController(IInvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Operation(summary = "Enviar una invitación de amistad")
    @PostMapping("/{senderUsername}")
    public ResponseEntity<InvitationDTO> enviarInvitacion(
            @PathVariable String senderUsername,
            @Valid @RequestBody InvitationCreateDTO dto) {
        InvitationDTO nueva = invitationService.enviarInvitacion(senderUsername, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Aceptar una invitación")
    @PutMapping("/{invitationId}/aceptar")
    public ResponseEntity<InvitationDTO> aceptarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String receiverUsername) {
        return ResponseEntity.ok(invitationService.aceptarInvitacion(invitationId, receiverUsername));
    }

    @Operation(summary = "Rechazar una invitación")
    @PutMapping("/{invitationId}/rechazar")
    public ResponseEntity<InvitationDTO> rechazarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String username) {
        return ResponseEntity.ok(invitationService.rechazarInvitacion(invitationId, username));
    }

    @Operation(summary = "Obtener invitaciones recibidas por un usuario")
    @GetMapping("/recibidas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerRecibidas(@PathVariable String username) {
        return ResponseEntity.ok(invitationService.obtenerInvitacionesRecibidas(username));
    }

    @Operation(summary = "Obtener invitaciones enviadas por un usuario")
    @GetMapping("/enviadas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerEnviadas(@PathVariable String username) {
        return ResponseEntity.ok(invitationService.obtenerInvitacionesEnviadas(username));
    }

    @Operation(summary = "Cancelar una invitación enviada (por el remitente)")
    @PutMapping("/{invitationId}/cancelar")
    public ResponseEntity<InvitationDTO> cancelarInvitacion(
            @PathVariable Long invitationId,
            @RequestHeader("username") String senderUsername) {
        return ResponseEntity.ok(invitationService.cancelarInvitacion(invitationId, senderUsername));
    }

    @Operation(summary = "Obtener invitaciones pendientes recibidas (aún no aceptadas/rechazadas)")
    @GetMapping("/pendientes/recibidas/{username}")
    public ResponseEntity<List<InvitationDTO>> obtenerPendientesRecibidas(
            @PathVariable String username) {
        return ResponseEntity.ok(invitationService.obtenerInvitacionesPendientesRecibidas(username));
    }

    @Operation(summary = "Obtener todas las invitaciones activas (admin)")
    @GetMapping("/activas")
    public ResponseEntity<List<InvitationDTO>> obtenerTodasActivas(
            @RequestHeader("role") String role) {
        if (!"ROLE_ADMIN".equalsIgnoreCase(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(invitationService.obtenerTodasInvitacionesActivas());
    }
}
