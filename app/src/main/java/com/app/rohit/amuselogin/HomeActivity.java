package com.app.rohit.amuselogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView mUsername;
    private TextView mEmail;
    private Button mLogout;
    private TextView type;

    String email="";
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        email=intent.getStringExtra("email");
        initView();
    }

    private void initView() {
        mUsername = (TextView) findViewById(R.id.username);
        mEmail = (TextView) findViewById(R.id.email);
        mLogout = (Button) findViewById(R.id.logout);
        type = (TextView) findViewById(R.id.type);
        mLogout.setOnClickListener(this);

       user= AppDatabase.getAppDatabase(this).userDao().findByEmail(email);
        mEmail.setText(user.getEmail());
        mUsername.setText(user.getUser_name());
        if(user.getType().equalsIgnoreCase("Google"))
        {
            type.setText("Google");
        }
        else if(user.getType().equalsIgnoreCase("fb"))
        {
            type.setText("Facebook");
        }
        else
        {
            type.setText("Email");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout:
                if(user.getType().equalsIgnoreCase("Google")) {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
                    if (account != null) {
                        mGoogleSignInClient.signOut()
                                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // ...
                                        setmLogout();
                                    }
                                });
                    }

                }
                else if(user.getType().equalsIgnoreCase("fb")) {

                    LoginManager.getInstance().logOut();
                    setmLogout();
                }
                else {
                    setmLogout();
                }

                break;
            default:
                break;
        }
    }

    public void setmLogout()
    {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);

    }


}
