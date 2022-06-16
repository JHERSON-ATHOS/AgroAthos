package com.example.agroathos.RRHH_TAREO_AR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

    //DATOS PARA SUBIDA
    String zonaUP = "";
    String fundoUP = "";
    String cultivoUP = "";
    String dni_supervisorUP = "";
    String horaUP = "";
    String fechaUP = "";

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
        obtenerDataRegistrada();

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
                registrarDatos();
                //Toast.makeText(this, "OSEA SÍ, PERO NO!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtenerDataRegistrada(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1+" WHERE "+Utilidades.CAMPO_DNI_NIVEL1+"="+"'"+dni+"'", null);
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
        }catch (Exception e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/nivel_uno", object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(SegundoNivelWelcome.this, "success", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SegundoNivelWelcome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(SegundoNivelWelcome.this);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SegundoNivelWelcome.this, MainActivity.class);
        startActivity(intent);
    }
}