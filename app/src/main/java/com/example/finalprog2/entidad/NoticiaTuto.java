package com.example.finalprog2.entidad;

import java.util.Objects;

public class NoticiaTuto {
    private int id;
    private String titulo;
    private String descripcion;
    private boolean Estado;

    public NoticiaTuto() {
    }

    public NoticiaTuto(int id, String titulo, String descripcion, boolean Estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.Estado = Estado;
    }

    public int getId() {
        return id;
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

    public boolean isEstado() {
        return Estado;
    }

    public void setEstado(boolean estado) {
        Estado = estado;
    }

    @Override
    public String toString() {
        return "NoticiaTuto{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", Estado=" + Estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoticiaTuto that = (NoticiaTuto) o;
        return id == that.id && Estado == that.Estado && Objects.equals(titulo, that.titulo) && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, descripcion, Estado);
    }
}
