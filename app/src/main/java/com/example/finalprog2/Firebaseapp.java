package com.example.finalprog2;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class Firebaseapp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar Firebase al inicio de la aplicación
        FirebaseApp.initializeApp(this);
    }
}
