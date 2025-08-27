package co.com.pragma.r2dbc;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name="users")
@Data
public class UserEntity {
    @Id
    private Long id_usuario;
    @Column("nombres")
    private String nombres;
    @Column("apellidos")
    private String apellidos;
    @Column("fecha_nacimiento")
    private LocalDate fecha_nacimiento;
    @Column("direccion")
    private String direccion;
    @Column("telefono")
    private String telefono;
    @Column("correo_electronico")
    private String correo_electronico;
    @Column("id_rol")
    private Long id_rol;
    @Column("salario_base")
    private BigDecimal salario_base;
}
