package com.example.pfc.email;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.pfc.R;

public class EmailDialog extends DialogFragment {
    private EditText txtAsunto;
    private EditText txtMensaje;
    public String destino;

    public EmailDialog() {    } //Empty constructor

    public static EmailDialog newInstance(String emailDestino, boolean colectivo) {
        EmailDialog frag = new EmailDialog();
        Bundle args = new Bundle();
        args.putString("destino", emailDestino);
        args.putBoolean("colectivo", colectivo);
        frag.setArguments(args);
        return frag;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.popup_email, container);
    }
    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Envío de email al tutor");
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        destino = getArguments().getString("destino", "");
        boolean colectivo=getArguments().getBoolean("colectivo", false);

        EditText edtAsunto=(EditText)view.findViewById(R.id.edtAsunto);
        EditText edtMensaje=(EditText)view.findViewById(R.id.edtMensaje);
        ImageButton btnSend=(ImageButton)view.findViewById(R.id.btnSend);
        //@@@ PRUEBAS
        //destino="manuel_campelo@yahoo.com";
        //@@@ PRUEBAS
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View vista) {
                String asunto=edtAsunto.getText().toString();
                String mensaje=edtMensaje.getText().toString();
                sendEmail(destino,asunto,mensaje);
                dismiss(); //Cierre del dialogo.
            }
        });
    }

    //Envio de email a tutores.
    private void sendEmail(String email,String subject,String message) {
        String mEmail = email;        String mSubject = subject;        String mMessage = message;
        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), mEmail, mSubject, mMessage);
        javaMailAPI.execute();
        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_msg_enviado), Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
