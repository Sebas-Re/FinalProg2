package com.example.finalprog2.entidad;

import java.util.Objects;

public class Publicacion {
    private int id;
    private Usuario usuario;
    private String titulo;
    private String descripcion;
    private String relacionEnergetica;
    private boolean estado;

    public Publicacion() {}

    public Publicacion(int id, Usuario usuario, String titulo, String descripcion, String relacionEnergetica, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.relacionEnergetica = relacionEnergetica;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRelacionEnergetica() {
        return relacionEnergetica;
    }

    public void setRelacionEnergetica(String relacionEnergetica) {
        this.relacionEnergetica = relacionEnergetica;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Publicacion{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", relacionEnergetica='" + relacionEnergetica + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publicacion that = (Publicacion) o;
        return id == that.id && estado == that.estado && Objects.equals(usuario, that.usuario) && Objects.equals(titulo, that.titulo) && Objects.equals(descripcion, that.descripcion) && Objects.equals(relacionEnergetica, that.relacionEnergetica);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usuario, titulo, descripcion, relacionEnergetica, estado);
    }
}
