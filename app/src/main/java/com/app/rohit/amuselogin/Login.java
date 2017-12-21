package com.app.rohit.amuselogin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SignInButton mSignInButtonGoogle;
    private LoginButton loginButton;
    private Button emailLogin;
    private EditText email;
    private EditText password;

    GoogleSignInClient mGoogleSignInClient;

   CallbackManager callbackManager = CallbackManager.Factory.create();

    public Login() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Login newInstance() {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);
        mSignInButtonGoogle = (SignInButton) rootview.findViewById(R.id.google_sign_in_button);

        emailLogin = (Button) rootview.findViewById(R.id.email_login);
        email = (EditText) rootview.findViewById(R.id.email);
        password = (EditText) rootview.findViewById(R.id.password);

        loginButton = (LoginButton) rootview.findViewById(R.id.fb_login_button);
        loginButton.setReadPermissions("email");
        // If using in a fragment
        loginButton.setFragment(this);

        try {

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Insert your code here
                                //  LoginActivity.this.progressDiaAppUtills.loggerismiss();
                                try {

                                    String name = object.getString("name");
                                    Log.d(TAG, "Name :" + name);

                                    String email = object.getString("email");
                                    Log.d(TAG, "Email :" + email);

                                    User user = new User();
                                    user.setEmail(email);
                                    user.setUser_name(name);
                                    user.setType("Fb");
                                    user.setPass("");

                                    AppDatabase.getAppDatabase(getContext()).userDao().insertAll(user);
                                    start_home_activity(email);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
              //  Toast.makeText(getContext(),loginResult.getAccessToken().getUserId(),Toast.LENGTH_LONG).show();
                AccessToken accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Insert your code here
                                //  LoginActivity.this.progressDiaAppUtills.loggerismiss();
                                try {

                                   String name = object.getString("name");
                                   Log.d(TAG, "Name :" + name);

                                   String email = object.getString("email");
                                    Log.d(TAG, "Email :" + email);

                                    User user = new User();
                                    user.setEmail(email);
                                    user.setUser_name(name);
                                    user.setType("Fb");
                                    user.setPass("");

                                    AppDatabase.getAppDatabase(getContext()).userDao().insertAll(user);
                                    start_home_activity(email);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();




                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if(account!=null) {
           // Toast.makeText(getContext(), account.getEmail(), Toast.LENGTH_LONG).show();
//            User user = new User();
//            user.setEmail(account.getEmail());
//            user.setUser_name(account.getDisplayName());
//            user.setType("Google");
//            user.setPass("");
//
//            AppDatabase.getAppDatabase(getContext()).userDao().insertAll(user);
            start_home_activity(account.getEmail());
        }
        mSignInButtonGoogle.setOnClickListener(this);
        emailLogin.setOnClickListener(this);

    }

    private void initView(final View itemView) {
        mSignInButtonGoogle = (SignInButton) itemView.findViewById(R.id.google_sign_in_button);
        mSignInButtonGoogle.setOnClickListener(this);
    }

    private static final int RC_SIGN_IN = 234;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                Log.d("Google Login ","Clicked");
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

                break;

            case R.id.email_login:

                if(verify(email)&&verify(password))
                {
                   User user= AppDatabase.getAppDatabase(getContext()).userDao().findByEmail(email.getText().toString());
                 if(user!=null) {
                     if (user.getPass().equalsIgnoreCase(password.getText().toString())) {
                         start_home_activity(user.getEmail());
                     } else {

                         Toast.makeText(getContext(),"Email and Password doesn't match",Toast.LENGTH_LONG).show();
                     }
                 }
                 else
                 {
                     Toast.makeText(getContext(),"Email doesn't exist",Toast.LENGTH_LONG).show();
                 }
                }


                break;
            default:
                break;
        }
    }

    public boolean verify(EditText a)
    {
        if(a.getText().toString().trim().length()>0)
        {
            return true;
        }
        else {
            a.setError("Please enter this field");
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
       // Toast.makeText(getContext(),String.valueOf(requestCode),Toast.LENGTH_LONG).show();
        Log.d("Request code",""+requestCode);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
      //  Toast.makeText(getContext(),"Handle Sign In Result",Toast.LENGTH_LONG).show();
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

          //  Toast.makeText(getContext(),account.getEmail(),Toast.LENGTH_LONG).show();
            User user = new User();
            user.setEmail(account.getEmail());
            user.setUser_name(account.getDisplayName());
            user.setType("Google");
            user.setPass("");

            AppDatabase.getAppDatabase(getContext()).userDao().insertAll(user);
            start_home_activity(account.getEmail());

            // Signed in successfully, show authenticated UI.
           // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
           // updateUI(null);
        }
    }

    public void start_home_activity(String email)
    {
        Intent intent  = new Intent(getContext(),HomeActivity.class);
        intent.putExtra("email",email);
        getActivity().startActivity(intent);
    }
}
