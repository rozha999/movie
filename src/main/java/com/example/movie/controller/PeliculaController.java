package com.example.movie.controller;

import com.example.movie.model.Pelicula;
import com.example.movie.service.PeliculaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/peliculas")
public class PeliculaController {

    private final PeliculaService peliculaService;

    public PeliculaController(PeliculaService peliculaService) {
        this.peliculaService = peliculaService;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("peliculas", peliculaService.findAll());
        return "peliculas/lista";
    }

    // FORMULARIO NUEVO
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("pelicula", new Pelicula());
        return "peliculas/formulario";
    }

    // GUARDAR
    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("pelicula") Pelicula pelicula,
            BindingResult result) {

        if (result.hasErrors()) {
            return "peliculas/formulario";
        }

        peliculaService.save(pelicula);

        return "redirect:/peliculas";
    }

    // FORMULARIO EDITAR
    @GetMapping("/editar/{id}")
    public String editar(
            @PathVariable Long id,
            Model model) {

        Pelicula pelicula = peliculaService
                .findById(id)
                .orElseThrow();

        model.addAttribute("pelicula", pelicula);

        return "peliculas/formulario";
    }

    // ELIMINAR
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {

        peliculaService.deleteById(id);

        return "redirect:/peliculas";
    }
}