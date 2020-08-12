package com.saad102.jobbazar;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class ProfileTab extends Fragment {
    private EditText edtCompanyName, edtCompanyGenre, edtCompanyMail, edtCompanymotto, edtCompanyLocation;
    private Button btnUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile_tab, container, false);
        edtCompanyName = view.findViewById(R.id.edtCompanyName);
        edtCompanyGenre = view.findViewById(R.id.edtCompanyGenre);
        edtCompanyMail = view.findViewById(R.id.edtCompanyMail);
        edtCompanymotto = view.findViewById(R.id.edtCompanymotto);
        btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        edtCompanyLocation = view.findViewById(R.id.edtCompanyLocation);

        final ParseUser currentUser = new ParseUser().getCurrentUser();

        if (currentUser.get("companyName") == null) {
            edtCompanyGenre.setText("");

        } else {
            edtCompanyName.setText(currentUser.get("companyName").toString());

        }
        if (currentUser.get("companyGenre") == null) {
            edtCompanyGenre.setText("");

        } else {
            edtCompanyGenre.setText(currentUser.get("companyGenre").toString());

        }
        if (currentUser.get("companyMail") == null) {
            edtCompanyMail.setText("");

        } else {
            edtCompanyMail.setText(currentUser.get("companyMail").toString());

        }
        if (currentUser.get("companyLocation") == null) {
            edtCompanyLocation.setText("");

        } else {
            edtCompanyLocation.setText(currentUser.get("companyLocation").toString());

        }
        if (currentUser.get("companyMotto") == null) {
            edtCompanymotto.setText("");

        } else {
            edtCompanymotto.setText(currentUser.get("companyMotto").toString());

        }

        btnUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("Updating Info");
                pd.show();
                pd.setCancelable(false);
                currentUser.put("companyName", edtCompanyName.getText().toString());
                currentUser.put("companyGenre", edtCompanyGenre.getText().toString());
                currentUser.put("companyMail", edtCompanyMail.getText().toString());
                currentUser.put("companyLocation", edtCompanyLocation.getText().toString());
                currentUser.put("companyMotto", edtCompanymotto.getText().toString());
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            FancyToast.makeText(getContext(),"Info updated successfully",FancyToast.LENGTH_LONG, FancyToast.SUCCESS,true).show();
                        }else {
                            FancyToast.makeText(getContext(),e.getMessage(),FancyToast.LENGTH_LONG, FancyToast.ERROR,true).show();

                        }
                        pd.dismiss();
                    }
                });
            }
        });



        return view;

    }
}