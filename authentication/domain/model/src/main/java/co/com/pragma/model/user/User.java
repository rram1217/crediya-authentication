package co.com.pragma.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id_usuario;
    private String nombres;
    private String apellidos;
    private LocalDate fecha_nacimiento;
    private String direccion;
    private String telefono;
    private String correo_electronico;
    private Long id_rol;
    private BigDecimal salario_base;
}
