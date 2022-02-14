package com.example.pfc.fragmentos;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.pfc.MainActivity;
import com.example.pfc.R;
import com.example.pfc.app.Constants;
import com.example.pfc.modelos_datos.Documento;
import java.util.List;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Documentos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Documentos extends Fragment {

    private int ordenCiclo;
    private boolean estado;
    List<Documento> docs;
    public Integer[] ids;

    public Documentos(int ordenCiclo, boolean estado) {    }

    @Override  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ordenCiclo = getArguments().getInt("ciclo");
            estado = getArguments().getBoolean("estado");
        }
    }

    /**
     * @param ordenCiclo Parameter 1.
     * @param estado
     * @return A new instance of fragment Inicio.
     */
    public static Documentos newInstance(int ordenCiclo, boolean estado) {
        Documentos fragment = new Documentos(ordenCiclo,estado);
        Bundle args = new Bundle();
        args.putInt("ciclo", ordenCiclo);
        args.putBoolean("estado", estado);
        fragment.setArguments(args);
        return fragment;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_documentos, container, false);

        //Establecemos el texto del ciclo a mostrar
        String curCicloTxt="";
        long ciclo_id=0;
        if (ordenCiclo==0) {
            curCicloTxt="Documentos Generales";
        } else {
            curCicloTxt="Documentos del ciclo: "+((MainActivity)getActivity()).ciclosList.get(ordenCiclo-1).getNombreCiclo();
            ciclo_id=((MainActivity)getActivity()).ciclosList.get(ordenCiclo-1).getId();
        }
        TextView docsCiclo=(TextView)view.findViewById(R.id.documentosCiclo);
        docsCiclo.setText(curCicloTxt);

        long user=0;
        if (estado) {user=1;}
        Call<List<Documento>> call_docs=  ((MainActivity)getActivity()).APIService.getDocumentos(user,ciclo_id);
        call_docs.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                if (response.body()==null) {
                    LinearLayout marco_docs=(LinearLayout)view.findViewById(R.id.marco_documentos);
                    LinearLayout marco_sin_docs=(LinearLayout)view.findViewById(R.id.marco_sin_docs);
                    marco_docs.setVisibility(View.GONE);
                    marco_sin_docs.setVisibility(View.VISIBLE);
                } else {
                    //Manejamos los datos obtenidos, creamos un array de
                    docs=(List<Documento>)response.body();
                    ids=new Integer[docs.size()];
                    for (int i=0; i<docs.size();i++) {
                        ids[i]=docs.get(i).getId();
                    }
                    //Asociamos el AdaptadorDocumentos con el listView
                    ListView recDocumentos = (ListView) view.findViewById(R.id.lstDocumentos);
                    AdaptadorDocumentos adptDocumentos = new AdaptadorDocumentos(getActivity().getApplicationContext(), R.layout.lista_documentos, ids);
                    recDocumentos.setAdapter(adptDocumentos);
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_docs), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        return view;
    }

    //Creamos la clase ViewHolder que guarda los datos de un registro a mostrar en el ListView
    static class ViewHolder {
        TextView txtNombre;        TextView txtTipo;
        TextView txtDescripcion;   Button btnOpen;
    }

    //Creamos la clase que gestiona el relleno de los documentos en el listview personalizado.
    public class AdaptadorDocumentos extends ArrayAdapter<Integer> {
        public AdaptadorDocumentos(Context ctx, int viewId, Integer[] objects) {
            super(ctx,viewId,objects);
        }
        @Override public View getView(int pos, View view, ViewGroup prntView) {
            View esteDoc=view;
            ViewHolder holder;
            if (esteDoc==null) {
                LayoutInflater inf=getLayoutInflater();
                esteDoc=inf.inflate(R.layout.lista_documentos,prntView,false);
                holder=new ViewHolder();
                holder.txtNombre=(TextView)esteDoc.findViewById(R.id.txtNombre);
                holder.txtTipo=(TextView)esteDoc.findViewById(R.id.txtTipo);
                holder.txtDescripcion=(TextView)esteDoc.findViewById(R.id.txtDescripcion);
                holder.btnOpen=(Button)esteDoc.findViewById(R.id.btnOpen);
                esteDoc.setTag(holder);
            } else {
                holder=(ViewHolder)esteDoc.getTag();
            }

            holder.txtNombre.setText(docs.get(pos).getNombre());
            holder.txtTipo.setText(docs.get(pos).getTipo());
            holder.txtDescripcion.setText(docs.get(pos).getDescripcion());
            if (docs.get(pos).getIsFile()==true) {
                holder.btnOpen.setText(getString(R.string.descargar_fichero));
                holder.btnOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fichero, 0, 0, 0);
            } else {
                holder.btnOpen.setText(getString(R.string.ir_link));
                holder.btnOpen.setCompoundDrawablesWithIntrinsicBounds(R.drawable.link, 0, 0, 0);
            }
            //Creamos el evento de click en la lista de documentos.
            holder.btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View view) {
                    String link="";
                    if (docs.get(pos).getIsFile()==true) {
                        link= Constants.BASE_URI_DOCS+docs.get(pos).getUri();
                        String DownloadUrl = link;
                        DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(DownloadUrl));
                        request1.setVisibleInDownloadsUi(false);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request1.allowScanningByMediaScanner();
                            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                        }
                        request1.setDestinationInExternalFilesDir(getActivity(), Environment.getRootDirectory()+"/Download", link.substring(link.lastIndexOf('/') + 1));
                        DownloadManager manager1 = (DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                        Objects.requireNonNull(manager1).enqueue(request1);
                        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_doc_down)+link.substring(link.lastIndexOf('/') + 1), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        }
                    } else {
                        link=docs.get(pos).getUri();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(link));
                        startActivity(i);
                    }
                }
            });
            return esteDoc;
        }
    }
}