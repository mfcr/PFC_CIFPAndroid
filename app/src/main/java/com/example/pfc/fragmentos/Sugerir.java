package com.example.pfc.fragmentos;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.pfc.MainActivity;
import com.example.pfc.R;
import com.example.pfc.modelos_datos.Ciclos;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Sugerir#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Sugerir extends Fragment {

    public Sugerir() {    }

    public static Sugerir newInstance(String param1, String param2) {
        Sugerir fragment = new Sugerir();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override  public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);    }

    @Override  public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_sugerir, container, false);
        EditText edtNombre=(EditText)view.findViewById(R.id.edtNombre);
        EditText edtEmail=(EditText)view.findViewById(R.id.edtEmail);
        EditText edtPropuesta=(EditText)view.findViewById(R.id.edtPropuesta);
        Button btnSend = (Button)view.findViewById(R.id.btnSend);
        Spinner spnCicloId=(Spinner)view.findViewById(R.id.spnCicloId);

        //Si usuario logeado rellenamos su email.
        if (((MainActivity)getActivity()).email.length()>0) {
            edtEmail.setText(((MainActivity)getActivity()).email);
        }

        //Capturamos los ciclos obtenidos en la actividad principal para mostrarlos en el spinner.
        List<Ciclos> ciclosList=((MainActivity)getActivity()).ciclosList;
        ArrayList<String> ciclos=new ArrayList<String>();
        ArrayList<Integer> ids =new ArrayList<Integer>();
        for (int i=0; i<ciclosList.size();i++) {
            ciclos.add(ciclosList.get(i).getCodigoCiclo()+": "+ciclosList.get(i).getNombreCiclo());
            ids.add(ciclosList.get(i).getId());
        }

        //Creamos el adaptador del spinner de ciclos y lo asignamos al spinner
        ArrayAdapter adapterCiclo =new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,ciclos);
        adapterCiclo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCicloId.setAdapter(adapterCiclo);
        //Listener del evento de click en enviar
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches() && edtPropuesta.getText().toString().length()>0) {
                    //Obtenemos el ciclo id del spinne
                    Call<Object> call_props=  ((MainActivity)getActivity()).APIService.postProyectosPropuestos(
                            edtNombre.getText().toString(),ids.get((int)spnCicloId.getSelectedItemId()).intValue(),edtEmail.getText().toString(),
                            edtPropuesta.getText().toString());
                    call_props.enqueue(new Callback() {
                        @Override  public void onResponse(Call call, Response response) {
                            if (response.code()<300) {
                                Toast toast=Toast.makeText(getActivity(), R.string.propuesta_ok, Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                                //Cargamos el fragment de inicio.
                                Fragment newFragment = new Inicio();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.marco, newFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_propuesta), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        }
                        @Override  public void onFailure(Call call, Throwable t) {
                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_propuesta), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    });
                } else {
                    if  (edtPropuesta.getText().toString().length()>0) {
                        Toast toast=Toast.makeText(getActivity(), R.string.email_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else {
                        Toast toast=Toast.makeText(getActivity(), R.string.empty_prop_err, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                }
            }
        });
        return view;
    }
}