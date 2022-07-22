package com.example.agroathos.RRHH_DESTAJO_AR;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.example.agroathos.TRANSPORTE_GARITA.PrimerNivelWelcomeGarita;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
    SharedPreferences preferences;

    ArrayList<String> arrayListDataLocal = new ArrayList<>();

    //DATOS ACT
    ArrayList<String> idProductividad = new ArrayList<>();
    int validarEstado = 0;
    ArrayList<String> arrayListIdBD = new ArrayList<>();
    ArrayList<String> arrayListFechaBD = new ArrayList<>();
    ArrayList<String> arrayListHoraBD = new ArrayList<>();
    ArrayList<String> arrayListJarraBD = new ArrayList<>();
    ArrayList<String> arrayListSincBD = new ArrayList<>();

    String fechaActual = "";
    String fechaBDAntigua = "";
    String dni = "";

    TextToSpeech voz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_destajo);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);
        preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
        dni = preferences.getString("dni", "");

        voz = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voz.setLanguage(new Locale("es", "pe"));
                }
            }
        });

        toolbar = findViewById(R.id.toolbarPRIMER_NIVEL_PRODUCTIVIDAD);
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

        iniciarScan();

        fabCamara.setOnClickListener(view -> {
            iniciarScan();
            lvJarras.setVisibility(View.VISIBLE);
            lvDatosProd.setVisibility(View.GONE);
        });

        btnListar.setOnClickListener(view -> {
            lvJarras.setVisibility(View.GONE);
            lvDatosProd.setVisibility(View.VISIBLE);
            listarDatos();
        });

        lvDatosProd.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = lvDatosProd.getItemAtPosition(position).toString();
                contarTrabajadores(nombre);
            }
        });

        btnRegistrar.setOnClickListener(view -> {
            if (lvJarras.getCount()>0){
                registrarJarras();
            }else{
                Toast.makeText(this, "Faltan Datos", Toast.LENGTH_SHORT).show();
            }
        });

        limpiezaPeriodica();

        if (!fechaBDAntigua.equals(fechaActual)){
            limpiarBDSQLite();
        }
    }

    public void contarTrabajadores(String nombre){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_DESTAJO_NIVEL1+" WHERE "+Utilidades.CAMPO_DESTAJO_JARRA_NIVEL1+"="+"'"+nombre+"'", null);
        if (cursorData != null){
            if (cursorData.moveToNext()){
                Toast.makeText(this, "Hay "+cursorData.getCount()+" registros.", Toast.LENGTH_SHORT).show();
            }
        }
        cursorData.close();
    }

    private void listarDatos(){
        arrayListDataLocal.clear();
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_DESTAJO_NIVEL1+" WHERE "+Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+"="+"'0' OR "+ Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+"="+"'1' AND "+Utilidades.CAMPO_DESTAJO_FECHA_NIVEL1+"="+"'"+obtenerFechaActual("AMERICA/Lima")+"' GROUP BY Jarra HAVING COUNT(*)>=1", null);
        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    arrayListDataLocal.add(cursorData.getString(3));
                }while (cursorData.moveToNext());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayListDataLocal);
        lvDatosProd.setAdapter(adapter);

        cursorData.close();
    }
    private void registrarJarras(){
        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<lvJarras.getAdapter().getCount(); i++){
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_DESTAJO_JARRA_NIVEL1, arrayJarras.get(i));
            values.put(Utilidades.CAMPO_DESTAJO_DNI_NIVEL1, dni);
            values.put(Utilidades.CAMPO_DESTAJO_FECHA_NIVEL1, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_DESTAJO_HORA_NIVEL1, arrayHoras.get(i));
            values.put(Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1, "0");

            database.insert(Utilidades.TABLA_DESTAJO_NIVEL1, Utilidades.CAMPO_DESTAJO_ID_NIVEL1, values);
        }

        Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();
        lvJarras.setAdapter(null);
        arrayInfo.clear();
        arrayJarras.clear();
    }
    private void verificarRegistros() {
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_DESTAJO_NIVEL1+" WHERE "+Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+"="+"'0'", null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                validarEstado = 1;
                arrayListIdBD.add(cursor.getString(0));
                arrayListFechaBD.add(cursor.getString(1));
                arrayListHoraBD.add(cursor.getString(2));
                arrayListJarraBD.add(cursor.getString(3));
                arrayListSincBD.add(cursor.getString(5));
            }
        }else{
            validarEstado = 0;
        }

    }
    private void registrarDatos(){
        for (int i=0; i<arrayListIdBD.size(); i++){
            JSONObject object = new JSONObject();
            try {
                object.put("fecha",arrayListFechaBD.get(i));
                object.put("hora",arrayListHoraBD.get(i));
                object.put("dni_personal",arrayListJarraBD.get(i));
                object.put("dni",dni);
                object.put("sinc","1");
            }catch (Exception e){
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://agroathos.com/api/productividad",
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

            RequestQueue requestQueue = Volley.newRequestQueue(PrimerNivelWelcomeDestajo.this);
            requestQueue.add(jsonObjectRequest);
        }
    }
    private void actualizarEstadoSincronizacion(){
        SQLiteDatabase dataObtenida = conn.getReadableDatabase();
        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursorData = dataObtenida.rawQuery("SELECT * FROM "+Utilidades.TABLA_DESTAJO_NIVEL1+" WHERE "
                +Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1+"="+"'0'", null);

        if (cursorData != null){
            if (cursorData.moveToFirst()){
                do {
                    idProductividad.add(cursorData.getString(0));
                    validarEstado = 0;
                }while (cursorData.moveToNext());

                for (int i=0; i<idProductividad.size(); i++){
                    String [] parametro = {idProductividad.get(i)};

                    ContentValues contentValuesActSincronizacion = new ContentValues();
                    contentValuesActSincronizacion.put("sincronizado","1");
                    database.update(Utilidades.TABLA_DESTAJO_NIVEL1, contentValuesActSincronizacion,
                            Utilidades.CAMPO_DESTAJO_ID_NIVEL1+"=?", parametro);
                }
                cursorData.close();
            }
        }

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
    public static String obtenerHoraActual(String zonaHoraria) {
        String formato = "HH:mm:ss";
        return obtenerFechaConFormato(formato, zonaHoraria);
    }
    public static String obtenerFechaActual(String zonaHoraria) {
        String formato = "dd-MM-yyyy";
        return obtenerFechaConFormato(formato, zonaHoraria);
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
                validarConexionInternet();
                break;
            case R.id.menu_cerrar_sesion_action:
                Intent intent = new Intent(PrimerNivelWelcomeDestajo.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarScan(){
        IntentIntegrator integrator = new IntentIntegrator(PrimerNivelWelcomeDestajo.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Lector QR Personal");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            String scanFormat = intentResult.getFormatName();

            if (!TextUtils.isEmpty(scanFormat)) {
                if (scanFormat.equals("QR_CODE") || scanFormat.equals("CODE_39")) {
                    if (intentResult.getContents() == null) {
                        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayInfo);
                        lvJarras.setAdapter(adapter);
                    }else{
                        try {
                            Bundle bundle = new Bundle();
                            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);

                            if (intentResult.getContents().length() > 8){
                                String dato = intentResult.getContents().substring(11);
                                voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle, null);
                                arrayInfo.add(intentResult.getContents());
                                arrayJarras.add(intentResult.getContents());
                                arrayHoras.add(obtenerHoraActual("GMT-5"));
                                iniciarScan();
                            }else{
                                voz.speak("REGISTRADO", TextToSpeech.QUEUE_FLUSH, bundle, null);
                                arrayInfo.add(intentResult.getContents());
                                arrayJarras.add(intentResult.getContents());
                                arrayHoras.add(obtenerHoraActual("GMT-5"));
                                iniciarScan();
                            }

                        }catch (Exception e){
                            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(this, "Formato de lectura no admitida", Toast.LENGTH_SHORT).show();
                }
            }else{
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayInfo);
                lvJarras.setAdapter(adapter);
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }

        /*JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://agroathos.com/api/productividad/"+intentResult.getContents(), null, new Response.Listener<JSONObject>() {
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
                requestQueue.add(jsonObjectRequest);*/
    }

    public void limpiezaPeriodica(){
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_DESTAJO_NIVEL1 + " WHERE " + Utilidades.CAMPO_DESTAJO_SINCRONIZADO_NIVEL1 + "=" + "'1'", null);
        if (cursor != null){

            if (cursor.moveToFirst()){
                do{
                    fechaActual = obtenerFechaActual("AMERICA/Lima");
                    fechaBDAntigua = cursor.getString(1);
                }while (cursor.moveToNext());
            }

        }
        cursor.close();
    }

    public void limpiarBDSQLite(){
        SQLiteDatabase database = this.conn.getWritableDatabase();
        String t1 = "DELETE FROM "+Utilidades.TABLA_DESTAJO_NIVEL1;
        database.execSQL(t1);
        database.close();
    }

    public void validarConexionInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            verificarRegistros();
            if (validarEstado == 1){
                registrarDatos();
                actualizarEstadoSincronizacion();
                Toast.makeText(this, "Datos Sincronizados", Toast.LENGTH_SHORT).show();
                validarEstado = 0;
            }else{
                Toast.makeText(this, "Ya se migró la data", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Necesitas conexión a internet.", Toast.LENGTH_SHORT).show();
        }

    }
}