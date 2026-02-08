package com.pablo.crud_articulos.model;

import jakarta.persistence.*;

@Entity
public class Articulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autores;
    private int anio;
    private String revista;

    @Column(length = 2000)
    private String resumen;

    private String palabrasClave;

    @Lob
    @Column(name = "archivo_pdf", columnDefinition = "LONGBLOB")
    private byte[] archivoPdf;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutores() { return autores; }
    public void setAutores(String autores) { this.autores = autores; }

    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }

    public String getRevista() { return revista; }
    public void setRevista(String revista) { this.revista = revista; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public String getPalabrasClave() { return palabrasClave; }
    public void setPalabrasClave(String palabrasClave) { this.palabrasClave = palabrasClave; }

    public byte[] getArchivoPdf() { return archivoPdf; }
    public void setArchivoPdf(byte[] archivoPdf) { this.archivoPdf = archivoPdf; }
}