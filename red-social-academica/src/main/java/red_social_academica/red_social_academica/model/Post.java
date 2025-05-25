package main.java.red_social_academica.red_social_academica.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(of = {"id", "title", "type", "date"})
@EqualsAndHashCode(of = "id")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "post_type", nullable = false)
    private String type; // "TEXT", "EVENT", "RESOURCE"

    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @Column(name = "image_url")
    private String imageUrl;

    private Boolean containsImage;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

}