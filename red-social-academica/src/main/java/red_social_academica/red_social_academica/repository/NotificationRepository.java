package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // Notificaciones para un usuario
    List<Notification> findByRecipientUsernameOrderByCreatedAtDesc(String username);

    // Contar no leídas
    long countByRecipientUsernameAndReadFalse(String username);

    //Listar las ultimas 10 notificaciones no leidas
    List<Notification> findTop10ByRecipientUsernameOrderByCreatedAtDesc(String username);

}