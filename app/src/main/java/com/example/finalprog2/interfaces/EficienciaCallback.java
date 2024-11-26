package com.example.finalprog2.interfaces;

import java.util.List;

public interface EficienciaCallback {
    void onEficienciasCargadas(List<String> eficiencias); // Callback para eficiencias
    void onErrorEficiencia(String error);
}
