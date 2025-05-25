package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.notification.*;

import java.util.List;

public interface INotificationService {

    List<NotificationDTO> obtenerNotificacionesDeUsuario(String username);

    List<NotificationDTO> obtenerUltimasNotificaciones(String username);

    long contarNoLeidas(String username);

    void marcarComoLeida(Long notificationId);

    void eliminarNotificacion(Long notificationId);
}
