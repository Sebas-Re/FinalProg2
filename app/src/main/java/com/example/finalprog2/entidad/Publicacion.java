package com.example.finalprog2.entidad;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

    // Métodos getter y setter
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRelacionEnergetica() {
        return relacionEnergetica;
    }

    public boolean isEstado() {
        return estado;
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


    // Método para guardar la publicación en la base de datos
    public void guardarPublicacion() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un mapa para representar el documento de la publicación
        Map<String, Object> data = new HashMap<>();
        data.put("titulo", titulo);
        data.put("descripcion", descripcion);
        data.put("relacionEnergetica", relacionEnergetica);
        data.put("estado", estado);
        // ... (agregar otros campos necesarios, como usuario)

        // Agregar el documento a la colección "publicaciones"
        db.collection("publicaciones")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Publicacion", "Publicación agregada con ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Publicacion", "Error al agregar la publicación", e);
                });
    }
}
