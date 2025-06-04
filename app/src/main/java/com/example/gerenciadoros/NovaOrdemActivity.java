package com.example.gerenciadoros;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem; 
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar; // Import Toolbar

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NovaOrdemActivity extends AppCompatActivity {
    EditText edtCliente, edtDescricao, edtData;
    Button btnSalvar;
    ArrayList<OrdemServico> lista;
    Calendar calendar = Calendar.getInstance();
    //Define os campos da tela e uma lista de ordens de serviço (lista) onde os dados serão armazenados.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_ordem);

        //Mostra o título e ativa o botão de voltar no topo da tela.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Nova Ordem de Serviço");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Conecta os campos do XML com o código.
        edtCliente = findViewById(R.id.edtCliente);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtData = findViewById(R.id.edtData);
        btnSalvar = findViewById(R.id.btnSalvar);

        //Mostra a data atual no campo de data ao abrir a tela.
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        edtData.setText(sdf.format(calendar.getTime()));

        //Quando o usuário toca no campo de data, um calendário aparece para ele escolher o dia. A data escolhida é exibida no campo.
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            edtData.setText(sdf.format(calendar.getTime()));
        };

        edtData.setOnClickListener(v -> {
            new DatePickerDialog(NovaOrdemActivity.this,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        edtData.setFocusable(false);

        //Busca as ordens já salvas (caso existam).
        SharedPreferences prefs = getSharedPreferences("ordens", MODE_PRIVATE);
        //Usa a biblioteca Gson para converter o JSON de volta para ArrayList<OrdemServico>.
        Gson gson = new Gson();
        String json = prefs.getString("ordens", "");
        Type type = new TypeToken<ArrayList<OrdemServico>>(){}.getType();
        lista = gson.fromJson(json, type);
        //Se não houver nada salvo, cria uma lista nova.
        if(lista == null) lista = new ArrayList<>();

        btnSalvar.setOnClickListener(v -> {
            String cliente = edtCliente.getText().toString().trim();
            String descricao = edtDescricao.getText().toString().trim();
            String dataStr = edtData.getText().toString();

            // Validação dos campos
            if (TextUtils.isEmpty(cliente)) {
                edtCliente.setError("Campo obrigatório");
                return;
            }
            if (TextUtils.isEmpty(descricao)) {
                edtDescricao.setError("Campo obrigatório");
                return;
            }


            Long dataTimestamp = calendar.getTimeInMillis();
            // Criação da nova ordem
            OrdemServico os = new OrdemServico(
                    cliente,
                    descricao,
                    dataTimestamp,
                    "Aberta"
            );
            // Adiciona à lista e salva no SharedPreferences
            lista.add(os);
            String novoJson = gson.toJson(lista);
            prefs.edit().putString("ordens", novoJson).apply();
            //Mostra uma mensagem e fecha a tela.
            Toast.makeText(this, "Ordem de Serviço salva com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    //Se o usuário clicar na setinha para voltar, a tela se fecha normalmente.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
