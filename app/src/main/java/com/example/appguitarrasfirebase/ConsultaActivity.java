package com.example.appguitarrasfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ConsultaActivity extends AppCompatActivity {
    Button buttonNovaGuitarra;
    ListView listViewGuitarras;
    DatabaseReference databaseReference;
    ArrayList<DtoGuitarra> guitarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        buttonNovaGuitarra = findViewById(R.id.buttonNovaGuitarra);
        listViewGuitarras = findViewById(R.id.listViewGuitarras);


        guitarras = new ArrayList<DtoGuitarra>();


        databaseReference = ConfFirebase.getFirebaseDatabase().child("guitarras");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                guitarras.clear();
                for(DataSnapshot prato : snapshot.getChildren()){
                    guitarras.add(prato.getValue(DtoGuitarra.class));
                }
                ArrayAdapter adapter = new ArrayAdapter(
                        ConsultaActivity.this,
                        android.R.layout.simple_list_item_1,
                        guitarras);
                listViewGuitarras.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        listViewGuitarras.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DtoGuitarra guitarra = guitarras.get(position);

                Intent intent = new Intent(ConsultaActivity.this, DetalhesActivity.class);

                intent.putExtra("modelo",guitarra.getModelo());
                intent.putExtra("marca",guitarra.getMarca());
                intent.putExtra("cor",guitarra.getCor());
                intent.putExtra("imagem",guitarra.getImagem());

                startActivity(intent);
            }
        });

        listViewGuitarras.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                DtoGuitarra guitarra = guitarras.get(position);
                databaseReference = ConfFirebase.getFirebaseDatabase()
                        .child("guitarras").child(guitarra.getModelo());
                databaseReference.removeValue();

                return false;
            }
        });



        buttonNovaGuitarra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }
}