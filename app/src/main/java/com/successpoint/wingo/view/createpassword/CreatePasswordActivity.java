package com.successpoint.wingo.view.createpassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.successpoint.wingo.R;
import com.successpoint.wingo.utils.Constants;
import com.successpoint.wingo.utils.PasswordStrength;
import com.successpoint.wingo.view.completeprofile.CompleteProfileActivity;
import com.successpoint.wingo.view.mainactivity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreatePasswordActivity extends MvpActivity<CreatePasswordView, CreatePasswordPresenter> implements CreatePasswordView {

    private Toolbar tbSign;
    private ImageView ivLoginLogo;
    private LinearLayout lSign;
    private TextView tvPassStatus;
    private EditText etCreatePass;
    private EditText etReCreatePass;
    private Button btSignup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpassword);

        tbSign = findViewById(R.id.tbSign);
        ivLoginLogo = findViewById(R.id.ivLoginLogo);
        lSign = findViewById(R.id.lSign);
        tvPassStatus = findViewById(R.id.tvPassStatus);
        etCreatePass = findViewById(R.id.etCreatePass);
        etReCreatePass = findViewById(R.id.etReCreatePass);
        btSignup = findViewById(R.id.btSignup);

        etCreatePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                updatePasswordStrength(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btSignup.setOnClickListener(view -> {
            if (etReCreatePass.getText().toString().equals(etCreatePass.getText().toString())) {
                HashMap<String, String> params = new HashMap<>();
                params.put("phone", getIntent().getStringExtra("phone"));
                params.put("password", etReCreatePass.getText().toString());
                params.put("api_token", Constants.api_token);
                presenter.signup(params);

            } else {
                etReCreatePass.setError(getString(R.string.pass_dont_match));
                etReCreatePass.requestFocus();
            }
        });
    }

    private void updatePasswordStrength(String password) {
        tvPassStatus.setVisibility(password.length() > 0 ? View.VISIBLE : View.INVISIBLE);

        PasswordStrength passwordStrength = PasswordStrength.calculateStrength(password);
        tvPassStatus.setText(passwordStrength.getText(this));
        tvPassStatus.setTextColor(passwordStrength.getColor());
    }

    @NonNull
    @Override
    public CreatePasswordPresenter createPresenter() {
        return new CreatePasswordPresenter(this, this);
    }

    @Override
    public void publishResponse(String response) {
        try {
            JSONObject result = new JSONObject(response);
            if (result.getInt("code") == 0) {
                startActivity(new Intent(CreatePasswordActivity.this,CompleteProfileActivity.class)
                        .putExtra("phone", getIntent().getStringExtra("phone"))
                        .putExtra("password", etReCreatePass.getText().toString()));
            }else{
                Log.e("Done","NOOOOOO");
                Toast.makeText(this, result.getString("message"), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}