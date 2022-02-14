package com.example.pfc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;
import com.example.pfc.app.Constants;
import com.example.pfc.fragmentos.Documentos;
import com.example.pfc.fragmentos.Inicio;
import com.example.pfc.fragmentos.Login;
import com.example.pfc.fragmentos.Login2;
import com.example.pfc.fragmentos.MisProyectos;
import com.example.pfc.fragmentos.Sugerir;
import com.example.pfc.modelos_datos.Alumnos;
import com.example.pfc.modelos_datos.Ciclos;
import com.example.pfc.modelos_datos.Parametros;
import com.example.pfc.modelos_datos.ProyectosPublicos;
import com.example.pfc.modelos_datos.User;
import com.example.pfc.modelos_datos.Users;
import com.example.pfc.remoto.PFCDataService;
import com.example.pfc.remoto.PfcApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Menu appMenu;
    public static final String DATOS = "logData";
    public boolean estado;
    public int user;
    public String email;
    public long hora;
    public boolean recordar;
    public boolean cambiada;
    public int cursoActual;
    public int minutosTimeout;
    public SharedPreferences valores;
    public PFCDataService APIService;
    public PFCDataService APIServiceLogin;
    public Parametros params;
    public List<Ciclos> ciclosList;
    public List<ProyectosPublicos> proyAnt;
    public List<ProyectosPublicos> misProy;
    public boolean server=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Asignamos la ActionBar
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        //Comprobamos estado de login y si timeout.
        valores = getSharedPreferences(DATOS, 0);
        estado = valores.getBoolean("estado", false);
        user = valores.getInt("alumnoId", -1);
        email = valores.getString("email", "");
        cambiada = valores.getBoolean("cambiada", false);
        minutosTimeout = valores.getInt("timeout", 10);
        long ultimaHora = valores.getLong("fecha", 0);
        long ahora = System.currentTimeMillis();
        if ((ahora - ultimaHora) > minutosTimeout * 60000 || ahora < ultimaHora || (estado && !cambiada)) {
            hora = 0;            user = -1;            email = "";
            estado = false;      cambiada = false;     minutosTimeout = 10;
        } else {
            hora = ahora;
        }

        //Obtenemos el curso actual
        APIService = PfcApiClient.getClient(Constants.BASE_URL_API).create(PFCDataService.class);
        Call<Parametros> call_params = APIService.getParametros();
        call_params.enqueue(new Callback() {
            @Override public void onResponse(Call call, Response response) {
                if (response.body() == null) {
                    server=false;
                    //Recarga menús.
                    invalidateOptionsMenu();
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fatal_err), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    cursoActual = ((Parametros) response.body()).getCursoActual();
                }
            }
            @Override public void onFailure(Call call, Throwable t) {
                server=false;
                //Recarga menús.
                invalidateOptionsMenu();
                Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
        guardaDatos(estado, user, hora, cambiada, cursoActual, email, minutosTimeout);
        //Cargamos pantalla principal
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.marco, new Inicio());
        ft.commit();

        //Comprobamos los permisos
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 22);
        }
    }

    @Override public void onBackPressed() {
        if (!getSupportFragmentManager().findFragmentById(R.id.marco).getClass().getSimpleName().equals("Inicio")) {
            setResult(RESULT_CANCELED);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco, new Inicio());
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override  public void onResume() {
        //Comprobamos si timeout -->pantalla principal.
        if (estado && ((System.currentTimeMillis() - hora) > minutosTimeout * 60000)) {
            logout();
        }
        if (estado && !cambiada) { //Carga fragment Cambio contraseña.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco, new Login2(email, recordar));
            ft.commit();
        }
        super.onResume();
    }

    @Override public void onPause() {
        if (estado) {
            hora = System.currentTimeMillis();
        }
        guardaDatos(estado, user, hora, cambiada, cursoActual, email, minutosTimeout);
        super.onPause();
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        appMenu = menu;
        menu.clear();
        //Añadimos logo a la barra.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        //Creamos los menús
        //MENU INICIO
        menu.add(0, 1, Menu.NONE, getText(R.string.inicio));
        if (server) {
            //MENU SUGERIR PROYECTOS
            menu.add(0, 5, Menu.NONE, getText(R.string.sugerir_proyecto));
            //MENU+SUBMENU DOCUMENTOS --> Llamadas para la carga del resto de los menus.
            if (ciclosList == null) {
                Call<List<Ciclos>> call_ciclos = APIService.getCiclos();
                call_ciclos.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.body() != null) {
                            ciclosList = (List<Ciclos>) response.body();
                            SubMenu sub = menu.addSubMenu(0, 3, Menu.NONE, getText(R.string.documentos));
                            if (ciclosList.size() > 0) {
                                sub.add(0, 3000, Menu.NONE, R.string.generales);
                                for (int i = 0; i < ciclosList.size(); i++) {
                                    sub.add(0, 3000 + i + 1, Menu.NONE, ciclosList.get(i).getCodigoCiclo());
                                }
                            } else {
                                sub.add(0, 2999, Menu.NONE, R.string.sin_documentos);
                            }
                            FillMenuItemsProyAnt(menu); //Llamada para la carga de los menus de proyectos anteriores
                            FillMenuItemsMisProy(menu); //Llamada para la carga de los menus de misproyectos
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.fatal_err), Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            } else {
                SubMenu sub = menu.addSubMenu(0, 3, Menu.NONE, getText(R.string.documentos));
                if (ciclosList.size() > 0) {
                    sub.add(0, 3000, Menu.NONE, R.string.generales);
                    for (int i = 0; i < ciclosList.size(); i++) {
                        sub.add(0, 3000 + i + 1, Menu.NONE, ciclosList.get(i).getCodigoCiclo());
                    }
                } else {
                    sub.add(0, 2999, Menu.NONE, R.string.sin_documentos);
                }
                FillMenuItemsProyAnt(menu);
                FillMenuItemsMisProy(menu);
            }
            //Inflamos el menú
            getMenuInflater().inflate(R.menu.bar_menu, menu); //menu_calc_toolbar.xml
            //Ocultamos login o logout en función del estado.
            if (estado && cambiada) {
                MenuItem item = (MenuItem) menu.findItem(R.id.login);
                item.setVisible(false);
            } else {
                MenuItem item = (MenuItem) menu.findItem(R.id.logout);
                item.setVisible(false);
            }
        } else { //no hay conexion de datos.
            //Inflamos el menú
            getMenuInflater().inflate(R.menu.bar_menu, menu); //menu_calc_toolbar.xml
            MenuItem item = (MenuItem) menu.findItem(R.id.login);
            if (item!=null) {item.setVisible(false);}
            item = (MenuItem) menu.findItem(R.id.logout);
            if (item!=null) {item.setVisible(false);}
        }
        return true;
    }

    public void FillMenuItemsProyAnt(Menu menu) {
        //SUBMENU PROYECTOS ANTERIORES
        if (proyAnt == null) {
            Call<List<ProyectosPublicos>> call_proyAnt = APIService.getProyectosAnteriores();
            call_proyAnt.enqueue(new Callback() {
                @Override public void onResponse(Call call, Response response) {
                    SubMenu sub = menu.addSubMenu(0, 4, Menu.NONE, getText(R.string.proyectos_anteriores));
                    if (response.body() != null) {
                        proyAnt = (List<ProyectosPublicos>) response.body();
                        if (proyAnt.size() > 0) {
                            for (int i = 0; i < proyAnt.size(); i++) {
                                int cic_id = proyAnt.get(i).getCicloId();
                                String cic_code = "";
                                String proy_name = proyAnt.get(i).getNombreProyecto();
                                if (proy_name == null) {
                                    proy_name = getString(R.string.msg_nombre_pdte);
                                }
                                for (int j = 0; j < ciclosList.size(); j++) {
                                    if (ciclosList.get(j).getId().equals(cic_id)) {
                                        cic_code = ciclosList.get(j).getCodigoCiclo();
                                        break;
                                    }
                                }
                                sub.add(0, 4000 + proyAnt.get(i).getId(), Menu.NONE, cic_code + "-" + proyAnt.get(i).getCurso() + "-" + proy_name);
                            }
                        } else {
                            sub.add(0, 3999, Menu.NONE, R.string.sin_proyectos);
                        }
                    } else { //No hay proyectos
                        sub.add(0, 3999, Menu.NONE, R.string.sin_proyectos);
                    }
                }
                @Override  public void onFailure(Call call, Throwable t) {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } else {
            SubMenu sub = menu.addSubMenu(0, 4, Menu.NONE, getText(R.string.proyectos_anteriores));
            if (proyAnt != null) {
                if (proyAnt.size() > 0) {
                    for (int i = 0; i < proyAnt.size(); i++) {
                        int cic_id = proyAnt.get(i).getCicloId();
                        String cic_code = "";
                        String proy_name = proyAnt.get(i).getNombreProyecto();
                        if (proy_name == null) {
                            proy_name = getString(R.string.msg_nombre_pdte);
                        }
                        for (int j = 0; j < ciclosList.size(); j++) {
                            if (ciclosList.get(j).getId().equals(cic_id)) {
                                cic_code = ciclosList.get(j).getCodigoCiclo();
                                break;
                            }
                        }
                        sub.add(0, 4000 + proyAnt.get(i).getId(), Menu.NONE, cic_code + "-" + proyAnt.get(i).getCurso() + "-" + proy_name);
                    }
                } else {
                    sub.add(0, 3999, Menu.NONE, R.string.sin_proyectos);
                }
            } else { //No hay proyectos
                sub.add(0, 3999, Menu.NONE, R.string.sin_proyectos);
            }
        }
    }
    public void FillMenuItemsMisProy(Menu menu) {
    //SUBMENU MISPROYECTOS
        if(estado)    {
            if (misProy == null) {
                Call<List<ProyectosPublicos>> call_proyAlumno = APIService.getProyectosAlumno((long) user);
                call_proyAlumno.enqueue(new Callback() {
                    @Override public void onResponse(Call call, Response response) {
                        SubMenu sub = menu.addSubMenu(0, 2, Menu.NONE, getText(R.string.mis_proyectos));
                        if (response.body() != null) {
                            misProy = (List<ProyectosPublicos>) response.body();
                            if (misProy.size() > 0) {
                                for (int i = 0; i < misProy.size(); i++) {
                                    int cic_id = misProy.get(i).getCicloId();
                                    String cic_code = "";
                                    String proy_name = misProy.get(i).getNombreProyecto();
                                    if (proy_name == null) {
                                        proy_name = getString(R.string.msg_nombre_pdte);
                                    }
                                    for (int j = 0; j < ciclosList.size(); j++) {
                                        if (ciclosList.get(j).getId().equals(cic_id)) {
                                            cic_code = ciclosList.get(j).getCodigoCiclo();
                                            break;
                                        }
                                    }
                                    sub.add(0, 2000 + misProy.get(i).getId(), Menu.NONE, cic_code + "-" + misProy.get(i).getCurso() + "-" + proy_name);
                                }
                            } else {
                                sub.add(0, 1999, Menu.NONE, R.string.sin_proyectos);
                            }
                        } else { //No hay proyectos
                            sub.add(0, 1999, Menu.NONE, R.string.sin_proyectos);
                        }
                    }
                    @Override public void onFailure(Call call, Throwable t) {
                        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.err_conexion) + t.getMessage(), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
            } else {
                SubMenu sub = menu.addSubMenu(0, 2, Menu.NONE, getText(R.string.mis_proyectos));
                if (misProy != null) {
                    if (misProy.size() > 0) {
                        for (int i = 0; i < misProy.size(); i++) {
                            int cic_id = misProy.get(i).getCicloId();
                            String cic_code = "";
                            String proy_name = misProy.get(i).getNombreProyecto();
                            if (proy_name == null) {
                                proy_name = getString(R.string.msg_nombre_pdte);
                            }
                            for (int j = 0; j < ciclosList.size(); j++) {
                                if (ciclosList.get(j).getId().equals(cic_id)) {
                                    cic_code = ciclosList.get(j).getCodigoCiclo();
                                    break;
                                }
                            }
                            sub.add(0, 2000 + misProy.get(i).getId(), Menu.NONE, cic_code + "-" + misProy.get(i).getCurso() + "-" + proy_name);
                        }
                    } else {
                        sub.add(0, 1999, Menu.NONE, R.string.sin_proyectos);
                    }
                } else { //No hay proyectos
                    sub.add(0, 1999, Menu.NONE, R.string.sin_proyectos);
                }
            }
        }
    }

    public void logout() {
        estado=false;       user=-1;    email="";    hora=0;     cambiada=false;  minutosTimeout=10;
        guardaDatos(estado,user,hora,cambiada,cursoActual,email,minutosTimeout);
        //Recarga menús.
        invalidateOptionsMenu();
        //Carga fragment Inicio.
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.marco, new Inicio());
        ft.commit();
    }

    public void login(String emailIntroducido,String pass,boolean recordar) {
        this.recordar=recordar;
        //Llamada tipo POST al servidor de la aplicación Web GestionPFC para autenticación del usuario.
        APIServiceLogin= PfcApiClient.getClient(Constants.BASE_URL_LOGIN).create(PFCDataService.class);
        Call<Users> call_params=  APIServiceLogin.postUsers(emailIntroducido,pass);
        call_params.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                if (response.body()==null) {
                    Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_user_pass_err), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else{
                    User currentUser=(User)((Users)response.body()).getUser();
                    Integer tipoUser=currentUser.getCategory();
                    if (tipoUser!=2) {
                        Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_acceso_no_alumno), Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else { //Usuario autenticado y además de tipo alumno.
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        email=emailIntroducido;
                        cambiada=currentUser.getPasswordModificada()==1;
                        //Obtenemos el id del usuario.
                        APIService= PfcApiClient.getClient(Constants.BASE_URL_API).create(PFCDataService.class);
                        Call<Alumnos> call_params=  APIService.postAlumnoEmail(email);
                        call_params.enqueue(new Callback() {
                            @Override  public void onResponse(Call call, Response response) {
                                if (response.body()==null) {
                                    Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_user_not_found), Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER,0,0);
                                    toast.show();
                                } else{
                                    user= ((Alumnos)response.body()).getId();
                                    if (recordar) { minutosTimeout=1000000; } else { minutosTimeout=10;}
                                    if (cambiada) {
                                        //Guardamos los datos de acceso
                                        if (user>-1) {
                                            estado=true;
                                            hora=System.currentTimeMillis();
                                            guardaDatos(estado,user,hora,cambiada,cursoActual,email,minutosTimeout);
                                            invalidateOptionsMenu(); //Recarga menús.
                                            Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_connected), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                            ft.replace(R.id.marco, new Inicio()); //Cargamos fragment de inicio
                                        } else {
                                            Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_acceso_user), Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER,0,0);
                                            toast.show();
                                        }
                                    } else {
                                        guardaDatos(estado,user,hora,cambiada,cursoActual,email,minutosTimeout);
                                        ft.replace(R.id.marco, new Login2(email,recordar));
                                    }
                                    ft.commit();
                                }
                            }
                            @Override  public void onFailure(Call call, Throwable t) {
                                Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_acceso_err), Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER,0,0);
                                toast.show();
                            }
                        });
                    }
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_acceso_err), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
        guardaDatos(estado,user,hora,cambiada,cursoActual,email,minutosTimeout);
    }

    public void cambiaContra(String contra, boolean recordar) {
        //Llamada tipo POST al servidor de la aplicación Web GestionPFC para autenticación del usuario.
        Call<List<String>> call_params=  APIServiceLogin.postChangePassword(email,contra);
        call_params.enqueue(new Callback() {
            @Override  public void onResponse(Call call, Response response) {
                if (response.code()==200) {
                    cambiada = true;
                    estado=true;
                    hora=System.currentTimeMillis();
                    guardaDatos(estado, user, hora, cambiada, cursoActual, email, minutosTimeout);
                    invalidateOptionsMenu(); //Recarga menús.
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction(); //Carga del fragment de inicio
                    ft.replace(R.id.marco, new Inicio()); //Cargamos fragment de inicio
                    ft.commit();
                    Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_ok_modif_contra), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                } else {
                    Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_err_modif_contra), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    logout();
                }
            }
            @Override  public void onFailure(Call call, Throwable t) {
                logout();
                Toast toast=Toast.makeText(getApplicationContext(), getString(R.string.msg_err_modif_contra), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
            }
        });
    }

    @Override  public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.logout) {
            logout();
        } else if (id==R.id.exit) {
            this.finish();
            System.exit(0);
        } else if (id==R.id.login) { //carga del fragment de Login
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco, new Login());
            ft.commit();
        } else if (id==1 || id==R.id.home) { //carga del fragment de Inicio
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco, new Inicio());
            ft.commit();
        } else if (id==5) { //Carga del fragment de sugerir proyectos.
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco, new Sugerir());
            ft.commit();
        } else if (id==3999) {
            Toast toast=Toast.makeText(this, R.string.sin_proyectos_anteriores, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else if (id==2999) {
            Toast toast=Toast.makeText(this, R.string.sin_ciclos, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        } else if (id>3999) { //Carga del fragment de proyectos anteriores pasandole el id.
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco,  MisProyectos.newInstance(id-4000,user,false));
            ft.commit();
        } else if (id>2999) { //Carga del fragment de documentos pasandole el id del ciclo.
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco,  Documentos.newInstance(id-3000,estado));
            ft.commit();
        } else if (id>1999) { //Carga del fragment de mis proyectos pasandole el id del proyecto y el id del alumno
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.marco,  MisProyectos.newInstance(id-2000,user,true));
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void guardaDatos(boolean estado, int user, long hora, boolean cambiada,int cursoActual,String email,int minutosTimeout) {
        SharedPreferences.Editor editor=valores.edit();
        editor.putBoolean("estado",estado);
        editor.putInt("alumnoId",user);
        editor.putLong("fecha",hora);
        editor.putBoolean("cambiada",cambiada);
        editor.putInt("cursoActual",cursoActual);
        editor.putString("email",email);
        editor.putInt("timeout",minutosTimeout);
        editor.commit();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 22:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.no_acceso_fichero), Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                break;
            default:
                break;
        }
    }

}