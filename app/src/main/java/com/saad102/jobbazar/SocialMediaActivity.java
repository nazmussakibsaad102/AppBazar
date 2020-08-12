package com.saad102.jobbazar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

import static androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class SocialMediaActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabAdapter mTabAdapter;
    private Bitmap receivedImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        setTitle("Hello " + ParseUser.getCurrentUser().getUsername());

        //initialization
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mViewPager = findViewById(R.id.pager);
        mTabAdapter = new TabAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(mTabAdapter);
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager, false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutUserItem:
                ParseUser.getCurrentUser().logOut();
                finish();
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    private void captureImage() {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,3000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3000 && resultCode == RESULT_OK && null != data) {


            try {
                Uri selectedImage = data.getData();
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
                receivedImageBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                receivedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                ParseFile parseFile = new ParseFile("img.png", bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("picture", parseFile);
                parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                final ProgressDialog dialog = new ProgressDialog(SocialMediaActivity.this);
                dialog.setMessage("Loading...");
                dialog.show();
                dialog.setCancelable(false);
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            FancyToast.makeText(SocialMediaActivity.this, "Done!!!", Toast.LENGTH_SHORT, FancyToast.SUCCESS, true).show();
                        } else {
                            FancyToast.makeText(SocialMediaActivity.this, "Unknown error: " + e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                        }
                        dialog.dismiss();
                    }
                });

            }catch (Exception e){

            }

        }
    }

}