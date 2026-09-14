package com.ecolim.app.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolim.app.R;
import com.ecolim.app.repository.RecoleccionRepository;
import com.ecolim.app.ui.adapters.RecoleccionAdapter;

public class HistorialFragment extends Fragment {

    private RecyclerView rvHistorial;
    private TextView tvSinDatos;
    private RecoleccionAdapter adapter;
    private RecoleccionRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historial, container, false);

        rvHistorial = root.findViewById(R.id.rvHistorial);
        tvSinDatos = root.findViewById(R.id.tvSinDatos);

        rvHistorial.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecoleccionAdapter();
        rvHistorial.setAdapter(adapter);

        repository = RecoleccionRepository.getInstance(requireContext());
        repository.getTodasLasRecolecciones().observe(getViewLifecycleOwner(), recolecciones -> {
            if (recolecciones != null && !recolecciones.isEmpty()) {
                adapter.setRecolecciones(recolecciones);
                rvHistorial.setVisibility(View.VISIBLE);
                tvSinDatos.setVisibility(View.GONE);
            } else {
                rvHistorial.setVisibility(View.GONE);
                tvSinDatos.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }
}
