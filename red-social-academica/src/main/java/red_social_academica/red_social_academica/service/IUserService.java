package red_social_academica.red_social_academica.service;

import red_social_academica.red_social_academica.dto.user.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz para la gestión de usuarios en la red social académica.
 * Define las operaciones principales de acceso, modificación y búsqueda de perfiles.
 */
public interface IUserService {

    /**
     * Registra un nuevo usuario.
     * @param userCreateDTO datos del nuevo usuario.
     * @return DTO del usuario creado.
     */
    UserDTO crearUsuario(UserCreateDTO userCreateDTO);

    /**
     * Actualiza el perfil del usuario autenticado.
     * @param username nombre de usuario (clave).
     * @param userUpdateDTO nuevos datos del perfil.
     * @return Usuario actualizado.
     */
    UserDTO actualizarPerfil(String username, UserUpdateDTO userUpdateDTO);

    /**
     * Devuelve los datos de perfil de un usuario por username.
     * @param username nombre de usuario.
     * @return DTO con datos del usuario.
     */
    UserDTO obtenerPorUsername(String username);

    /**
     * Obtiene una lista paginada de usuarios por rol.
     * @param role tipo de rol (ej: ROLE_PUBLIC, ROLE_ADMIN)
     * @param pageable paginación
     * @return Página de usuarios.
     */
    Page<UserDTO> obtenerPorRol(String role, Pageable pageable);

    /**
     * Busca usuarios por texto en nombre completo o email, filtrado por rol.
     * @param texto texto de búsqueda.
     * @param role rol de usuario (ej. ROLE_PUBLIC)
     * @param pageable paginación
     * @return Página de resultados.
     */
    Page<UserDTO> buscarPorNombreYCorreo(String texto, String role, Pageable pageable);

    /**
     * Obtiene la lista de amigos de un usuario.
     * @param username del usuario.
     * @return Lista de amigos.
     */
    List<UserDTO> obtenerAmigos(String username);

    /**
     * Obtiene la lista paginada de amigos de un usuario.
     * @param username del usuario.
     * @param pageable paginación
     * @return Página de amigos.
     */
    Page<UserDTO> obtenerAmigos(String username, Pageable pageable);

    /**
     * Elimina un usuario y sus relaciones de amistad.
     * @param username nombre de usuario a eliminar.
     * @return DTO del usuario eliminado.
     */
    UserDTO eliminarUsuario(String username);

    /**
     * Verifica si un usuario existe por su username.
     * @param username nombre de usuario
     * @return true si existe
     */
    boolean existePorUsername(String username);

    /**
     * Verifica si un usuario existe por su email.
     * @param email del usuario
     * @return true si existe
     */
    boolean existePorEmail(String email);
}   