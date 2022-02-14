package com.example.pfc.fragmentos;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.pfc.MainActivity;
import com.example.pfc.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login2 extends Fragment {

    public String email;
    public boolean recordar;

    public Login2(String email, boolean recordar) {   }

    public static Login2 newInstance(String email, boolean recordar) {
        Login2 fragment = new Login2(email,recordar);
        Bundle args = new Bundle();
        args.putString("email", email);
        args.putBoolean("recordar", recordar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email= getArguments().getString("email");
            recordar= getArguments().getBoolean("recordar");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_login2, container, false);
        EditText edtPass1=(EditText) view.findViewById(R.id.edtPass1);
        EditText edtPass2=(EditText) view.findViewById(R.id.edtPass2);

        Button btnExit = (Button)view.findViewById(R.id.exitButton);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((MainActivity)getActivity()).logout();
            }
        });
        Button btnLogin = (Button)view.findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (edtPass1.getText().toString().equals(edtPass2.getText().toString()) && edtPass2.getText().toString().length()>=8) {
                    ((MainActivity)getActivity()).cambiaContra(edtPass1.getText().toString(),recordar);
                } else {
                    if (edtPass2.getText().toString().length()>=8) {
                        Toast toast=Toast.makeText(getActivity(), R.string.login_pass_distintas_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }else{
                        Toast toast=Toast.makeText(getActivity(), R.string.login_min_pass_length_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            }
        });
        return view;
    }
}