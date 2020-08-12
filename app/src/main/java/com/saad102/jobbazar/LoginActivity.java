package com.saad102.jobbazar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtMail, edtPass;
    private Button btnSignUp, btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");
        edtMail = findViewById(R.id.edtLoginEmail);
        edtPass = findViewById(R.id.edtLoginPassword);
        edtPass.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    onClick(btnLogin);
                    return true;
                }
                return false;
            }
        });
        btnLogin = findViewById(R.id.btnLoginActivity);
        btnSignUp = findViewById(R.id.btnSignUpLoginActivity);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLoginActivity:

                if (edtMail.getText().toString().equals("")
                        || edtPass.getText().toString().equals("")){
                    FancyToast.makeText(LoginActivity.this,"Email/ Username and password both required!",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();

                }else {

                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Logging in");
                    pd.show();
                    pd.setCancelable(false);
                    ParseUser.logInInBackground(edtMail.getText().toString(),
                            edtPass.getText().toString(),
                            new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null && e == null) {
                                        transitionToSocialMediaActivity();
                                        // Hooray! The user is logged in.
                                        FancyToast.makeText(LoginActivity.this, ParseUser.getCurrentUser().getUsername() + " logged in successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();

                                    } else if (e != null) {
                                        // Signup failed. Look at the ParseException to see what happened.
                                        FancyToast.makeText(LoginActivity.this, e.getMessage(), FancyToast.LENGTH_LONG, FancyToast.ERROR, true).show();
                                    }
                                }
                            });
                    pd.dismiss();
                }
                break;
            case R.id.btnSignUpLoginActivity:
                Intent i = new Intent(this, SignUpActivity.class);
                startActivity(i);
                finish();
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
        Intent i = new Intent(LoginActivity.this,SocialMediaActivity.class);
        startActivity(i);
        finish();
    }
}