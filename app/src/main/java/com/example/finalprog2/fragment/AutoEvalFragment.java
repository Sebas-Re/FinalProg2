package com.example.finalprog2.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.finalprog2.interfaces.EficienciaCallback;
import com.example.finalprog2.interfaces.ElectrodomesticoCallback;
import com.example.finalprog2.interfaces.KwhCallback;
import com.example.finalprog2.interfaces.ObtenerConsumoCallback;
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

        // Configuración del botón de menú en el Toolbar
        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setTag("left");
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());
        });

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setTag("right");
        rightUserButton.setOnClickListener(v -> {
            //navigateToFragment(new EditarPerfilFragment());
            PopupMenuHelper.showPopupMenu(getContext(), rightUserButton, requireActivity());
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
        NegocioElectrodomestico.obtenerKwhPorEficiencia(tipo, eficiencia, new KwhCallback() {
            @Override
            public void onKwhObtenido(int kwh) {
                tvKwh.setText(String.format("%s kWh", kwh));
            }

            @Override
            public void onErrorKwh(String error) {
                //Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                popupmsg("Error",error);
            }
        });
    }

    private void cargarElectrodomesticos() {
        NegocioElectrodomestico.obtenerElectrodomesticos(new ElectrodomesticoCallback() {
            @Override
            public void onElectrodomesticosCargados(List<String> listaElectrodomesticos) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, listaElectrodomesticos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerElectrodomestico.setAdapter(adapter);
            }

            @Override
            public void onErrorElectrodomestico(String error) {
                //Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                popupmsg("Error",error);
            }
        });
    }

    private void cargarEficiencias(String tipo) {
        NegocioElectrodomestico.obtenerEficiencias(tipo, new EficienciaCallback() {
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
            electrodomestico.setTipo(tipoSeleccionado);
            electrodomestico.setEficienciaPorHoras(tipoSeleccionado, kwhPorHora);

            // Obtener el consumo semanal utilizando la interfaz
            NegocioElectrodomestico.obtenerConsumoSemanal(tipoSeleccionado, new ObtenerConsumoCallback() {
                @Override
                public void onObtenerconsumo(List<String> listaConsumo) {
                    if (!listaConsumo.isEmpty()) {
                        int consumoSemanal = Integer.parseInt(listaConsumo.get(0)); // Supongamos que el consumo está en la primera posición
                        electrodomestico.setconsumoPromedio((long) consumoSemanal);

                        // Agregar a la lista de electrodomésticos
                        electrodomesticos.add(electrodomestico);

                        // Actualizar la vista
                        actualizarVistaElectrodomesticos(electrodomestico);
                    } else {
                        //Toast.makeText(requireContext(), "No se encontraron datos para este electrodoméstico.", Toast.LENGTH_SHORT).show();
                        popupmsg("","No se encontraron datos para este electrodoméstico.");
                    }
                }

                @Override
                public void onErrorObtenerConsumo(String error) {
                    //Toast.makeText(requireContext(), "Error al obtener el consumo semanal: " + error, Toast.LENGTH_SHORT).show();
                    popupmsg("","Error al obtener el consumo semanal: " + error);
                }
            });
        } else {
            //Toast.makeText(requireContext(), "Selecciona un electrodoméstico y su eficiencia.", Toast.LENGTH_SHORT).show();
            popupmsg("","Selecciona un electrodoméstico y su eficiencia.");
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
                //Valida que el costo no sea 0
                if (tarifaKWh == 0) {
                    popupmsg("Error", "Por favor, ingrese una tarifa válida (diferente de 0).");
                    return; // Detener la ejecución de la función
                }
                // Aquí ya no necesitamos obtener la lista, ya que la lista es la misma que se actualiza en `agregarElectrodomestico`
                if (!electrodomesticos.isEmpty()) {
                    int totalConsumoSemanal = 0;

                    for (Electrodomestico electrodomestico : electrodomesticos) {
                        String tipoSeleccionado = electrodomestico.getTipo();

                        int eficienciaHora = electrodomestico.getEficienciaPorHora(tipoSeleccionado);

                        totalConsumoSemanal += electrodomestico.getconsumoPromedio() * eficienciaHora;
                    }

                    TextView tvtotalconsumo = getView().findViewById(R.id.tv_promedio_consumo_semanal);
                    tvtotalconsumo.setText(String.format("PROMEDIO SEMANAL: %d kWh", totalConsumoSemanal));
                    // Calcular el costo total
                    double costoEstimado = totalConsumoSemanal * tarifaKWh;

                    // Mostrar el resultado en el TextView
                    TextView tvCostoEstimado = getView().findViewById(R.id.tv_costo_estimado);
                    tvCostoEstimado.setText(String.format("COSTO ESTIMADO SEMANAL: $%.2f", costoEstimado));
                } else {
                    //Toast.makeText(requireContext(), "No hay electrodomésticos agregados.", Toast.LENGTH_SHORT).show();
                    popupmsg("","No hay electrodomésticos agregados.");
                }

            } catch (NumberFormatException e) {
                // Mostrar un mensaje si la tarifa no es un número válido
                //Toast.makeText(requireContext(), "Por favor ingrese una tarifa válida.", Toast.LENGTH_SHORT).show();
                popupmsg("","Por favor ingrese una tarifa válida.");
            }
        } else {
            // Si el campo de tarifa está vacío
            //Toast.makeText(requireContext(), "Por favor ingrese la tarifa por kWh.", Toast.LENGTH_SHORT).show();
            popupmsg("","Por favor ingrese la tarifa por kWh.");
        }
    }

    private void actualizarVistaElectrodomesticos(Electrodomestico electrodomestico) {
        String textoActual = tvListaElectrodomesticos.getText().toString();
        String nuevoTexto = textoActual + "\n" + electrodomestico.getTipo() + " "+   electrodomestico.getEficienciaPorHora(electrodomestico.getTipo()) + "kwh" + ": " + electrodomestico.getconsumoPromedio() + " kWh semanal";
        tvListaElectrodomesticos.setText(nuevoTexto);
    }

    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
