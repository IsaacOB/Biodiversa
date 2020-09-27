package com.example.biodiversa;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnCapture;
    ImageView imageView;
    Uri imagem_uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_teste);

        imageView = findViewById(R.id.image_view);
        btnCapture = findViewById(R.id.btn_capture_image);


        //Ação de Click
        btnCapture.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                        || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    String[] permissao = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    requestPermissions(permissao, PERMISSION_CODE);
                } else {
                    abrirCamera();
                }

            }

        });
    }

    private void abrirCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nova foto");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Nova descrição da foto");

        imagem_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imagem_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    abrirCamera();
                } else {
                    Toast.makeText(this, "Permissão de acesso a camera foi negada", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            imageView.setImageURI(imagem_uri);
        }
    }
}
