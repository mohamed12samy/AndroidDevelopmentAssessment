package com.example.androiddevelopmentassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class resetPassword extends AppCompatActivity {

    private EditText name;
    private EditText resetMethod;
    private EditText password;
    private EditText resetCode;
    private Button resetButton;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        name = findViewById(R.id.emailORphone);
        resetMethod = findViewById(R.id.resetMethod);
        password = findViewById(R.id.Newpassword);
        resetButton = findViewById(R.id.submitTOreset);
        resetCode = findViewById(R.id.resetCode);

        name.setText(getIntent().getStringExtra("reset_name"));
        resetMethod.setText(getIntent().getStringExtra("reset_method"));
        resetCode.setText(getIntent().getStringExtra("reset_code"));

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (password.getText().length() == 0 || password.getText().toString().equals(" "))
                    Toast.makeText(getApplicationContext(), "Please enter your new password.", Toast.LENGTH_SHORT).show();

                else if (password.getText().length() < 6 )
                    Toast.makeText(getApplicationContext(), "password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
                else {
                    url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/password/reset?reset_password_code=" + resetCode.getText() + "&name=" + name.getText()
                            + "&password=" + password.getText() + "&reset_method=" + resetMethod.getText();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.PATCH, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response != null && response.length() > 0) {
                                        Log.d("TAG", response.toString());

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.putExtra("name", name.getText());
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), "password must has samll and capital characters and numers",Toast.LENGTH_SHORT).show();
                                    MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                                }
                            });

                    // Adding to request queue in MySingleton class
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
    }
}
