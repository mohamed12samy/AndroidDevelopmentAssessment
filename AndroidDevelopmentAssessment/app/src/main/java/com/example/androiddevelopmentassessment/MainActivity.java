package com.example.androiddevelopmentassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button signInButton;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.emailSignin);
        password = findViewById(R.id.passwordSignin);
        signInButton = findViewById(R.id.SignIN);

        email.setText(getIntent().getStringExtra("name"));


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckIfFieldsAreFilled())
                {
                    url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/signin?name="+email.getText()+"&password="+password.getText();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    String api_token ="" ;
                                    if(response != null && response.length() > 0) {
                                        Log.d("TAG", response.toString());

                                        api_token = response.optJSONObject("user").optString("api_token");
                                        Intent intent = new Intent(getApplicationContext(),userProfile.class);
                                        intent.putExtra("api_token",api_token);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    MySingleton.getInstance( getApplicationContext()).parseError(error , getApplicationContext());
                                }
                            });

                    // Adding to request queue in MySingleton class
                    MySingleton.getInstance( getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });
        }

    public void forgotPassword(View view)
    {
        if(email.getText().length() == 0 || email.getText().toString().equals(" "))
            Toast.makeText(getApplicationContext(), "Please enter your email or phone.", Toast.LENGTH_SHORT).show();

        else
        {
            final String mEmail = email.getText().toString();
            final String type ;

            if(mEmail.contains("@")) type = "email";
            else type = "phone";

            url="https://internship-api-v0.7.intcore.net/api/v1/user/auth/password/email?name="+mEmail+"&reset_method="+type;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response)
                        {
                            int code=0;
                            if(response != null && response.length() > 0) {
                                Log.d("TAG", response.toString());

                                try {
                                    code = response.getInt("code");
                                    Toast.makeText(getApplicationContext(), response.getString("message") ,Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Intent intent = new Intent(getApplicationContext() , resetPassword.class);
                                intent.putExtra("reset_name", mEmail);
                                intent.putExtra("reset_method", type);
                                intent.putExtra("reset_code", Integer.toString(code));
                                Log.i("trtr",mEmail);
                                Log.i("trtr",type);
                                Log.i("trtr",Integer.toString(code));
                                startActivity(intent);

                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            MySingleton.getInstance( getApplicationContext()).parseError(error , getApplicationContext());
                        }
                    });

            // Adding to request queue in MySingleton class
            MySingleton.getInstance( getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            /////////////////////////////////////////////////////////////////////////////////////////calling api////////////

        }
    }
    private boolean CheckIfFieldsAreFilled()
    {
        boolean check = true;
        if(email.getText().length() == 0 || password.getText().length() == 0 || email.getText().toString().equals(" ") || password.getText().toString().equals(" "))
        {
            Log.i("asdfr",email.getText().toString());
            check = false;
            Toast.makeText(getApplicationContext(), "Please enter all required fields", Toast.LENGTH_SHORT).show();
        }

        return check;
    }

    public void GoToSignUp(View view) {
        Intent intent = new Intent(MainActivity.this, SignUP.class);
        startActivity(intent);
    }

}

