package com.example.finalprog2.negocio;

import android.content.Context;

import com.example.finalprog2.entidad.Electrodomestico;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NegocioElectrodomestico {

    public NegocioElectrodomestico (Context context){

    }

    public interface ElectrodomesticoCallback {
        void onElectrodomesticosCargados(List<String> listaElectrodomesticos);
        void onErrorElectrodomestico(String error);
    }

    public interface EficienciaCallback {
        void onEficienciasCargadas(List<String> eficiencias); // Callback para eficiencias
        void onErrorEficiencia(String error);
    }

    public interface KwhCallback {
        void onKwhObtenido(int kwh);
        void onErrorKwh(String error);
    }

    public static void obtenerElectrodomesticos(final ElectrodomesticoCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> listaElectrodomesticos = new ArrayList<>();

                        if (!task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Electrodomestico electrodomestico = document.toObject(Electrodomestico.class);
                                if (electrodomestico != null) {
                                    listaElectrodomesticos.add(electrodomestico.getTipo());
                                }
                            }
                            callback.onElectrodomesticosCargados(listaElectrodomesticos);
                        } else {
                            callback.onErrorElectrodomestico("No hay electrodomésticos cargados");
                        }
                    } else {
                        callback.onErrorElectrodomestico("Error al obtener electrodomésticos: " + task.getException());
                    }
                });
    }

    public static void obtenerEficiencias(String tipo, final EficienciaCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico")
                .whereEqualTo("tipo", tipo) // Filtramos por tipo de electrodoméstico
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> eficiencias = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Electrodomestico electrodomestico = document.toObject(Electrodomestico.class);
                            if (electrodomestico != null && electrodomestico.getEficiencia() != null) {
                                // Agregamos las claves de las eficiencias (A+, A++, etc.)
                                eficiencias.addAll(electrodomestico.getEficiencia().keySet());
                            }
                        }
                        callback.onEficienciasCargadas(eficiencias); // Devolvemos las eficiencias al fragmento
                    } else {
                        callback.onErrorEficiencia("Error al obtener eficiencias: " + task.getException());
                    }
                });
    }

    public static void obtenerKwhPorEficiencia(String tipo, String eficiencia, KwhCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico")
                .whereEqualTo("tipo", tipo)  // Filtra por tipo de electrodoméstico
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Map<String, Object> eficienciaMap = (Map<String, Object>) document.get("eficiencia");
                            if (eficienciaMap != null && eficienciaMap.containsKey(eficiencia)) {
                                Map<String, Object> kwhMap = (Map<String, Object>) eficienciaMap.get(eficiencia);
                                if (kwhMap != null && kwhMap.containsKey("kwh")) {
                                    Long kwhLong = (Long) kwhMap.get("kwh");
                                    int kwh = kwhLong != null ? kwhLong.intValue() : 0;
                                    callback.onKwhObtenido(kwh);
                                    return;
                                }
                            }
                        }
                        callback.onErrorKwh("No se encontró el valor de kWh para la eficiencia seleccionada.");
                    } else {
                        callback.onErrorKwh("No se encontraron electrodomésticos para el tipo seleccionado.");
                    }
                })
                .addOnFailureListener(e -> callback.onErrorKwh("Error al obtener kWh: " + e.getMessage()));
    }
}


