package com.threedev.machinetest.login;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.threedev.machinetest.R;
import com.threedev.machinetest.home.MainActivity;
import com.threedev.machinetest.login.model.LoginModel;
import com.threedev.machinetest.utils.Constant;
import com.threedev.machinetest.utils.network.ApiClient;
import com.threedev.machinetest.utils.network.ApiInterface;
import com.threedev.machinetest.utils.network.NetworkUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmailId, edtPassword;
    private Button btnLogin;

    private ApiInterface apiInterface;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        edtEmailId = findViewById(R.id.edt_email_id);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        
        
        
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=edtEmailId.getText().toString().trim();
                String pass=edtPassword.getText().toString().trim();


                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                }
                else if (!validate(email)) {

                    Toast.makeText(LoginActivity.this, "Invalid Email Id", Toast.LENGTH_SHORT).show();

                }
                else if(pass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (NetworkUtils.isNetworkConnected(LoginActivity.this)) {
                        try {

                            getLoginDetails(email, pass);


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(LoginActivity.this, Constant.NIC, Toast.LENGTH_SHORT).show();
                    }
                }
        }
        });


    }

    private void getLoginDetails(String email, String pass) {
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

        Call<LoginModel> call = apiInterface.getLogin(email,pass);

            call.enqueue(new Callback<LoginModel>() {
                @Override
                public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                    pDialog.dismiss();
                    LoginModel model = response.body();

                    if(model != null){

                        if(TextUtils.isEmpty(model.getToken())){
                            Toast.makeText(LoginActivity.this, "" + model.getError(), Toast.LENGTH_SHORT).show();

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "" + model.getToken(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid Login Credentials !!", Toast.LENGTH_SHORT).show();

                    }


                }

                @Override
                public void onFailure(Call<LoginModel> call, Throwable t) {
                    pDialog.dismiss();
                    Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();


                }
            });



    }
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}