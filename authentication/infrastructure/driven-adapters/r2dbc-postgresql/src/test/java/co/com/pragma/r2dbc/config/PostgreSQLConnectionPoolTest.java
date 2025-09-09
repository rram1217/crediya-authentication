package co.com.pragma.r2dbc.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PostgreSQLConnectionPoolTest {

    @Test
    void getConnectionConfigSuccess() {
        // Crear una instancia del record con valores de prueba
        PostgreSQLConnectionPool properties = new PostgreSQLConnectionPool(
                "localhost",
                5432,
                "dbName",
                "schema",
                "username",
                "password"
        );

        // Aquí puedes usarlo para obtener tu configuración de conexión
        assertNotNull(properties.host());
        assertNotNull(properties.port());
        assertNotNull(properties.database());
        assertNotNull(properties.schema());
        assertNotNull(properties.username());
        assertNotNull(properties.password());
    }
}
