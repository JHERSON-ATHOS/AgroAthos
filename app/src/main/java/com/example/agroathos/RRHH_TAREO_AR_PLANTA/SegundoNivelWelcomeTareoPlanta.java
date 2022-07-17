package com.example.agroathos.RRHH_TAREO_AR_PLANTA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_GruposPlanta;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelRegistrarGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES.AdaptadorListarGrupoTrabajoPlanta;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SegundoNivelWelcomeTareoPlanta extends AppCompatActivity {

    FloatingActionButton fabRegistrar;
    ListView listView;
    Toolbar toolbar;

    ConexionSQLiteHelper conn;
    SharedPreferences preferences;

    ArrayList<E_GruposPlanta> arrayListGruposPlanta;

    String idNivel1Capa1 = "";
    String nave = "";
    String linea = "";
    String turno = "";
    String dni = "";

    //DATOS DE LA BD
    String idGrupo = "";
    String idSupervisor = "";
    String idNivel1 = "";
    String idNivel2 = "";
    String idNivel3 = "";

    //DATOS PARA SUBIDA
    String idNivel1UP = "";
    String naveUP = "";
    String lineaUP = "";
    String turnoUP = "";
    String dniUP = "";
    String horaUP = "";
    String fechaUP = "";

    //NIVEL 2
    ArrayList<String> arrayListNivelDosIdGrupo = new ArrayList<>();
    ArrayList<String> arrayListNivelDosIdNivel1 = new ArrayList<>();
    ArrayList<String> arrayListNivelDosContador = new ArrayList<>();
    ArrayList<String> arrayListNivelDosAnexoSupervisor = new ArrayList<>();
    ArrayList<String> arrayListNivelDosEstado = new ArrayList<>();

    //NIVEL 3
    ArrayList<String> arrayListNivelTresIdGrupo = new ArrayList<>();
    ArrayList<String> arrayListNivelTresProceso = new ArrayList<>();
    ArrayList<String> arrayListNivelTresActividad = new ArrayList<>();
    ArrayList<String> arrayListNivelTresLabor = new ArrayList<>();
    ArrayList<String> arrayListNivelTresMesa = new ArrayList<>();
    ArrayList<String> arrayListNivelTresSupervisor = new ArrayList<>();
    ArrayList<String> arrayListNivelTresFecha = new ArrayList<>();
    ArrayList<String> arrayListNivelTresHora = new ArrayList<>();
    ArrayList<String> arrayListNivelTresEstado = new ArrayList<>();

    String sincUP1 = "";
    String sincUP2 = "";
    String sincUP3 = "";

    String fechaActual = "";
    String fechaBDAntigua = "";

    //DATOS PARA SUBIR MASIVO
    ArrayList<String> idNivel1ArrayList = new ArrayList<>();
    ArrayList<String> idNivel2ArrayList = new ArrayList<>();
    ArrayList<String> idNivel3ArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_nivel_welcome_tareo_planta);

        toolbar = findViewById(R.id.toolbarSEGUNDO_NIVEL_WELCOME_RRHH_TAREO_AR_PLANTA);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        conn = new ConexionSQLiteHelper(this,"athos0",null, Utilidades.VERSION_APP);

        fabRegistrar = findViewById(R.id.fbAgregarTrabajoRRHH_TAREO_ARANDANO_PLANTA_SEGUNDO_NIVEL);
        listView = findViewById(R.id.lvGruposTrabajoRRHH_TAREO_ARANDANO_PLANTA_SEGUNDO_NIVEL);

        preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
        idNivel1Capa1 = preferences.getString("idNivel1","");
        dni = preferences.getString("dni","");
        nave = preferences.getString("nave","");
        linea = preferences.getString("linea","");
        turno = preferences.getString("turno","");

        obtenerIdGrupo();

        validarDatosNivel1(idNivel1Capa1);

        fabRegistrar.setOnClickListener(view -> irRegistoGrupo());

        limpiezaPeriodica();

        if (!fechaBDAntigua.equals(fechaActual)){
            limpiarBDSQLite();
        }
    }

    public void limpiezaPeriodica(){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL1 + " WHERE "
                + Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL1 + "=" + "'1'", null);
        if (cursor != null){

            if (cursor.moveToFirst()){
                do{
                    fechaActual = obtenerFechaActual("AMERICA/Lima");
                    fechaBDAntigua = cursor.getString(6);
                }while (cursor.moveToNext());
            }

        }
        cursor.close();
    }

    public void obtenerIdGrupo(){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL3 + " WHERE "
                + Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL3 + "=" + "'"+dni+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    idGrupo = cursor.getString(1);
                    idSupervisor = cursor.getString(6);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    public void validarDatosNivel1(String idNive1){
        SQLiteDatabase database = conn.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL2+" WHERE "
                +Utilidades.CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2+"="+ "'"+idNive1+"'", null);

        if (cursor != null){
            if (cursor.moveToNext()){
                consultarGruposTrabajo(cursor.getString(1),cursor.getString(2));
                listView.setAdapter(new AdaptadorListarGrupoTrabajoPlanta(this,arrayListGruposPlanta));
                cursor.close();
            }
        }
    }

    public void consultarGruposTrabajo(String idAnexo, String idAnexo2){
        SQLiteDatabase database = conn.getReadableDatabase();

        E_GruposPlanta e_grupos = null;
        arrayListGruposPlanta = new ArrayList<E_GruposPlanta>();
        Cursor cursor2 = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL2 + " WHERE "
                + Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL2 + "=" + "'"+idSupervisor+"' AND "+
                Utilidades.CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2 + "=" + "'"+idAnexo+"' ", null);

        if (cursor2 != null){
            while (cursor2.moveToNext()) {
                e_grupos = new E_GruposPlanta();
                e_grupos.setId(cursor2.getString(0));
                e_grupos.setAnexoNivel1(cursor2.getString(1));
                e_grupos.setId_grupo(cursor2.getString(2));
                e_grupos.setEstado(cursor2.getString(6));
                arrayListGruposPlanta.add(e_grupos);
            }
            cursor2.close();
        }
    }

    private void irRegistoGrupo(){
        Intent intent = new Intent(SegundoNivelWelcomeTareoPlanta.this, TercerNivelWelcomeTareoPlanta.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_cerrar_sesion_action:
                SharedPreferences preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(SegundoNivelWelcomeTareoPlanta.this, PrimerNivelWelcomeTareoPlanta.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

            case R.id.menu_sincronizar_action:

                validarConexionInternet();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato, String zonaHoraria) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria));
        return sdf.format(date);
    }
    public static String obtenerFechaActual(String zonaHoraria) {
        String formato = "dd-MM-yyyy";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }

    public void limpiarBDSQLite(){
        SQLiteDatabase database = this.conn.getWritableDatabase();
        String t1 = "DELETE FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL1;
        String t2 = "DELETE FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL2;
        String t3 = "DELETE FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL3;
        database.execSQL(t1);
        database.execSQL(t2);
        database.execSQL(t3);
        database.close();

        Intent intent = new Intent(SegundoNivelWelcomeTareoPlanta.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().apply();
    }

    public void validarConexionInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){

            arrayListNivelDosIdGrupo.clear();
            arrayListNivelTresIdGrupo.clear();

            obtenerDataRegistrada();
            obtenerDataRegistradaNivelDos();
            obtenerDataRegistradaNivelTres();

            Toast.makeText(this, sincUP1.concat("-").concat(sincUP2).concat("-").concat(sincUP3), Toast.LENGTH_SHORT).show();

            if (sincUP1.equals("1") && sincUP2.equals("2") && sincUP3.equals("3")){
                registrarDatos();
                actualizarEstadoSincronizacionNivelUno();

                registrarDatosNivelDos();
                actualizarEstadoSincronizacionNivelDos();

                registrarDatosNivelTres();
                actualizarEstadoSincronizacionNivelTres();

                Toast.makeText(this, "Data Sincronizada!", Toast.LENGTH_SHORT).show();
            }else{
                if (sincUP1.equals("1")){
                    registrarDatos();
                    actualizarEstadoSincronizacionNivelUno();
                }else{
                    if (sincUP2.equals("2")){
                        registrarDatosNivelDos();
                        actualizarEstadoSincronizacionNivelDos();
                    }else{
                        if (sincUP3.equals("3")){
                            registrarDatosNivelTres();
                            actualizarEstadoSincronizacionNivelTres();
                            Toast.makeText(this, "Data Sincronizada!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "ERROR: La data ya se migró", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        }else{
            Toast.makeText(this, "Necesitas conexión a internet.", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerDataRegistrada(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL1+" WHERE "
                +Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL1+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL1+
                "="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel1UP = cursorData.getString(1);
                    naveUP = cursorData.getString(2);
                    lineaUP = cursorData.getString(3);
                    turnoUP = cursorData.getString(4);
                    dniUP = cursorData.getString(5);
                    fechaUP = cursorData.getString(6);
                    horaUP = cursorData.getString(7);
                    sincUP1 = "1";

                }while (cursorData.moveToNext());
            }
        }
        cursorData.close();
    }
    private void registrarDatos(){
        JSONObject object = new JSONObject();
        try {
            object.put("id_nivel_uno",idNivel1UP);
            object.put("nave",naveUP);
            object.put("linea",lineaUP);
            object.put("turno",turnoUP);
            object.put("dni",dniUP);
            object.put("fecha",fechaUP);
            object.put("hora",horaUP);
            object.put("sinc","1");
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/tareo_planta_nivel_uno",
                object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcomeTareoPlanta.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void obtenerDataRegistradaNivelDos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL2+" WHERE "
                +Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL2+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2+
                "="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    arrayListNivelDosIdNivel1.add(cursorData.getString(1));
                    arrayListNivelDosIdGrupo.add(cursorData.getString(2));
                    arrayListNivelDosContador.add(cursorData.getString(3));
                    arrayListNivelDosAnexoSupervisor.add(cursorData.getString(4));
                    arrayListNivelDosEstado.add(cursorData.getString(5));
                    sincUP2 = "2";
                }while (cursorData.moveToNext());
            }
        }
        cursorData.close();
    }
    private void registrarDatosNivelDos(){
        for (int i=0; i<arrayListNivelDosIdNivel1.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("anexo_nivel1",arrayListNivelDosIdNivel1.get(i));
                object.put("id_grupo",arrayListNivelDosIdGrupo.get(i));
                object.put("contador",arrayListNivelDosContador.get(i));
                object.put("dni",arrayListNivelDosAnexoSupervisor.get(i));
                object.put("estado",arrayListNivelDosEstado.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/tareo_planta_nivel_dos",
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcomeTareoPlanta.this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void obtenerDataRegistradaNivelTres(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL3+" WHERE "
                +Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL3+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL3
                +"="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    arrayListNivelTresIdGrupo.add(cursorData.getString(1));
                    arrayListNivelTresProceso.add(cursorData.getString(2));
                    arrayListNivelTresActividad.add(cursorData.getString(3));
                    arrayListNivelTresLabor.add(cursorData.getString(4));
                    arrayListNivelTresMesa.add(cursorData.getString(5));
                    arrayListNivelTresSupervisor.add(cursorData.getString(6));
                    arrayListNivelTresFecha.add(cursorData.getString(7));
                    arrayListNivelTresHora.add(cursorData.getString(8));
                    arrayListNivelTresEstado.add(cursorData.getString(9));

                    sincUP3 = "3";
                }while (cursorData.moveToNext());
            }
        }
        cursorData.close();
    }
    private void registrarDatosNivelTres(){
        for (int i=0; i<arrayListNivelTresIdGrupo.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("anexo_grupo",arrayListNivelTresIdGrupo.get(i));
                object.put("proceso",arrayListNivelTresProceso.get(i));
                object.put("actividad",arrayListNivelTresActividad.get(i));
                object.put("labor",arrayListNivelTresLabor.get(i));
                object.put("mesa",arrayListNivelTresMesa.get(i));
                object.put("dni",arrayListNivelTresSupervisor.get(i));
                object.put("fecha",arrayListNivelTresFecha.get(i));
                object.put("hora",arrayListNivelTresHora.get(i));
                object.put("estado",arrayListNivelTresEstado.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/tareo_planta_nivel_tres",
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    //Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcomeTareoPlanta.this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void actualizarEstadoSincronizacionNivelUno(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL1+" WHERE "
                +Utilidades.CAMPO_IDNIVEL1_TAREO_PLANTA_NIVEL1+"="+"'"+idNivel1Capa1+"' AND "+Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL1
                +"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel1ArrayList.add(cursorData.getString(0));
                    sincUP1 = "";
                }while (cursorData.moveToNext());

                for (int i=0; i<idNivel1ArrayList.size() ;i++){
                    String [] parametro = {idNivel1ArrayList.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_TAREO_PLANTA_NIVEL1, contentValuesActSincronizacion,
                            Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL1+"=?", parametro);
                }

                cursorData.close();
            }
        }
    }
    private void actualizarEstadoSincronizacionNivelDos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL2+" WHERE "
                +Utilidades.CAMPO_ANEXONIVEL1_TAREO_PLANTA_NIVEL2+"="+"'"+idNivel1Capa1+"' AND "+Utilidades.CAMPO_SINCRONIZADO_TAREO_PLANTA_NIVEL2
                +"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel2ArrayList.add(cursorData.getString(0));
                    sincUP2 = "";
                }while (cursorData.moveToNext());

                for (int i=0; i<idNivel2ArrayList.size() ;i++) {
                    String[] parametro = {idNivel2ArrayList.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado", "1");
                    database.update(Utilidades.TABLA_TAREO_PLANTA_NIVEL2, contentValuesActSincronizacion,
                            Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL2 + "=?", parametro);
                }
                cursorData.close();
            }
        }
    }
    private void actualizarEstadoSincronizacionNivelTres(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_TAREO_PLANTA_NIVEL3+" WHERE "
                +Utilidades.CAMPO_DNI_TAREO_PLANTA_NIVEL3+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL2
                +"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel3ArrayList.add(cursorData.getString(0));
                    sincUP3 = "";
                }while (cursorData.moveToNext());

                for (int i=0; i<idNivel3ArrayList.size() ;i++) {
                    String[] parametro = {idNivel3ArrayList.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado", "1");
                    database.update(Utilidades.TABLA_TAREO_PLANTA_NIVEL3, contentValuesActSincronizacion,
                            Utilidades.CAMPO_ID_TAREO_PLANTA_NIVEL3 + "=?", parametro);
                }
                cursorData.close();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SegundoNivelWelcomeTareoPlanta.this, MainActivity.class);
        startActivity(intent);
    }
}