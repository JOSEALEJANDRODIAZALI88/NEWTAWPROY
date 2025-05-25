package red_social_academica.red_social_academica.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * Clase base para heredar campos de auditoría.
 * Registra información sobre la creación, modificación y baja de entidades.
 */
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class AuditableEntity {

    @Column(name = "usuario_alta", updatable = false)
    protected String usuarioAlta;

    @Column(name = "fecha_alta", nullable = false, updatable = false)
    protected LocalDate fechaAlta;

    @Column(name = "usuario_modificacion")
    protected String usuarioModificacion;

    @Column(name = "fecha_modificacion")
    protected LocalDate fechaModificacion;

    @Column(name = "usuario_baja")
    protected String usuarioBaja;

    @Column(name = "fecha_baja")
    protected LocalDate fechaBaja;

    @Column(name = "motivo_baja")
    protected String motivoBaja;

    @Column(name = "activo", nullable = false)
    protected boolean activo = true;
}