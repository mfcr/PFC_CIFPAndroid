package com.example.pfc.fragmentos;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.pfc.MainActivity;
import com.example.pfc.app.Constants;
import com.example.pfc.app.contextoApp;
import com.example.pfc.clases_auxiliares.FileUtils;
import com.example.pfc.email.EmailDialog;
import com.example.pfc.clases_auxiliares.ListViewExpanded;
import com.example.pfc.R;
import com.example.pfc.modelos_datos.Alumnos;
import com.example.pfc.modelos_datos.DocenteTutColectivoCiclo;
import com.example.pfc.modelos_datos.DocentesModulosTutoriasColectivas;
import com.example.pfc.modelos_datos.DocumentosProyectos;
import com.example.pfc.modelos_datos.ModulosMatriculado;
import com.example.pfc.modelos_datos.ProyectosFull;
import com.example.pfc.modelos_datos.RubricasConGrupos;
import com.example.pfc.modelos_datos.TiposProyectoCiclosConTipos;
import com.example.pfc.remoto.PFCDataService;
import com.example.pfc.remoto.PfcApiClient;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MisProyectos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MisProyectos extends Fragment {

    public DocentesModulosTutoriasColectivas tutIndividual=null;
    public DocentesModulosTutoriasColectivas tutColectivo=null;
    public List<RubricasConGrupos> rubricasConGrupos;
    public ProyectosFull proyecto;
    public String[] posiblesEstadosMatricula={"Superado", "Convalidado","Exento", "Solicitado convalidación", "Matriculado", "No Matriculado"};
    public String [] posiblesTiposDocumentosProyecto={"Proyecto Completo","Memoria","Imagen","Video","Anexo","Codigo","Otros"};
    Integer [] idsDocs;
    Intent chooseFile;
    Uri DocProyecto;
    View misProyMainView;

    private int idProyecto;
    private int idAlumno;
    private boolean propio;

    public MisProyectos() {    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param idProyecto Parameter 1.
     * @param idAlumno Parameter 2.
     * @param propio
     * @return A new instance of fragment Inicio.
     */
    public static MisProyectos newInstance(int idProyecto, int idAlumno, boolean propio) {
        MisProyectos fragment = new MisProyectos();
        Bundle args = new Bundle();
        args.putInt("proyecto", idProyecto);
        args.putInt("alumno", idAlumno);
        args.putBoolean("propio", propio);
        fragment.setArguments(args);
        return fragment;
    }

    @Override    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idProyecto = getArguments().getInt("proyecto");
            idAlumno = getArguments().getInt("alumno");
            propio = getArguments().getBoolean("propio");
        }
    }

    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_mis_proyectos, container, false);
        misProyMainView=view;
        //Ocultamos todo y mostramos sólo un mensaje de cargando.
        LinearLayout total_proyectos=(LinearLayout)view.findViewById(R.id.total_proyectos);
        LinearLayout cargando_proyectos=(LinearLayout)view.findViewById(R.id.cargando_proyectos);
        total_proyectos.setVisibility(View.GONE);
        cargando_proyectos.setVisibility(View.VISIBLE);

        Call<List<ProyectosFull>> call_proy=  ((MainActivity)getActivity()).APIService.getProyectosFull((long)idProyecto);
        call_proy.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                LinearLayout marco_sin_proy=(LinearLayout)view.findViewById(R.id.marco_sin_proy);
                LinearLayout marco_total_proy=(LinearLayout)view.findViewById(R.id.marco_total_proy);
                if (response.body()==null) {
                    marco_total_proy.setVisibility(View.GONE);
                    marco_sin_proy.setVisibility(View.VISIBLE);
                } else {
                    proyecto=((List<ProyectosFull>)response.body()).get(0);
                    TextView lblTituloProyecto=(TextView)view.findViewById(R.id.lblTituloProyecto);
                    String titulo="";
                    if (proyecto.getNombreProyecto().isEmpty()) {
                        titulo="NOMBRE PROYECTO PDTE";
                    } else {
                        titulo=proyecto.getNombreProyecto();
                    }
                    titulo+="- CICLO: "+proyecto.getCiclos().getCodigoCiclo()+" - CURSO: "+proyecto.getCurso();
                    lblTituloProyecto.setText(titulo);
                    FillAlumno(view,proyecto.getAlumnos(),propio);
                    FillAnteproyecto(view,proyecto,propio);
                    FillTutores(view,proyecto,propio);
                    FillRubricas(view,proyecto,propio);
                    FillMatricula(view,proyecto,propio);
                    FillDocumentos(view,proyecto,propio);
                    //mostramos el contenido.
                    LinearLayout total_proyectos=(LinearLayout)view.findViewById(R.id.total_proyectos);
                    LinearLayout cargando_proyectos=(LinearLayout)view.findViewById(R.id.cargando_proyectos);
                    total_proyectos.setVisibility(View.VISIBLE);
                    cargando_proyectos.setVisibility(View.GONE);
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_docs), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });

