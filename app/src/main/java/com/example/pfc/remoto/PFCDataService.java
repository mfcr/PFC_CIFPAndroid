package com.example.pfc.remoto;

import com.example.pfc.modelos_datos.Alumnos;
import com.example.pfc.modelos_datos.Ciclos;
import com.example.pfc.modelos_datos.DocenteTutColectivoCiclos;
import com.example.pfc.modelos_datos.DocentesModulosTutoriasColectivas;
import com.example.pfc.modelos_datos.Documento;
import com.example.pfc.modelos_datos.EstadosProyectos;
import com.example.pfc.modelos_datos.Parametros;
import com.example.pfc.modelos_datos.ProyectosFull;
import com.example.pfc.modelos_datos.ProyectosPublicos;
import com.example.pfc.modelos_datos.RubricasConGrupos;
import com.example.pfc.modelos_datos.TiposProyectoCiclosConTipos;
import com.example.pfc.modelos_datos.Users;
import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface PFCDataService {
    //Métodos para datos iniciales
    @GET("parametros/")
    Call<Parametros> getParametros();
    @GET("ciclos/")
    Call<List<Ciclos>> getCiclos();
    @GET("proyectos_publicos/")
    Call<List<ProyectosPublicos>> getProyectosAnteriores();
    @GET("proyectos/alumno/{id}/")
    Call<List<ProyectosPublicos>> getProyectosAlumno(@Path("id") Long id);

    //Métodos para login y cambio de contraseña.
    @FormUrlEncoded
    @POST("foreignlogin/")
    Call<Users> postUsers(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("changePassword/")
    Call<List<String>> postChangePassword(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("alumnos_email/")
    Call<Alumnos> postAlumnoEmail(@Field("email") String email);

    //Métodos para fragment Sugerir Proyectos
    @FormUrlEncoded
    @POST("proyectos_propuestos/")
    Call<Object> postProyectosPropuestos(@Field("nombre") String nombre,@Field("ciclo_id") int ciclo_id,@Field("email") String email,@Field("propuesta") String propuesta);

    //Métodos para fragment Documentos
    @GET("documentos/filter/{user}/{ciclo}")
    Call<List<Documento>> getDocumentos(@Path("user") Long user, @Path("ciclo") Long ciclo);

    //Métodos para Fragment MisProyectos
        //Métodos para Obtención de datos
    @GET("proyectos_full/{id}/")
    Call<List<ProyectosFull>> getProyectosFull(@Path("id") Long id);
    @GET("estados_proyectos/")
    Call<List<EstadosProyectos>> getEstadosProyectos();
    @GET("docentes_modulos_tutoriascolectivas/")
    Call<List<DocentesModulosTutoriasColectivas>> getDocentesModulosTutoriasColectivas();
    @GET("docente_tut_colectivo_ciclos/{curso}/{idCiclo}/")
    Call<DocenteTutColectivoCiclos> getDocenteTutColectivoCiclos(@Path("curso") Long curso, @Path("idCiclo") Long idCiclo);
    @GET("rubricas_con_grupos/{curso}/{idCiclo}/")
    Call<List<RubricasConGrupos>> getRubricasConGrupos(@Path("curso") Long curso, @Path("idCiclo") Long idCiclo);
    @GET("tipos_proyecto_ciclos_con_tipos/{idCiclo}/")
    Call<List<TiposProyectoCiclosConTipos>> getTiposProyectosCiclosConTipos(@Path("idCiclo") Long idCiclo);
        //Métodos para gaurdar cambios.
    @FormUrlEncoded
    @POST("alumnos/{id}")
    Call<Object> postAlumnos(@Path("id") Long id, @Field("nombre") String nombre, @Field("apellido1") String apellido1,
                                         @Field("apellido2") String apellido2,@Field("dni") String dni,@Field("telefono") String telefono);
    @FormUrlEncoded
    @POST("proyectos/{id}")
    Call<Object> postAnteproyectos(@Path("id") Long id, @Field("nombreProyecto") String nombreProyecto,@Field("tipo_proyecto_id") Long tipo_proyecto_id,
                             @Field("descTipo") String descTipo, @Field("textoPropuestaProyecto") String textoPropuestaProyecto,
                             @Field("textoRequisitosFuncionales") String textoRequisitosFuncionales,@Field("textoModulosRelacionados") String textoModulosRelacionados);
    @FormUrlEncoded
    @POST("modulos_matriculados/{id}")
    Call<Object> postMatriculas(@Path("id") Long id, @Field("estado") String estado);
    @FormUrlEncoded
    @POST("documentos_proyectos/")
    Call<Object> postDocProySaveLink(@Field("proyecto_id") Long proyecto_id, @Field("descripcion") String descripcion,@Field("tipoDocumento") String tipoDocumento,
                                   @Field("UriDocumento") String UriDocumento, @Field("isFile") int isFile,@Field("publico") int publico);
    @FormUrlEncoded
    @POST("documentos_proyectos/{id}")
    Call<Object> postDocProyEdit(@Path("id") Long id, @Field("descripcion") String descripcion, @Field("tipoDocumento") String tipoDocumento);
    @DELETE("documentos_proyectos/{idDoc}")
    Call<Object> postDocProyDelete(@Path("idDoc") Long idDoc);

    @Multipart
    @POST("documentos_proyectos/")
    Call<Object> postDocProySaveFile(@Part MultipartBody.Part fichero,
                                     @Part("proyecto_id") RequestBody proyecto_id,
                                     @Part("descripcion") RequestBody descripcion,
                                     @Part("tipoDocumento") RequestBody tipoDocumento,
                                     @Part("isFile") RequestBody isFile,
                                     @Part("publico") RequestBody publico );
    @Multipart
    @POST("upload")
    Call<ResponseBody> upload(
            @Part("description") RequestBody description,
            @Part MultipartBody.Part file
    );

    /*
     @FormUrlEncoded
    @POST("/api/userlogin")
    Call<ResponseBody>  getUserLogin(
            @Field("client_id") String id,
            @Field("client_secret") String secret,
            @Field("username") String uname,
            @Field("password") String password
    );
     */

/*
    @GET("group/{id}/users")

    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);

 */
    //FORMATO DE ENVIO DE CONSULTA GET A the movie database.
    // https://api.themoviedb.org/3/movie/popular?api_key=56f3abab9b3ae19829f42bcd798c1b0e&language=es-ES&page=1
    //@Path modificara el texto category por la categria buscada
    //@Query ira construyendo el método get para enviar a Tmdb.org el formato correcto con las opciones buscadas.á

}
