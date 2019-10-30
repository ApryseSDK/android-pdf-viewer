package com.pdftron.android.androidpdfviewer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.pdftron.pdf.controls.DocumentActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button openWithAndroid = findViewById(R.id.open_pdf_android);
        openWithAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfWithAndroidSDK();
            }
        });

        Button openWithPDFTron = findViewById(R.id.open_pdf_pdftron);
        openWithPDFTron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfWithPDFTron();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void openPdfWithAndroidSDK() {
        Intent intent = new Intent(this, AndroidPDFViewer.class);
        startActivity(intent);
    }

    void openPdfWithPDFTron() {
        DocumentActivity.openDocument(this, R.raw.sample);
    }
}
