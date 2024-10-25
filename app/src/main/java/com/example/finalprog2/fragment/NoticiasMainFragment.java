package com.example.finalprog2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.finalprog2.R;
import com.example.finalprog2.adapter.MyPagerAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

public class NoticiasMainFragment extends Fragment {

    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private Button btn_mostrarMas;
    ImageView img_encabezado;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_noticias_main, container, false);

        // Inicializa la Toolbar
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        // Inicializa el ViewPager
        viewPager = view.findViewById(R.id.viewPager);
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager()); // Usa getChildFragmentManager
        viewPager.setAdapter(myPagerAdapter);

        // Inicializa el botón "Ver más"
        btn_mostrarMas = view.findViewById(R.id.btn_mostrarMas);
        btn_mostrarMas.setOnClickListener(v -> {
            // Maneja el clic del botón "Ver más"
            // Aquí puedes implementar la acción que deseas cuando se clickea el botón
        });

        ImageView img_encabezado = view.findViewById(R.id.img_encabezado);
        img_encabezado.setOnClickListener(v -> {
            // Acciones al hacer clic en la imagen
        });

        return view; // Devuelve la vista inflada
    }
}