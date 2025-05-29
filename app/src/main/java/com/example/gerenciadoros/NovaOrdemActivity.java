package com.example.gerenciadoros;


import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class NovaOrdemActivity extends AppCompatActivity {
    EditText edtCliente, edtDescricao, edtData;
    Button btnSalvar;
    ArrayList<OrdemServico> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ordem);

        edtCliente = findViewById(R.id.edtCliente);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtData = findViewById(R.id.edtData);
        btnSalvar = findViewById(R.id.btnSalvar);

        SharedPreferences prefs = getSharedPreferences("ordens", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("ordens", "");
        Type type = new TypeToken<ArrayList<OrdemServico>>(){}.getType();
        lista = gson.fromJson(json, type);
        if(lista == null) lista = new ArrayList<>();

        btnSalvar.setOnClickListener(v -> {
            OrdemServico os = new OrdemServico(
                    edtCliente.getText().toString(),
                    edtDescricao.getText().toString(),
                    edtData.getText().toString(),
                    "Aberta"
            );
            lista.add(os);
            String novoJson = gson.toJson(lista);
            prefs.edit().putString("ordens", novoJson).apply();
            finish();
        });
    }
}