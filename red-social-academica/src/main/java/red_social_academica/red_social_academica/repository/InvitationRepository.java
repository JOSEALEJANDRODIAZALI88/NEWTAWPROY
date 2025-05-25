package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    // Invitaciones enviadas por username
    List<Invitation> findBySenderUsername(String username);

    // Invitaciones recibidas por username
    List<Invitation> findByReceiverUsername(String username);

    // Verificar si ya existe una invitación entre dos usuarios
    boolean existsBySenderUsernameAndReceiverUsername(String sender, String receiver);
}