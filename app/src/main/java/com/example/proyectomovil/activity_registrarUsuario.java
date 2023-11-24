package com.example.proyectomovil;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectomovil.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class activity_registrarUsuario extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private EditText emailET;
    private EditText nombreET;
    private EditText passET;
    private Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_registrar_usuario);

        emailET = (EditText) findViewById(R.id.txtEmail);
        nombreET = (EditText) findViewById(R.id.txtNombre);
        passET = (EditText) findViewById(R.id.txtPassword);
        registerButton = (Button) findViewById(R.id.btnRegistrar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
    }

    private void registrar(){
        String url= " https://004c-179-6-27-16.ngrok-free.app/api/register";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", nombreET.getText());
            jsonBody.put("email", emailET.getText());
            jsonBody.put("password", passET.getText());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        final String requestBody = jsonBody.toString();
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    Integer code = jsonObject.getInt("code");
                    if (code == 1) {
                        updateUiWithUser();
                    } else {
                        showSignupFailed();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Log.e("respuesta", jsonObject.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());
            }
        }
        )
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }


        };

            Volley.newRequestQueue(this).add(postRequest);
    }

    public void updateUiWithUser() {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, MainActivity.class));

    }

    private void showSignupFailed() {
        Toast.makeText(getApplicationContext(), "Error en el registro", Toast.LENGTH_SHORT).show();
    }
}