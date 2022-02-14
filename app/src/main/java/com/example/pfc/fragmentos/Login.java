package com.example.pfc.fragmentos;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.pfc.MainActivity;
import com.example.pfc.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    public Login() {    }

    public static Login newInstance() {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login, container, false);
        EditText edtEmail=(EditText) view.findViewById(R.id.edtEmail);
        EditText edtPassword=(EditText) view.findViewById(R.id.edtPassword);
        CheckBox chkRemember=(CheckBox)view.findViewById(R.id.chkRemember);

        Button btnExit = (Button)view.findViewById(R.id.exitButton);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((MainActivity)getActivity()).logout();
            }
        });
        Button btnLogin = (Button)view.findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches() && edtPassword.getText().toString().length()>0) {
                    ((MainActivity)getActivity()).login(edtEmail.getText().toString().toLowerCase(),edtPassword.getText().toString(),chkRemember.isChecked());
                } else {
                    if  (edtPassword.getText().toString().length()>0) {
                        Toast toast=Toast.makeText(getActivity(), R.string.email_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else {
                        Toast toast=Toast.makeText(getActivity(), R.string.empty_pass_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            }
        });
        return view;
    }
}