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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastrarUsuarioActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        final EditText editTextEmail2 = findViewById(R.id.editTextEmail2);
        final EditText editTextSenha2 = findViewById(R.id.editTextSenha2);
        final Button buttonCadastrarUsuario = findViewById(R.id.buttonCadastrarUsuario);

        buttonCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail2.getText().toString();
                String senha = editTextSenha2.getText().toString();

                firebaseAuth = ConfFirebase.getFirebaseAuth();

                firebaseAuth.createUserWithEmailAndPassword(email,senha)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(
                                    CadastrarUsuarioActivity.this, LoginActivity.class);

                        }
                        else
                        {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException ex) {
                                Toast.makeText(CadastrarUsuarioActivity.this, "Senha Fraca", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthEmailException ex) {
                                Toast.makeText(CadastrarUsuarioActivity.this, "Padrão email inválido", Toast.LENGTH_SHORT).show();
                            } catch (FirebaseAuthUserCollisionException ex) {
                                Toast.makeText(CadastrarUsuarioActivity.this, "Esse usuário já está cadastrado", Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(CadastrarUsuarioActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }
}