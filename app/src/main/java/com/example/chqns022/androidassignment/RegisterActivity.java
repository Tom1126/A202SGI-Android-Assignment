package com.example.chqns022.androidassignment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Registration Activity";

    //Widgets
    private TextView loginText;
    private EditText email, password, confirmPassword;
    private ProgressBar progressBar;
    private Button register;
    private String emailStr, passwordStr, confirmPasswordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        confirmPassword = findViewById(R.id.confirmPasswordRegister);

        loginText = findViewById(R.id.loginForRegistration);
        register = findViewById(R.id.registerButtonr);

        initProgressBar();
        init();

        hideSoftKeyboard();

    }

    private void init() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailStr = email.getText().toString();
                passwordStr = password.getText().toString();
                confirmPasswordStr = confirmPassword.getText().toString();

                boolean inputsValid;

                Log.d(TAG, "Checking input validation of registration details");

                inputsValid = checkInputs(emailStr, passwordStr, confirmPasswordStr);

                if (inputsValid) {
                    Log.d(TAG, "Saving details to database...");

                    showProgressBar();
                    registerUser(emailStr, passwordStr);
                }
            }


        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Redirecting to Login page...");
                redirectLoginScreen();
            }

        });

    }

    private void registerUser(String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        FirebaseDB mAuth = new MainActivity().getFirebaseDB();
        mAuth.registerUser(user, RegisterActivity.this);
    }

    private void resetFields() {

        email.setText("");
        password.setText("");
        confirmPassword.setText("");
    }

    private boolean checkInputs(String emailStr, String passwordStr, String confirmPasswordStr) {

        boolean eValid, pValid, pcValid;

        eValid = validateEmail(emailStr);
        pValid = validatePassword(passwordStr);
        pcValid = validatePasswordConfirmation(passwordStr, confirmPasswordStr);

        return eValid && pValid && pcValid;
    }

    private boolean validatePasswordConfirmation(String passwordStr, String confirmPasswordStr) {
        if (isEmpty(confirmPasswordStr)) {
            confirmPassword.setError(getResources().getString(R.string.password_confirmation_required_error));
            return false;
        } else if (!stringsMatch(passwordStr, confirmPasswordStr)) {
            confirmPassword.setError(getResources().getString(R.string.password_no_match_error));
            return false;
        } else
            return true;
    }

    private boolean validatePassword(String passwordStr) {
        if (isEmpty(passwordStr)) {
            password.setError(getResources().getString(R.string.password_required_error));
            return false;
        } else if (passwordStr.length() < 8) {
            password.setError(getResources().getString(R.string.minimum_8_message));
            return false;
        } else
            return true;

    }

    private boolean validateEmail(String emailStr) {
        // VALIDATE EMAIL
        if (isEmpty(emailStr)) {
            email.setError(getResources().getString(R.string.email_required_error));
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
            email.setError(getResources().getString(R.string.invalid_email_error));
            return false;
        } else
            return true;
    }

    private boolean isEmpty(String string) {
        return string.equals("");
    }

    private boolean stringsMatch(String s1, String s2) {
        return s1.equals(s2);
    }

    private void redirectLoginScreen() {
        Log.d(TAG, "Redirecting to Login Page...");

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void createToast(String s)
    {
        Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void initProgressBar() {
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }


    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
