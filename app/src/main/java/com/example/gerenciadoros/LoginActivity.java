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
    //declarando os elementos que vai usar no código
    EditText edtEmail, edtSenha;
    Button btnLogin;
    TextView txtCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Carrega a tela com o layout activity_login.xml.
        setContentView(R.layout.activity_login);

        //Conecta os elementos do XML aos objetos no código.
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogin = findViewById(R.id.btnLogin);
        txtCadastrar = findViewById(R.id.txtCadastrar);

        btnLogin.setOnClickListener(v -> {
            //Acessa as informações salvas em SharedPreferences (como se fosse um pequeno banco de dados local).
            SharedPreferences prefs = getSharedPreferences("usuarios", MODE_PRIVATE);
            //Compara o email e a senha digitados com os valores salvos.
            String emailSalvo = prefs.getString("email", "");
            String senhaSalva = prefs.getString("senha", "");

            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();


            if(email.equals(emailSalvo) && senha.equals(senhaSalva)) {
                //Se forem iguais, abre a próxima tela MainMenuActivity.
                startActivity(new Intent(this, MainMenuActivity.class));
                finish();
            } else {
                //Se forem diferentes, mostra um aviso "Login inválido".
                Toast.makeText(this, "Login inválido", Toast.LENGTH_SHORT).show();
            }
        });

        txtCadastrar.setOnClickListener(v -> {
            //Ao clicar no texto, o usuário vai para a tela de cadastro (RegisterActivity).
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
