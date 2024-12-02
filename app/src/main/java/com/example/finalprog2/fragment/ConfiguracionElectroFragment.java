package com.example.finalprog2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalprog2.entidad.Electrodomestico;
import com.example.finalprog2.interfaces.GuardarElectrodomesticoCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import com.example.finalprog2.R;
import com.example.finalprog2.negocio.NegocioElectrodomestico;

public class ConfiguracionElectroFragment extends Fragment {

    private EditText etTipo, etConsumoSemanal;
    private EditText etAplus, etAplusplus, etB, etC;
    private Button btnGuardar;
    private FirebaseFirestore db;

    public ConfiguracionElectroFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracion_electro, container, false);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // Referencias a los campos del formulario
        etTipo = view.findViewById(R.id.etTipo);
        etConsumoSemanal = view.findViewById(R.id.etConsumoSemanal);
        etAplus = view.findViewById(R.id.etAplus);
        etAplusplus = view.findViewById(R.id.etAplusplus);
        etB = view.findViewById(R.id.etB);
        etC = view.findViewById(R.id.etC);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Inicializar negocio con una instancia de NegocioElectrodomestico
        //negocioElectrodomestico = new NegocioElectrodomestico();

        // Configuración del botón para guardar
        btnGuardar.setOnClickListener(v -> guardarElectrodomestico());

        return view;
    }

    private void guardarElectrodomestico() {
        String tipo = etTipo.getText().toString().trim();
        String consumoSemanalStr = etConsumoSemanal.getText().toString().trim();
        String aplus = etAplus.getText().toString().trim();
        String aplusplus = etAplusplus.getText().toString().trim();
        String b = etB.getText().toString().trim();
        String c = etC.getText().toString().trim();

        if (tipo.isEmpty() || consumoSemanalStr.isEmpty() || aplus.isEmpty() || aplusplus.isEmpty() || b.isEmpty() || c.isEmpty()) {
            //Toast.makeText(getActivity(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            popupmsg("","Por favor complete todos los campos");
            return;
        }

        try {
            int consumoPromedio = Integer.parseInt(consumoSemanalStr);

            // Crear el objeto Electrodomestico
            Electrodomestico electrodomestico = new Electrodomestico();
            electrodomestico.setTipo(tipo);
            electrodomestico.setconsumoPromedio((long) consumoPromedio);

            // Crear el mapa de eficiencia
            Map<String, Map<String, Integer>> eficiencia = new HashMap<>();
            eficiencia.put("A+", createEfficiencyMap(Integer.parseInt(aplus)));
            eficiencia.put("A++", createEfficiencyMap(Integer.parseInt(aplusplus)));
            eficiencia.put("B", createEfficiencyMap(Integer.parseInt(b)));
            eficiencia.put("C", createEfficiencyMap(Integer.parseInt(c)));

            electrodomestico.setEficiencia(eficiencia);

            // Inicializar negocio con una instancia de NegocioElectrodomestico
            NegocioElectrodomestico negocioElectrodomestico = new NegocioElectrodomestico(getContext());

            // Llamar al negocio con Callback
            negocioElectrodomestico.GuardarElectrodomestico(electrodomestico, new GuardarElectrodomesticoCallback() {
                @Override
                public void onGuardarElectro() {
                    //Toast.makeText(getActivity(), "Electrodoméstico guardado exitosamente", Toast.LENGTH_SHORT).show();
                    popupmsg("","Electrodoméstico guardado exitosamente");
                    clearFields();
                }

                @Override
                public void onErrorGuardarElectro(String error) {
                    //Toast.makeText(getActivity(), "Error al guardar: " + error, Toast.LENGTH_SHORT).show();
                    popupmsg("Error","Error al guardar: " + error);
                }
            });

        } catch (NumberFormatException e) {
            //Toast.makeText(getActivity(), "Por favor ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            popupmsg("","Por favor ingrese valores numéricos válidos");
        }
    }

    private Map<String, Integer> createEfficiencyMap(int kwh) {
        Map<String, Integer> efficiency = new HashMap<>();
        efficiency.put("kwh", kwh);
        return efficiency;
    }

    private void clearFields() {
        etTipo.setText("");
        etConsumoSemanal.setText("");
        etAplus.setText("");
        etAplusplus.setText("");
        etB.setText("");
        etC.setText("");
    }

    private void popupmsg(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Cierra el diálogo
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
