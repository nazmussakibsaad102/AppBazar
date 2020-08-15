package com.saad102.jobbazar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

public class JobPost extends AppCompatActivity {
    private TextView txtJobTitle,txtJobDescription, txtJobLink;
    private ImageView jobScreenShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        txtJobDescription = findViewById(R.id.txtJobDescription);
        txtJobLink = findViewById(R.id.txtJobLink);
        jobScreenShot = findViewById(R.id.jobScreenShot);
        Intent recievedIntentObject = getIntent();
        final String recievedID = recievedIntentObject.getStringExtra("objectId");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Photo");
        query.whereEqualTo("objectId", recievedID);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null){
                    txtJobTitle.setText(object.get("job_title") + "");
                    txtJobDescription.setText(object.get("job_des") + "");
                    String text = object.get("job_link") + "";
                    SpannableString content = new SpannableString(text);
                    content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
                    txtJobLink.setText(content);
                    txtJobLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url = "http://" + txtJobLink.getText() ;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            startActivity(i);
                        }
                    });
                    ParseFile postPicture = (ParseFile) object.get("picture");
                    postPicture.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (data != null && e == null) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                jobScreenShot.setImageBitmap(bitmap);
                            }else {
                                FancyToast.makeText(JobPost.this,e.getMessage(),FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                            }
                        }
                    });
                }else{
                    FancyToast.makeText(JobPost.this,e.getMessage() + " for " + recievedID,FancyToast.LENGTH_LONG,FancyToast.ERROR,true).show();

                }
            }
        });
    }
}