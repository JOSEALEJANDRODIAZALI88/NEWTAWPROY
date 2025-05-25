package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Comentarios por post
    List<Comment> findByPostIdOrderByCreatedAtAsc(Long postId);

    // Comentarios por autor
    List<Comment> findByAuthorUsername(String username);
}