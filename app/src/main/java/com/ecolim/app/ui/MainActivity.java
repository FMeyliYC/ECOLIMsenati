package com.ecolim.app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ecolim.app.R;
import com.ecolim.app.ui.fragments.HistorialFragment;
import com.ecolim.app.ui.fragments.RegistroFragment;
import com.ecolim.app.ui.fragments.ReportesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            cargarFragment(new RegistroFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragmentSeleccionado = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_registro) {
                fragmentSeleccionado = new RegistroFragment();
            } else if (itemId == R.id.nav_historial) {
                fragmentSeleccionado = new HistorialFragment();
            } else if (itemId == R.id.nav_reportes) {
                fragmentSeleccionado = new ReportesFragment();
            }

            if (fragmentSeleccionado != null) {
                cargarFragment(fragmentSeleccionado);
                return true;
            }
            return false;
        });
    }

    private void cargarFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
