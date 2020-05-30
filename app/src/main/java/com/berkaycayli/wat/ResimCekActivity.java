package com.berkaycayli.wat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ResimCekActivity extends AppCompatActivity {

    private Button buttonResimCek;
    private ImageView imageViewResimCek;

    public static final int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resim_cek);

        buttonResimCek = findViewById(R.id.buttonResimCek);
        imageViewResimCek = findViewById(R.id.imageViewResimCek);

    }

    public void resimCek(View v){
        Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageViewResimCek.setImageBitmap(photo);
        }
    }
}
