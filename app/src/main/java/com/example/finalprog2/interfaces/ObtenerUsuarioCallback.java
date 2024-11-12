package com.example.finalprog2.interfaces;

import com.example.finalprog2.entidad.Usuario;

public interface ObtenerUsuarioCallback {
    boolean onSuccess(Usuario usuarioEncontrado);
    void onFailure(Exception e);
}
