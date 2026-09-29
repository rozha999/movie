package com.example.movie.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class Pelicula {

    private Long id;

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo no puede superar los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El genero es obligatorio")
    @Size(max = 50, message = "El genero no puede superar los 50 caracteres")
    private String genero;

    @NotNull(message = "El anio es obligatorio")
    @Min(value = 1900, message = "El anio no es valido")
    @Max(value = 2100, message = "El anio no es valido")
    private Integer anio;

    @NotNull(message = "La duracion es obligatoria")
    @Positive(message = "La duracion debe ser mayor que 0")
    private Integer duracion;

    @NotNull(message = "La puntuacion es obligatoria")
    @DecimalMin(value = "0.0", message = "La puntuacion minima es 0")
    @DecimalMax(value = "10.0", message = "La puntuacion maxima es 10")
    private BigDecimal puntuacion;

    public Pelicula() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(BigDecimal puntuacion) {
        this.puntuacion = puntuacion;
    }
}