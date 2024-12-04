package com.example.finalprog2.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.entidad.Sitio;
import com.example.finalprog2.utils.PopupMenuHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.example.finalprog2.conexion.FirebaseTestData;

import java.util.ArrayList;
import java.util.List;

public class MapaSitiosFragment extends Fragment {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment mapFragment;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseFirestore db;
    private Spinner spinnerCategoria;
    private List<Sitio> sitios = new ArrayList<>();
    private List<Sitio> sitiosFiltrados = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        FirebaseTestData firebaseTestData = new FirebaseTestData();

    }

    private final OnMapReadyCallback callback = googleMap -> {
        mMap = googleMap;

        // Configurar estilo del mapa
        try {
            boolean success = mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
            if (!success) {
                //Toast.makeText(getContext(), "No se pudo aplicar el estilo del mapa.", Toast.LENGTH_SHORT).show();
                popupmsg("","No se pudo aplicar el estilo del mapa.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map", "No se pudo encontrar el estilo del mapa.", e);
        }

        // Configuraciones del mapa
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocationPermission();
                return;
            }
            mMap.setMyLocationEnabled(true);
            getDeviceLocation();
        } else {
            requestLocationPermission();
        }

        // Cargar sitios desde Firestore
        db = FirebaseFirestore.getInstance();
        loadSitesOnMap();

        // Listener para clics en los marcadores
        mMap.setOnMarkerClickListener(marker -> {
            Sitio sitio = (Sitio) marker.getTag();
            if (sitio != null) showSiteInfo(sitio);
            return true;
        });

        // Listener para clics en el mapa
        mMap.setOnMapClickListener(latLng -> hideSiteInfo());
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mapa_sitios, container, false);

        sitios = new ArrayList<>();
        sitiosFiltrados = new ArrayList<>();

        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Mapa de Sitios");
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


        // Inicializar el fragmento del mapa
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        // Inicializar el Spinner
        spinnerCategoria = view.findViewById(R.id.spinner_categoria);

        // Cargar categorías desde strings.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.categorias_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);

        // Listener para el cambio de selección en el Spinner
        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String categoriaSeleccionada = (String) parentView.getItemAtPosition(position);

                // Si la opción "Todos" es seleccionada, cargar todos los sitios
                if ("Todos".equalsIgnoreCase(categoriaSeleccionada)) {
                    loadSitesOnMap();  // Cargar todos los sitios sin filtrar
                } else {
                    // Si no es "Todos", filtrar los sitios por la categoría seleccionada
                    filterSitesByCategory(categoriaSeleccionada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                loadSitesOnMap();  // Cargar todos los sitios si no hay nada seleccionado
            }
        });
        return view;
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                LatLng currentLocation = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
            } else {
                Log.e("Location", "No se pudo obtener la ubicación actual.");
            }
        });
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    private void loadSitesOnMap() {
        db.collection("sitios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                sitios.clear(); // Limpiar lista de sitios para evitar duplicados
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Sitio sitio = document.toObject(Sitio.class); // Carga del objeto Sitio
                    sitios.add(sitio);
                    GeoPoint location = sitio.getUbicacion();
                    if (location != null) {
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(sitio.getNombre()));
                        marker.setTag(sitio); // Asocia el sitio al marcador
                    }
                }
            } else {
                Log.e("Firestore", "Error al cargar sitios", task.getException());
            }
        });
    }

    private void filterSitesByCategory(String category) {
        // Limpiar los sitios del mapa
        mMap.clear();

        // Filtrar los sitios
        sitiosFiltrados.clear();
        for (Sitio sitio : sitios) {
            if (sitio.getCategoria().equalsIgnoreCase(category)) {
                sitiosFiltrados.add(sitio);
            }
        }

        if (getView() != null) {
            TextView tvNoResults = getView().findViewById(R.id.tv_no_results);

            if (sitiosFiltrados.isEmpty()) {
                // Muestra el mensaje de no resultados
                tvNoResults.setVisibility(View.VISIBLE);
            } else {
                // Oculta el mensaje y actualiza la lista
                tvNoResults.setVisibility(View.GONE);
            }
        }

        // Cargar los sitios filtrados en el mapa
        for (Sitio sitio : sitiosFiltrados) {
            GeoPoint location = sitio.getUbicacion();
            if (location != null) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).title(sitio.getNombre()));
                marker.setTag(sitio); // Asocia el sitio al marcador
            }
        }


    }


    private void showSiteInfo(Sitio sitio) {
        View view = getView();
        if (view != null) {
            LinearLayout infoLayout = view.findViewById(R.id.infoLocal);
            infoLayout.setVisibility(View.VISIBLE);

            // Dirección y horarios
            TextView direccion = view.findViewById(R.id.direccion);
            direccion.setText("Dirección: " + sitio.getDireccion());

            TextView horarios = view.findViewById(R.id.horarios);
            horarios.setText("Horarios: " + sitio.getHorarios());

            // Mostrar la categoría
            TextView categoria = view.findViewById(R.id.categoria);
            if (sitio.getCategoria() != null && !sitio.getCategoria().isEmpty()) {
                categoria.setVisibility(View.VISIBLE);
                categoria.setText("Categoría: " + sitio.getCategoria());
            }

            // Redes sociales
            // (similar a los métodos previos)
        }
    }

    private void hideSiteInfo() {
        View view = getView();
        if (view != null) {
            LinearLayout infoLayout = view.findViewById(R.id.infoLocal);
            infoLayout.setVisibility(View.GONE);
        }
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