//Creamos los eventos de pulsar en el boton mas y menos de todas los marcos.
//Creamos los eventos de pulsar en el boton mas y menos de todas los marcos.
//Creamos los eventos de pulsar en el boton mas y menos de todas los marcos.
        LinearLayout CardAlumno=(LinearLayout)view.findViewById(R.id.CardAlumno);
        TextView txtHeaderAlumno=(TextView)view.findViewById(R.id.txtHeaderAlumno);
        LinearLayout CardMatricula=(LinearLayout)view.findViewById(R.id.CardMatricula);
        TextView txtHeaderMatricula=(TextView)view.findViewById(R.id.txtHeaderMatricula);
        LinearLayout CardAnteproyecto=(LinearLayout)view.findViewById(R.id.CardAnteproyecto);
        TextView txtHeaderAnteproyecto=(TextView)view.findViewById(R.id.txtHeaderAnteproyecto);
        LinearLayout CardDocentesMensajesEvaluacion=(LinearLayout)view.findViewById(R.id.CardDocentesMensajesEvaluacion);
        TextView txtHeaderDocentesMensajesEvaluacion=(TextView)view.findViewById(R.id.txtHeaderDocentesMensajesEvaluacion);
        LinearLayout CardDocumentosProyecto=(LinearLayout)view.findViewById(R.id.CardDocumentosProyecto);
        TextView txtHeaderDocumentosProyecto=(TextView)view.findViewById(R.id.txtHeaderDocumentosProyecto);
        LinearLayout CardRubricasEvaluacion=(LinearLayout)view.findViewById(R.id.CardRubricasEvaluacion);
        TextView txtHeaderRubricasEvaluacion=(TextView)view.findViewById(R.id.txtHeaderRubricasEvaluacion);
        txtHeaderAlumno.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardAlumno.getVisibility()==View.GONE) {
                    CardAlumno.setVisibility(View.VISIBLE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });
        txtHeaderMatricula.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardMatricula.getVisibility()==View.GONE) {
                    CardMatricula.setVisibility(View.VISIBLE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });
        txtHeaderAnteproyecto.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardAnteproyecto.getVisibility()==View.GONE) {
                    CardAnteproyecto.setVisibility(View.VISIBLE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });
        txtHeaderDocentesMensajesEvaluacion.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardDocentesMensajesEvaluacion.getVisibility()==View.GONE) {
                    CardDocentesMensajesEvaluacion.setVisibility(View.VISIBLE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });
        txtHeaderDocumentosProyecto.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardDocumentosProyecto.getVisibility()==View.GONE) {
                    CardDocumentosProyecto.setVisibility(View.VISIBLE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });
        txtHeaderRubricasEvaluacion.setOnClickListener(new View.OnClickListener() {
            @Override   public void onClick( View view) {
                if (CardRubricasEvaluacion.getVisibility()==View.GONE) {
                    CardRubricasEvaluacion.setVisibility(View.VISIBLE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
                    CardAlumno.setVisibility(View.GONE);
                    txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardMatricula.setVisibility(View.GONE);
                    txtHeaderMatricula.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardAnteproyecto.setVisibility(View.GONE);
                    txtHeaderAnteproyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocentesMensajesEvaluacion.setVisibility(View.GONE);
                    txtHeaderDocentesMensajesEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                    CardDocumentosProyecto.setVisibility(View.GONE);
                    txtHeaderDocumentosProyecto.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                } else {
                    CardRubricasEvaluacion.setVisibility(View.GONE);
                    txtHeaderRubricasEvaluacion.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_input_add, 0, 0, 0);
                }
            }
        });

        //Registramos el Intent de seleccion de ficheros.
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");


        return view;
    }
    /*Funcion quese encarga de mostrar y gurdar los documentos del proyecto*/
    public void FillDocumentos(View view,ProyectosFull proyecto, boolean propio) {

        //Código relativo al marco_Documentos.
        ListViewExpanded lstDocsProyecto = view.findViewById(R.id.lstDocumentos);
        LinearLayout marco_sin_docs=(LinearLayout)view.findViewById(R.id.marco_sin_docs);


        idsDocs=new Integer[proyecto.getDocumentosProyectos().size()];
        for (int i=0;i<idsDocs.length;i++) {
            idsDocs[i]=proyecto.getDocumentosProyectos().get(i).getId();
        }

        //1º Carga de los tipos de documentos existentes en el ListView.
        Spinner spnTipoDocumento=(Spinner)view.findViewById(R.id.spnTipoDocumento);
        ArrayAdapter adapterTiposDeDocs =new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,posiblesTiposDocumentosProyecto);
        adapterTiposDeDocs.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDocumento.setAdapter(adapterTiposDeDocs);

        //Limpiamos campos del formulario de añadir..
        ((EditText)view.findViewById(R.id.txtNombreDocumento)).setText("");
        ((EditText)view.findViewById(R.id.txtLink)).setText("");
        ((TextView)view.findViewById(R.id.txtFichero)).setText("");
        spnTipoDocumento.setSelection(0);
        ((LinearLayout)view.findViewById(R.id.grupo_Fichero)).setVisibility(View.VISIBLE);
        ((LinearLayout)view.findViewById(R.id.grupo_Link)).setVisibility(View.GONE);
        DocProyecto=null;


        if (proyecto.getDocumentosProyectos().size()>0) {
            AdaptadorDocsProyecto adptDocsProyecto = new AdaptadorDocsProyecto(getActivity().getApplicationContext(), R.layout.lista_matricula, idsDocs);
            lstDocsProyecto.setAdapter(adptDocsProyecto);
        } else {
            lstDocsProyecto.setVisibility(View.GONE);
            marco_sin_docs.setVisibility(View.VISIBLE);
        }
        if (!propio || proyecto.getEstado()>=4) { //Si proyectos anteriores o estado > ProyectoAprobado no se muestra el formulario de añadir
            ((LinearLayout)view.findViewById(R.id.marco_agregar_documentos)).setVisibility(View.GONE);
            (view.findViewById(R.id.sep2)).setVisibility(View.GONE);
        } else {
            //2º Listener del boton de agregar documentos.
            Button btnSaveDocumento =(Button)view.findViewById(R.id.btnSaveDocumento);
            btnSaveDocumento.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View vista) {
                    boolean continuar=true;
                    boolean esFichero=true;
                    String uri="";
                    LinearLayout grupo_Fichero=(LinearLayout)view.findViewById(R.id.grupo_Fichero);
                    if (grupo_Fichero.getVisibility()==View.GONE) { esFichero=false; }

                    EditText txtNombreDocumento=(EditText)view.findViewById(R.id.txtNombreDocumento);
                    Spinner spnTipoDocumento=(Spinner)view.findViewById(R.id.spnTipoDocumento);
                    if (txtNombreDocumento.getText().toString().isEmpty()) {
                        continuar=false;
                        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_doc_noName), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                    if (continuar && esFichero) {
                        TextView txtFichero=(TextView)view.findViewById(R.id.txtFichero);
                        if (txtFichero.getText().toString().isEmpty()) {
                            continuar=false;
                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_doc_noFile), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER,0,0);
                            toast.show();
                        } else {
                            uri=Constants.BASE_URI_DOCS+txtFichero.getText().toString();
                        }
                    } else if (continuar && !esFichero) {
                        TextView txtLink=(TextView)view.findViewById(R.id.txtLink);
                        if (txtLink.getText().toString().isEmpty()) {
                            continuar = false;
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_doc_noLink), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else if (!URLUtil.isValidUrl(txtLink.getText().toString())) {
                            continuar = false;
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_doc_badLink), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            uri=txtLink.getText().toString();
                        }
                    }
                    if (continuar) {
                        if(esFichero) {
                            if(DocProyecto!=null){ //reponer al final
                                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_adding_doc), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();

                                RequestBody descripcion = RequestBody.create(((EditText)getView().findViewById(R.id.txtNombreDocumento)).getText().toString(),
                                        MediaType.parse("multipart/form-data"));
                                RequestBody tipoDocumento = RequestBody.create(((Spinner)getView().findViewById(R.id.spnTipoDocumento)).getSelectedItem().toString(),
                                        MediaType.parse("multipart/form-data"));
                                RequestBody isFile = RequestBody.create(String.valueOf(1),MediaType.parse("multipart/form-data"));
                                RequestBody publico = RequestBody.create(String.valueOf(1),MediaType.parse("multipart/form-data"));
                                RequestBody proyecto_id = RequestBody.create(String.valueOf(proyecto.getId()),MediaType.parse("multipart/form-data"));

                                // Get the Uri of the selected file
                                File file = new File(DocProyecto.getPath());
                                ContentResolver cR = contextoApp.getContext().getContentResolver();
                                RequestBody requestFile =RequestBody.create(file,null);
                                MultipartBody.Part fichero = MultipartBody.Part.createFormData("fichero", file.getName(), requestFile);

                                PFCDataService APIServiceDocs= PfcApiClient.getClient(Constants.BASE_URL_API_4_DOCUPLOAD).create(PFCDataService.class);
                                Call<Object> call_upload = APIServiceDocs.postDocProySaveFile(fichero,proyecto_id, descripcion, tipoDocumento,isFile,publico);
                                call_upload.enqueue(new Callback() {
                                    @Override  public void onResponse(Call call, Response response) {
                                        if (response.code()<300) {
                                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_doc_added), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                            ReloadDocumentosProyecto(misProyMainView);
                                        } else {
                                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_doc), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                        }
                                    }
                                    @Override  public void onFailure(Call call, Throwable t) {
                                        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_doc), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                    }
                                });
                            } else {
                                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_noDoc), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        } else {
                            Call<Object> call_post_doc = ((MainActivity) getActivity()).APIService.postDocProySaveLink((long) proyecto.getId(), txtNombreDocumento.getText().toString(),
                                    spnTipoDocumento.getSelectedItem().toString(), uri, 0, 1);
                            call_post_doc.enqueue(new Callback() {
                                @Override public void onResponse(Call call, Response response) {
                                    if (response.code() < 300) {
                                        Toast toast = Toast.makeText(getActivity(), R.string.msg_doc_added, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        ReloadDocumentosProyecto(misProyMainView);
                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                }
                                @Override public void onFailure(Call call, Throwable t) {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                }
            });
            //3º Crear un listener para la seleccion de link o fichero
            RadioGroup radiogrupo=(RadioGroup) view.findViewById(R.id.radiogrupo_fichero_link);
            radiogrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override  public void onCheckedChanged(RadioGroup group, int checkedId) {
                    LinearLayout grupo_Fichero=(LinearLayout)view.findViewById(R.id.grupo_Fichero);
                    LinearLayout grupo_Link=(LinearLayout)view.findViewById(R.id.grupo_Link);
                    if (checkedId == R.id.radio_fichero) {
                        if (grupo_Fichero.getVisibility()==View.GONE) {
                            grupo_Fichero.setVisibility(View.VISIBLE);
                            grupo_Link.setVisibility(View.GONE);
                        }
                    } else if (checkedId == R.id.radio_link) {
                        if (grupo_Link.getVisibility()==View.GONE) {
                            grupo_Fichero.setVisibility(View.GONE);
                            grupo_Link.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            //4º Crear un listener para selector de fichero
            ImageButton btnAddFile=(ImageButton)view.findViewById(R.id.btnAddFile);
            btnAddFile.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View vista) { //Lanzamos el file picker.
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, 100);
                }
            });
        }
    }

    @Override  public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode== Activity.RESULT_OK && data!=null){
            Uri uri = data.getData();

            FileUtils fu=new FileUtils(contextoApp.getContext());
            String path=fu.getPath(uri);
            File fichero=new File(path);
            if (fichero.exists()) {
                DocProyecto=Uri.fromFile(fichero);
                ((TextView)misProyMainView.findViewById(R.id.txtFichero)).setText(fichero.getName());
            } else {
                DocProyecto=null;
                ((TextView)misProyMainView.findViewById(R.id.txtFichero)).setText("");
            }
        }
    }

    public void ReloadDocumentosProyecto(View view) {
        //Recargamos los datos del proyecto.
        Call<List<ProyectosFull>> call_proy=  ((MainActivity)getActivity()).APIService.getProyectosFull((long)proyecto.getId());
        call_proy.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                LinearLayout marco_sin_proy=(LinearLayout)view.findViewById(R.id.marco_sin_proy);
                LinearLayout marco_total_proy=(LinearLayout)view.findViewById(R.id.marco_total_proy);
                if (response.body()==null) {
                    marco_total_proy.setVisibility(View.GONE);
                    marco_sin_proy.setVisibility(View.VISIBLE);
                } else {
                    proyecto=((List<ProyectosFull>)response.body()).get(0);
                    FillDocumentos(view,proyecto,propio);
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_docs), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    /*Funcion quese encarga de guardar los datos de las matrículas del proyecto y la preevealuacion de los docentes*/
    public void FillMatricula(View view,ProyectosFull proyecto, boolean propio) {
        //Código relativo al marco_DatosMatrícula.
        if (!propio) { //No se muestra en modo de proyectos anteriores.
            ((LinearLayout)view.findViewById(R.id.marco_matricula)).setVisibility(View.GONE);
        } else { //Modo misproyectos.
            List<ModulosMatriculado> matriculas=proyecto.getModulosMatriculados();
            Integer [] idsMods=new Integer[matriculas.size()];
            for (int i=0; i<matriculas.size();i++) {
                idsMods[i]=matriculas.get(i).getCicloModuloId();
            }
            //1º Rellenar los datos y poner a la escucha los botones de guardar cambios.
            ListViewExpanded lstMatricula= view.findViewById(R.id.lstMatricula);
            AdaptadorMatricula adptMatricula=new AdaptadorMatricula(getActivity().getApplicationContext(),R.layout.lista_matricula,idsMods);
            lstMatricula.setAdapter(adptMatricula);
            //2º Listener del boton de guardar cambios.
            Button btnSaveMatricula =(Button)view.findViewById(R.id.btnSaveMatriculas);
            if (proyecto.getEstado()>=4) { //No se admiten cambios en las matriculas.
                btnSaveMatricula.setVisibility(View.GONE);
            } else {
                btnSaveMatricula.setOnClickListener(new View.OnClickListener() {
                    @Override   public void onClick( View vista) {
                        for (int i=0;i<idsMods.length;i++) {
                            if (proyecto.getModulosMatriculados().get(i).getProyectoId()==-1) {
                                Call<Object> call_matric=  ((MainActivity)getActivity()).APIService.postMatriculas((long)proyecto.getModulosMatriculados().get(i).getId()
                                        ,proyecto.getModulosMatriculados().get(i).getEstado());
                                call_matric.enqueue(new Callback() {
                                    @Override  public void onResponse(Call call, Response response) {
                                        if (response.code()>=300) {
                                            Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_matricula), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                        }
                                    }
                                    @Override  public void onFailure(Call call, Throwable t) {
                                        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_matricula), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                    }
                                });
                            }
                        }
                        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_saved_mat), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                });
            }
        }
    }

    /*Funcion quese encarga de guardar los datos del las valoraciones de las rúbricas*/
    public void FillRubricas(View view,ProyectosFull proyecto, boolean propio) {
        //Código relativo al marco_Rubricas.
        if (!propio) { //No se muestra en modo de proyectos anteriores.
            ((LinearLayout)view.findViewById(R.id.marco_rubricas_valoraciones)).setVisibility(View.GONE);
        } else {

            Call<List<RubricasConGrupos>> call_rubricas=  ((MainActivity)getActivity()).APIService.getRubricasConGrupos((long)proyecto.getCurso(),(long)proyecto.getCicloId());
            call_rubricas.enqueue(new Callback() {
                @Override  public void onResponse(Call call, Response response) {
                    LinearLayout marco_sin_proy=(LinearLayout)view.findViewById(R.id.marco_sin_proy);
                    LinearLayout marco_total_proy=(LinearLayout)view.findViewById(R.id.marco_total_proy);
                    if (response.body()==null) {
                        marco_total_proy.setVisibility(View.GONE);
                        marco_sin_proy.setVisibility(View.VISIBLE);
                    } else {
                        rubricasConGrupos=(List<RubricasConGrupos>)response.body();
                        Integer [] rubIds=new Integer[rubricasConGrupos.size()];
                        for (int i=0; i<rubricasConGrupos.size();i++) {
                            rubIds[i]=rubricasConGrupos.get(i).getGrupoRubricaId();
                        }
                        //1ª Rellenar el ListView de rúbricas
                        ListViewExpanded lstRubricas= view.findViewById(R.id.lstRubricas);
                        AdaptadorRubricas adptRubricas=new AdaptadorRubricas(getActivity().getApplicationContext(),R.layout.lista_rubricas,rubIds);
                        lstRubricas.setAdapter(adptRubricas);
                    }
                }
                @Override  public void onFailure(Call call, Throwable t) {
                    Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_rub), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
            });
        }
    }

    /*Funcion quese encarga de guardar los datos del tutores, estados del proyecto y notas y de responder a los eventos dentro de su marco*/
    public void FillTutores(View view,ProyectosFull proyecto, boolean propio) {

        //Código relativo al marco_Docentes.

        //Ver u ocultar datos en función del tipo de acceso o si hay o no tutor individual asignado.
        if (!propio) {
            LinearLayout grupo_TutInd2=(LinearLayout)view.findViewById(R.id.grupo_TutInd2);
            grupo_TutInd2.setVisibility(View.GONE);
            ImageButton sendMailIndividual=(ImageButton)view.findViewById(R.id.sendMailIndividual);
            ImageButton sendMailColectivo=(ImageButton)view.findViewById(R.id.sendMailColectivo);
            TextView txtEstado=(TextView)view.findViewById(R.id.txtEstado);
            sendMailIndividual.setVisibility(View.GONE);
            sendMailColectivo.setVisibility(View.GONE);
            TextView lblEstado=(TextView)view.findViewById(R.id.lblEstado);
            lblEstado.setVisibility(View.GONE);
            txtEstado.setVisibility(View.GONE);
        }
        if (proyecto.getDocenteId()==null) { //Ocultamos datos del tutor individual
            LinearLayout grupo_TutInd2=(LinearLayout)view.findViewById(R.id.grupo_TutInd2);
            LinearLayout grupo_TutInd1=(LinearLayout)view.findViewById(R.id.grupo_TutInd1);
            TextView msg_sin_tutIndiv=(TextView)view.findViewById(R.id.msg_sin_tutIndiv);
            View sep3=view.findViewById(R.id.sep3);
            grupo_TutInd1.setVisibility(View.GONE);
            grupo_TutInd2.setVisibility(View.GONE);
            sep3.setVisibility(View.GONE);
            msg_sin_tutIndiv.setVisibility(View.VISIBLE);
        }
        Call<List<DocentesModulosTutoriasColectivas>> call_docentes1=  ((MainActivity)getActivity()).APIService.getDocentesModulosTutoriasColectivas();
        call_docentes1.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                LinearLayout marco_sin_proy=(LinearLayout)view.findViewById(R.id.marco_sin_proy);
                LinearLayout marco_total_proy=(LinearLayout)view.findViewById(R.id.marco_total_proy);
                if (response.body()==null) {
                    marco_total_proy.setVisibility(View.GONE);
                    marco_sin_proy.setVisibility(View.VISIBLE);
                } else {
                    List<DocentesModulosTutoriasColectivas> docentesFull=(List<DocentesModulosTutoriasColectivas>)response.body();
                    for (int i=0; i<docentesFull.size();i++) {
                        if (proyecto.getDocenteId()!=null && proyecto.getDocenteId().equals(docentesFull.get(i).getId())) {
                            tutIndividual=docentesFull.get(i);
                        }
                        List<DocenteTutColectivoCiclo> tutColCiclos =docentesFull.get(i).getDocenteTutColectivoCiclos();
                        for (int j=0;j<tutColCiclos.size();j++) {
                            if (proyecto.getCurso().equals(tutColCiclos.get(j).getCurso()) && proyecto.getCicloId().equals(tutColCiclos.get(j).getCicloId())) {
                                tutColectivo=docentesFull.get(i);
                            }
                        }
                        if (tutIndividual!=null && tutColectivo!=null) {break;}
                    }
                    // Rellenar los datos
                    TextView txtTutorIndividual=(TextView)view.findViewById(R.id.txtTutorIndividual);
                    TextView txtNotaPrevia=(TextView)view.findViewById(R.id.txtNotaPrevia);
                    TextView txtComentarioPrevio=(TextView)view.findViewById(R.id.txtComentarioPrevio);
                    TextView txtTutorColectivo=(TextView)view.findViewById(R.id.txtTutorColectivo);
                    TextView txtNotaFinal=(TextView)view.findViewById(R.id.txtNotaFinal);
                    if (tutIndividual!=null) {
                        txtTutorIndividual.setText(tutIndividual.getNombre() + " " + tutIndividual.getApellido1() + " " + tutIndividual.getApellido2());
                        if (proyecto.getNotaPrevia() == null) {         txtNotaPrevia.setText(getString(R.string.pdte));
                        } else {                                        txtNotaPrevia.setText(proyecto.getNotaPrevia().toString());   }
                        if (proyecto.getComentarioPrevio() == null) {   txtComentarioPrevio.setText(getString(R.string.pendiente));
                        } else {                                        txtComentarioPrevio.setText(proyecto.getComentarioPrevio());  }
                    }
                    if (tutColectivo!=null) {
                        txtTutorColectivo.setText(tutColectivo.getNombre()+" "+tutColectivo.getApellido1()+" "+tutColectivo.getApellido2());
                        if (proyecto.getNotaFinal()==null) {            txtNotaFinal.setText(getString(R.string.pendiente));
                        } else {                                        txtNotaFinal.setText(proyecto.getNotaFinal().toString());    }
                        if (proyecto.getEstados()!=null) {
                            TextView txtEstado=(TextView)view.findViewById(R.id.txtEstado);
                            txtEstado.setText(proyecto.getEstados().getEstado());
                        }
                    }
                    //2º Establecer los listeners para el envío de emails.
                    ImageButton sendMailIndividual=(ImageButton)view.findViewById(R.id.sendMailIndividual);
                    ImageButton sendMailColectivo=(ImageButton)view.findViewById(R.id.sendMailColectivo);
                    sendMailIndividual.setOnClickListener(new View.OnClickListener() {
                        @Override   public void onClick( View vista) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            EmailDialog emailFrg = EmailDialog.newInstance(tutIndividual.getEmail(),false);
                            emailFrg.show(fm, "fragment_edit_name");
                        }
                    });
                    sendMailColectivo.setOnClickListener(new View.OnClickListener() {
                        @Override   public void onClick( View vista) {
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            EmailDialog emailFrg = EmailDialog.newInstance(tutColectivo.getEmail(),true);
                            emailFrg.show(fm, "fragment_edit_name");
                        }
                    });
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_docentes), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

        /*Funcion quese encarga de guardar los datos del anteproyecto y de responder a los eventos dentro de su marco*/
    public void FillAnteproyecto(View view,ProyectosFull proyecto, boolean propio) {
        //Código relativo al marco_Anteproyecto.

        //1º Rellenar los datos a mostrar.
        EditText edtNombreProyecto=(EditText)view.findViewById(R.id.edtNombreProyecto);
        EditText edtOtroTipoProyecto=(EditText)view.findViewById(R.id.edtOtroTipoProyecto);
        EditText edtDescripcionProyecto=(EditText)view.findViewById(R.id.edtDescripcionProyecto);
        EditText edtRequisitosProyecto=(EditText)view.findViewById(R.id.edtRequisitosProyecto);
        EditText edtModulosProyecto=(EditText)view.findViewById(R.id.edtModulosProyecto);
        Spinner spnTipoProyecto=(Spinner) view.findViewById(R.id.spnTipoProyecto);
        Button btnSaveAnteproyecto=(Button) view.findViewById(R.id.btnSaveAnteproyecto);
        if (proyecto.getNombreProyecto()!=null) {                  edtNombreProyecto.setText(proyecto.getNombreProyecto()); }
        if (proyecto.getDescTipo()!=null) {                        edtOtroTipoProyecto.setText(proyecto.getDescTipo()); }
        if (proyecto.getTextoModulosRelacionados()!=null) {        edtDescripcionProyecto.setText(proyecto.getTextoModulosRelacionados()); }
        if (proyecto.getTextoRequisitosFuncionales()!=null) {      edtRequisitosProyecto.setText(proyecto.getTextoRequisitosFuncionales()); }
        if (proyecto.getTextoModulosRelacionados()!=null) {        edtModulosProyecto.setText(proyecto.getTextoModulosRelacionados()); }

        if (!propio || proyecto.getEstado()>=2 ) { //Si proyectos_anteriores o estado del proyecto > Propuesta aprobada.
            edtNombreProyecto.setFocusable(false);
            edtOtroTipoProyecto.setFocusable(false);
            edtDescripcionProyecto.setFocusable(false);
            edtRequisitosProyecto.setFocusable(false);
            edtModulosProyecto.setFocusable(false);
            spnTipoProyecto.setEnabled(false);
            btnSaveAnteproyecto.setVisibility(View.GONE);
        }

        Call<List<TiposProyectoCiclosConTipos>> call_tipos_proy=  ((MainActivity)getActivity()).APIService.getTiposProyectosCiclosConTipos((long)proyecto.getCicloId());
        call_tipos_proy.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                LinearLayout marco_sin_proy=(LinearLayout)view.findViewById(R.id.marco_sin_proy);
                LinearLayout marco_total_proy=(LinearLayout)view.findViewById(R.id.marco_total_proy);
                if (response.body()==null) {
                    marco_total_proy.setVisibility(View.GONE);
                    marco_sin_proy.setVisibility(View.VISIBLE);
                } else {
                    List<TiposProyectoCiclosConTipos> tipos_proyecto=(List<TiposProyectoCiclosConTipos>)response.body();
                    String [] tps=new String[tipos_proyecto.size()];
                    int selectedPos=0;
                    for (int i=0; i<tipos_proyecto.size();i++) {
                        tps[i]=tipos_proyecto.get(i).getTipoProyectos().getTipo();
                        if (proyecto.getTipoProyectoId()==tipos_proyecto.get(i).getTipoProyectoId()) {
                            selectedPos=i;
                        }
                    }
                    //Inflamos el Spinner: Creamos el adaptador del spinner de Tipos y lo asignamos al spinner, seleccionamos el valor guardado si lo hay
                    ArrayAdapter adapterTiposDeProyecto =new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item,tps);
                    adapterTiposDeProyecto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spnTipoProyecto.setAdapter(adapterTiposDeProyecto);
                    spnTipoProyecto.setSelection(selectedPos);

                    //Listener del boton guardar.
                    btnSaveAnteproyecto.setOnClickListener(new View.OnClickListener() {
                        @Override   public void onClick( View vista) {
                            Call<Object> call_post_antepr=  ((MainActivity)getActivity()).APIService.postAnteproyectos(
                                    (long)proyecto.getId(),edtNombreProyecto.getText().toString(),
                                    (long)tipos_proyecto.get((int)spnTipoProyecto.getSelectedItemId()).getTipoProyectoId(),
                                    edtOtroTipoProyecto.getText().toString(),edtDescripcionProyecto.getText().toString(),
                                    edtRequisitosProyecto.getText().toString(),edtModulosProyecto.getText().toString());
                            call_post_antepr.enqueue(new Callback() {
                                @Override  public void onResponse(Call call, Response response) {
                                    if (response.code()<300) {
                                        Toast toast=Toast.makeText(getActivity(), R.string.cambiado, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                    } else {
                                        Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER,0,0);
                                        toast.show();
                                    }
                                }
                                @Override  public void onFailure(Call call, Throwable t) {
                                    Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            });
                        }
                    });

                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_tipos), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }


    /*Funcion quese encarga de guardar los datos del alumno y de responder a los eventos dentro de su marco*/
    public void FillAlumno(View view, Alumnos alumno, boolean propio) {
        //Código relativo al marco_alumno.

        //1º Rellenar datos desde los obtenidos
        EditText edtNombreAlumno=(EditText)view.findViewById(R.id.edtNombreAlumno);
        EditText edtApellido1Alumno=(EditText)view.findViewById(R.id.edtApellido1Alumno);
        EditText edtApellido2Alumno=(EditText)view.findViewById(R.id.edtApellido2Alumno);
        EditText edtEmailAlumno=(EditText)view.findViewById(R.id.edtEmailAlumno);
        EditText edtNIFAlumno=(EditText)view.findViewById(R.id.edtNIFAlumno);
        EditText edtTelefonoAlumno=(EditText)view.findViewById(R.id.edtTelefonoAlumno);
        Button btnSaveAlumno=(Button)view.findViewById(R.id.btnSaveAlumno);
        //Datos mínimos obligatorios: nombre, apellido1, email y nif
        boolean datos=true;
        if (alumno.getNombre()!=null) {
            edtNombreAlumno.setText(alumno.getNombre());
        } else {
            datos=false;
        }
        if (alumno.getApellido1()!=null) {
            edtApellido1Alumno.setText(alumno.getApellido1());
        } else {
            datos=false;
        }
        if (alumno.getApellido2()!=null) {            edtApellido2Alumno.setText(alumno.getApellido2());        }
        if (alumno.getEmail()!=null) {            edtEmailAlumno.setText(alumno.getEmail());        }
        if (alumno.getDni()!=null) {
            edtNIFAlumno.setText(alumno.getDni());
        } else {
            datos=false;
        }
        if (alumno.getTelefono()!=null) {            edtTelefonoAlumno.setText(alumno.getTelefono());        }

        //2º Comprobar si hay datos personales guardados -->ocultar resto si no lo hay
        if (!propio ){ //Ocultamos campos no visibles en modo proyectos anteriores
            LinearLayout hide_alumno=(LinearLayout)view.findViewById(R.id.hide_alumno);
            hide_alumno.setVisibility(View.GONE);
            edtNombreAlumno.setFocusable(false);
            edtApellido1Alumno.setFocusable(false);
            edtApellido2Alumno.setFocusable(false);
        } else {
            if (proyecto.getEstado()>=4) {
                btnSaveAlumno.setVisibility(View.GONE);
                edtNombreAlumno.setFocusable(false);
                edtApellido1Alumno.setFocusable(false);
                edtApellido2Alumno.setFocusable(false);
                edtNIFAlumno.setFocusable(false);
                edtTelefonoAlumno.setFocusable(false);
            }
            if (!datos) {
                //Ocultamos todos los marcos distintos de alumno
                LinearLayout marcoMatricula=(LinearLayout)view.findViewById(R.id.marco_matricula);
                LinearLayout marcoAnteproyecto=(LinearLayout)view.findViewById(R.id.marco_anteproyecto);
                LinearLayout marcoDocentes=(LinearLayout)view.findViewById(R.id.marco_docentes_mensajes_evaluacion);
                LinearLayout marcoDocumentos=(LinearLayout)view.findViewById(R.id.marco_documentos_proyecto);
                LinearLayout marcoRubricas=(LinearLayout)view.findViewById(R.id.marco_rubricas_valoraciones);
                marcoMatricula.setVisibility(View.GONE);
                marcoAnteproyecto.setVisibility(View.GONE);
                marcoDocentes.setVisibility(View.GONE);
                marcoDocumentos.setVisibility(View.GONE);
                marcoRubricas.setVisibility(View.GONE);
                //Mostramos mensaje rellenar alumno primero + CardAlumno
                TextView msgDatosPersonales =(TextView)view.findViewById(R.id.msg_datos_personales);
                msgDatosPersonales.setVisibility(View.VISIBLE);
                LinearLayout CardAlumno=(LinearLayout)view.findViewById(R.id.CardAlumno);
                TextView txtHeaderAlumno=(TextView)view.findViewById(R.id.txtHeaderAlumno);
                CardAlumno.setVisibility(View.VISIBLE);
                txtHeaderAlumno.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_revert, 0, 0, 0);
            }
        }
        //3º Listener del boton guardar datos. Solo si proyecto propio
        if (propio) {

            btnSaveAlumno.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View vista) {
                    //Para guardar debe haber nombre apellido1 email y nif.
                    if (edtNombreAlumno.getText().toString().length() == 0 || edtApellido1Alumno.getText().toString().length() == 0 ||
                            edtNIFAlumno.getText().toString().length() == 0) {
                        Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_data_alumno), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Call<Object> call_post_alumno=  ((MainActivity)getActivity()).APIService.postAlumnos(
                                (long)alumno.getId(),edtNombreAlumno.getText().toString(),edtApellido1Alumno.getText().toString(),
                                edtApellido2Alumno.getText().toString(),edtNIFAlumno.getText().toString(),edtTelefonoAlumno.getText().toString());
                        call_post_alumno.enqueue(new Callback() {
                            @Override  public void onResponse(Call call, Response response) {
                                if (response.code()<300) {
                                    Toast toast=Toast.makeText(getActivity(), R.string.cambiado, Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                    //Si el resto estaba oculto, lo mostramos de nuevo
                                    TextView msgDatosPersonales =(TextView)view.findViewById(R.id.msg_datos_personales);
                                    if (msgDatosPersonales.getVisibility()==View.VISIBLE) {
                                        LinearLayout marcoMatricula = (LinearLayout) view.findViewById(R.id.marco_matricula);
                                        LinearLayout marcoAnteproyecto = (LinearLayout) view.findViewById(R.id.marco_anteproyecto);
                                        LinearLayout marcoDocentes = (LinearLayout) view.findViewById(R.id.marco_docentes_mensajes_evaluacion);
                                        LinearLayout marcoDocumentos = (LinearLayout) view.findViewById(R.id.marco_documentos_proyecto);
                                        LinearLayout marcoRubricas = (LinearLayout) view.findViewById(R.id.marco_rubricas_valoraciones);
                                        marcoMatricula.setVisibility(View.VISIBLE);
                                        marcoAnteproyecto.setVisibility(View.VISIBLE);
                                        marcoDocentes.setVisibility(View.VISIBLE);
                                        marcoDocumentos.setVisibility(View.VISIBLE);
                                        marcoRubricas.setVisibility(View.VISIBLE);
                                        msgDatosPersonales.setVisibility(View.GONE);
                                    }
                                } else {
                                    Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                }
                            }
                            @Override  public void onFailure(Call call, Throwable t) {
                                Toast toast=Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        });
                    }
                }
            });
        }
    }

    //Creamos la clase MatriculaViewHolder que guarda los datos de un registro de matricula.
    static class MatriculaViewHolder {
        TextView txtModulo;
        Spinner spnEstadosMatricula;
        TextView txtValoracionAnteproyecto;
        TextView txtComentario;
    }

    //Creamos la clase que gestiona el relleno de las matrñicula en el listview del marco_matricula.
    public class AdaptadorMatricula extends ArrayAdapter<Integer> {
        public AdaptadorMatricula(Context ctx, int viewId, Integer[] objects) {
            super(ctx, viewId, objects);
        }

        @Override
        public View getView(int pos, View view, ViewGroup prntView) {
            View estaMatricula = view;
            MatriculaViewHolder holder;
            if (estaMatricula == null) {
                LayoutInflater inf = getLayoutInflater();
                estaMatricula = inf.inflate(R.layout.lista_matricula, prntView, false);
                holder = new MatriculaViewHolder();
                holder.txtModulo = (TextView) estaMatricula.findViewById(R.id.txtModulo);
                holder.spnEstadosMatricula=(Spinner)estaMatricula.findViewById(R.id.spnEstadoMatricula);
                holder.txtValoracionAnteproyecto = (TextView) estaMatricula.findViewById(R.id.txtValoracionAnteproyecto);
                holder.txtComentario = (TextView) estaMatricula.findViewById(R.id.txtComentario);
                estaMatricula.setTag(holder);
            } else {
                holder = (MatriculaViewHolder) estaMatricula.getTag();
            }

            holder.txtModulo.setText(proyecto.getModulosMatriculados().get(pos).getModulos().getNombreModulo());
            if (proyecto.getModulosMatriculados().get(pos).getPreevaluado()==null) {
                holder.txtValoracionAnteproyecto.setText(getString(R.string.pdteEval));
            } else if (proyecto.getModulosMatriculados().get(pos).getPreevaluado()==0) {
                holder.txtValoracionAnteproyecto.setText(getString(R.string.noapto));
            } else {
                holder.txtValoracionAnteproyecto.setText(getString(R.string.apto));
            }
            if (proyecto.getModulosMatriculados().get(pos).getComentario()==null) {
                holder.txtComentario.setText(getString(R.string.pte_comentario));
            } else {
                holder.txtComentario.setText(proyecto.getModulosMatriculados().get(pos).getComentario());
            }
            //Creamos el adaptador del spinner de Estados de la matricula y lo asignamos al spinner
            ArrayAdapter<String> adapterSpinnerEstados = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, posiblesEstadosMatricula);
            holder.spnEstadosMatricula.setAdapter(adapterSpinnerEstados);
            holder.spnEstadosMatricula.setSelection(adapterSpinnerEstados.getPosition(proyecto.getModulosMatriculados().get(pos).getEstado()));
            if (proyecto.getEstado()>=4) {
                holder.spnEstadosMatricula.setEnabled(false);
            } else {
                //Evento del cambio de valor del spinner
                holder.spnEstadosMatricula.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                //Cambiamos el valor en el array de modulosmatriculados de forma que al dar en el boton guardar  --> guarde resultados.
                                //Usamos el campo proyecto_id a -1 si ha habido cambios de modo que no se hagan llamadas innecesarias.
                                String valorAnterior=proyecto.getModulosMatriculados().get(pos).getEstado();
                                String valorActual=holder.spnEstadosMatricula.getSelectedItem().toString();
                                if (!valorAnterior.equals(valorActual)) {
                                    proyecto.getModulosMatriculados().get(pos).setEstado(valorActual);
                                    proyecto.getModulosMatriculados().get(pos).setProyectoId(-1);
                                }
                            }
                            @Override  public void onNothingSelected(AdapterView<?> parent) { }
                        }
                );
            }
            return estaMatricula;
        }
    }

    //Creamos la clase DocsProyectoViewHolder que guarda los datos de un registro de docuemnto de proyecto.
    static class DocsProyectoViewHolder {
        EditText edtDocumento;
        Spinner spnTipo;
        CheckBox chkPublico;
        ImageButton btnLink;
        ImageButton btnGuardar;
        ImageButton btnBorrar;
        TextView lblGuardar;
        TextView lblBorrar;
    }

    //Creamos la clase que gestiona el relleno de los documentos del proyecto en el listview del marco_documentos.
    public class AdaptadorDocsProyecto extends ArrayAdapter<Integer> {
        public AdaptadorDocsProyecto(Context ctx, int viewId, Integer[] objects) {
            super(ctx,viewId,objects);
        }

        @Override public View getView(int pos, View view, ViewGroup prntView) {
            View esteDocumento=view;
            MisProyectos.DocsProyectoViewHolder holder;
            if (esteDocumento==null) {
                LayoutInflater inf=getLayoutInflater();
                esteDocumento=inf.inflate(R.layout.lista_documentos_proyecto,prntView,false);
                holder=new MisProyectos.DocsProyectoViewHolder();
                holder.edtDocumento=(EditText)esteDocumento.findViewById(R.id.edtDocumento);
                holder.spnTipo=(Spinner)esteDocumento.findViewById(R.id.spnTipo);
                holder.chkPublico=(CheckBox)esteDocumento.findViewById(R.id.chkPublico);
                holder.btnLink=(ImageButton)esteDocumento.findViewById(R.id.btnLink);
                holder.btnGuardar=(ImageButton)esteDocumento.findViewById(R.id.btnGuardar);
                holder.btnBorrar=(ImageButton)esteDocumento.findViewById(R.id.btnBorrar);
                holder.lblGuardar=(TextView)esteDocumento.findViewById(R.id.lblGuardar);
                holder.lblBorrar=(TextView)esteDocumento.findViewById(R.id.lblBorrar);
                esteDocumento.setTag(holder);
            } else {
                holder=(MisProyectos.DocsProyectoViewHolder)esteDocumento.getTag();
            }
            //Rellenamos datos
            DocumentosProyectos doc=proyecto.getDocumentosProyectos().get(pos);
            holder.edtDocumento.setText(doc.getDescripcion());
            holder.chkPublico.setChecked(doc.getPublico());
            if (doc.getIsFile()) {
                holder.btnLink.setImageResource(R.drawable.fichero);
            } else {
                holder.btnLink.setImageResource(R.drawable.link);
            }
            //Creamos el adaptador del spinner de tipos de docuemntos y lo asignamos al spinner
            ArrayAdapter<String> adapterSpinnerTiposDoc = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, posiblesTiposDocumentosProyecto);
            holder.spnTipo.setAdapter(adapterSpinnerTiposDoc);
            holder.spnTipo.setSelection(adapterSpinnerTiposDoc.getPosition(doc.getTipoDocumento()));

            //Creamos el evento de click en el link de un docuemnto de la lista de documentos.
            holder.btnLink.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View view) {
                    String link="";
                    if (doc.getIsFile()==true) {
                        link= Constants.BASE_URI_DOCS+doc.getUriDocumento();
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
                        link=doc.getUriDocumento();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(link));
                        startActivity(i);
                    }
                }
            });

            //Ocultamos vistas o desactivamos posibilidad de cambios si el usuario no es el propietario o el proyecto ya esta acabado
            if (!propio || proyecto.getEstado()>=4) {
                holder.lblBorrar.setVisibility(View.GONE);
                holder.btnBorrar.setVisibility(View.GONE);
                holder.lblGuardar.setVisibility(View.GONE);
                holder.btnGuardar.setVisibility(View.GONE);
                holder.edtDocumento.setFocusable(false);
                holder.spnTipo.setEnabled(false);
            } else {
                //Creamos el evento de click en el link de un docuemnto de la lista de documentos.
                holder.btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        if (holder.edtDocumento.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_doc_noName), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Call<Object> call_post_doc = ((MainActivity) getActivity()).APIService.postDocProyEdit(
                                    (long) doc.getId(), holder.edtDocumento.getText().toString(), holder.spnTipo.getSelectedItem().toString());
                            call_post_doc.enqueue(new Callback() {
                                @Override public void onResponse(Call call, Response response) {
                                    if (response.code() < 300) {
                                        Toast toast = Toast.makeText(getActivity(), R.string.cambiado, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        //Guardamos tambien los cambios en el array.
                                        proyecto.getDocumentosProyectos().get(pos).setDescripcion(holder.edtDocumento.getText().toString());
                                        proyecto.getDocumentosProyectos().get(pos).setTipoDocumento(holder.spnTipo.getSelectedItem().toString());
                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                }

                                @Override public void onFailure(Call call, Throwable t) {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_cambios), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                });
                //Creamos el evento de click en el link de un docuemnto de la lista de documentos.
                holder.btnBorrar.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View vista) {
                        if (holder.edtDocumento.getText().toString().isEmpty()) {
                            Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_doc_noName), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {
                            Call<Object> call_post_doc = ((MainActivity) getActivity()).APIService.postDocProyDelete((long) doc.getId());
                            call_post_doc.enqueue(new Callback() {
                                @Override public void onResponse(Call call, Response response) {
                                    if (response.code() < 300) {
                                        Toast toast = Toast.makeText(getActivity(), R.string.msg_doc_eliminado, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        ReloadDocumentosProyecto(misProyMainView);
                                    } else {
                                        Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_delete), Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }
                                }
                                @Override public void onFailure(Call call, Throwable t) {
                                    Toast toast = Toast.makeText(getActivity(), getString(R.string.msg_err_delete), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            });
                        }
                    }
                });
            }
            return esteDocumento;
        }
    }

    //Creamos la clase DocsProyectoViewHolder que guarda los datos de un registro de docuemnto de proyecto.
    static class RubricasViewHolder {
        TextView txtRubrica;
        TextView txtPorcentaje;
        TextView txtValorTutIndiv;
        TextView txtComentarioTutIndiv;
        TextView txtValorTutCol;
        TextView txtComentarioTutCol;
        ImageButton btnSeeDetails;
    }

    //Creamos la clase que gestiona el relleno de los documentos del proyecto en el listview del marco_documentos.
    public class AdaptadorRubricas extends ArrayAdapter<Integer> {
        public AdaptadorRubricas(Context ctx, int viewId, Integer[] objects) {
            super(ctx,viewId,objects);
        }
        @Override public View getView(int pos, View view, ViewGroup prntView) {
            View estaRubrica=view;
            MisProyectos.RubricasViewHolder holder;
            if (estaRubrica==null) {
                LayoutInflater inf=getLayoutInflater();
                estaRubrica=inf.inflate(R.layout.lista_rubricas,prntView,false);
                holder=new MisProyectos.RubricasViewHolder();
                holder.txtRubrica=(TextView)estaRubrica.findViewById(R.id.txtRubrica);
                holder.txtPorcentaje=(TextView)estaRubrica.findViewById(R.id.txtPorcentaje);
                holder.txtValorTutIndiv=(TextView)estaRubrica.findViewById(R.id.txtValorTutIndiv);
                holder.txtComentarioTutIndiv=(TextView)estaRubrica.findViewById(R.id.txtComentarioTutIndiv);
                holder.txtValorTutCol=(TextView)estaRubrica.findViewById(R.id.txtValorTutCol);
                holder.txtComentarioTutCol=(TextView)estaRubrica.findViewById(R.id.txtComentarioTutCol);
                holder.btnSeeDetails=(ImageButton)estaRubrica.findViewById(R.id.btnSeeDetails);
                estaRubrica.setTag(holder);
            } else {
                holder=(MisProyectos.RubricasViewHolder)estaRubrica.getTag();
            }

            holder.txtRubrica.setText(rubricasConGrupos.get(pos).getGrupoRubricas().getGrupo().toUpperCase()+": "+rubricasConGrupos.get(pos).getRubrica());
            holder.txtPorcentaje.setText(rubricasConGrupos.get(pos).getPorcentaje().toString()+" %");
            boolean hayValorIndiv=false;
            boolean hayValorCol=false;
            for (int i=0;i<proyecto.getTutorEvaluaProyectos().size();i++) {
                if (proyecto.getTutorEvaluaProyectos().get(i).getRubricaId().equals(rubricasConGrupos.get(pos).getId())) {
                    //La evaluación se corresponde con esta rúbrica. Puede ser del tutorcolectivo o del individual.
                    Integer valor=proyecto.getTutorEvaluaProyectos().get(i).getNota();
                    String mensaje=getString(R.string.pdteEval);
                    if (valor==0) {mensaje=getString(R.string.insuficiente);} else if (valor==1) {mensaje=getString(R.string.regular);}
                    else if (valor==2) {mensaje=getString(R.string.bien);} else if (valor==3) {mensaje=getString(R.string.excelente);}
                    if (proyecto.getTutorEvaluaProyectos().get(i).getEsColectivo()) {
                        holder.txtValorTutCol.setText(mensaje);
                        holder.txtComentarioTutCol.setText(proyecto.getTutorEvaluaProyectos().get(i).getComentario());
                        hayValorCol=true;
                    } else {
                        holder.txtValorTutIndiv.setText(mensaje);
                        holder.txtComentarioTutIndiv.setText(proyecto.getTutorEvaluaProyectos().get(i).getComentario());
                        hayValorIndiv=true;
                    }
                }
            }
            if (!hayValorCol) { holder.txtValorTutCol.setText(getString(R.string.pdteEval));}
            if (!hayValorIndiv) { holder.txtValorTutIndiv.setText(getString(R.string.pdteEval));}
            if (holder.txtComentarioTutIndiv.getText().toString().isEmpty()) { holder.txtComentarioTutIndiv.setText(R.string.pte_comentario);}
            if (holder.txtComentarioTutCol.getText().toString().isEmpty()) { holder.txtComentarioTutCol.setText(R.string.pte_comentario);}

            //Creamos el evento de click en el link de un docuemnto de la lista de documentos.
            holder.btnSeeDetails.setOnClickListener(new View.OnClickListener() {
                @Override   public void onClick( View vista) {
                    //Creamos el AlertDialog para mostrar el detalle de la rúbrica.
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.lista_detalle_rubricas, null);
                    dialogBuilder.setView(dialogView);
                    TextView txtDescExc = (TextView) dialogView.findViewById(R.id.txtDescExc);
                    TextView txtDescBien = (TextView) dialogView.findViewById(R.id.txtDescBien);
                    TextView txtDescReg = (TextView) dialogView.findViewById(R.id.txtDescReg);
                    TextView txtDescMal = (TextView) dialogView.findViewById(R.id.txtDescMal);

                    txtDescExc.setText(rubricasConGrupos.get(pos).getDescExcelente());
                    txtDescBien.setText(rubricasConGrupos.get(pos).getDescBien());
                    txtDescReg.setText(rubricasConGrupos.get(pos).getDescRegular());
                    txtDescMal.setText(rubricasConGrupos.get(pos).getDescInsuficiente());

                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            });
            return estaRubrica;
        }
    }
}
