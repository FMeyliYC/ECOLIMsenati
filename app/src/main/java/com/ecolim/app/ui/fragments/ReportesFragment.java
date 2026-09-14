package com.ecolim.app.ui.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolim.app.R;
import com.ecolim.app.data.local.entities.ResumenTipoResiduo;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.ui.adapters.ReporteResumenAdapter;
import com.ecolim.app.utils.DateUtils;
import com.ecolim.app.viewmodel.ReportesViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReportesFragment extends Fragment {

    private ReportesViewModel viewModel;
    private EditText etFechaDesde;
    private EditText etFechaHasta;
    private Spinner spTipoResiduoReporte;
    private RecyclerView rvResumenReporte;
    private TextView tvTotalConsolidadoKg;
    private ReporteResumenAdapter adapter;

    private List<TipoResiduo> tiposList = new ArrayList<>();
    private String fechaSqlDesde = "2026-09-01";
    private String fechaSqlHasta = "2026-09-12";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reportes, container, false);

        viewModel = new ViewModelProvider(this).get(ReportesViewModel.class);

        etFechaDesde = root.findViewById(R.id.etFechaDesde);
        etFechaHasta = root.findViewById(R.id.etFechaHasta);
        spTipoResiduoReporte = root.findViewById(R.id.spTipoResiduoReporte);
        rvResumenReporte = root.findViewById(R.id.rvResumenReporte);
        tvTotalConsolidadoKg = root.findViewById(R.id.tvTotalConsolidadoKg);

        rvResumenReporte.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ReporteResumenAdapter();
        rvResumenReporte.setAdapter(adapter);

        // Valores iniciales según el wireframe: Desde 01/09/2026 hasta 12/09/2026
        etFechaDesde.setText("Desde: 01/09/2026");
        etFechaHasta.setText("Hasta: 12/09/2026");

        etFechaDesde.setOnClickListener(v -> mostrarDatePicker(true));
        etFechaHasta.setOnClickListener(v -> mostrarDatePicker(false));

        root.findViewById(R.id.btnFiltrar).setOnClickListener(v -> aplicarFiltro());
        root.findViewById(R.id.btnExportarCompartir).setOnClickListener(v -> exportarCompartirReporte());

        configurarObservadores();

        // Aplicar filtro inicial de manera predeterminada
        aplicarFiltro();

        return root;
    }

    private void mostrarDatePicker(boolean esDesde) {
        Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            String fechaVisual = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month + 1, year);
            String fechaSql = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

            if (esDesde) {
                etFechaDesde.setText("Desde: " + fechaVisual);
                fechaSqlDesde = fechaSql;
            } else {
                etFechaHasta.setText("Hasta: " + fechaVisual);
                fechaSqlHasta = fechaSql;
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void configurarObservadores() {
        viewModel.getTiposResiduo().observe(getViewLifecycleOwner(), tipos -> {
            if (tipos != null) {
                tiposList = tipos;
                List<String> nombres = new ArrayList<>();
                nombres.add("Tipo de residuo ▼ (todos)");
                for (TipoResiduo t : tipos) {
                    nombres.add(t.getNombreResiduo());
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, nombres);
                spTipoResiduoReporte.setAdapter(spinnerAdapter);
            }
        });

        viewModel.getResumenReporte().observe(getViewLifecycleOwner(), resumen -> {
            if (resumen != null) {
                adapter.setItems(resumen);
            }
        });

        viewModel.getTotalKgPeriodo().observe(getViewLifecycleOwner(), total -> {
            double valor = total != null ? total : 0.0;
            tvTotalConsolidadoKg.setText(String.format(Locale.getDefault(), "%.1f kg", valor));
        });
    }

    private void aplicarFiltro() {
        int pos = spTipoResiduoReporte.getSelectedItemPosition();
        int idTipo = 0;
        if (pos > 0 && pos <= tiposList.size()) {
            idTipo = tiposList.get(pos - 1).getIdTipo();
        }

        viewModel.aplicarFiltros(fechaSqlDesde, fechaSqlHasta, idTipo);
    }

    private void exportarCompartirReporte() {
        List<ResumenTipoResiduo> items = adapter.getItems();
        if (items.isEmpty()) {
            Toast.makeText(requireContext(), "No hay datos para exportar en este período.", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE RECOLECCIÓN ECOLIM S.A.C. ===\n");
        sb.append("Período: ").append(fechaSqlDesde).append(" al ").append(fechaSqlHasta).append("\n");
        sb.append("Generado el: ").append(DateUtils.getFechaHoraActual()).append("\n\n");
        sb.append("TIPO DE RESIDUO | TOTAL (KG)\n");
        sb.append("------------------------------------------\n");

        double granTotal = 0.0;
        for (ResumenTipoResiduo item : items) {
            sb.append(String.format(Locale.getDefault(), "%-18s : %8.1f kg\n", item.getNombreResiduo(), item.getTotalKg()));
            granTotal += item.getTotalKg();
        }
        sb.append("------------------------------------------\n");
        sb.append(String.format(Locale.getDefault(), "TOTAL CONSOLIDADO  : %8.1f kg\n", granTotal));

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Reporte Recolección ECOLIM");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(shareIntent, "Exportar / Compartir reporte con:"));
    }
}
