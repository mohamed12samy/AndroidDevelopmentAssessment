package com.example.androiddevelopmentassessment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUP extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private EditText email;
    private EditText password;
    private Button signUpButton;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        name = findViewById(R.id.name);
        phone = findViewById(R.id.phoneNum);
        email = findViewById(R.id.emailSignup);
        password = findViewById(R.id.passwordSignup);
        signUpButton = findViewById(R.id.signUP);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (name.getText().length() == 0 || phone.getText().length() == 0 || password.getText().length() == 0 || email.getText().length() == 0)
                    Toast.makeText(getApplicationContext(), "please enter all required fields.", Toast.LENGTH_LONG).show();
                else {
                    url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/signup?name="
                            + name.getText() + "&phone=" + phone.getText()
                            + "&password=" + password.getText() + "&email=" + email.getText();

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response != null && response.length() > 0) {
                                        Log.d("TAG", response.toString());

                                        String api_token = response.optJSONObject("user").optString("api_token");
                                        Intent intent = new Intent(getApplicationContext(), userProfile.class);
                                        intent.putExtra("api_token", api_token);
                                        startActivity(intent);
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("errr",error.toString());

                                    MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                                }
                            });

                    // Adding to request queue in MySingleton class
                    MySingleton.getInstance(SignUP.this).addToRequestQueue(jsonObjectRequest);
                }
            }
        });

    }


}
