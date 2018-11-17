package com.pdftron.android.androidpdfviewer;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RawRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Activity that opens the first page of a PDF document using {@link PdfRenderer}
 * and displays it in an {@link ImageView}.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class AndroidPDFViewer extends AppCompatActivity {

    private static final String FILE_NAME = "sample_cache.pdf";
    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pdfviewer);

        ImageView pdfViewer = findViewById(R.id.pdf_view_android);
        openPdfWithAndroidSDK(pdfViewer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPdfRenderer.close();
        mPdfPage.close();
    }

    /**
     * Opens the first page of the PDF document.
     * @param imageView used to display the PDF
     */
    void openPdfWithAndroidSDK(ImageView imageView) {
        final File pdfFile = new File(getCacheDir(), FILE_NAME);
        try {
            // Copy the PDF file in resource folder locally so PdfRenderer can handle it
            copyToLocalCache(pdfFile, R.raw.sample);
            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
            // PdfRenderer we will use to
            mPdfRenderer = new PdfRenderer(fileDescriptor);
            mPdfPage = mPdfRenderer.openPage(0);
            Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(), mPdfPage.getHeight(),
                    Bitmap.Config.ARGB_8888);
            mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies the resource PDF file locally so that {@link PdfRenderer} can handle the file
     *
     * @param outputFile  location of copied file
     * @param pdfResource pdf resource file
     */
    void copyToLocalCache(File outputFile, @RawRes int pdfResource) {
        if (!outputFile.exists()) {
            try {
                final InputStream pdf = getResources().openRawResource(pdfResource);
                final FileOutputStream output;
                output = new FileOutputStream(outputFile);
                final byte[] buffer = new byte[1024];
                int size;
                while ((size = pdf.read(buffer)) != -1) {
                    output.write(buffer, 0, size);
                }
                pdf.close();
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
