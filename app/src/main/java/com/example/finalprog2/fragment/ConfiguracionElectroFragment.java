package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.finalprog2.entidad.Electrodomestico;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;
import com.example.finalprog2.R;

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

        // Configuración del botón para guardar
        btnGuardar.setOnClickListener(v -> guardarElectrodomestico());

        return view;
    }

    private void guardarElectrodomestico() {
        String tipo = etTipo.getText().toString();
        String consumoSemanalStr = etConsumoSemanal.getText().toString();
        String aplus = etAplus.getText().toString();
        String aplusplus = etAplusplus.getText().toString();
        String b = etB.getText().toString();
        String c = etC.getText().toString();

        if (tipo.isEmpty() || consumoSemanalStr.isEmpty() || aplus.isEmpty() || aplusplus.isEmpty() || b.isEmpty() || c.isEmpty()) {
            Toast.makeText(getActivity(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

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

        // Obtener el número actual de registros para asignar un id incremental
        db.collection("electrodomestico").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        int currentId = snapshot.size(); // Obtener el número actual de documentos
                        electrodomestico.setId(currentId + 1); // Asignar un id incremental

                        // Guardar el electrodoméstico en Firebase con el id incremental
                        db.collection("electrodomestico")
                                .add(electrodomestico)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(getActivity(), "Electrodoméstico guardado exitosamente", Toast.LENGTH_SHORT).show();
                                    clearFields();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Error al guardar el electrodoméstico", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getActivity(), "Error al obtener los registros", Toast.LENGTH_SHORT).show();
                    }
                });
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
}
