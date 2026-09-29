package com.example.movie.repository;

import com.example.movie.model.Pelicula;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class PeliculaRepositoryImpl implements PeliculaRepository {

    private static final String SQL_SELECT_BASE =
            "SELECT id, titulo, genero, anio, duracion, puntuacion " +
                    "FROM pelicula";

    private final JdbcTemplate jdbcTemplate;

    public PeliculaRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Pelicula> findAll() {
        String sql = SQL_SELECT_BASE + " ORDER BY id";
        return jdbcTemplate.query(sql, peliculaRowMapper());
    }

    @Override
    public Optional<Pelicula> findById(Long id) {
        String sql = SQL_SELECT_BASE + " WHERE id = ?";

        List<Pelicula> resultado =
                jdbcTemplate.query(sql, peliculaRowMapper(), id);

        return resultado.stream().findFirst();
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
        jdbcTemplate.update(sql, id);
    }

    private Pelicula insertar(Pelicula pelicula) {

        String sql = "INSERT INTO pelicula " +
                "(titulo, genero, anio, duracion, puntuacion) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {

            PreparedStatement ps =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            ps.setString(1, pelicula.getTitulo());
            ps.setString(2, pelicula.getGenero());
            ps.setInt(3, pelicula.getAnio());
            ps.setInt(4, pelicula.getDuracion());
            ps.setBigDecimal(5, pelicula.getPuntuacion());

            return ps;

        }, keyHolder);

        pelicula.setId(keyHolder.getKey().longValue());

        return pelicula;
    }

    private Pelicula actualizar(Pelicula pelicula) {

        String sql = "UPDATE pelicula SET " +
                "titulo = ?, genero = ?, anio = ?, " +
                "duracion = ?, puntuacion = ? " +
                "WHERE id = ?";

        jdbcTemplate.update(
                sql,
                pelicula.getTitulo(),
                pelicula.getGenero(),
                pelicula.getAnio(),
                pelicula.getDuracion(),
                pelicula.getPuntuacion(),
                pelicula.getId()
        );

        return pelicula;
    }

    private RowMapper<Pelicula> peliculaRowMapper() {

        return (rs, rowNum) -> {

            Pelicula pelicula = new Pelicula();

            pelicula.setId(rs.getLong("id"));
            pelicula.setTitulo(rs.getString("titulo"));
            pelicula.setGenero(rs.getString("genero"));
            pelicula.setAnio(rs.getInt("anio"));
            pelicula.setDuracion(rs.getInt("duracion"));
            pelicula.setPuntuacion(
                    rs.getBigDecimal("puntuacion")
            );

            return pelicula;
        };
    }
}