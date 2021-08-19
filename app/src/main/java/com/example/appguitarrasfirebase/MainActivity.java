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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    EditText editTextMarca, editTextModelo, editTextCor;
    Button buttonInserir;
    ImageView imageViewGuitarra;
    String[] permissoes = {Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int CAMERA = 1;
    private final int GALERIA = 2;
    AlertDialog.Builder msg;
    Bitmap bitmap;
    DatabaseReference databaseReference;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg = new AlertDialog.Builder(MainActivity.this);
        msg.setNegativeButton("Canvelar",null);
        msg.setMessage("Você precisa conceder ao menos uma permissão");

        editTextMarca = findViewById(R.id.editTextMarca);
        editTextModelo = findViewById(R.id.editTextModelo);
        editTextCor = findViewById(R.id.editTextCor);
        buttonInserir = findViewById(R.id.buttonInserir);
        imageViewGuitarra = findViewById(R.id.imageViewGuitarra);

        imageViewGuitarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissoes.validarPermissoes(permissoes,MainActivity.this,1);

                int permissionGaleria = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                int permissionCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                if (permissionCamera == PackageManager.PERMISSION_GRANTED)
                {
                    msg.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentCamera, CAMERA);
                        }
                    });
                }
                if (permissionGaleria == PackageManager.PERMISSION_GRANTED)
                {
                    msg.setNeutralButton("Galeria", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intentGaleria = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intentGaleria,GALERIA);
                        }
                    });
                }
                if (permissionCamera == PackageManager.PERMISSION_GRANTED && permissionGaleria == PackageManager.PERMISSION_GRANTED)
                    msg.show();
            }
        });

        buttonInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String modelo = editTextModelo.getText().toString();
                String marca = editTextMarca.getText().toString();
                String cor = editTextCor.getText().toString();
                String imagem = uploadImagem(marca,modelo,cor);

                DtoGuitarra guitarra = new DtoGuitarra(modelo,marca,cor,imagem);

                databaseReference = ConfFirebase.getFirebaseDatabase()
                        .child("guitarras").child(modelo);
                databaseReference.setValue(guitarra).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ConsultaActivity.class);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(MainActivity.this, "Falha ao cadastrar: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }

    private String uploadImagem(String marca, String modelo, String cor) {

        storageReference = ConfFirebase.getFirebaseStorage()
                .child("imagens").child("guitarras").child(modelo+".JPEG");

        if (bitmap != null)
        {
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,bais);
            byte[] bytes = bais.toByteArray();

            UploadTask uploadTask = storageReference.putBytes(bytes);
        }
        else
            Toast.makeText(this, "Imagem Não Selecionada!", Toast.LENGTH_SHORT).show();

        return modelo+".JPEG";
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i<permissions.length; i++)
        {
            if (permissions[i].equals("android.permission.CAMERA") && grantResults[i]==0)
            {
                //Camera ok
                msg.setPositiveButton("Camera",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentGaleria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intentGaleria,GALERIA);
                    }
                });
            }

            if (permissions[i].equals("android.permission.READ_EXTERNAL_STORAGE") && grantResults[i]==0)
            {
                //Galeria ok
                msg.setNeutralButton("Galeria",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intentGaleria = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intentGaleria,GALERIA);
                    }
                });
            }
            msg.show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            bitmap = null;
            if (requestCode == CAMERA)
            {
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            else if (requestCode == GALERIA)
            {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
            }

            if (bitmap != null)
                imageViewGuitarra.setImageBitmap(bitmap);
        }
        else
        {
            Toast.makeText(this, "Erro ao carregar imagem", Toast.LENGTH_SHORT).show();
        }
    }

}