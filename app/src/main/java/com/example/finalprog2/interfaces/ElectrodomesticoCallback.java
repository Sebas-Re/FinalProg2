package com.example.finalprog2.interfaces;

import java.util.List;

public interface ElectrodomesticoCallback {
    void onElectrodomesticosCargados(List<String> listaElectrodomesticos);
    void onErrorElectrodomestico(String error);
}