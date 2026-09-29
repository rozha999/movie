package com.example.movie.repository;

import com.example.movie.config.DatabaseConnection;
import com.example.movie.model.Pelicula;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PeliculaRepositoryImpl implements PeliculaRepository {

    private static final String SQL_SELECT_BASE =
            "SELECT id, titulo, genero, anio, duracion, puntuacion " +
                    "FROM pelicula";

    private final DatabaseConnection db = DatabaseConnection.getInstance();

    @Override
    public List<Pelicula> findAll() {
        String sql = SQL_SELECT_BASE + " ORDER BY id";
        List<Pelicula> peliculas = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                peliculas.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar peliculas", e);
        }

        return peliculas;
    }

    @Override
    public Optional<Pelicula> findById(Long id) {
        String sql = SQL_SELECT_BASE + " WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar la pelicula " + id, e);
        }

        return Optional.empty();
    }

    @Override
    public Pelicula save(Pelicula pelicula) {

        if (pelicula.getId() == null) {
            return insertar(pelicula);
        }

        return actualizar(pelicula);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM pelicula WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Error al eliminar la pelicula " + id, e);
        }
    }

    private Pelicula insertar(Pelicula pelicula) {

        String sql = "INSERT INTO pelicula " +
                "(titulo, genero, anio, duracion, puntuacion) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getGenero());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setBigDecimal(5, pelicula.getPuntuacion());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    pelicula.setId(keys.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Error al insertar la pelicula", e);
        }

        return pelicula;
    }

    private Pelicula actualizar(Pelicula pelicula) {

        String sql = "UPDATE pelicula SET " +
                "titulo = ?, genero = ?, anio = ?, " +
                "duracion = ?, puntuacion = ? " +
                "WHERE id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getGenero());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setBigDecimal(5, pelicula.getPuntuacion());
            ps.setLong(6, pelicula.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar la pelicula", e);
        }

        return pelicula;
    }

    private Pelicula mapear(ResultSet rs) throws SQLException {

        Pelicula pelicula = new Pelicula();

        pelicula.setId(rs.getLong("id"));
        pelicula.setTitulo(rs.getString("titulo"));
        pelicula.setGenero(rs.getString("genero"));
        pelicula.setAnio(rs.getInt("anio"));
        pelicula.setDuracion(rs.getInt("duracion"));
        pelicula.setPuntuacion(rs.getBigDecimal("puntuacion"));

        return pelicula;
    }
}
