package com.example.movie.repository;

import com.example.movie.model.Pelicula;

import java.util.List;
import java.util.Optional;

public interface PeliculaRepository {

    List<Pelicula> findAll();

    Optional<Pelicula> findById(Long id);

    Pelicula save(Pelicula pelicula);

    void deleteById(Long id);
}