package com.example.chqns022.androidassignment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    /**
     * Variable for database operations
     */
    private FirebaseDB firebaseDB = new FirebaseDB();

    //Widgets
    private TextView forgotPasswordText, registerText;
    private EditText userName, password;
    private Button login;
    private CheckBox rememberPassword;
    private ProgressBar progressBar;

    /**
     * Method to get firebasedb object
     * @return FirebaseDB
     */
    public FirebaseDB getFirebaseDB(){
        return firebaseDB;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this); //Initialize paper

        //Set language according to language key from Paper
        String lang = "";

        if(Paper.book().read("language") == null){
            lang = "en";

        }

        else if(Paper.book().read("language").equals("en")){
          lang = "en";
        }

        else{
          lang = "zh";
        }

        //Update language in Paper
        Paper.book().write("language", lang);

        //Change app language
        Configuration configuration = new Configuration();
        configuration.locale = new Locale(lang);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());


        forgotPasswordText = findViewById(R.id.forgotPasswordLogin);
        userName = findViewById(R.id.userNameLogin);
        password = findViewById(R.id.passwordLogin);
        rememberPassword = findViewById(R.id.rememberPasswordLogin);
        login = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.register_text);

        setRememberMeKnowns();

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        init();

    }

    private void updateTextboxValue(String email, String password, boolean isChecked){
        this.userName.setText(isChecked ? email : "");
        this.password.setText(isChecked ? password : "");
        this.rememberPassword.setChecked(isChecked);
    }

    private void setRememberMeKnowns()
    {

        final String gotRememberMe = "" + Paper.book().read("gotRememberMe");
        final String rememberMeEmail = "" + Paper.book().read("rememberMeEmail");
        final String rememberMePassword = "" + Paper.book().read("rememberMePassword");


        if(gotRememberMe.equals("true")){
            updateTextboxValue(rememberMeEmail, rememberMePassword, true);
        }

        else{
            Paper.book().write("gotRememberMe", "false");
            updateTextboxValue(rememberMeEmail, rememberMePassword, false);
        }


    }

    private void init()
    {

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String checked;

                checked = rememberPassword.isChecked() ? "true" : "false";

                updateRememberMeKnowns(userName.getText().toString(), password.getText().toString(), checked);

                if(!isEmpty(userName.getText().toString())
                        && !isEmpty(password.getText().toString()))
                {

                    authenticateUser(userName.getText().toString(),password.getText().toString());

                }

                else if (isEmpty(userName.getText().toString()))
                {

                    userName.setError(getResources().getString(R.string.username_error));
                }

                else if (isEmpty(password.getText().toString()))
                {

                    password.setError(getResources().getString(R.string.password_required_error));

                }
            }
        });



        hideSoftKeyboard();

    }

    private void authenticateUser(String email, String password) {
        User curUser = new User();
        curUser.setEmail(email);
        curUser.setPassword(password);

        firebaseDB.signInUser(curUser, MainActivity.this);

    }

    private void updateRememberMeKnowns(String email, String password, String checked)
    {

        Paper.book().write("gotRememberMe", checked);
        Paper.book().write("rememberMeEmail", email);
        Paper.book().write("rememberMePassword", password);

    }

    private boolean isEmpty(String string)
    {
        return string.equals("");
    }

    private void hideSoftKeyboard()
    {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }



}
