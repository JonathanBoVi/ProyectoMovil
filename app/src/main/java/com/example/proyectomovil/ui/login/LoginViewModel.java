package com.example.proyectomovil.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectomovil.data.LoginRepository;
import com.example.proyectomovil.data.Result;
import com.example.proyectomovil.data.model.LoggedInUser;
import com.example.proyectomovil.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    private Context context;

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
       // Result<LoggedInUser> result = loginRepository.login(username, password);
        String url= "https://cdfe-179-6-27-16.ngrok-free.app/api/login";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", username);
            jsonBody.put("password", password);

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
                         LoggedInUser data = new LoggedInUser("0",username);
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                        } else {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
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
                ;
        Volley.newRequestQueue(context).add(postRequest);

      //  if (result instanceof Result.Success) {
          //  LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
           //loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        //} else {
           // loginResult.setValue(new LoginResult(R.string.login_failed));
        //}
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public void setContext(Context context){
        this.context= context;

    }
}