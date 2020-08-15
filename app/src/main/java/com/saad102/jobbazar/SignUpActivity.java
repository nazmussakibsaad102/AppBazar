package com.saad102.jobbazar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMail, edtUserName, edtPassword;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (ParseUser.getCurrentUser() != null){
            //ParseUser.getCurrentUser().logOut();
            transitionToSocialMediaActivity();
        }

        setContentView(R.layout.activity_sign_up);


        setTitle("Sign Up");


        edtMail = findViewById(R.id.edtSignUpEmail);
        edtUserName = findViewById(R.id.edtSignUpUsername);
        edtPassword = findViewById(R.id.edtSignUpPassword);


        edtPassword.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onClick(btnSignUp);
                    return true;
                }
                return false;
            }
        });

        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogIn);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {




        switch (view.getId()){
            case R.id.btnSignUp:
                if (edtMail.getText().toString().equals("") ||
                        edtUserName.getText().toString().equals("")||
                        edtPassword.getText().toString().equals("")){
                                   FancyToast.makeText(SignUpActivity.this,
                                           "E mail, Username and Password all are requred!",
                                           FancyToast.LENGTH_LONG,FancyToast.ERROR,true)
                                           .show();


                }else  {
                    final ParseUser user = new ParseUser();
                    user.setUsername(edtUserName.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    user.setEmail(edtMail.getText().toString());
                    final ProgressDialog pd = new ProgressDialog(SignUpActivity.this);
                    pd.setMessage("Signing Up");
                    pd.show();
                    pd.setCancelable(false);




// other fields can be set just like with ParseObject

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null ) {
                                // Hooray! Let them use the app now.
                                ParseUser.logOut();
                                FancyToast.makeText(SignUpActivity.this, "Congratulations " + user.getUsername() + ", your id is created successfully. Please varify your account.", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
//                                transitionToSocialMediaActivity();
                            } else {

                                // Sign up didn't succeed. Look at the ParseException
                                // to figure out what went wrong
                                FancyToast.makeText(SignUpActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true);
                            }
                            pd.dismiss();
                        }
                    });
                }

                break;
            case R.id.btnLogIn:
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                break;
        }
    }
    public void onScreenTap(View view){
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        }catch (Exception e){

        }

    }
    private void transitionToSocialMediaActivity() {
        Intent i = new Intent(SignUpActivity.this,SocialMediaActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
//        IsFinish("Want to go to job posts?");
//        //super.onBackPressed();
        finish();
    }

    private void IsFinish(String alertmessage) {

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
//                        android.os.Process.killProcess(android.os.Process.myPid());
                        // This above line close correctly
                        Intent i = new Intent(SignUpActivity.this, HomePageActivity.class);
                        startActivity(i);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(alertmessage)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
}