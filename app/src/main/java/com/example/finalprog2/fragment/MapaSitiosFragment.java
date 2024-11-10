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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprog2.R;
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


public class MapaSitiosFragment extends Fragment {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private SupportMapFragment mapFragment; // Mover la declaración aquí
    private LatLng mOriginalLatLng;  // Para almacenar la ubicación original del mapa

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Cargar el estilo de mapa desde el archivo JSON
            try {
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.map_style));
                if (!success) {
                    Toast.makeText(getContext(), "No se pudo aplicar el estilo del mapa.", Toast.LENGTH_SHORT).show();
                }
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

            mMap.getUiSettings().setZoomControlsEnabled(true);       // Controles de zoom
            mMap.getUiSettings().setCompassEnabled(true);            // Brújula
            mMap.getUiSettings().setZoomGesturesEnabled(true);       // Gesto de zoom
            mMap.getUiSettings().setScrollGesturesEnabled(true);

            // Verificar permisos y obtener la ubicación
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true); // Habilitar el marcador de ubicación
            } else {
                // Si no se tienen permisos, pedirlos
                requestLocationPermission();
            };

            LatLng puntoInteres = new LatLng(-34.6037, -58.3816); // Ejemplo: coordenadas de Buenos Aires
            mMap.addMarker(new MarkerOptions()
                    .position(puntoInteres)
                    .title("Punto de Interés")
                    .snippet("Descripción del lugar opcional"));

            // Mueve la cámara al punto de interés
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoInteres, 12));

            // Guardar la posición original del mapa después de mover la cámara
            mOriginalLatLng = mMap.getCameraPosition().target;  // Asegúrate de que no sea null

            // Configuración del listener para clic en los marcadores
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    // Obtener las coordenadas del marcador
                    LatLng markerPosition = marker.getPosition();

                    // Mostrar la información del local
                    LinearLayout infoLocal = getView().findViewById(R.id.infoLocal);
                    infoLocal.setVisibility(View.VISIBLE);

                    // Ajustar el mapa para que ocupe la mitad de la pantalla
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapFragment.getView().getLayoutParams();
                    params.height = 0; // Ocupa la mitad
                    mapFragment.getView().setLayoutParams(params);

                    // Actualizar los textos con la información del marcador
                    TextView direccion = getView().findViewById(R.id.direccion);
                    direccion.setText("Dirección: " + marker.getTitle());

                    TextView horarios = getView().findViewById(R.id.horarios);
                    horarios.setText("Horarios: Lunes a Viernes 9:00 - 18:00");

                    return false;  // Devuelve false para que el marcador no cambie su estado automáticamente
                }
            });

            // Listener para hacer clic fuera del pin
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (mOriginalLatLng != null) {
                        // Restaurar la visibilidad de la información del local
                        LinearLayout infoLocal = getView().findViewById(R.id.infoLocal);
                        infoLocal.setVisibility(View.GONE);

                        // Volver a la posición original del mapa
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOriginalLatLng, 12));

                        // Restaurar la altura del fragmento de mapa (para que ocupe toda la pantalla de nuevo)
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mapFragment.getView().getLayoutParams();
                        params.height = LinearLayout.LayoutParams.MATCH_PARENT;  // Restaura el tamaño original
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar el Toolbar
        Toolbar toolbar = view.findViewById(R.id.custom_toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Configurar el título
        TextView toolbarTitle = toolbar.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText("Mapa de Sitios");
        }

        ImageButton leftMenuButton = view.findViewById(R.id.left_menu_button); // Tu ImageButton

        // Configurar evento de clic para el botón del menú
        leftMenuButton.setOnClickListener(v -> {
            PopupMenuHelper.showPopupMenu(getContext(), leftMenuButton, requireActivity());

        });

        // Configurar el fragmento del mapa
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map); // Inicializa mapFragment aquí
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        // Configurar iconos de redes sociales con links
        ImageView whatsappIcon = view.findViewById(R.id.whatsappIcon);
        ImageView instagramIcon = view.findViewById(R.id.instagramIcon);
        ImageView mailIcon = view.findViewById(R.id.mailIcon);

        // Configurar el click para WhatsApp
        whatsappIcon.setOnClickListener(v -> {
            String whatsappUrl = "https://wa.me/1165188743"; // Cambia el número por el tuyo
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(whatsappUrl));
            startActivity(intent);
        });

        // Configurar el click para Instagram
        instagramIcon.setOnClickListener(v -> {
            String instagramUrl = "https://www.instagram.com/manu_pais_"; // Cambia por tu URL de Instagram
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
            startActivity(intent);
        });

        // Configurar el click para Mail
        mailIcon.setOnClickListener(v -> {
            String email = "mailto:manupais22gmail.com"; // Cambia por tu correo
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(email));
            startActivity(intent);
        });

        // Inicializar el FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
    }

    private void getDeviceLocation() {
        // Obtener la ubicación actual
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<android.location.Location>() {
                    @Override
                    public void onComplete(@NonNull Task<android.location.Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            android.location.Location currentLocation = task.getResult();
                            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            // Mover la cámara a la ubicación del dispositivo
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                            // Agregar marcador en la ubicación actual
                            mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Tu ubicación"));

                        } else {
                            Toast.makeText(getContext(), "No se pudo obtener la ubicación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void requestLocationPermission() {
        // Solicitar permiso de ubicación
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    // Callback para manejar el resultado de la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso otorgado, intentar obtener la ubicación
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getDeviceLocation();
                }
            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
