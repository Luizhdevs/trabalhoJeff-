package com.example.gerenciadoros;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


public class OrdemServicoActivity extends AppCompatActivity {
    TextView txtCliente, txtDescricao, txtData, txtStatus;
    Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordem_servico);

        txtCliente = findViewById(R.id.txtCliente);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtData = findViewById(R.id.txtData);
        txtStatus = findViewById(R.id.txtStatus);
        btnVoltar = findViewById(R.id.btnVoltar);

        OrdemServico os = (OrdemServico) getIntent().getSerializableExtra("ordem");

        if (os != null) {
            txtCliente.setText(os.getCliente());
            txtDescricao.setText(os.getDescricao());
            txtData.setText(os.getData());
            txtStatus.setText(os.getStatus());
        }

        btnVoltar.setOnClickListener(v -> finish());
    }
}