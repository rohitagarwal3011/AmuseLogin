package com.app.rohit.amuselogin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText mUsername;
    private EditText mEmail;
    private EditText mPassword;
    private Button mSignup;


    public Signup() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Signup newInstance() {
        Signup fragment = new Signup();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_signup, container, false);
        mUsername = (EditText) itemView.findViewById(R.id.username);
        mEmail = (EditText) itemView.findViewById(R.id.email);
        mPassword = (EditText) itemView.findViewById(R.id.password);
        mSignup = (Button) itemView.findViewById(R.id.signup);
        mSignup.setOnClickListener(this);
        return itemView;
    }

    private void initView( final View itemView) {
        mUsername = (EditText) itemView.findViewById(R.id.username);
        mEmail = (EditText) itemView.findViewById(R.id.email);
        mPassword = (EditText) itemView.findViewById(R.id.password);
        mSignup = (Button) itemView.findViewById(R.id.signup);
        mSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signup:
                if(verify(mUsername)&&verify(mEmail)&&verify(mPassword))
                {
                    Toast.makeText(getContext(),"SignUp Successful",Toast.LENGTH_LONG).show();
                    User user = new User();
                    user.setEmail(mEmail.getText().toString());
                    user.setUser_name(mUsername.getText().toString());
                    user.setType("manual");
                    user.setPass(mPassword.getText().toString());

                    AppDatabase.getAppDatabase(getContext()).userDao().insertAll(user);
                    Intent intent  = new Intent(getContext(),HomeActivity.class);
                    intent.putExtra("email",mEmail.getText().toString());
                    getActivity().startActivity(intent);
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
}
