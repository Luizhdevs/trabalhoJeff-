package com.example.gerenciadoros;

import androidx.appcompat.app.AlertDialog; // Import AlertDialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends AppCompatActivity {
    Button btnNovaOS, btnListarOS, btnSair;
    TextView txtSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Menu Principal");

        }

        txtSaudacao = findViewById(R.id.txtSaudacao);
        btnNovaOS = findViewById(R.id.btnNovaOS);
        btnListarOS = findViewById(R.id.btnListarOS);
        btnSair = findViewById(R.id.btnSair);


        SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
        String nomeUsuario = prefs.getString("nome", "Usuário");
        txtSaudacao.setText("Bem-vindo(a), " + nomeUsuario + "!");


        btnNovaOS.setOnClickListener(v -> startActivity(new Intent(this, NovaOrdemActivity.class)));
        btnListarOS.setOnClickListener(v -> startActivity(new Intent(this, ListaOrdensActivity.class)));


        btnSair.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar Saída")
                    .setMessage("Deseja realmente sair do aplicativo?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        finishAffinity();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });

    }
}
