package com.example.androiddevelopmentassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class userProfile extends AppCompatActivity {

    private String api_token;
    private String url;

    private LinearLayout linearLayout;

    private TextView userName;
    private TextView phone;
    private TextView Email;
    private ImageView photo;
    private Button update_profile;
    private Button change_pasword;
    private Button change_phone;
    private EditText oldphone;
    private Button showUpdates;
    private String flag = "";
    private int activation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        api_token = getIntent().getStringExtra("api_token");

        userName = findViewById(R.id.userName);
        phone = findViewById(R.id.userPhone);
        Email = findViewById(R.id.userEmail);
        update_profile = findViewById(R.id.upsdateProfileButton);

        change_phone = findViewById(R.id.changePhone);
        change_pasword = findViewById(R.id.ChangePassoword);
        oldphone = findViewById(R.id.currentPhone);

        linearLayout = findViewById(R.id.layoutUpdate);
        showUpdates = findViewById(R.id.showupdates);

        url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/get-profile?api_token=" + api_token;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            Log.d("TAG1", response.toString());

                            userName.setText(response.optJSONObject("user").optString("name"));
                            phone.setText(response.optJSONObject("user").optString("phone"));
                            Email.setText(response.optJSONObject("user").optString("email"));
                            activation = response.optJSONObject("user").optInt("activation");

                            if(activation == 1 )
                                Toast.makeText(getApplicationContext() , "Your account is activated",Toast.LENGTH_SHORT).show();
                            else{
                                url="https://internship-api-v0.7.intcore.net/api/v1/user/auth/resend-activation-code?api_token="+api_token;
                                activation(activation,url);
                            }
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                    }
                });

        // Adding to request queue in MySingleton class
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        /////////////////********************************************/////////////////////

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag = "profile";
                goToSettingProfileActivity();
            }
        });


        change_pasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flag = "password";
                goToSettingProfileActivity();
            }
        });

        change_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (oldphone.getText().length() != 11)
                    Toast.makeText(getApplicationContext(), "please enter your current phone number", Toast.LENGTH_LONG).show();
                else {
                    url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/request-update-phone?api_token=" + api_token + "&phone=" + oldphone.getText();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    if (response != null && response.length() > 0) {
                                        Log.d("TAG2", response.toString());

                                        int code = 0;
                                        code = response.optInt("code");
                                        Log.d("asde", Integer.toString(code));
                                        flag = "phone";
                                        Intent intent = new Intent(getApplicationContext(), Setting_profile.class);
                                        intent.putExtra("api_token", api_token);
                                        intent.putExtra("flag", flag);
                                        intent.putExtra("code", Integer.toString(code));
                                        startActivity(intent);
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                                }
                            });

                    // Adding to request queue in MySingleton class
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
    }
    private void goToSettingProfileActivity()
    {
        Intent intent = new Intent(getApplicationContext(), Setting_profile.class);
        intent.putExtra("api_token",api_token);
        intent.putExtra("flag",flag);
        startActivity(intent);
    }

    public void showUpdates(View view) {

        if(showUpdates.getText().equals("update") ) {
            linearLayout.setVisibility(View.VISIBLE);
            showUpdates.setText("Cancel");
        }
        else if (showUpdates.getText().equals("Cancel") ) {
            linearLayout.setVisibility(View.GONE);
            showUpdates.setText("update");
        }

    }
    int code;
private void activation(int activation ,String url){

    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (response != null && response.length() > 0) {
                        Log.d("TAG2", response.toString());
                        code = response.optInt("code");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                }
            });

    // Adding to request queue in MySingleton class
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/active-account?api_token="+api_token+"&code="+code;
     jsonObjectRequest = new JsonObjectRequest
            (Request.Method.PATCH, url, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    if (response != null && response.length() > 0) {
                        Log.d("TAG2", response.toString());
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MySingleton.getInstance(getApplicationContext()).parseError(error, getApplicationContext());
                }
            });

    // Adding to request queue in MySingleton class
    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
}


    public void signOut(View view) {
        api_token="";
        Intent intent = new Intent(getApplicationContext() , MainActivity.class);
        startActivity(intent);
        finish();
    }
}


