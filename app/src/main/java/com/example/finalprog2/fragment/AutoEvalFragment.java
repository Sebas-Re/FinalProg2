package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Electrodomestico;
import com.example.finalprog2.negocio.NegocioElectrodomestico;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutoEvalFragment extends Fragment {

    private Spinner spinnerElectrodomestico;
    private Spinner spinnerEficiencia;
    private TextView tvKwh;
    private TextView tvListaElectrodomesticos;
    private Button btnAgregar;
    private List<Electrodomestico> electrodomesticos = new ArrayList<>(); // Lista para almacenar los electrodomésticos
    private Map<String, Integer> eficienciaPorHoras; // Map para almacenar consumo por cada tipo o categoría
    private int consumoSemanal;
    public AutoEvalFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_eval, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("AutoEvaluacion");
        }

        //Ocultar el nombre de la app
        toolbar.setTitle("");

        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity()));

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setOnClickListener(v -> {
            navigateToFragment(new EditarPerfilFragment());
        });

        // Inicializar vistas
        spinnerElectrodomestico = view.findViewById(R.id.spinner_electrodomestico);
        spinnerEficiencia = view.findViewById(R.id.spinner_eficiencia);
        tvKwh = view.findViewById(R.id.tv_kwh);
        tvListaElectrodomesticos = view.findViewById(R.id.tv_lista_electrodomesticos);
        btnAgregar = view.findViewById(R.id.btn_agregar);

        Button btnLimpiar = view.findViewById(R.id.btn_limpiar); // Referencia al botón limpiar
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Limpiar la lista de electrodomésticos
                tvListaElectrodomesticos.setText("ELECTRODOMÉSTICOS SELECCIONADOS:\n");
                electrodomesticos.clear(); // Limpiar la lista de electrodomésticos
            }
        });

        Button btnCalcularCosto = view.findViewById(R.id.btn_calcular_costo);
        btnCalcularCosto.setOnClickListener(v -> calcularCosto());

        // Cargar opciones de electrodomésticos en el primer Spinner
        cargarElectrodomesticos();

        // Configurar listeners
        configurarListeners();
    }

    private void configurarListeners() {
        // Listener para el spinner de electrodomésticos
        spinnerElectrodomestico.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String tipoSeleccionado = parentView.getItemAtPosition(position).toString();
                cargarEficiencias(tipoSeleccionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no hay selección
            }
        });

        // Listener para el spinner de eficiencia
        spinnerEficiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String eficienciaSeleccionada = parent.getItemAtPosition(position).toString();
                String tipoSeleccionado = spinnerElectrodomestico.getSelectedItem().toString();
                actualizarKwh(tipoSeleccionado, eficienciaSeleccionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });

        // Listener para el botón "Agregar"
        btnAgregar.setOnClickListener(v -> agregarElectrodomestico());
    }

    private void actualizarKwh(String tipo, String eficiencia) {
        NegocioElectrodomestico.obtenerKwhPorEficiencia(tipo, eficiencia, new NegocioElectrodomestico.KwhCallback() {
            @Override
            public void onKwhObtenido(int kwh) {
                tvKwh.setText(String.format("%s kWh", kwh));
            }

            @Override
            public void onErrorKwh(String error) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarElectrodomesticos() {
        NegocioElectrodomestico.obtenerElectrodomesticos(new NegocioElectrodomestico.ElectrodomesticoCallback() {
            @Override
            public void onElectrodomesticosCargados(List<String> listaElectrodomesticos) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, listaElectrodomesticos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerElectrodomestico.setAdapter(adapter);
            }

            @Override
            public void onErrorElectrodomestico(String error) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarEficiencias(String tipo) {
        NegocioElectrodomestico.obtenerEficiencias(tipo, new NegocioElectrodomestico.EficienciaCallback() {
            @Override
            public void onEficienciasCargadas(List<String> eficiencias) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, eficiencias);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEficiencia.setAdapter(adapter);
            }

            @Override
            public void onErrorEficiencia(String error) {
                Log.e("NegocioElectrodomestico", error);
            }
        });
    }

    private void agregarElectrodomestico() {
        String tipoSeleccionado = spinnerElectrodomestico.getSelectedItem().toString();
        String kwhConsumidos = tvKwh.getText().toString();

        if (!tipoSeleccionado.isEmpty() && !kwhConsumidos.equals("0 kWh")) {
            int kwhPorHora = Integer.parseInt(kwhConsumidos.replace(" kWh", ""));

            // Crear el objeto Electrodomestico
            Electrodomestico electrodomestico = new Electrodomestico();

            // Guardar la eficiencia en el Map con el tipo como clave
            electrodomestico.setEficienciaPorHoras(tipoSeleccionado, kwhPorHora);
            electrodomestico.setTipo(tipoSeleccionado);
            // Obtener el consumo semanal desde Firebase y luego agregar a la lista
          obtenerConsumoSemanalDesdeFirebase(tipoSeleccionado, electrodomestico);


        } else {
            Toast.makeText(requireContext(), "Selecciona un electrodoméstico y su eficiencia.", Toast.LENGTH_SHORT).show();
        }
    }
    private void calcularCosto() {
        // Obtener el valor ingresado por el usuario en el campo de tarifa kWh
        EditText inputTarifaKwh = getView().findViewById(R.id.input_tarifaKwh);
        String tarifaKwhString = inputTarifaKwh.getText().toString().trim();

        // Validar si el usuario ha ingresado un valor
        if (!tarifaKwhString.isEmpty()) {
            try {
                // Convertir el valor de tarifa kWh a un número decimal (double)
                double tarifaKWh = Double.parseDouble(tarifaKwhString);

                // Aquí ya no necesitamos obtener la lista, ya que la lista es la misma que se actualiza en `agregarElectrodomestico`
                if (!electrodomesticos.isEmpty()) {
                    int totalConsumoSemanal = 0;

                    for (Electrodomestico electrodomestico : electrodomesticos) {
                        String tipoSeleccionado = electrodomestico.getTipo();

                        int eficienciaHora = electrodomestico.getEficienciaPorHora(tipoSeleccionado);

                        totalConsumoSemanal += electrodomestico.getConsumoSemanal() * eficienciaHora;
                    }

                    TextView tvtotalconsumo = getView().findViewById(R.id.tv_promedio_consumo_semanal);
                    tvtotalconsumo.setText(String.format("PROMEDIO SEMANAL: %d kWh", totalConsumoSemanal));
                    // Calcular el costo total
                    double costoEstimado = totalConsumoSemanal * tarifaKWh;

                    // Mostrar el resultado en el TextView
                    TextView tvCostoEstimado = getView().findViewById(R.id.tv_costo_estimado);
                    tvCostoEstimado.setText(String.format("COSTO ESTIMADO SEMANAL: $%.2f", costoEstimado));
                } else {
                    Toast.makeText(requireContext(), "No hay electrodomésticos agregados.", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                // Mostrar un mensaje si la tarifa no es un número válido
                Toast.makeText(requireContext(), "Por favor ingrese una tarifa válida.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Si el campo de tarifa está vacío
            Toast.makeText(requireContext(), "Por favor ingrese la tarifa por kWh.", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerConsumoSemanalDesdeFirebase(String tipo, Electrodomestico electrodomestico) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("electrodomestico")
                .whereEqualTo("tipo", tipo)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Supongamos que el campo en Firebase para el consumo semanal se llama "consumoSemanal"
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        int consumoSemanal = document.getLong("consumoPromedio").intValue();

                        // Guardar el consumo semanal en el objeto Electrodomestico
                        electrodomestico.setConsumoSemanal((long) consumoSemanal);

                        // Agregar a la lista después de obtener el consumo
                        electrodomesticos.add(electrodomestico);

                        // Actualizar la vista
                        actualizarVistaElectrodomesticos(electrodomestico);

                    } else {
                        Toast.makeText(requireContext(), "No se encontró el tipo de electrodoméstico en la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Error al obtener datos de Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void actualizarVistaElectrodomesticos(Electrodomestico electrodomestico) {
        String textoActual = tvListaElectrodomesticos.getText().toString();
        String nuevoTexto = textoActual + "\n" + electrodomestico.getTipo() + " "+   electrodomestico.getEficienciaPorHora(electrodomestico.getTipo()) + "kwh" + ": " + electrodomestico.getConsumoSemanal() + " kWh semanal";
        tvListaElectrodomesticos.setText(nuevoTexto);
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
