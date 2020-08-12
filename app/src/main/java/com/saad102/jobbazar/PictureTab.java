package com.saad102.jobbazar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class PictureTab extends Fragment {
    private ImageView ivImageView;
    private EditText edtImageDescription;
    private Button btnSharePicture;
    private Bitmap receivedImageBitmap;


    public PictureTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_picture_tab, container, false);
        ivImageView = view.findViewById(R.id.ivImage);
        edtImageDescription = view.findViewById(R.id.edtJobLink);
        btnSharePicture = view.findViewById(R.id.btnSharePicture);

        ivImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT>=23 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant


                }else {
                    getChosenImage();
                }
            }
        });

        btnSharePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (receivedImageBitmap != null) {

                    if (edtImageDescription.getText().toString().equals("")) {
                        FancyToast.makeText(getContext(), "Error: You must specify a description."  , Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();


                    } else {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("img.png", bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("picture", parseFile);
                        parseObject.put("image_des", edtImageDescription.getText().toString());
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Loading...");
                        dialog.show();
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    FancyToast.makeText(getContext(), "Done!!!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                                } else {
                                    FancyToast.makeText(getContext(), "Unknown error: " + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                                }
                                dialog.dismiss();
                            }
                        });


                    }

                } else {

                    FancyToast.makeText(getContext(), "Error: You must select an image."  , Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();

                }

            }
        });
        return view;
    }

    private void getChosenImage() {
        FancyToast.makeText(getActivity(),"Email/ Username and password both required!",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getChosenImage();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000 && resultCode == RESULT_OK && null != data) {


            try {
                Uri imageUri = data.getData();
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
//                cursor.moveToFirst();
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
//                // String picturePath contains the path of selected Image
//                Bitmap recievedImagebitmap = BitmapFactory.decodeFile(picturePath);
//                ivImageView.setImageBitmap(recievedImagebitmap);
                receivedImageBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                receivedImageBitmap = getResizedBitmap(receivedImageBitmap, 1000);// 400 is for example, replace with desired size
                ivImageView.setImageBitmap(receivedImageBitmap);

            }catch (Exception e){

            }

        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}