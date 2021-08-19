package com.example.appguitarrasfirebase;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Permissoes {
    public static void validarPermissoes(String[] permissoes, Activity activity, int requestCode)
    {
        if (Build.VERSION.SDK_INT >=23)
        {
            ArrayList<String> listaPermissoes = new ArrayList<>();

            //Verificando permissões já concedidas
            for (String permissao: permissoes) {
                boolean temPermissao =
                        ContextCompat.checkSelfPermission(activity, permissao)
                                ==
                                PackageManager.PERMISSION_GRANTED;
                if (!temPermissao) {
                    listaPermissoes.add(permissao);
                }
            }

            if (!listaPermissoes.isEmpty())
            {
                String[] vetorPermissoes = new String[listaPermissoes.size()];
                listaPermissoes.toArray(vetorPermissoes);
                ActivityCompat.requestPermissions(activity,vetorPermissoes,requestCode);
            }
        }
    }
}


