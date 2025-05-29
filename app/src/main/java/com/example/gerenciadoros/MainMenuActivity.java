package com.example.gerenciadoros;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class MainMenuActivity extends AppCompatActivity {
    Button btnNovaOS, btnListarOS, btnSair;
    TextView txtSaudacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        txtSaudacao = findViewById(R.id.txtSaudacao);
        btnNovaOS = findViewById(R.id.btnNovaOS);
        btnListarOS = findViewById(R.id.btnListarOS);
        btnSair = findViewById(R.id.btnSair);

        btnNovaOS.setOnClickListener(v -> startActivity(new Intent(this, NovaOrdemActivity.class)));
        btnListarOS.setOnClickListener(v -> startActivity(new Intent(this, ListaOrdensActivity.class)));
        btnSair.setOnClickListener(v -> finish());
    }
}