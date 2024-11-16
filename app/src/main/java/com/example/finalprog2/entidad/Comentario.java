package com.example.finalprog2.entidad;

public class Comentario {
    private int idPublicacion;
    private String usuario;
    private String texto;
    private long timestamp;

    public Comentario() {}

    public Comentario(String usuario, String texto, long timestamp) {
        this.usuario = usuario;
        this.texto = texto;
        this.timestamp = timestamp;
    }

    // Getters y setters
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getIdPublicacion() {
        return idPublicacion;
    }

    public void setIdPublicacion(int idPublicacion) {
        this.idPublicacion = idPublicacion;
    }
}
