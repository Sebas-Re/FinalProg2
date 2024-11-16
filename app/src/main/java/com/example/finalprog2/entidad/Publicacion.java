package com.example.finalprog2.entidad;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Publicacion {
    private Long id; // Cambiado a String para usar el ID generado por Firebase
    private String usuario;
    private String titulo;
    private String descripcion;
    private String relacionEnergetica;
    private List<Comentario> comentarios;
    private boolean estado;

    // Constructor vacío necesario para Firebase
    public Publicacion() {
        comentarios = new ArrayList<>(); // Inicializar lista de comentarios
    }

    public Publicacion(String usuario, String titulo, String descripcion, String relacionEnergetica, boolean estado) {
        this.usuario = usuario;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.relacionEnergetica = relacionEnergetica;
        this.estado = estado;
        this.comentarios = new ArrayList<>();
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRelacionEnergetica() {
        return relacionEnergetica;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRelacionEnergetica(String relacionEnergetica) {
        this.relacionEnergetica = relacionEnergetica;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Método para guardar la publicación en Firebase
    public void guardarPublicacion() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa para representar el documento de la publicación
        Map<String, Object> data = new HashMap<>();
        data.put("titulo", titulo);
        data.put("descripcion", descripcion);
        data.put("relacionEnergetica", relacionEnergetica);
        data.put("estado", estado);
        data.put("usuario", usuario); // Nombre del usuario o "Anónimo"

        // Primero, obtener el número de publicaciones existentes para asignar un ID incrementado
        db.collection("publicaciones")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Obtener el total de documentos en la colección
                    int totalPublicaciones = queryDocumentSnapshots.size();

                    // El nuevo ID será el total de publicaciones + 1
                    int nuevoId = totalPublicaciones + 1;

                    // Asignar el ID como un campo más en el documento
                    data.put("id", nuevoId); // Agregar el ID incrementado

                    // Agregar el documento a la colección "publicaciones"
                    db.collection("publicaciones")
                            .add(data)
                            .addOnSuccessListener(documentReference -> {
                                this.id = Long.valueOf(String.valueOf(nuevoId)); // Asignar el ID generado
                                Log.d("Publicacion", "Publicación agregada con ID: " + this.id);
                            })
                            .addOnFailureListener(e -> {
                                Log.w("Publicacion", "Error al agregar la publicación", e);
                            });

                })
                .addOnFailureListener(e -> {
                    Log.w("Publicacion", "Error al contar las publicaciones", e);
                });
    }

    // Método para agregar un comentario a la publicación
    public void agregarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
}
