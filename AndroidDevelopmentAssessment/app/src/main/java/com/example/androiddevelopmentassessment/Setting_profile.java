package com.example.androiddevelopmentassessment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Setting_profile extends AppCompatActivity {

    private static final int OPEN_DOCUMENT_CODE = 2 ;
    private String api_token;
    private String url;

    Bitmap bitmap = null;

    private TextView newUserName;
    private TextView phone;
    private TextView newEmail;
    private ImageView photo;
    private Button saveupdates;

    private LinearLayout linearLayout;
    private EditText new_password;
    private EditText old_password;
    private Button Save;
    private Button cancel;

    private EditText temp_phone_code;
    private EditText newphone;

    private String flag;

    private ImageView img ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_profile);

        newUserName = findViewById(R.id.newname);
        newEmail = findViewById(R.id.newemail);
        photo = findViewById(R.id.profileImage);
        saveupdates = findViewById(R.id.saveUpdates);

        linearLayout = findViewById(R.id.layout1);
        new_password = findViewById(R.id.newpassword);
        old_password = findViewById(R.id.oldpassword);
        cancel = findViewById(R.id.cancelChanges);

        newphone = findViewById(R.id.newPhone);
        temp_phone_code = findViewById(R.id.temp_phone_code);

        img = findViewById(R.id.updateProfileImage);


        api_token = getIntent().getStringExtra("api_token");
        flag = getIntent().getStringExtra("flag");


        if (flag.equals("profile")) {
            linearLayout = findViewById(R.id.layout1);
            linearLayout.setVisibility(View.VISIBLE);
        }
        else if (flag.equals("password")) {
            linearLayout = findViewById(R.id.layout2);
            linearLayout.setVisibility(View.VISIBLE);
        }
        else if (flag.equals("phone")) {
            temp_phone_code.setText(getIntent().getStringExtra("code"));
            linearLayout = findViewById(R.id.layout3);
            linearLayout.setVisibility(View.VISIBLE);
        }

        saveupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (flag.equals("profile")) {
                    if (newUserName.getText().length() == 0 || newEmail.getText().length() == 0 || newUserName.getText().toString().equals(" ") || newEmail.getText().toString().equals(" "))
                        Toast.makeText(getApplicationContext(), "Please enter your new Email and new Name.", Toast.LENGTH_SHORT).show();
                    else {
                        url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/update-profile?api_token="
                                + api_token + "&name=" + newUserName.getText()
                                + "&email=" + newEmail.getText() + "&image=uploads/VaGL669bZtPv4OJo4lueiVTStkKuto4XU1ymT04C.png";

                        sendRequest(url);
                    }
                } else if (flag.equals("password")) {
                    if (old_password.getText().length() < 6 || new_password.getText().length() < 6 || old_password.getText().toString().equals(" ") || new_password.getText().toString().equals(" "))
                        Toast.makeText(getApplicationContext(), "Please enter your valid password.", Toast.LENGTH_LONG).show();
                    else {
                        url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/update-password?api_token=" + api_token
                                + "&new_password=" + new_password.getText() + "&old_password=" + old_password.getText();
                        sendRequest(url);
                    }
                } else if (flag.equals("phone")) {

                    if (newphone.getText().length() != 11 || newphone.getText().toString().equals(" "))
                        Toast.makeText(getApplicationContext(), "Please enter valid phone.", Toast.LENGTH_LONG).show();
                    else {
                        url = "https://internship-api-v0.7.intcore.net/api/v1/user/auth/update-phone?" +
                                "api_token=" + api_token+
                                "&phone=" + newphone.getText()+
                                "&temp_phone_code="+temp_phone_code.getText();

                        sendRequest(url);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new_password.setText("");
                old_password.setText("");
                newphone.setText("");
                newEmail.setText("");
                newUserName.setText("");
                linearLayout.setVisibility(View.GONE);
                newphone.setVisibility(View.GONE);

                goToActivityUserprofile();
            }
        });
    }

    private void sendRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PATCH, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null && response.length() > 0) {
                            Log.d("updating", response.toString());
                            goToActivityUserprofile();

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

    private void goToActivityUserprofile()
    {
        Intent intent = new Intent(getApplicationContext(), userProfile.class);
        intent.putExtra("api_token", api_token);
        startActivity(intent);
    }

    public void pickPhoto(View view) {
        Toast.makeText(getApplicationContext(), "Please enter your new Email and new Name.", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, OPEN_DOCUMENT_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == OPEN_DOCUMENT_CODE && resultCode == RESULT_OK) {
            if (resultData != null) {
                // this is the image selected by the user
                Uri imageUri = resultData.getData();
                Log.d("imagee",imageUri.toString());

                Glide.with(img.getContext())
                        .load(imageUri)
                        .into(img);
                imageUri.getPath();

                try {
                    bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                url="https://internship-api-v0.7.intcore.net/api/v1/user/auth/file/upload";
                VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                        new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                try {
                                    JSONObject obj = new JSONObject(new String(response.data));
                                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {

                    /*
                     * If you want to add more parameters with the image
                     * you can do it here
                     * here we have only one parameter with the image
                     * which is tags
                     * */
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("tags", "");
                        return params;
                    }

                    /*
                     * Here we are passing image by renaming it with a unique name
                     * */
                    @Override
                    protected Map<String, DataPart> getByteData() {
                        Map<String, DataPart> params = new HashMap<>();
                        long imagename = System.currentTimeMillis();
                        params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                        return params;
                    }
                };

                //adding the request to volley
                Volley.newRequestQueue(this).add(volleyMultipartRequest);

            }
        }
    }
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
