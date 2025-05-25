package main.java.red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import main.java.red_social_academica.model.Invitation;

import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "email")
@ToString(of = {"id", "email", "name", "lastName", "role"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private String passwordConfirm;

    private String role;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(length = 300)
    private String bio;

    private String career;

    private LocalDate birthdate;

    // Relaciones
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "friends",
            joinColumns = @JoinColumn(name = "friend_id"),
            inverseJoinColumns = @JoinColumn(name = "aux_friend_id"))
    private Set<User> friends = new HashSet<>();

    @ManyToMany(mappedBy = "friends")
    private Set<User> auxFriends = new HashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> receivedInvitations = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> sendedInvitations = new HashSet<>();

    public String getFullName() {
        return this.name + " " + this.lastName;
    }

    public void borrarAmigos() {
        User[] amigos = friends.toArray(new User[0]);
        for (User amigo : amigos) {
            this.friends.remove(amigo);
            this.auxFriends.remove(amigo);
            amigo.getFriends().remove(this);
            amigo.getAuxFriends().remove(this);
        }
    }

    public boolean canInvite(String email) {
        if (this.email.equals(email)) return false;

        return friends.stream().noneMatch(f -> f.getEmail().equals(email)) &&
               auxFriends.stream().noneMatch(f -> f.getEmail().equals(email)) &&
               sendedInvitations.stream().noneMatch(i -> i.esDelUsuario(email)) &&
               receivedInvitations.stream().noneMatch(i -> i.esDelUsuario(email));
    }

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();


}