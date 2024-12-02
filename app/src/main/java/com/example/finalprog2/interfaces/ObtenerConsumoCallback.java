package com.example.finalprog2.interfaces;

import java.util.List;

public interface ObtenerConsumoCallback {

    void onObtenerconsumo(List<String> listaElectrodomesticos);
    void onErrorObtenerConsumo(String error);
}
