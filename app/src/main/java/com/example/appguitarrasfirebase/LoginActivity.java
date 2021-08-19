package com.example.appguitarrasfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText editTextEmail = findViewById(R.id.editTextEmail);
        final EditText editTextSenha = findViewById(R.id.editTextSenha);
        final Button buttonLogin = findViewById(R.id.buttonLogin);
        final TextView textViewCadastro = findViewById(R.id.textViewCadastro);


        textViewCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, CadastrarUsuarioActivity.class);
                startActivity(intent);
            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = ConfFirebase.getFirebaseAuth();
                firebaseAuth.signInWithEmailAndPassword(
                        editTextEmail.getText().toString(),
                        editTextSenha.getText().toString()
                ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, ConsultaActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(LoginActivity.this, "Email e/ou senha incorreto(s)!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}