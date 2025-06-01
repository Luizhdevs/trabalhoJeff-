package com.example.gerenciadoros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class OrdemServicoActivity extends AppCompatActivity {
    TextView txtCliente, txtDescricao, txtData, txtStatus;
    // Button btnVoltar; // Button removed, Toolbar handles back navigation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Using the redesigned layout
        setContentView(R.layout.activity_ordem_servico);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); // Assuming toolbar ID in the new layout
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detalhes da Ordem");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        }

        txtCliente = findViewById(R.id.txtCliente); // Ensure these IDs match the new layout
        txtDescricao = findViewById(R.id.txtDescricao);
        txtData = findViewById(R.id.txtData);
        txtStatus = findViewById(R.id.txtStatus);
        // btnVoltar = findViewById(R.id.btnVoltar); // Button removed

        OrdemServico os = (OrdemServico) getIntent().getSerializableExtra("ordem");

        if (os != null) {
            txtCliente.setText(os.getCliente());
            txtDescricao.setText(os.getDescricao());
            // Use the correct formatting method from OrdemServico class
            txtData.setText(os.getDataFormatada()); // Using the long format for details view
            txtStatus.setText(os.getStatus());
            // TODO: Set status background/style based on os.getStatus()
        }

        // Button click listener removed as Toolbar handles back navigation
        // btnVoltar.setOnClickListener(v -> finish());
    }

    // Handle ActionBar back button press
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Close activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
