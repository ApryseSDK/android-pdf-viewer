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
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_pdfviewer);

        mImageView = findViewById(R.id.pdf_view_android);
        try {
            openPdfWithAndroidSDK(mImageView, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPdfPage != null) {
            mPdfPage.close();
        }
        if (mPdfRenderer != null) {
            mPdfRenderer.close();
        }
    }

    /**
     * Render a given page in the PDF document into an ImageView.
     *
     * @param imageView  used to display the PDF
     * @param pageNumber page of the PDF to view (index starting at 0)
     */
    void openPdfWithAndroidSDK(ImageView imageView, int pageNumber) throws IOException {
        // Copy sample.pdf from raw resource folder into local cache, so PdfRenderer can handle it
        File fileCopy = new File(getCacheDir(), FILE_NAME);
        copyToLocalCache(fileCopy, R.raw.sample);

        // We will get a page from the PDF file by calling PdfRenderer.openPage
        ParcelFileDescriptor fileDescriptor =
                ParcelFileDescriptor.open(fileCopy,
                        ParcelFileDescriptor.MODE_READ_ONLY);
        mPdfRenderer = new PdfRenderer(fileDescriptor);
        mPdfPage = mPdfRenderer.openPage(pageNumber);

        // Create a new bitmap and render the page contents on to it
        Bitmap bitmap = Bitmap.createBitmap(mPdfPage.getWidth(),
                mPdfPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        // Set the bitmap in the ImageView so we can view it
        imageView.setImageBitmap(bitmap);
    }

    /**
     * Copies the resource PDF file locally so that {@link PdfRenderer} can handle the file
     *
     * @param outputFile  location of copied file
     * @param pdfResource pdf resource file
     */
    void copyToLocalCache(File outputFile, @RawRes int pdfResource) throws IOException {
        if (!outputFile.exists()) {
            InputStream input = getResources().openRawResource(pdfResource);
            FileOutputStream output;
            output = new FileOutputStream(outputFile);
            byte[] buffer = new byte[1024];
            int size;
            // Just copy the entire contents of the file
            while ((size = input.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            input.close();
            output.close();
        }
    }
}
