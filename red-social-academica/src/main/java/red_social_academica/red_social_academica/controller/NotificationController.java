package red_social_academica.red_social_academica.controller;

import red_social_academica.red_social_academica.dto.notification.NotificationDTO;
import red_social_academica.red_social_academica.service.INotificationService;
import red_social_academica.red_social_academica.auth.security.AuthUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del usuario")
public class NotificationController {

    private final INotificationService notificationService;

    @Autowired
    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "Listar todas las notificaciones activas del usuario autenticado")
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> obtenerNotificaciones() {
        String username = AuthUtils.getCurrentUsername();
        return ResponseEntity.ok(notificationService.obtenerTodas(username));
    }

    @Operation(summary = "Obtener las 10 notificaciones no leídas más recientes")
    @GetMapping("/no-leidas/top")
    public ResponseEntity<List<NotificationDTO>> ultimas10NoLeidas() {
        String username = AuthUtils.getCurrentUsername();
        return ResponseEntity.ok(notificationService.obtenerUltimasNoLeidas(username));
    }

    @Operation(summary = "Contar notificaciones no leídas")
    @GetMapping("/no-leidas/count")
    public ResponseEntity<Long> contarNoLeidas() {
        String username = AuthUtils.getCurrentUsername();
        return ResponseEntity.ok(notificationService.contarNoLeidas(username));
    }

    @Operation(summary = "Marcar una notificación como leída")
    @PutMapping("/{id}/leer")
    public ResponseEntity<NotificationDTO> marcarComoLeida(@PathVariable Long id) {
        String username = AuthUtils.getCurrentUsername();
        return ResponseEntity.ok(notificationService.marcarComoLeida(id, username));
    }

    @Operation(summary = "Eliminar una notificación (lógicamente)")
    @PutMapping("/{id}/baja")
    public ResponseEntity<NotificationDTO> eliminarNotificacion(@PathVariable Long id) {
        String username = AuthUtils.getCurrentUsername();
        return ResponseEntity.ok(notificationService.eliminarNotificacion(id, username));
    }

}
