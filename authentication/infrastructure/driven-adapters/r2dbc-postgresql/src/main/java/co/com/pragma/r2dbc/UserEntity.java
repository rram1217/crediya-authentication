package co.com.pragma.r2dbc;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="users")
@Data
public class UserEntity {
    @Id
    @Column("id_usuario")
    private Long userId;
    @Column("nombre")
    private String firstName;
    @Column("apellido")
    private String lastName;
    @Column("fecha_nacimiento")
    private LocalDate birthDate;
    @Column("direccion")
    private String address;
    @Column("telefono")
    private String phoneNumber;
    @Column("correo_electronico")
    private String email;
    @Column("id_rol")
    private Long rolId;
    @Column("salario_base")
    private BigDecimal baseSalary;
}
