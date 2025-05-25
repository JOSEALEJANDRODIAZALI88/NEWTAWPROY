package red_social_academica.red_social_academica.repository;

import red_social_academica.red_social_academica.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Buscar por username (identificador principal)
    Optional<User> findByUsername(String username);

    // Buscar por email (usado internamente en registro/login)
    Optional<User> findByEmail(String email);

    // Verificar existencia
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Listar usuarios por rol (ADMIN, USER, etc.)
    List<User> findAllByRole(String role);
    Page<User> findAllByRole(String role, Pageable pageable);

    // Buscar usuarios por nombre o email que contengan texto
    @Query("""
        SELECT u FROM User u 
        WHERE u.role = :role AND 
              (LOWER(CONCAT(u.name, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :searchText, '%')) 
              OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchText, '%')))
        """)
    Page<User> searchByEmailAndNameByRole(@Param("searchText") String searchText,
                                          @Param("role") String role,
                                          Pageable pageable);
    
    //Buscar usuarios por carrera
    @Query("""
    SELECT u FROM User u 
    WHERE (:career IS NULL OR u.career = :career) 
        AND (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) 
            OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    Page<User> searchByCareerAndName(@Param("career") String career,
                                    @Param("search") String search,
                                    Pageable pageable);

    
    // Obtener amigos por username (relación ManyToMany)
    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.username = :username")
    Page<User> getFriendsOf(@Param("username") String username, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.friends f WHERE f.username = :username")
    List<User> getFriendsOf(@Param("username") String username);

    // Bloqueo pesimista opcional para ediciones seguras
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<User> findById(Long id);
}