package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;


public class LogInFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_log_in, container, false);
        Button btn_login = view.findViewById(R.id.btn_login);
        TextView tv_pass_olvidada = view.findViewById(R.id.tv_pass_olvidada);
        TextView tv_registro_usuario = view.findViewById(R.id.tv_registro_usuario);


        btn_login.setOnClickListener(v -> {
            // implementar lógica para iniciar sesion
            Toast.makeText(getActivity(), "Iniciar Sesión", Toast.LENGTH_SHORT).show();
        });

        tv_pass_olvidada.setOnClickListener(v -> {
            // implementar lógica para olvide mi contraseña
            Toast.makeText(getActivity(), "Olvide mi contraseña", Toast.LENGTH_SHORT).show();

        });

        tv_registro_usuario.setOnClickListener(v -> {
            // implementar lógica para registrarse
            Toast.makeText(getActivity(), "Registrate", Toast.LENGTH_SHORT).show();

        });

     return view;
    }
}