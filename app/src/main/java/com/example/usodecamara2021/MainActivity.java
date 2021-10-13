package com.example.usodecamara2021;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String[] permisos = {Manifest.permission.CAMERA};
    private ImageView foto;
    private boolean hayPermiso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hayPermiso = false;

        foto = findViewById(R.id.fotito);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            requestPermissions(permisos, 100);
        }

    }

    private void comprobarPermisos(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permisos OK", Toast.LENGTH_SHORT).show();
                hayPermiso = true;
            } else {
                solicitarPermisos();
            }
        }
    }

    private void solicitarPermisos(){
        new AlertDialog.Builder(this)
                .setTitle("Se requiere permisos de cámara")
                .setMessage("Esta aplicación hace uso de la cámara para tomar una foto. Por favor " +
                        "otorgue este permiso para que la aplicación funcione correctamente")
                .setPositiveButton("Ver Permiso", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permisos, 100);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Se otorgaron permisos de cámara", Toast.LENGTH_SHORT).show();
                hayPermiso = true;
            } else {
                Toast.makeText(this, "Se requiere permisos de cámara para funcionar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void tomarFoto(View view){
        comprobarPermisos();

        if(hayPermiso){
            Intent intento = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intento, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap b = (Bitmap) data.getExtras().get("data");
            foto.setImageBitmap(b);
        }
    }
}