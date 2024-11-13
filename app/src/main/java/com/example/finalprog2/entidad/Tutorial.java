package com.example.finalprog2.entidad;

import java.util.Date;

public class Tutorial {
    private String titulo;
    private String imagenUrl;
    private String descripcionCorta;
    private String textoCompleto;
    private Date fechaPublicacion;
    private String enlace;

    // Constructor
    public Tutorial(String titulo, String imagenUrl, String descripcionCorta, String textoCompleto, Date fechaPublicacion, String enlace) {
        this.titulo = titulo;
        this.imagenUrl = imagenUrl;
        this.descripcionCorta = descripcionCorta;
        this.textoCompleto = textoCompleto;
        this.fechaPublicacion = fechaPublicacion;
        this.enlace = enlace;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getDescripcionCorta() {
        return descripcionCorta;
    }

    public void setDescripcionCorta(String descripcionCorta) {
        this.descripcionCorta = descripcionCorta;
    }

    public String getTextoCompleto() {
        return textoCompleto;
    }

    public void setTextoCompleto(String textoCompleto) {
        this.textoCompleto = textoCompleto;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
}
