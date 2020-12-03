package com.jimac.vetclinicapp.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Base64;
import androidx.core.content.FileProvider;
import com.jimac.vetclinicapp.R;
import com.yalantis.ucrop.UCrop;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private final Activity mActivity;

    private String mImagePath = "";

    public ImageUtils(final Activity activity) {
        mActivity = activity;
    }

    public String convertImageToBase64(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 70, baos); // bm is the bitmap object
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT | Base64.NO_WRAP);
    }

    public String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size / 1024.0;
        double m = ((size / 1024.0) / 1024.0);
        double g = (((size / 1024.0) / 1024.0) / 1024.0);
        double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if (t > 1) {
            hrSize = dec.format(t).concat(" TB");
        } else if (g > 1) {
            hrSize = dec.format(g).concat(" GB");
        } else if (m > 1) {
            hrSize = dec.format(m).concat(" MB");
        } else if (k > 1) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }

    public Bitmap getBitmapFromUri(Uri uri) throws IOException {
        return MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);
    }

    public int getFileSizeInKb(File file) {
        return Integer.parseInt(String.valueOf(file.length() / 1024));
    }

    public void getImageFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mActivity.startActivityForResult(pickPhoto, RequestCodes.OPEN_GALLERY);
    }

    public String getImagePath() {
        return mImagePath;
    }

    public long getImageSizeInKb(Uri uri) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap bitmap = getBitmapFromUri(uri);
        int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        scaled.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] imageInByte = stream.toByteArray();
        return imageInByte.length;
    }

    public void openCamera() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(mActivity.getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider
                        .getUriForFile(mActivity, "com.jimac.vetclinicapp.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                mActivity.startActivityForResult(pictureIntent, RequestCodes.OPEN_CAMERA);
            }
        }
    }

    public void startImageCrop(Uri sourceUri) {
        UCrop uCrop = UCrop.of(sourceUri, Uri.fromFile(
                new File(mActivity.getCacheDir(), "temp" + "_" + System.currentTimeMillis() + ".jpg")));
        uCrop.withOptions(getCropOptions());
        uCrop.start(mActivity);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mImagePath = image.getAbsolutePath();
        return image;
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(mActivity.getResources().getColor(R.color.color_primary_variant));
        options.setToolbarColor(mActivity.getResources().getColor(R.color.color_primary));

        options.setToolbarTitle("Crop Image");
        return options;
    }
}
