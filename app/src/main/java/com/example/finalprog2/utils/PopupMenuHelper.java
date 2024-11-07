package com.example.finalprog2.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.example.finalprog2.R;
import com.example.finalprog2.fragment.AutoEvalFragment;
import com.example.finalprog2.fragment.NoticiasMainFragment;
import com.example.finalprog2.fragment.TutorialesMainFragment;
import com.example.finalprog2.fragment.ListarForoFragment;
import com.example.finalprog2.fragment.MapaSitiosFragment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupMenuHelper {

    public static void showPopupMenu(Context context, ImageButton button, FragmentActivity activity) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, button);

        // Inflar el menú
        popupMenu.getMenuInflater().inflate(R.menu.toolbar_menu, popupMenu.getMenu());

        // Habilitar iconos en el menú
        enableIconsInPopupMenu(popupMenu);

        // Manejar el clic en los ítems del menú
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.action_news) {
                replaceFragment(new NoticiasMainFragment(), activity);
                return true;

            } else if (id == R.id.action_sites) {
                replaceFragment(new MapaSitiosFragment(), activity);
                return true;

            } else if (id == R.id.action_tutorials) {
                replaceFragment(new TutorialesMainFragment(), activity);
                return true;

            } else if (id == R.id.action_self_assessment) {
                replaceFragment(new AutoEvalFragment(), activity);
                return true;

            } else if (id == R.id.action_forums) {
                replaceFragment(new ListarForoFragment(), activity);
                return true;
            }

            return false;
        });

        // Aplicar el estilo de fondo (esto depende de la versión de Android)
        try {
            Field mPopupField = PopupMenu.class.getDeclaredField("mPopup");
            mPopupField.setAccessible(true);
            Object menuPopupHelper = mPopupField.get(popupMenu);
            Method setBackgroundDrawableMethod = menuPopupHelper.getClass().getMethod("setBackgroundDrawable", Drawable.class);
            setBackgroundDrawableMethod.invoke(menuPopupHelper, ContextCompat.getDrawable(context, R.color.dark_green));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Mostrar el menú
        popupMenu.show();
    }

    private static void enableIconsInPopupMenu(PopupMenu popupMenu) {
        try {
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menuPopupHelper = popup.get(popupMenu);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void replaceFragment(Fragment fragment, FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}