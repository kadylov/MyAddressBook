package com.example.myaddressbook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;


import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public abstract class ContactDialog extends DialogFragment {

    private final int TAKE_PHOTO = 0;
    private final int CHOOSE_FROM_GALLERY = 1;

    private Uri imgUri;
    private CircleImageView imgBtn;

    private EditText txtFullName;
    private EditText txtPhoneNumber;
    private Spinner spnrPhoneType;
    private EditText txtEmail;
    private EditText txtStreet;
    private EditText txtCity;
    private EditText txtState;
    private EditText txtZip;


    private AlertDialog.Builder builder;
    private View dialogView;

    private LayoutInflater inflater;
    private Activity activity;

    private String takenImageUrl = "";

    public ContactDialog(Activity activity, int inflaterResource) {

        this.activity = activity;
        builder = new AlertDialog.Builder(activity);

        inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(inflaterResource, null);
        init();

        builder.setView(dialogView);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptionDialog();
            }
        });

        txtPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

    }

    @Override
    public abstract android.app.Dialog onCreateDialog(Bundle savedInstanceState);


    private void init() {

        imgBtn = dialogView.findViewById(R.id.profileImageCircleView);

        txtFullName = dialogView.findViewById(R.id.txtFullName);
        txtPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);
        spnrPhoneType = dialogView.findViewById(R.id.spnrPhoneType);
        txtEmail = dialogView.findViewById(R.id.txtEmail);
        txtStreet = dialogView.findViewById(R.id.txtStreet);
        txtCity = dialogView.findViewById(R.id.txtCity);
        txtState = dialogView.findViewById(R.id.txtState);
        txtZip = dialogView.findViewById(R.id.txtZip);

    }


    private void showImageOptionDialog() {

        final String[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case TAKE_PHOTO:                // Take Photo
                        takePictureFromCamera();
                        break;

                    case CHOOSE_FROM_GALLERY:       // Choose from Gallery
                        pickPhotoFromGallery();
                        break;

                    default:                        // cancel
                        dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void pickPhotoFromGallery() {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, CHOOSE_FROM_GALLERY);


    }

    private void takePictureFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgUri = FileProvider.getUriForFile(activity,
                "com.example.android.fileprovider",
                file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        intent.putExtra("return data", true);
        startActivityForResult(intent, TAKE_PHOTO);
    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {

                        cropImage(imgUri);

                    }
                    break;

                case CHOOSE_FROM_GALLERY:
                    if (resultCode == RESULT_OK) {

                        imgUri = data.getData();
                        cropImage(imgUri);
                    }
                    break;

                case UCrop
                        .REQUEST_CROP:
                    if (resultCode == RESULT_OK) {

                        final Uri uri = UCrop.getOutput(data);
                        if (uri != null) {
                            imgBtn.setImageURI(uri);
                        }
                    }
                    break;

                case UCrop.RESULT_ERROR: {
                    Toast.makeText(activity, "uCrop error", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

    }

    private void cropImage(Uri uri) {
        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(activity.getCacheDir(), "temp.jpg")));
        uCrop.withMaxResultSize(400, 400).withOptions(getCropOptions());

        startActivityForResult(uCrop.getIntent(getContext()), UCrop.REQUEST_CROP);

    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(20);
        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
//        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setCircleDimmedLayer(true);

        options.setToolbarTitle("Crop Image");
        return options;

    }

    public byte[] getProfileImageInBytes() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgBtn.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 1, stream);
        byte[] array = stream.toByteArray();

        return array;
    }

    public EditText getEditTextFulltName() {
        return txtFullName;
    }

    public EditText getEditTextPhoneNumber() {
        return txtPhoneNumber;
    }

    public Spinner getSpinnerPhoneType() {
        return spnrPhoneType;
    }

    public EditText getEditTextEmail() {
        return txtEmail;
    }

    public EditText getEditTextStreet() {
        return txtStreet;
    }

    public EditText getEditTextCity() {
        return txtCity;
    }

    public EditText getEditTextState() {
        return txtState;
    }

    public EditText getEditTextZip() {
        return txtZip;
    }

    public AlertDialog.Builder getBuilder() {
        return builder;
    }

    public View getDialogView() {
        return dialogView;
    }

    public CircleImageView getCircleViewProfileImage() {
        return imgBtn;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        takenImageUrl = image.getAbsolutePath();
        return image;
    }

}
