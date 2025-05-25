package red_social_academica.red_social_academica.validation;

import org.springframework.stereotype.Component;
import red_social_academica.red_social_academica.dto.post.PostCreateDTO;
import red_social_academica.red_social_academica.dto.post.PostUpdateDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class PostValidator {

    public void validarCreacion(PostCreateDTO dto) {
        validarTitulo(dto.getTitle());
        validarContenidoMinimo(dto);
        validarFechaEvento(dto.getEventDate());
        validarURLs(dto.getImageUrl(), dto.getResourceUrl());
    }

    public void validarActualizacion(PostUpdateDTO dto) {
        if (dto.getTitle() != null) validarTitulo(dto.getTitle());
        if (dto.getEventDate() != null) validarFechaEvento(dto.getEventDate());
        if (dto.getImageUrl() != null || dto.getResourceUrl() != null)
            validarURLs(dto.getImageUrl(), dto.getResourceUrl());
    }

    private void validarTitulo(String title) {
        if (title == null || title.trim().length() < 3) {
            throw new BusinessException("El titulo es obligatorio y debe tener al menos 3 caracteres");
        }
    }

    private void validarContenidoMinimo(PostCreateDTO dto) {
        boolean textoVacio = dto.getText() == null || dto.getText().trim().isEmpty();
        boolean sinRecursos = (dto.getImageUrl() == null || dto.getImageUrl().trim().isEmpty()) &&
                              (dto.getResourceUrl() == null || dto.getResourceUrl().trim().isEmpty());

        if (textoVacio && sinRecursos) {
            throw new BusinessException("Debe proporcionar al menos texto o una URL de imagen/recurso");
        }
    }

    private void validarFechaEvento(LocalDate eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDate.now())) {
            throw new BusinessException("La fecha del evento no puede ser anterior a hoy");
        }
    }

    private void validarURLs(String imageUrl, String resourceUrl) {
        List<String> dominiosBloqueados = Arrays.asList("spamcdn.com", "falso.net");

        if (imageUrl != null && contieneDominioBloqueado(imageUrl, dominiosBloqueados)) {
            throw new BusinessException("El dominio de la imagen no está permitido");
        }
        if (resourceUrl != null && contieneDominioBloqueado(resourceUrl, dominiosBloqueados)) {
            throw new BusinessException("El dominio del recurso no está permitido");
        }
    }

    private boolean contieneDominioBloqueado(String url, List<String> dominiosBloqueados) {
        return dominiosBloqueados.stream().anyMatch(url::contains);
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String mensaje) {
            super(mensaje);
        }
    }
}