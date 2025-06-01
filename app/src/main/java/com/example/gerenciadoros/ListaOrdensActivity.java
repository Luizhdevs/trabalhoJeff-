package com.example.gerenciadoros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class ListaOrdensActivity extends AppCompatActivity implements OrdemServicoAdapter.OnItemClickListener {

    RecyclerView recyclerViewOrdens;
    TextView txtEmptyList;
    OrdemServicoAdapter adapter;
    ArrayList<OrdemServico> lista;
    SharedPreferences prefs;
    Gson gson = new Gson();
    ActionMode actionMode;
    Button btnApagarSelecionados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ordens);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ordens de Serviço");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerViewOrdens = findViewById(R.id.recyclerViewOrdens);
        txtEmptyList = findViewById(R.id.txtEmptyList);
        btnApagarSelecionados = findViewById(R.id.btnApagarSelecionados);

        prefs = getSharedPreferences("ordens", MODE_PRIVATE);
        loadOrdens();

        setupRecyclerView();
        checkEmptyList();


        btnApagarSelecionados.setOnClickListener(v -> deleteSelectedItems());
    }

    private void loadOrdens() {
        String json = prefs.getString("ordens", "");
        Type type = new TypeToken<ArrayList<OrdemServico>>() {}.getType();
        lista = gson.fromJson(json, type);
        if (lista == null) {
            lista = new ArrayList<>();
        }

        Collections.sort(lista, (o1, o2) -> {
            if (o1.getDataTimestamp() == null && o2.getDataTimestamp() == null) return 0;
            if (o1.getDataTimestamp() == null) return 1; // nulls last
            if (o2.getDataTimestamp() == null) return -1;
            return o2.getDataTimestamp().compareTo(o1.getDataTimestamp()); // Descending order
        });
    }

    private void saveOrdens() {
        String novoJson = gson.toJson(lista);
        prefs.edit().putString("ordens", novoJson).apply();
    }

    private void setupRecyclerView() {
        recyclerViewOrdens.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrdemServicoAdapter(this, lista, this);
        recyclerViewOrdens.setAdapter(adapter);
    }

    private void checkEmptyList() {
        if (lista.isEmpty()) {
            recyclerViewOrdens.setVisibility(View.GONE);
            txtEmptyList.setVisibility(View.VISIBLE);
        } else {
            recyclerViewOrdens.setVisibility(View.VISIBLE);
            txtEmptyList.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadOrdens();
        if (adapter != null) {
            adapter.updateData(lista);
        }
        checkEmptyList();

        if (actionMode != null) {
            actionMode.finish();
        }
    }


    @Override
    public void onItemClick(OrdemServico ordem) {
        if (adapter.isSelectionMode()) {

            int position = lista.indexOf(ordem);
            if (position != -1) {
                toggleSelection(position);
            }
        } else {

            Intent intent = new Intent(this, OrdemServicoActivity.class);
            intent.putExtra("ordem", ordem);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLongClick(int position) {
        if (!adapter.isSelectionMode()) {
            startSupportActionMode(actionModeCallback);
            toggleSelection(position);
        }
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
        int count = adapter.getSelectedItemCount();
        if (count == 0 && actionMode != null) {
            actionMode.finish();
        } else if (actionMode != null) {
            actionMode.setTitle(count + " selecionada(s)");
            actionMode.invalidate();
            btnApagarSelecionados.setVisibility(View.VISIBLE);
        } else {
            btnApagarSelecionados.setVisibility(View.GONE);
        }
    }

    private void deleteSelectedItems() {
        Set<Integer> selectedPositions = adapter.getSelectedItemPositions();
        if (selectedPositions.isEmpty()) {
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar Exclusão")
                .setMessage("Deseja realmente apagar as " + selectedPositions.size() + " ordens selecionadas?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    List<Integer> positionsToRemove = new ArrayList<>(selectedPositions);
                    Collections.sort(positionsToRemove, Collections.reverseOrder());

                    for (int position : positionsToRemove) {
                        if (position >= 0 && position < lista.size()) {
                            lista.remove(position);
                        }
                    }
                    saveOrdens();
                    adapter.updateData(lista);
                    checkEmptyList();
                    Toast.makeText(this, "Ordens apagadas", Toast.LENGTH_SHORT).show();
                    if (actionMode != null) {
                        actionMode.finish();
                    }
                })
                .setNegativeButton("Não", null)
                .show();
    }


    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.context_menu_lista_ordens, menu);
            actionMode = mode;
            adapter.setSelectionMode(true);
            btnApagarSelecionados.setVisibility(View.VISIBLE);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                deleteSelectedItems();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.exitSelectionMode();
            btnApagarSelecionados.setVisibility(View.GONE);
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (actionMode != null) {
                actionMode.finish();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (actionMode != null) {
            actionMode.finish();
        } else {
            super.onBackPressed();
        }
    }
}
