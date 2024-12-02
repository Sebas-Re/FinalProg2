package com.example.finalprog2;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.fragment.HomeFragment;
import com.example.finalprog2.fragment.LogInFragment;
import com.example.finalprog2.fragment.RegistroUsuarioFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            // Cargar el fragmento de inicio de sesión
            LogInFragment logInFragment = new LogInFragment();
            //CrearForoFragment logInFragment = new CrearForoFragment();
            //ListarForoFragment logInFragment = new ListarForoFragment();
            //RegistroUsuarioFragment logInFragment = new RegistroUsuarioFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, logInFragment) // Utiliza el ID del contenedor
                    .commit();
        }
    }
}