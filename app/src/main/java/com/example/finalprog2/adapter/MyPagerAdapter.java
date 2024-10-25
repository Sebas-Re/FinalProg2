package com.example.finalprog2.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.finalprog2.fragment.noticiasFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Devolver fragmento de ejemplo
        return noticiasFragment.newInstance("Item " + (position + 1)); // Cambiar el nombre según el índice
    }

    @Override
    public int getCount() {
        return 3; // Cambiar con la cantidad total de elementos
    }
}