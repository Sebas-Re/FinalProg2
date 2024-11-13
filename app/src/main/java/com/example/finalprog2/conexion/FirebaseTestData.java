package com.example.finalprog2.conexion;

import android.util.Log;

import com.example.finalprog2.entidad.Sitio;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
public class FirebaseTestData {
    private FirebaseFirestore db;

    public FirebaseTestData() {
        db = FirebaseFirestore.getInstance();
    }

    public void generarSitiosAleatorios(int cantidad) {
        String[] nombres = {"Café Tigre", "Heladería Río", "Pizzería Canal", "Librería Delta", "Bazar Isla"};
        String[] descripciones = {"Delicioso café", "Exquisito helado", "Pizza tradicional", "Libros y más", "Artículos varios"};

        for (int i = 0; i < cantidad; i++) {
            String nombre = nombres[i % nombres.length];
            String descripcion = descripciones[i % descripciones.length];

            GeoPoint ubicacion = generarUbicacionAleatoriaTigre();
            String horarios = "9:00 - 18:00";
            String instagram = "@negocio";
            String whatsapp = "+5491123456789";
            String direccion = "Dirección ficticia " + (i + 1);
            String correo= "test@test.com";

            Sitio sitio = new Sitio(nombre, direccion, horarios, instagram, whatsapp,correo, ubicacion, descripcion);

            db.collection("sitios")
                    .add(sitio)
                    .addOnSuccessListener(documentReference -> Log.d("FirebaseTestData", "Sitio agregado con ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.e("FirebaseTestData", "Error al agregar sitio", e));
        }
    }

    private GeoPoint generarUbicacionAleatoriaTigre() {
        double latMin = -34.428345;
        double latMax = -34.382422;
        double lonMin = -58.622666;
        double lonMax = -58.573539;

        double randomLat = latMin + (Math.random() * (latMax - latMin));
        double randomLon = lonMin + (Math.random() * (lonMax - lonMin));
        return new GeoPoint(randomLat, randomLon);
    }

    public void crearNoticiasDePrueba() {
        // Crear una instancia de FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un nuevo mapa de datos para la noticia principal
        Map<String, Object> noticiaPrincipal = new HashMap<>();
        noticiaPrincipal.put("titulo", "Noticia Principal");
        noticiaPrincipal.put("imagen", "url_imagen_principal"); // Puedes poner una URL de una imagen aquí
        noticiaPrincipal.put("descripcion", "Descripción breve de la noticia principal");
        noticiaPrincipal.put("texto_completo", "Texto completo de la noticia principal.");
        noticiaPrincipal.put("fecha_publicacion", new Timestamp(new Date())); // Usa la fecha actual
        noticiaPrincipal.put("enlace", "https://www.clarin.com/");

        // Crear el documento de la noticia principal en la colección "noticias"
        db.collection("noticias")
                .add(noticiaPrincipal)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Noticias", "Noticia principal agregada correctamente: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Noticias", "Error al agregar la noticia principal", e);
                });

        // Crear una lista de noticias adicionales
        for (int i = 1; i <= 3; i++) {
            final int noticiaIndex = i; // Hacemos que i sea final

            Map<String, Object> noticia = new HashMap<>();
            noticia.put("titulo", "Noticia " + noticiaIndex);
            noticia.put("imagen", "url_imagen_" + noticiaIndex);
            noticia.put("descripcion", "Descripción de la noticia " + noticiaIndex);
            noticia.put("texto_completo", "Texto completo de la noticia " + noticiaIndex);
            noticia.put("fecha_publicacion", new Timestamp(new Date()));
            noticia.put("enlace", "https://www.clarin.com/");

            // Agregar las noticias adicionales a la colección "noticias"
            db.collection("noticias")
                    .add(noticia)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Noticias", "Noticia " + noticiaIndex + " agregada correctamente: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Noticias", "Error al agregar la noticia " + noticiaIndex, e);
                    });
        }
    }


    public void crearTutorialDePrueba() {
        // Crear una instancia de FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear un nuevo mapa de datos para la noticia principal
        Map<String, Object> tutorialPrincipal = new HashMap<>();
        tutorialPrincipal.put("titulo", "Tutorial Principal");
        tutorialPrincipal.put("imagen", "url_imagen_principal"); // Puedes poner una URL de una imagen aquí
        tutorialPrincipal.put("descripcion", "Descripción breve del tutorial principal");
        tutorialPrincipal.put("texto_completo", "Texto completo del tutorial principal.");
        tutorialPrincipal.put("fecha_publicacion", new Timestamp(new Date())); // Usa la fecha actual
        tutorialPrincipal.put("enlace", "https://www.clarin.com/");

        // Crear el documento de la noticia principal en la colección "noticias"
        db.collection("tutoriales")
                .add(tutorialPrincipal)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Tutorial", "Tutorial principal agregada correctamente: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.w("Tutorial", "Error al agregar el Tutorial principal", e);
                });

        // Crear una lista de Tutorial adicionales
        for (int i = 1; i <= 10; i++) {
            final int tutorialIndex = i; // Hacemos que i sea final

            Map<String, Object> tutorial = new HashMap<>();
            tutorial.put("titulo", "Tutorial " + tutorialIndex);
            tutorial.put("imagen", "url_imagen_" + tutorialIndex);
            tutorial.put("descripcion", "Descripción del tutorial " + tutorialIndex);
            tutorial.put("texto_completo", "Texto completo del tutorial " + tutorialIndex);
            tutorial.put("fecha_publicacion", new Timestamp(new Date()));
            tutorial.put("enlace", "https://www.clarin.com/");

            // Agregar los tutoriales adicionales a la colección "tutorial"
            db.collection("tutoriales")
                    .add(tutorial)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Tutorial", "Tutorial " + tutorialIndex + " agregada correctamente: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Tutorial", "Error al agregar el tutorial " + tutorialIndex, e);
                    });
        }
    }
}
