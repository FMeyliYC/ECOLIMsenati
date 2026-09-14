package com.ecolim.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ecolim.app.R;
import com.ecolim.app.data.local.AppDatabase;
import com.ecolim.app.data.local.entities.Usuario;
import com.ecolim.app.utils.SecurityUtils;
import com.ecolim.app.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario;
    private EditText etPassword;
    private CheckBox chkRecordarSesion;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        if (sessionManager.isLoggedIn()) {
            abrirMainActivity();
            return;
        }

        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        chkRecordarSesion = findViewById(R.id.chkRecordarSesion);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.btnIngresar).setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String usuarioOIdentificador = etUsuario.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(usuarioOIdentificador) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            Usuario usuario = db.usuarioDao().autenticar(usuarioOIdentificador);

            runOnUiThread(() -> {
                progressBar.setVisibility(View.GONE);

                if (usuario != null && SecurityUtils.verificarPassword(password, usuario.getPassword())) {
                    boolean recordar = chkRecordarSesion.isChecked();
                    sessionManager.createLoginSession(
                            usuario.getIdUsuario(),
                            usuario.getNombre(),
                            usuario.getRol(),
                            recordar
                    );

                    Toast.makeText(LoginActivity.this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                    abrirMainActivity();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.error_credenciales, Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    private void abrirMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
