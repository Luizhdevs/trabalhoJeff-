package com.example.gerenciadoros;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {
    EditText edtEmail, edtSenha;
    Button btnLogin;
    TextView txtCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        txtCadastrar = findViewById(R.id.txtCadastrar);

        btnLogin.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
            String emailSalvo = prefs.getString("email", "");
            String senhaSalva = prefs.getString("senha", "");

            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();


            if(email.equals(emailSalvo) && senha.equals(senhaSalva)) {
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Login inválido", Toast.LENGTH_SHORT).show();
            }
        });

        txtCadastrar.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
