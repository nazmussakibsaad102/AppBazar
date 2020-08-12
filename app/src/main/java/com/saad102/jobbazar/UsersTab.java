package com.saad102.jobbazar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


public class UsersTab extends Fragment {

    private ListView listView;
    private ArrayList<String> arraylist;
    private ArrayAdapter mArrayAdapter;
    private TextView txtLoading;

    public UsersTab() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users_tab, container, false);
        listView = view.findViewById(R.id.listView);
        arraylist = new ArrayList();
        mArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arraylist);
        txtLoading = view.findViewById(R.id.txtLoading);

        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e == null){
                    if (users.size() > 0){
                        for (ParseUser user: users){
                            arraylist.add(user.getUsername());
                        }
                        listView.setAdapter(mArrayAdapter);
                        txtLoading.animate().alpha(0).setDuration(2000);
                        listView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent  =  new Intent(getContext(), UsersPosts.class);
                intent.putExtra("username", arraylist.get(i));
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username", arraylist.get(i));

                query.getFirstInBackground(new GetCallback<ParseUser>() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null && e == null) {

//                    FancyToast.makeText(getContext(), user.get("profileProfession") + "",
//                            Toast.LENGTH_SHORT, FancyToast.SUCCESS,
//                            true).show();
                            final PrettyDialog prettyDialog =  new PrettyDialog(getContext());

                            prettyDialog.setTitle(user.getUsername() + " 's Info")
                                    .setMessage("company Name: " + user.get("companyName") + "\n"
                                            + "Mail: " +user.get("companyMail") + "\n"
                                            + "Genre" + user.get("companyGenre") + "\n"
                                            + "Location" + user.get("companyLocation")+ "\n"
                                            + "Motto" + user.get("companyMotto"))
                                    .setIcon(R.drawable.person)
                                    .addButton(
                                            "OK",     // button text
                                            R.color.pdlg_color_white,  // button text color
                                            R.color.pdlg_color_green,  // button background color
                                            new PrettyDialogCallback() {  // button OnClick listener
                                                @Override
                                                public void onClick() {
                                                    // Do what you gotta do
                                                    prettyDialog.dismiss();
                                                }
                                            }
                                    )
                                    .show();


                        }
                    }
                });
                return true;
            }
        });
        return view;
    }
}