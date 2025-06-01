package com.example.gerenciadoros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout; // Import TextInputLayout

public class RegisterActivity extends AppCompatActivity {
    EditText edtNome, edtEmail, edtSenha, edtConfirmaSenha;
    TextInputLayout tilNome, tilEmail, tilSenha, tilConfirmaSenha; // Add TextInputLayouts
    Button btnCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar); // Assuming toolbar ID in the new layout
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Cadastro de Usuário");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show back button
        }

        // Initialize Views and TextInputLayouts
        tilNome = findViewById(R.id.tilNome);
        tilEmail = findViewById(R.id.tilEmail);
        tilSenha = findViewById(R.id.tilSenha);
        tilConfirmaSenha = findViewById(R.id.tilConfirmaSenha);
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmaSenha = findViewById(R.id.edtConfirmaSenha); // Initialize confirmation field
        btnCadastrar = findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(v -> {
            if (validateInput()) {
                // TODO: Implement password hashing before saving
                String email = edtEmail.getText().toString().trim();
                String senha = edtSenha.getText().toString(); // Get password
                String nome = edtNome.getText().toString().trim();

                SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("nome", nome); // Save user's name
                editor.putString("email", email);
                editor.putString("senha", senha); // Save plain text password (Needs Hashing!)
                editor.apply();

                Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to LoginActivity
            }
        });
    }

    private boolean validateInput() {
        // Clear previous errors
        tilNome.setError(null);
        tilEmail.setError(null);
        tilSenha.setError(null);
        tilConfirmaSenha.setError(null);

        String nome = edtNome.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String senha = edtSenha.getText().toString();
        String confirmaSenha = edtConfirmaSenha.getText().toString();

        boolean isValid = true;

        if (TextUtils.isEmpty(nome)) {
            tilNome.setError("Nome é obrigatório");
            isValid = false;
        }

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("Email é obrigatório");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Email inválido");
            isValid = false;
        }

        if (TextUtils.isEmpty(senha)) {
            tilSenha.setError("Senha é obrigatória");
            isValid = false;
        } else if (senha.length() < 6) { // Example: Minimum password length
            tilSenha.setError("Senha deve ter no mínimo 6 caracteres");
            isValid = false;
        }

        if (TextUtils.isEmpty(confirmaSenha)) {
            tilConfirmaSenha.setError("Confirmação de senha é obrigatória");
            isValid = false;
        } else if (!senha.equals(confirmaSenha)) {
            tilConfirmaSenha.setError("As senhas não coincidem");
            isValid = false;
        }

        return isValid;
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
