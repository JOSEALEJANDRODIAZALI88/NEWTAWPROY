package main.java.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "invitation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = {"sender", "receiver"})
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User receiver;

    @ManyToOne(optional = false)
    private User sender;

    public Invitation(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
        sender.getSendInvitations().add(this);
        receiver.getReceivedInvitations().add(this);
    }

    public void accept() {
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);
        sender.getAuxFriends().add(receiver);
        receiver.getAuxFriends().add(sender);
    }

    public void unlink() {
        sender.getSendInvitations().remove(this);
        receiver.getReceivedInvitations().remove(this);
        this.sender = null;
        this.receiver = null;
    }

    public boolean esDelUsuario(String email) {
        return sender.getEmail().equals(email) || receiver.getEmail().equals(email);
    }
}