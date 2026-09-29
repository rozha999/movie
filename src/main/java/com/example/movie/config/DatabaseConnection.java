package com.example.movie.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Singleton que centraliza la conexion JDBC a MySQL.
 * Lee la configuracion de application.properties (claves db.*)
 * y crea la tabla pelicula si no existe.
 */
public final class DatabaseConnection {

    private static final DatabaseConnection INSTANCE = new DatabaseConnection();

    private final String url;
    private final String username;
    private final String password;

    private DatabaseConnection() {
        Properties props = new Properties();

        try (InputStream in = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (in == null) {
                throw new IllegalStateException("No se encontro application.properties");
            }
            props.load(in);

        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer application.properties", e);
        }

        this.url = props.getProperty("db.url");
        this.username = props.getProperty("db.username");
        this.password = props.getProperty("db.password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Driver de MySQL no encontrado", e);
        }

        crearTabla();
    }

    public static DatabaseConnection getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS pelicula (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "titulo VARCHAR(100) NOT NULL, " +
                "genero VARCHAR(50) NOT NULL, " +
                "anio INT NOT NULL, " +
                "duracion INT NOT NULL, " +
                "puntuacion DECIMAL(3,1) NOT NULL)";

        try (Connection conn = getConnection();
             Statement st = conn.createStatement()) {

            st.execute(sql);

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No se pudo conectar a la base de datos. Verifica que MySQL este encendido y las credenciales en application.properties", e);
        }
    }
}
