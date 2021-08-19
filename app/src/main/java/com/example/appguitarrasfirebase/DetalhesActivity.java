package com.example.appguitarrasfirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetalhesActivity extends AppCompatActivity {
    EditText editTextMarca, editTextModelo, editTextCor;
    Button buttonAlterar;
    ImageView imageViewGuitarra;
    String[] permissoes = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int CAMERA = 1;
    private final int GALERIA = 2;
    AlertDialog.Builder msg;
    Bitmap bitmap;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String modelo, marca, cor, imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        editTextMarca = findViewById(R.id.editTextMarca2);
        editTextModelo = findViewById(R.id.editTextModelo2);
        editTextCor = findViewById(R.id.editTextCor2);
        imageViewGuitarra = findViewById(R.id.imageViewGuitarra2);
        buttonAlterar = findViewById(R.id.buttonAlterar);

        Bundle bundle = getIntent().getExtras();
        modelo = bundle.getString("modelo");
        marca = bundle.getString("marca");
        cor = bundle.getString("cor");

        editTextMarca.setText(marca);
        editTextModelo.setText(modelo);
        editTextCor.setText(cor);

        storageReference = ConfFirebase.getFirebaseStorage();
        imagem = bundle.getString("imagem");
        storageReference = ConfFirebase.getFirebaseStorage()
                .child("imagens").child("guitarras").child(imagem);



        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri.toString()).into(imageViewGuitarra);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetalhesActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });




        buttonAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marca = editTextMarca.getText().toString();
                cor = editTextCor.getText().toString();

                DtoGuitarra guitarra = new DtoGuitarra(modelo,marca,cor,imagem);

                databaseReference = ConfFirebase.getFirebaseDatabase()
                        .child("guitarras").child(modelo);
                databaseReference.setValue(guitarra).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(DetalhesActivity.this, "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DetalhesActivity.this, ConsultaActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(DetalhesActivity.this, "Falha ao alterar: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

}