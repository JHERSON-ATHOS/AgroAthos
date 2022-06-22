package com.example.agroathos.RRHH_TAREO_AR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorListarGrupoTrabajo;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SegundoNivelWelcome extends AppCompatActivity {

    FloatingActionButton fabRegistrar;
    ListView listView;
    Toolbar toolbar;

    ArrayList<E_Grupos> arrayGruposList;

    ConexionSQLiteHelper conn;

    String zona = "";
    String fundo = "";
    String cultivo = "";
    String dni = "";

    //DATOS DE LA BD
    String idGrupo = "";
    String idSupervisor = "";
    String idNivel1 = "";
    String idNivel2 = "";
    String idNivel3 = "";

    //DATOS PARA SUBIDA
    String zonaUP = "";
    String fundoUP = "";
    String cultivoUP = "";
    String dni_supervisorUP = "";
    String horaUP = "";
    String fechaUP = "";
    String sincUP = "";

    //NIVEL 2
    ArrayList<String> arrayListNivelDosIdGrupo = new ArrayList<>();
    ArrayList<String> arrayListNivelDosContador = new ArrayList<>();
    ArrayList<String> arrayListNivelDosAnexoSupervisor = new ArrayList<>();
    ArrayList<String> arrayListNivelDosEstado = new ArrayList<>();

    //NIVEL 3
    ArrayList<String> arrayListNivelTresIdGrupo = new ArrayList<>();
    ArrayList<String> arrayListNivelTresFundo = new ArrayList<>();
    ArrayList<String> arrayListNivelTresModulo = new ArrayList<>();
    ArrayList<String> arrayListNivelTresLote = new ArrayList<>();
    ArrayList<String> arrayListNivelTresLabor = new ArrayList<>();
    ArrayList<String> arrayListNivelTresPersonal = new ArrayList<>();
    ArrayList<String> arrayListNivelTresSupervisor = new ArrayList<>();
    ArrayList<String> arrayListNivelTresJarraUno = new ArrayList<>();
    ArrayList<String> arrayListNivelTresJarraDos = new ArrayList<>();
    ArrayList<String> arrayListNivelTresFecha = new ArrayList<>();
    ArrayList<String> arrayListNivelTresHoraInicio = new ArrayList<>();
    ArrayList<String> arrayListNivelTresHoraFinal = new ArrayList<>();
    ArrayList<String> arrayListNivelTresEstado = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_nivel_welcome);

        toolbar = findViewById(R.id.toolbarSEGUNDO_NIVEL_WELCOME_RRHH_TAREO_AR);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        fabRegistrar = findViewById(R.id.fbAgregarTrabajoRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL);
        listView = findViewById(R.id.lvGruposTrabajoRRHH_TAREO_ARANDANO_SEGUNDO_NIVEL);

        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        dni = preferences.getString("dni","");
        zona = preferences.getString("zona","");
        fundo = preferences.getString("fundo","");
        cultivo = preferences.getString("cultivo","");

        obtenerIdGrupo();
        consultarGruposTrabajo();

        listView.setAdapter(new AdaptadorListarGrupoTrabajo(this,arrayGruposList));

        fabRegistrar.setOnClickListener(view -> irRegistoGrupo());
    }

    public void obtenerIdGrupo(){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_DNI_NIVEL2 + "=" + "'"+dni+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    idGrupo = cursor.getString(1);
                    idSupervisor = cursor.getString(7);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    public void consultarGruposTrabajo(){
        SQLiteDatabase database = conn.getReadableDatabase();

        E_Grupos e_grupos = null;
        arrayGruposList = new ArrayList<E_Grupos>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL1_5 + " WHERE " + Utilidades.CAMPO_DNI_NIVEL1_5 + "=" + "'"+idSupervisor+"'", null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                e_grupos = new E_Grupos();
                e_grupos.setId(cursor.getString(0));
                e_grupos.setId_grupo(cursor.getString(1));
                e_grupos.setContadorGrupo(cursor.getString(2));
                e_grupos.setAnexo_supervisor(cursor.getString(3));
                arrayGruposList.add(e_grupos);
            }
            cursor.close();
        }

    }

    private void irRegistoGrupo(){
        Intent intent = new Intent(SegundoNivelWelcome.this, SegundoNivelRegistrarGrupoTrabajo.class);
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
                SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent intent = new Intent(SegundoNivelWelcome.this, PrimerNivelWelcomeTareo.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;

            case R.id.menu_sincronizar_action:

                arrayListNivelDosIdGrupo.clear();
                arrayListNivelTresIdGrupo.clear();

                obtenerDataRegistrada();
                obtenerDataRegistradaNivelDos();
                obtenerDataRegistradaNivelTres();

                if (!zonaUP.isEmpty() || sincUP.equals("1")){
                    if (sincUP.equals("1")){
                        if (!arrayListNivelDosIdGrupo.isEmpty()){
                            if (!arrayListNivelTresIdGrupo.isEmpty()){
                                registrarDatosNivelDos();
                                actualizarEstadoSincronizacionNivelDos();

                                registrarDatosNivelTres();
                                actualizarEstadoSincronizacionNivelTres();

                                Toast.makeText(this, "Data Sincronizada!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (!arrayListNivelDosIdGrupo.isEmpty()){
                            if (!arrayListNivelTresIdGrupo.isEmpty()){
                                registrarDatos();
                                actualizarEstadoSincronizacionNivelUno();

                                registrarDatosNivelDos();
                                actualizarEstadoSincronizacionNivelDos();

                                registrarDatosNivelTres();
                                actualizarEstadoSincronizacionNivelTres();

                                sincUP = "1";
                                Toast.makeText(this, "Data Sincronizada!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerDataRegistrada(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1+" WHERE "+Utilidades.CAMPO_DNI_NIVEL1+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL1+"="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    zonaUP = cursorData.getString(1);
                    fundoUP = cursorData.getString(2);
                    cultivoUP = cursorData.getString(3);
                    dni_supervisorUP = cursorData.getString(4);
                    fechaUP = cursorData.getString(5);
                    horaUP = cursorData.getString(6);
                }while (cursorData.moveToNext());
            }
        }
        cursorData.close();
    }
    private void registrarDatos(){
        JSONObject object = new JSONObject();
        try {
            object.put("zona",zonaUP);
            object.put("fundo",fundoUP);
            object.put("cultivo",cultivoUP);
            object.put("dni_supervisor",dni_supervisorUP);
            object.put("fecha",fechaUP);
            object.put("hora",horaUP);
            object.put("sinc","1");
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/nivel_uno", object, new Response.Listener<JSONObject>() {
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

        RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcome.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void obtenerDataRegistradaNivelDos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1_5+" WHERE "+Utilidades.CAMPO_DNI_NIVEL1_5+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL1_5+"="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    arrayListNivelDosIdGrupo.add(cursorData.getString(1));
                    arrayListNivelDosContador.add(cursorData.getString(2));
                    arrayListNivelDosAnexoSupervisor.add(cursorData.getString(3));
                    arrayListNivelDosEstado.add(cursorData.getString(4));
                }while (cursorData.moveToNext());
            }
        }
        cursorData.close();
    }
    private void registrarDatosNivelDos(){
        for (int i=0; i<arrayListNivelDosIdGrupo.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("id_grupo",arrayListNivelDosIdGrupo.get(i));
                object.put("contador",arrayListNivelDosContador.get(i));
                object.put("anexo_supervisor",arrayListNivelDosAnexoSupervisor.get(i));
                object.put("estado",arrayListNivelDosEstado.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/nivel_dos", object, new Response.Listener<JSONObject>() {
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

            RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcome.this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void obtenerDataRegistradaNivelTres(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL2+" WHERE "+Utilidades.CAMPO_DNI_NIVEL2+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL2+"="+"'0'", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    arrayListNivelTresIdGrupo.add(cursorData.getString(1));
                    arrayListNivelTresFundo.add(cursorData.getString(2));
                    arrayListNivelTresModulo.add(cursorData.getString(3));
                    arrayListNivelTresLote.add(cursorData.getString(4));
                    arrayListNivelTresLabor.add(cursorData.getString(5));
                    arrayListNivelTresPersonal.add(cursorData.getString(6));
                    arrayListNivelTresSupervisor.add(cursorData.getString(7));
                    arrayListNivelTresJarraUno.add(cursorData.getString(8));
                    arrayListNivelTresJarraDos.add(cursorData.getString(9));
                    arrayListNivelTresFecha.add(cursorData.getString(10));
                    arrayListNivelTresHoraInicio.add(cursorData.getString(11));
                    arrayListNivelTresHoraFinal.add(cursorData.getString(12));
                    arrayListNivelTresEstado.add(cursorData.getString(13));
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
                object.put("fundo",arrayListNivelTresFundo.get(i));
                object.put("modulo",arrayListNivelTresModulo.get(i));
                object.put("lote",arrayListNivelTresLote.get(i));
                object.put("labor",arrayListNivelTresLabor.get(i));
                object.put("personal",arrayListNivelTresPersonal.get(i));
                object.put("anexo_supervisor",arrayListNivelTresSupervisor.get(i));
                object.put("jarra_uno",arrayListNivelTresJarraUno.get(i));
                object.put("jarra_dos",arrayListNivelTresJarraDos.get(i));
                object.put("fecha",arrayListNivelTresFecha.get(i));
                object.put("hora_inicio",arrayListNivelTresHoraInicio.get(i));
                object.put("hora_final",arrayListNivelTresHoraFinal.get(i));
                object.put("estado",arrayListNivelTresEstado.get(i));
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/nivel_tres", object, new Response.Listener<JSONObject>() {
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

            RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcome.this);
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void actualizarEstadoSincronizacionNivelUno(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1+" WHERE "+Utilidades.CAMPO_DNI_NIVEL1+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL1+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel1 = cursorData.getString(0);
                }while (cursorData.moveToNext());

                String [] parametro = {idNivel1};

                ContentValues contentValuesActSincronizacion = new ContentValues();
                contentValuesActSincronizacion.put("sincronizado","1");
                database.update(Utilidades.TABLA_NIVEL1, contentValuesActSincronizacion, Utilidades.CAMPO_ID_NIVEL1+"=?", parametro);
                cursorData.close();
            }
        }

    }
    private void actualizarEstadoSincronizacionNivelDos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1_5+" WHERE "+Utilidades.CAMPO_DNI_NIVEL1_5+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL1_5+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel2 = cursorData.getString(1);
                }while (cursorData.moveToNext());

                String [] parametro = {idNivel2};

                ContentValues contentValuesActSincronizacion = new ContentValues();
                contentValuesActSincronizacion.put("sincronizado","1");
                database.update(Utilidades.TABLA_NIVEL1_5, contentValuesActSincronizacion, Utilidades.CAMPO_ID_GRUPO_NIVEL1_5+"=?", parametro);
                cursorData.close();
            }
        }

    }
    private void actualizarEstadoSincronizacionNivelTres(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL2+" WHERE "+Utilidades.CAMPO_DNI_NIVEL2+"="+"'"+dni+"' AND "+Utilidades.CAMPO_SINCRONIZADO_NIVEL2+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idNivel3 = cursorData.getString(1);
                }while (cursorData.moveToNext());

                String [] parametro = {idNivel3};

                ContentValues contentValuesActSincronizacion = new ContentValues();
                contentValuesActSincronizacion.put("sincronizado","1");
                database.update(Utilidades.TABLA_NIVEL2, contentValuesActSincronizacion, Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2+"=?", parametro);
                cursorData.close();
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SegundoNivelWelcome.this, MainActivity.class);
        startActivity(intent);
    }
}