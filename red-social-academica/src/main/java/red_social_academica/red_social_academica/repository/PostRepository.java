package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Publicaciones por usuario
    Page<Post> findByUserUsername(String username, Pageable pageable);

    // Búsqueda por texto en título o contenido
    @Query("""
        SELECT p FROM Post p 
        WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :text, '%')) 
           OR LOWER(p.text) LIKE LOWER(CONCAT('%', :text, '%'))
        """)
    Page<Post> searchByTitleOrText(@Param("text") String text, Pageable pageable);

    // Últimos posts (ordenados por fecha)
    List<Post> findTop10ByOrderByDateDesc();
}