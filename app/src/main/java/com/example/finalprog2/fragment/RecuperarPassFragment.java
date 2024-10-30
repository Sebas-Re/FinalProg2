package com.example.finalprog2.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.negocio.NegocioUsuario;


public class RecuperarPassFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recuperar_pass, container, false);

        EditText input_email = view.findViewById(R.id.input_email);
        EditText input_codigo = view.findViewById(R.id.input_codigo);
        Button btn_recu_pass = view.findViewById(R.id.btn_recu_pass);


        btn_recu_pass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = input_email.getText().toString();
                String codigo = input_codigo.getText().toString();

                //si ambos campos no estan vacios
                if(!email.isEmpty() && !codigo.isEmpty()) {

                    NegocioUsuario negocioUsuario = new NegocioUsuario(getActivity());
                    if(negocioUsuario.verificarEmail(email)){
                        if(negocioUsuario.verificarCodigo(email, Integer.parseInt(codigo))){

                            // Redireccionar a la pantalla de nueva contraseña, pasando el email como argumento
                         Bundle bundle = new Bundle();
                         bundle.putString("email", email);
                         NuevaPassFragment nuevaPassFragment = new NuevaPassFragment();
                         nuevaPassFragment.setArguments(bundle);
                         requireActivity().getSupportFragmentManager()
                                 .beginTransaction()
                                 .replace(R.id.fragment_container, nuevaPassFragment)
                                 .addToBackStack(null) // Esto permite regresar al fragmento anterior
                                 .commit();
                         }
                         else{
                             Toast.makeText(getActivity(), "Código incorrecto", Toast.LENGTH_SHORT).show();
                         }

                     }
                    else{
                        Toast.makeText(getActivity(), "Email incorrecto", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}