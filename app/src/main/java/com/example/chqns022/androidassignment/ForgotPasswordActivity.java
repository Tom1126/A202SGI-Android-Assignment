package com.example.chqns022.androidassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    // Widgets
    private EditText emailAddress;
    private Button resetPassword;
    private TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailAddress = findViewById(R.id.emailAddForgotPassword);
        resetPassword = findViewById(R.id.resetPassword);
        backToLogin = findViewById(R.id.backToLogin);

        init();
        hideSoftKeyboard();

    }

    private void init()
    {
        // When Reset Password is clicked
        resetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                // If email and password entered are valid
                boolean emailValid;

                emailValid = isValid();

                if (emailValid)
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ForgotPasswordActivity.this, R.style.Theme_AppCompat_Light_Dialog));

                    builder.setTitle(getResources().getString(R.string.title_activity_reset_password));

                    builder.setMessage(getResources().getString(R.string.confirm_reset_password));

                    builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendResetPasswordEmail(emailAddress.getText().toString());
                        }
                    });

                    builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                }

            }

        });

        backToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d(TAG, "Redirecting to Login page...");
                redirectLoginScreen();
            }

        });

    }

    private void sendResetPasswordEmail(String email){
        FirebaseDB firebaseAuth = new FirebaseDB();
        firebaseAuth.sendForgotPasswordEmail(email, ForgotPasswordActivity.this);
    }

    // Setting errors on email
    private boolean isValid()
    {
        // If email address field is left empty
        if(isEmpty(emailAddress.getText().toString()))
        {
            emailAddress.setError(getResources().getString(R.string.email_required_error));
            return false;
        }

        // If email address is not valid
        else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches())
        {
            emailAddress.setError(getResources().getString(R.string.invalid_email_error));
            return false;
        }

        return true;
    }


    private void resetFields()
    {
        emailAddress.setText("");
    }

    private void redirectLoginScreen()
    {
        Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Check if string is empty
    private boolean isEmpty(String string)
    {
        return string.equals("");
    }

    // Hide keyboard
    private void hideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
