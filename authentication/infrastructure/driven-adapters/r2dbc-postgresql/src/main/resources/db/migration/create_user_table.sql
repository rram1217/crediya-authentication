CREATE TABLE "users" (
    id_usuario      BIGSERIAL PRIMARY KEY,
    nombres         VARCHAR(100) NOT NULL,
    apellidos       VARCHAR(100) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    direccion       VARCHAR(255),
    telefono        VARCHAR(20),
    correo_electronico VARCHAR(150) UNIQUE NOT NULL,
    id_rol          BIGINT NOT NULL,
    salario_base    NUMERIC(15,2) NOT NULL
);
