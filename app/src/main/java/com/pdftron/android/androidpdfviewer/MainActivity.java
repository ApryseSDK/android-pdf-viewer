package com.pdftron.android.androidpdfviewer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    ImageView mPdfViewWithAndroid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPdfViewWithAndroid = findViewById(R.id.pdf_view_android);
        Button openWithAndroid = findViewById(R.id.open_pdf_android);
        openWithAndroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPdfWithAndroidSDK();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void openPdfWithAndroidSDK() {
        final File outputFile = new File(getCacheDir(), "sample.pdf");
        try {
            copyToLocalCache(outputFile, R.raw.sample);
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(outputFile, ParcelFileDescriptor.MODE_READ_ONLY);

            PdfRenderer renderer = new PdfRenderer(fileDescriptor);

            PdfRenderer.Page firstPage = renderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(firstPage.getWidth(), firstPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
            firstPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            mPdfViewWithAndroid.setImageBitmap(bitmap);

//            final int pageCount = renderer.getPageCount();
//            for (int i = 0; i < pageCount; i++) {
//                PdfRenderer.Page page = renderer.openPage(i);
//                page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//                page.close();
//            }

            // close the renderer
//            renderer.close();
//            fileDescriptor.close();
        } catch (IOException e) {

        }
    }

    void copyToLocalCache(File outputFile, @RawRes int pdfResource) throws IOException {
        if (!outputFile.exists()) {
            final InputStream pdf = getResources().openRawResource(pdfResource);
            final FileOutputStream output = new FileOutputStream(outputFile);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = pdf.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            pdf.close();
            output.close();
        }
    }
}
