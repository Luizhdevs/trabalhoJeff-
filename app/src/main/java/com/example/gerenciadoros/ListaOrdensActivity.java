package com.example.gerenciadoros;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class ListaOrdensActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<OrdemServico> lista;
    Button btnLimparOrdens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordens);

        listView = findViewById(R.id.listView);
        btnLimparOrdens = findViewById(R.id.btnLimparOrdens);

        SharedPreferences prefs = getSharedPreferences("ordens", MODE_PRIVATE);
        String json = prefs.getString("ordens", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<OrdemServico>>(){}.getType();
        lista = gson.fromJson(json, type);
        if(lista == null) lista = new ArrayList<>();

        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, OrdemServicoActivity.class);
            intent.putExtra("ordem", lista.get(position));
            startActivity(intent);
        });

        btnLimparOrdens.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Confirmar")
                    .setMessage("Deseja realmente apagar todas as ordens de serviço?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        lista.clear();
                        prefs.edit().remove("ordens").apply();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(this, "Ordens apagadas", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }
}