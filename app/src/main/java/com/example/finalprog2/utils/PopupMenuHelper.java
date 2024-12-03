package com.example.finalprog2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import com.example.finalprog2.R;
import com.example.finalprog2.fragment.AutoEvalFragment;
import com.example.finalprog2.fragment.EditarPerfilFragment;
import com.example.finalprog2.fragment.LogInFragment;
import com.example.finalprog2.fragment.NoticiasMainFragment;
import com.example.finalprog2.fragment.TutorialesMainFragment;
import com.example.finalprog2.fragment.ListarForoFragment;
import com.example.finalprog2.fragment.MapaSitiosFragment;
import com.sun.mail.imap.Rights;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupMenuHelper {

    @OptIn(markerClass = UnstableApi.class)
    public static void showPopupMenu(Context context, ImageButton button, FragmentActivity activity) {
        // Crear el PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, button);
        Object tag = button.getTag();

        // Inflar el menú de acuerdo al tag seteado en HomeFragment
        if ("left".equals(tag)) {
            popupMenu.getMenuInflater().inflate(R.menu.toolbar_menu, popupMenu.getMenu());
        }
        else if  ("right".equals(tag)) {
            popupMenu.getMenuInflater().inflate(R.menu.user_menu, popupMenu.getMenu());
        } else {
            throw new IllegalArgumentException("Tag no reconocido para el botón.");
        }


        // Habilitar iconos en el menú
        enableIconsInPopupMenu(popupMenu);

            // Manejar el clic en los ítems del menú
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();

                if ("left".equals(tag)) {

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
                }
                if ("right".equals(tag)) {
                    if (id == R.id.menu_editar_perfil) {
                        // Llamar a la función para navegar al fragmento de editar perfil
                        replaceFragment(new EditarPerfilFragment(), activity);
                        return true;

                    } else if (id == R.id.menu_cerrar_sesion) {
                        // Lógica para cerrar sesión
                        SharedPreferences sharedPreferences = activity.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        String usuario = sharedPreferences.getString("usuario", null);
                        editor.clear();
                        if (usuario != null) {
                            editor.putString("usuario", usuario);
                        }
                        editor.apply();
                        replaceFragment(new LogInFragment(), activity);
                        Toast.makeText(activity, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                        return true;
                    }
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
           Log.e("PopupMenuHelper", "Error al aplicar el estilo de fondo al menú", e);
        }

        // Mostrar el menú
        popupMenu.show();
    }

    @OptIn(markerClass = UnstableApi.class)
    private static void enableIconsInPopupMenu(PopupMenu popupMenu) {
        try {
            Field popup = PopupMenu.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            Object menuPopupHelper = popup.get(popupMenu);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            Log.e("PopupMenuHelper", "Error al habilitar iconos en el menú", e);
        }
    }

    private static void replaceFragment(Fragment fragment, FragmentActivity activity) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

}