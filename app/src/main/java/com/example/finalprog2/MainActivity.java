package com.example.finalprog2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprog2.fragment.CrearForoFragment;
import com.example.finalprog2.fragment.ListarForoFragment;
import com.example.finalprog2.fragment.LogInFragment;

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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, logInFragment) // Utiliza el ID del contenedor
                    .commit();
        }
    }
}