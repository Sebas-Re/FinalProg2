package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalprog2.R;
import com.example.finalprog2.negocio.NegocioElectrodomestico;

import java.util.List;

public class AutoEvalFragment extends Fragment {

    private Spinner spinnerElectrodomestico;
    private Spinner spinnerEficiencia;
    private TextView tvKwh;

    public AutoEvalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auto_eval, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerElectrodomestico = view.findViewById(R.id.spinner_electrodomestico);

        // Configurar el spinner de electrodomésticos
        spinnerElectrodomestico = view.findViewById(R.id.spinner_electrodomestico);
        spinnerEficiencia = view.findViewById(R.id.spinner_eficiencia);
        tvKwh = view.findViewById(R.id.tv_kwh);

        cargarElectrodomesticos();

        // Configurar el spinner de electrodomésticos
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

        // Configurar el listener para el spinner de eficiencia
        spinnerEficiencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String eficienciaSeleccionada = parent.getItemAtPosition(position).toString();
                String tipoSeleccionado = spinnerElectrodomestico.getSelectedItem().toString();
                actualizarKwh(tipoSeleccionado, eficienciaSeleccionada); // Pasar ambos
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no hay selección
            }
        });


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

    public void cargarEficiencias(String tipo) {
        NegocioElectrodomestico.obtenerEficiencias(tipo, new NegocioElectrodomestico.EficienciaCallback() {
            @Override
            public void onEficienciasCargadas(List<String> eficiencias) {
                // Si obtenemos las eficiencias correctamente, las pasamos al Spinner
                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                            android.R.layout.simple_spinner_item, eficiencias);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    Spinner spinnerEficiencia = getView().findViewById(R.id.spinner_eficiencia);
                    spinnerEficiencia.setAdapter(adapter);
                }
            }

            @Override
            public void onErrorEficiencia(String error) {
                Log.e("NegocioElectrodomestico", error);
            }
        });
    }

}
