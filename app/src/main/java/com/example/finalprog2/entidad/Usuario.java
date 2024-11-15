package com.example.finalprog2.entidad;

import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
    private int id;
    private String nombre;
    private String apellido;
    private String usuario;
    private String email;
    private String pass;
    private int token;
    private boolean estado;

    // Constructor, getters y setters...

    public Usuario() {}

    public Usuario(String nombre, String apellido, String usuario, String email, String pass, int token, boolean estado, int id) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.email = email;
        this.pass = pass;
        this.token = token;
        this.estado = estado;
        this.id = id;
    }


    // Constructor para LogIn
    public Usuario(String usuarioEmail, String pass) {

        //Verificar si lo ingresado en usuario es un email
        if (usuarioEmail.contains("@")) {
            this.email = usuarioEmail;
            this.usuario = null;
        } else {
            this.usuario = usuarioEmail;
            this.email = null;
        }
        this.pass = pass;
        this.estado = true;
    }

    // Constructor de RegistroUsuario
    public Usuario(String nombre, String apellido, String usuario, String email, String pass) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.email = email;
        this.pass = pass;
        this.estado = true;
    }

    public void setId(int idUsuario) {
        this.id = idUsuario;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario1 = (Usuario) o;
        return id == usuario1.id && token == usuario1.token && estado == usuario1.estado && Objects.equals(nombre, usuario1.nombre) && Objects.equals(apellido, usuario1.apellido) && Objects.equals(usuario, usuario1.usuario) && Objects.equals(email, usuario1.email) && Objects.equals(pass, usuario1.pass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, apellido, usuario, email, pass, token, estado);
    }



}
