package com.example.gerenciadoros;


import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {
    EditText edtNome, edtEmail, edtSenha;
    Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", edtEmail.getText().toString());
            editor.putString("senha", edtSenha.getText().toString());
            editor.apply();

            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}