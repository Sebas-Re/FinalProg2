package com.example.finalprog2.entidad;

import com.google.firebase.firestore.GeoPoint;
public class Sitio {

    private String nombre;
    private String descripcion;
    private String direccion;
    private String horarios;
    private String instagram;
    private String whatsapp;
    private String correo;
    private GeoPoint ubicacion;
    private String categoria;

    public Sitio() {}

    public Sitio(String nombre, String direccion, String horarios, String instagram, String whatsapp, String correo, GeoPoint ubicacion, String descripcion, String categoria) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.horarios = horarios;
        this.instagram = instagram;
        this.whatsapp = whatsapp;
        this.correo = correo;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.categoria= categoria;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    // Getters y setters para cada campo
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getters y setters para cada atributo
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public GeoPoint getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(GeoPoint ubicacion) {
        this.ubicacion = ubicacion;
    }
}
