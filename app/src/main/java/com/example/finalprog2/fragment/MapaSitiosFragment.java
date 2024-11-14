package com.example.finalprog2.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;
import com.example.finalprog2.conexion.FirebaseTestData;
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
import com.google.firebase.firestore.QuerySnapshot;

public class MapaSitiosFragment extends Fragment {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment mapFragment;
    private LatLng mOriginalLatLng;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FirebaseFirestore db;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Cargar el estilo del mapa
            try {
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                if (!success) {
                    Toast.makeText(getContext(), "No se pudo aplicar el estilo del mapa.", Toast.LENGTH_SHORT).show();
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true); // Habilitar la ubicación
            } else {
                requestLocationPermission();
            }

            // Crear instancias de FirebaseTestData y agregar los datos
            FirebaseTestData testData = new FirebaseTestData();
          //  testData.generarSitiosAleatorios(20); // Este método agregará los 20 registros

            db = FirebaseFirestore.getInstance();

            loadSitesOnMap();  // Cargar sitios desde Firestore

            // Restablecer la ubicación inicial del mapa
            mOriginalLatLng = mMap.getCameraPosition().target;

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Sitio sitio = (Sitio) marker.getTag();  // Obtener el objeto Sitio asociado con el marcador

                    // Mostrar la información del sitio
                    LinearLayout infoLocal = getView().findViewById(R.id.infoLocal);
                    infoLocal.setVisibility(View.VISIBLE);

                    // Ajustar el mapa para que ocupe la mitad de la pantalla
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapFragment.getView().getLayoutParams();
                    params.height = 0; // Ocupa la mitad de la pantalla
                    mapFragment.getView().setLayoutParams(params);

                    // Actualizar la información del layout
                    TextView direccion = getView().findViewById(R.id.direccion);
                    direccion.setText("Dirección: " + sitio.getDireccion());

                    TextView horarios = getView().findViewById(R.id.horarios);
                    horarios.setText("Horarios: " + sitio.getHorarios());

                    // Configurar los botones con los datos del sitio
                    ImageView whatsappIcon = getView().findViewById(R.id.whatsappIcon);
                    whatsappIcon.setOnClickListener(v -> {
                        String whatsappUrl = "https://wa.me/" + sitio.getWhatsapp();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl));
                        startActivity(intent);
                    });

                    ImageView instagramIcon = getView().findViewById(R.id.instagramIcon);
                    instagramIcon.setOnClickListener(v -> {
                        String instagramUrl = "https://www.instagram.com/" + sitio.getInstagram();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                        startActivity(intent);
                    });

                    ImageView mailIcon = getView().findViewById(R.id.mailIcon);
                    mailIcon.setOnClickListener(v -> {
                        String email = "mailto:" + sitio.getCorreo();
                        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(email));
                        startActivity(intent);
                    });

                    return false;  // Devolver false para no cambiar el estado del marcador
                }
            });

            // Restaurar la vista del mapa al hacer clic fuera del marcador
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (mOriginalLatLng != null) {
                        LinearLayout infoLocal = getView().findViewById(R.id.infoLocal);
                        infoLocal.setVisibility(View.GONE);

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOriginalLatLng, 12));

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapFragment.getView().getLayoutParams();
                        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
                        mapFragment.getView().setLayoutParams(params);
                    }
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mapa_sitios, container, false);
    }

    private void loadSitesOnMap() {
        Log.d("Firestore", "Iniciando consulta...");
        db.collection("sitios").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firestore", "Consulta exitosa");
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Sitio sitio = document.toObject(Sitio.class);

                    GeoPoint location = sitio.getUbicacion();
                    if (location != null) {
                        Log.d("Firestore", "Ubicación obtenida: Lat=" + location.getLatitude() + ", Lng=" + location.getLongitude());
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(sitio.getNombre())
                                .snippet(sitio.getDescripcion()));

                        marker.setTag(sitio);

                        // Mover la cámara para ver el marcador
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                    } else {
                        Log.d("Firestore", "Ubicación no disponible para el sitio: " + sitio.getNombre());
                    }
                }
            } else {
                Log.e("Firestore", "Error al cargar sitios: ", task.getException());
                Toast.makeText(getContext(), "Error al cargar sitios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Mapa de Sitios");
        }

        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button);
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());
        });

        // Configuracion del boton perfil
        ImageButton rightUserButton = view.findViewById(R.id.right_user_button);
        rightUserButton.setOnClickListener(v -> {
            navigateToFragment(new EditarPerfilFragment());
        });


        // Configurar el fragmento del mapa
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        } else {
            Log.e("Map", "El fragmento del mapa no se encontró.");
        }

        // Configurar los iconos de redes sociales
        ImageView whatsappIcon = view.findViewById(R.id.whatsappIcon);
        ImageView instagramIcon = view.findViewById(R.id.instagramIcon);
        ImageView mailIcon = view.findViewById(R.id.mailIcon);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }

        Task<android.location.Location> locationResult = fusedLocationClient.getLastLocation();
        locationResult.addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                android.location.Location currentLocation = task.getResult();
                if (currentLocation != null) {
                    LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }
            } else {
                Log.d("Location", "Error al obtener la ubicación.");
            }
        });
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }


    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
