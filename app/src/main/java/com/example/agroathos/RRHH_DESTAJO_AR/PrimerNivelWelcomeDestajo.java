package com.example.agroathos.RRHH_DESTAJO_AR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PrimerNivelWelcomeDestajo extends AppCompatActivity {

    TextView tvFecha;
    ListView lvJarras, lvDatosProd;
    Button btnRegistrar, btnListar;
    FloatingActionButton fabCamara;
    Toolbar toolbar;

    ArrayList<String> arrayJarras = new ArrayList<>();
    ArrayList<String> arrayHoras = new ArrayList<>();
    ArrayList<String> arrayInfo = new ArrayList<>();

    ConexionSQLiteHelper conn;

    String dniObtenido = "";

    String dni_personal_BD = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_destajo);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        toolbar = findViewById(R.id.toolbarPRIMER_NIVEL_LISTAR_REGISTROS_GARITA);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("¡JUNTOS HACEMOS MÁS!");
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.white));

        tvFecha = findViewById(R.id.tvFechaRRHH_DESTAJO_AR);
        lvJarras = findViewById(R.id.lvJarrasRRHH_DESTAJO_AR);
        lvDatosProd = findViewById(R.id.lvListarDatosProductividadRRHH_DESTAJO_AR);
        btnRegistrar = findViewById(R.id.btnRegistrarJarrasRRHH_DESTAJO_AR);
        btnListar = findViewById(R.id.btnListarJarrasRRHH_DESTAJO_AR);
        fabCamara = findViewById(R.id.fabCamaraRRHH_DESTAJO_AR);

        tvFecha.setText(obtenerFechaActual("AMERICA/Lima"));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SELECCION DE LABOR");
        builder.setCancelable(false);
        builder.setPositiveButton("COSECHA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                iniciarScan();
            }
        });
        builder.create().show();

        fabCamara.setOnClickListener(view -> {
            iniciarScan();
        });
        btnListar.setOnClickListener(view -> {
            lvJarras.setVisibility(View.GONE);
            lvDatosProd.setVisibility(View.VISIBLE);
            listarDatos();
        });
        btnRegistrar.setOnClickListener(view -> registrarJarras());
    }

    private void listarDatos(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_DESTAJO_NIVEL1+" WHERE "+Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+"="+"'0'", null);
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
    private void registrarJarras(){
        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<lvJarras.getAdapter().getCount(); i++){
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_DESTAJO_JARRA_NIVEL1, arrayJarras.get(i));
            values.put(Utilidades.CAMPO_DESTAJO_FECHA_NIVEL1, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_DESTAJO_HORA_NIVEL1, arrayHoras.get(i));
            values.put(Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1, "0");

            database.insert(Utilidades.TABLA_DESTAJO_NIVEL1, Utilidades.CAMPO_DESTAJO_ID_NIVEL1, values);

        }

        Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();
        lvJarras.setAdapter(null);
    }

    private void iniciarScan(){
        IntentIntegrator integrator = new IntentIntegrator(PrimerNivelWelcomeDestajo.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Lector QR Jarras");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    public static String obtenerHoraActual(String zonaHoraria) {
        String formato = "HH:mm:ss";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }
    public static String obtenerFechaActual(String zonaHoraria) {
        String formato = "dd-MM-yyyy";
        return obtenerFechaConFormato(formato, zonaHoraria);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sincronizar_action:
                //OSEA SÍ, PERO NO!
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            if (intentResult.getContents() == null){
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayInfo);
                lvJarras.setAdapter(adapter);
            }else{

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://agroathos.com/api/productividad/"+intentResult.getContents(), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("data_unitaria");
                            dni_personal_BD = object.getString("personal");

                            arrayInfo.add(intentResult.getContents().concat(" - DNI= ").concat(dni_personal_BD));

                        } catch (JSONException e) {
                            Toast.makeText(PrimerNivelWelcomeDestajo.this, "Error: No existe usuario", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeDestajo.this);
                requestQueue.add(jsonObjectRequest);

                arrayJarras.add(intentResult.getContents());
                arrayHoras.add(obtenerHoraActual("GMT-5"));
                iniciarScan();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}