CREATE TABLE "users" (
    id_usuario      BIGSERIAL PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    apellido       VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion       VARCHAR(255),
    telefono        VARCHAR(20),
    correo_electronico VARCHAR(150) UNIQUE NOT NULL,
    id_rol          BIGINT NOT NULL,
    salario_base    NUMERIC(15,2) NOT NULL
);
