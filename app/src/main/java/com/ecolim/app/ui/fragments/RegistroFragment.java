package com.ecolim.app.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ecolim.app.R;
import com.ecolim.app.data.local.entities.TipoResiduo;
import com.ecolim.app.data.local.entities.Zona;
import com.ecolim.app.utils.DateUtils;
import com.ecolim.app.utils.SessionManager;
import com.ecolim.app.viewmodel.RegistroViewModel;

import java.util.ArrayList;
import java.util.List;

public class RegistroFragment extends Fragment {

    private RegistroViewModel viewModel;
    private Spinner spTipoResiduo;
    private Spinner spZona;
    private EditText etCantidadKg;
    private EditText etFechaHora;
    private TextView tvEscanearFotoLabel;
    private TextView tvCodigoCapturado;
    private TextView tvEstadoPendientes;
    private ProgressBar pbRegistro;

    private List<TipoResiduo> tiposList = new ArrayList<>();
    private List<Zona> zonasList = new ArrayList<>();

    private String fotoEvidenciaRuta = "";
    private String codigoEscaneado = "";
    private SessionManager sessionManager;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    mostrarDialogoEscanearFoto();
                } else {
                    Toast.makeText(requireContext(), R.string.permiso_camara_necesario, Toast.LENGTH_SHORT).show();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registro, container, false);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(RegistroViewModel.class);

        spTipoResiduo = root.findViewById(R.id.spTipoResiduo);
        spZona = root.findViewById(R.id.spZona);
        etCantidadKg = root.findViewById(R.id.etCantidadKg);
        etFechaHora = root.findViewById(R.id.etFechaHora);
        tvEscanearFotoLabel = root.findViewById(R.id.tvEscanearFotoLabel);
        tvCodigoCapturado = root.findViewById(R.id.tvCodigoCapturado);
        tvEstadoPendientes = root.findViewById(R.id.tvEstadoPendientes);
        pbRegistro = root.findViewById(R.id.pbRegistro);

        // Fecha y hora automática
        actualizarFechaHora();

        // Botón con borde punteado: [Cam] Escanear código / Foto
        root.findViewById(R.id.btnEscanearFoto).setOnClickListener(v -> verificarPermisoCamara());

        // Botón verde: GUARDAR REGISTRO
        root.findViewById(R.id.btnGuardarRegistro).setOnClickListener(v -> procesarGuardado());

        // Botón azul: SINCRONIZAR AHORA
        root.findViewById(R.id.btnSincronizarAhora).setOnClickListener(v -> viewModel.sincronizarAhora());

        configurarObservadores();

        return root;
    }

    private void actualizarFechaHora() {
        etFechaHora.setText(DateUtils.getFechaHoraActual());
    }

    private void configurarObservadores() {
        viewModel.getTiposResiduo().observe(getViewLifecycleOwner(), tipos -> {
            if (tipos != null) {
                tiposList = tipos;
                List<String> nombres = new ArrayList<>();
                nombres.add("Tipo de residuo ▼ (spinner)");
                for (TipoResiduo t : tipos) {
                    nombres.add(t.getNombreResiduo() + " (" + t.getCategoria() + ")");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, nombres);
                spTipoResiduo.setAdapter(adapter);
            }
        });

        viewModel.getZonas().observe(getViewLifecycleOwner(), zonas -> {
            if (zonas != null) {
                zonasList = zonas;
                List<String> nombres = new ArrayList<>();
                nombres.add("Zona / Área ▼ (spinner)");
                for (Zona z : zonas) {
                    nombres.add(z.getNombreZona());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_dropdown_item, nombres);
                spZona.setAdapter(adapter);
            }
        });

        viewModel.getCantidadPendientes().observe(getViewLifecycleOwner(), pendientes -> {
            int count = pendientes != null ? pendientes : 0;
            tvEstadoPendientes.setText("Pendientes de sincronizar: " + count);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            pbRegistro.setVisibility(Boolean.TRUE.equals(loading) ? View.VISIBLE : View.GONE);
        });

        viewModel.getMensajeExito().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null) {
                Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getMensajeError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            mostrarDialogoEscanearFoto();
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void mostrarDialogoEscanearFoto() {
        String[] opciones = {"Simular escaneo de código de barras / QR", "Capturar foto de evidencia", "Ingresar código manual"};
        new AlertDialog.Builder(requireContext())
                .setTitle("Cámara & Trazabilidad")
                .setItems(opciones, (dialog, which) -> {
                    if (which == 0) {
                        // Código simulado para contenedores
                        codigoEscaneado = "ECO-CONT-0" + (int)(Math.random() * 900 + 100);
                        tvCodigoCapturado.setVisibility(View.VISIBLE);
                        tvCodigoCapturado.setText("Código escaneado: " + codigoEscaneado);
                        tvEscanearFotoLabel.setText("[Cam] Código: " + codigoEscaneado);
                        Toast.makeText(requireContext(), "Código detectado: " + codigoEscaneado, Toast.LENGTH_SHORT).show();
                    } else if (which == 1) {
                        fotoEvidenciaRuta = "/storage/emulated/0/Pictures/ecolim_evidencia_" + System.currentTimeMillis() + ".jpg";
                        tvCodigoCapturado.setVisibility(View.VISIBLE);
                        tvCodigoCapturado.setText("Foto guardada: " + fotoEvidenciaRuta);
                        tvEscanearFotoLabel.setText("[Cam] Foto adjuntada ✓");
                        Toast.makeText(requireContext(), "Foto de evidencia adjuntada", Toast.LENGTH_SHORT).show();
                    } else {
                        EditText input = new EditText(requireContext());
                        input.setHint("Ej: ECO-RES-104");
                        new AlertDialog.Builder(requireContext())
                                .setTitle("Código de residuo / contenedor")
                                .setView(input)
                                .setPositiveButton("Aceptar", (d, w) -> {
                                    codigoEscaneado = input.getText().toString().trim();
                                    tvCodigoCapturado.setVisibility(View.VISIBLE);
                                    tvCodigoCapturado.setText("Código: " + codigoEscaneado);
                                    tvEscanearFotoLabel.setText("[Cam] Código: " + codigoEscaneado);
                                })
                                .setNegativeButton("Cancelar", null)
                                .show();
                    }
                })
                .setNegativeButton("Cerrar", null)
                .show();
    }

    private void procesarGuardado() {
        int posTipo = spTipoResiduo.getSelectedItemPosition();
        int posZona = spZona.getSelectedItemPosition();

        if (posTipo <= 0 || posTipo > tiposList.size()) {
            Toast.makeText(requireContext(), "Seleccione un tipo de residuo.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (posZona <= 0 || posZona > zonasList.size()) {
            Toast.makeText(requireContext(), "Seleccione una zona válida.", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantStr = etCantidadKg.getText().toString().trim();
        if (TextUtils.isEmpty(cantStr)) {
            Toast.makeText(requireContext(), "Ingrese la cantidad en kg.", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad;
        try {
            cantidad = Double.parseDouble(cantStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Cantidad numérica inválida.", Toast.LENGTH_SHORT).show();
            return;
        }

        int idTipo = tiposList.get(posTipo - 1).getIdTipo();
        int idZona = zonasList.get(posZona - 1).getIdZona();
        int idUsuario = sessionManager.getUserId();
        String fechaHora = etFechaHora.getText().toString().trim();
        String ubicacion = "-12.0531,-77.0345"; // Coordenadas industriales ECOLIM

        viewModel.guardarRegistro(
                idUsuario,
                idTipo,
                idZona,
                cantidad,
                fechaHora,
                ubicacion,
                fotoEvidenciaRuta
        );

        // Limpiar formulario y regenerar timestamp
        etCantidadKg.setText("");
        spTipoResiduo.setSelection(0);
        spZona.setSelection(0);
        fotoEvidenciaRuta = "";
        codigoEscaneado = "";
        tvCodigoCapturado.setVisibility(View.GONE);
        tvEscanearFotoLabel.setText(R.string.btn_escanear_foto);
        actualizarFechaHora();
    }
}
