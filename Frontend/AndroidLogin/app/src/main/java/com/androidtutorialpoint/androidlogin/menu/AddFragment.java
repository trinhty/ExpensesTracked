package com.androidtutorialpoint.androidlogin.menu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidtutorialpoint.androidlogin.AppSingleton;
import com.androidtutorialpoint.androidlogin.LoginActivity;
import com.androidtutorialpoint.androidlogin.R;
import com.androidtutorialpoint.androidlogin.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class AddFragment extends Fragment {
    // Instance variables
    private long categoryId = -1;
    private static final String TAG = "Adding expenses";
    private static final String URL_FOR_Adding = "http://cs309-yt-7.misc.iastate.edu:8080/demo/expenses/add";
    ProgressDialog progressDialog;
    TextView desc;
    TextView amount;
    private String category;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Variables
        View v = inflater.inflate(R.layout.fragment_add, null);
        final AppCompatSpinner dropDown = v.findViewById(R.id.add_dropdown);
        Button add = v.findViewById(R.id.add_addButton);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);

        desc = (TextView) v.findViewById(R.id.add_description);
        amount = (TextView) v.findViewById(R.id.add_amount);

        // Create an ArrayAdapter using the string array and spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.starting_categories,
                android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        dropDown.setAdapter(adapter);

        // OnClick listener
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add expense to server
                addExpense(category, desc.getText().toString(), amount.getText().toString());
            }
        });

        // OnItemSelected listener
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                categoryId = id;
                category = dropDown.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Return
        return v;
    }

    private void addExpense(final String category,final String description , final String amount) {
        // Tag used to cancel the request
        String cancel_req_tag = "added";

        progressDialog.setMessage("Adding Expenses...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_Adding, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Adding Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                        /*String user = jObj.getJSONObject("user").getString("name");*/
                        Toast.makeText(getActivity().getApplicationContext(), "Hi. Your expense has been added succesfully!",
                                Toast.LENGTH_SHORT).show();

                        // Launch login activity
                        /*Intent intent = new Intent(AddFragment.this);
                        startActivity(intent);
                        finish();*/
                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("Category", category);
                params.put("Description", description);
                params.put("Amount", amount);
                /*params.put("gender", gender);
                params.put("age", dob);*/
                return params;
            }
        };

        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

}
