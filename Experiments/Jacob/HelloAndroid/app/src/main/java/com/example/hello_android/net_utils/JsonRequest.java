package com.example.hello_android.net_utils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hello_android.R;

public class JsonRequest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Variables
        Button jsonObjRequest, jsonArrRequest;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_request);

        jsonObjRequest = findViewById(R.id.buttonJsonObjRequest);
        jsonArrRequest = findViewById(R.id.buttonJsonArrRequest);

        jsonObjRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        jsonArrRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
