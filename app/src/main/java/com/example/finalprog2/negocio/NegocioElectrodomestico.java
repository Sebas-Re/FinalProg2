package com.example.finalprog2.negocio;

import android.content.Context;

import com.example.finalprog2.entidad.Electrodomestico;
import com.example.finalprog2.interfaces.EficienciaCallback;
import com.example.finalprog2.interfaces.ElectrodomesticoCallback;
import com.example.finalprog2.interfaces.GuardarElectrodomesticoCallback;
import com.example.finalprog2.interfaces.KwhCallback;
import com.example.finalprog2.interfaces.ObtenerConsumoCallback;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NegocioElectrodomestico {

    public NegocioElectrodomestico (Context context){

    }

    public static void obtenerElectrodomesticos(ElectrodomesticoCallback callback) {
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

    public static void obtenerEficiencias(String tipo, EficienciaCallback callback) {
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

    public static void obtenerConsumoSemanal(String tipo, ObtenerConsumoCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico")
                .whereEqualTo("tipo", tipo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<String> listaConsumo = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            int consumoSemanal = document.getLong("consumoPromedio").intValue();
                            listaConsumo.add(String.valueOf(consumoSemanal));
                        }
                        callback.onObtenerconsumo(listaConsumo);
                    } else {
                        callback.onErrorObtenerConsumo("No se encontraron datos para este tipo de electrodoméstico.");
                    }
                })
                .addOnFailureListener(e -> callback.onErrorObtenerConsumo("Error en Firestore: " + e.getMessage()));
    }

    public void GuardarElectrodomestico(Electrodomestico electrodomestico, GuardarElectrodomesticoCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        int currentId = (snapshot != null) ? snapshot.size() : 0;
                        electrodomestico.setId(currentId + 1);

                        // Guardar el electrodoméstico en Firestore
                        db.collection("electrodomestico")
                                .add(electrodomestico)
                                .addOnSuccessListener(documentReference -> callback.onGuardarElectro())
                                .addOnFailureListener(e -> callback.onErrorGuardarElectro(e.getMessage()));
                    } else {
                        callback.onErrorGuardarElectro("Error al obtener registros de la colección.");
                    }
                });
    }
}


