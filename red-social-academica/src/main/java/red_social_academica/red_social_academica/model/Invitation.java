package red_social_academica.red_social_academica.model;

import red_social_academica.red_social_academica.model.AuditableEntity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Entidad JPA que representa una invitación de amistad entre dos usuarios.
 * Permite establecer relaciones sociales en la plataforma y gestionar
 * solicitudes pendientes, aceptadas o eliminadas.
 */


@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true, of = {"sender", "receiver"})
public class Invitation extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User receiver;

    @ManyToOne(optional = false)
    private User sender;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    private boolean activo = true;

    public Invitation(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        sender.getSendedInvitations().add(this);
        receiver.getReceivedInvitations().add(this);
    }

    public void accept() {
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);
        sender.getAuxFriends().add(receiver);
        receiver.getAuxFriends().add(sender);
    }

    public void unlink() {
        sender.getSendedInvitations().remove(this);
        receiver.getReceivedInvitations().remove(this);
        this.sender = null;
        this.receiver = null;
    }

    public boolean esDelUsuario(String username) {
        return sender.getUsername().equals(username) || receiver.getUsername().equals(username);
    }
}
