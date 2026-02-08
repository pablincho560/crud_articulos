package com.pablo.crud_articulos.controller;

import com.pablo.crud_articulos.model.Articulo;
import com.pablo.crud_articulos.repository.ArticuloRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ArticuloController {

    private final ArticuloRepository articuloRepository;

    public ArticuloController(ArticuloRepository articuloRepository) {
        this.articuloRepository = articuloRepository;
    }

    // Lista principal
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articulos", articuloRepository.findAll());
        return "index";
    }

    // Buscador
    @GetMapping("/buscar")
    public String buscar(@RequestParam(value = "query", required = false) String query, Model model) {
        if (query == null || query.isBlank()) {
            model.addAttribute("articulos", articuloRepository.findAll());
        } else {
            List<Articulo> resultados = new ArrayList<>();
            String q = query.trim();

            if (q.toUpperCase().contains(" AND ")) {
                String[] parts = q.split("(?i) AND ");
                resultados.addAll(articuloRepository.searchWithAnd(parts[0].trim(), parts[1].trim()));
            } else if (q.toUpperCase().contains(" OR ")) {
                String[] parts = q.split("(?i) OR ");
                resultados.addAll(articuloRepository.searchWithOr(parts[0].trim(), parts[1].trim()));
            } else if (q.toUpperCase().contains(" XOR ")) {
                String[] parts = q.split("(?i) XOR ");
                List<Articulo> r1 = articuloRepository.searchWithOr(parts[0].trim(), parts[0].trim());
                List<Articulo> r2 = articuloRepository.searchWithOr(parts[1].trim(), parts[1].trim());

                List<Articulo> xorResultados = new ArrayList<>(r1);
                xorResultados.addAll(r2);
                xorResultados.removeAll(r1.stream().filter(r2::contains).toList());

                resultados.addAll(xorResultados);
            } else {
                resultados.addAll(
                    articuloRepository.findByTituloContainingIgnoreCaseOrAutoresContainingIgnoreCaseOrRevistaContainingIgnoreCaseOrResumenContainingIgnoreCaseOrPalabrasClaveContainingIgnoreCase(
                        q, q, q, q, q));
            }

            model.addAttribute("articulos", resultados);
        }
        return "index";
    }

    // Formulario nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("articulo", new Articulo());
        return "create";
    }

    // Guardar nuevo con PDF opcional
    @PostMapping("/guardar")
    public String guardar(@RequestParam("titulo") String titulo,
                          @RequestParam("autores") String autores,
                          @RequestParam("anio") int anio,
                          @RequestParam(value = "revista", required = false) String revista,
                          @RequestParam(value = "resumen", required = false) String resumen,
                          @RequestParam(value = "palabrasClave", required = false) String palabrasClave,
                          @RequestParam(value = "archivoPdf", required = false) MultipartFile archivoPdf) {
        try {
            Articulo articulo = new Articulo();
            articulo.setTitulo(titulo);
            articulo.setAutores(autores);
            articulo.setAnio(anio);
            articulo.setRevista(revista);
            articulo.setResumen(resumen);
            articulo.setPalabrasClave(palabrasClave);

            if (archivoPdf != null && !archivoPdf.isEmpty()) {
                articulo.setArchivoPdf(archivoPdf.getBytes());
            }

            articuloRepository.save(articulo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    // Formulario editar
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Articulo articulo = articuloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id inválido:" + id));
        model.addAttribute("articulo", articulo);
        return "update";
    }

    // Actualizar con PDF opcional
    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id,
                             @RequestParam("titulo") String titulo,
                             @RequestParam("autores") String autores,
                             @RequestParam("anio") int anio,
                             @RequestParam(value = "revista", required = false) String revista,
                             @RequestParam(value = "resumen", required = false) String resumen,
                             @RequestParam(value = "palabrasClave", required = false) String palabrasClave,
                             @RequestParam(value = "archivoPdf", required = false) MultipartFile archivoPdf) {
        try {
            Articulo existente = articuloRepository.findById(id).orElse(null);
            if (existente != null) {
                existente.setTitulo(titulo);
                existente.setAutores(autores);
                existente.setAnio(anio);
                existente.setRevista(revista);
                existente.setResumen(resumen);
                existente.setPalabrasClave(palabrasClave);

                if (archivoPdf != null && !archivoPdf.isEmpty()) {
                    existente.setArchivoPdf(archivoPdf.getBytes());
                }

                articuloRepository.save(existente);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
    // Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        articuloRepository.deleteById(id);
        return "redirect:/";
    }

    // Descargar PDF
    @GetMapping("/descargar/{id}")
    public ResponseEntity<byte[]> descargar(@PathVariable Long id) {
        Articulo articulo = articuloRepository.findById(id).orElse(null);
        if (articulo != null && articulo.getArchivoPdf() != null) {
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articulo.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(articulo.getArchivoPdf());
        }
        return ResponseEntity.notFound().build();
    }
}