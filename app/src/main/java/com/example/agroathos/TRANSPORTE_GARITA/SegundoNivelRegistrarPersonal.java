package com.example.agroathos.TRANSPORTE_GARITA;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class SegundoNivelRegistrarPersonal extends AppCompatActivity {

    TextView tvFecha;
    EditText etPlaca;
    ListView lvData;
    Button btnRegistrar;
    FloatingActionButton fabCamara;

    int valorScan = 0;

    ArrayAdapter adapter;
    ArrayList<String> arrayDataPersonal = new ArrayList<>();
    ArrayList<String> arrayDataUnidad = new ArrayList<>();
    ArrayList<String> arrayHorasPersonal = new ArrayList<>();
    ArrayList<String> arrayHorasUnidad = new ArrayList<>();
    ConexionSQLiteHelper conn;

    String zona = "";
    String fundo = "";
    int valor = 0;
    String tipo = "";
    String idGrupo = "";

    ContentValues values = new ContentValues();
    ContentValues valuesIntermedio = new ContentValues();

    TextToSpeech voz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_segundo_nivel_registrar_personal);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        voz = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    voz.setLanguage(new Locale("es", "pe"));
                }
            }
        });

        tvFecha = findViewById(R.id.tvFechaBusGARITA);
        etPlaca = findViewById(R.id.etPlacaBusGARITA);
        lvData = findViewById(R.id.lvDataGARITA);
        btnRegistrar = findViewById(R.id.btnRegistrarDataGARITA);
        fabCamara = findViewById(R.id.fbActivarCamaraGARITA);

        recibirDatos();

        fabCamara.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etPlaca.getText().toString())){
                iniciarScanBus();
            }else{
                iniciarScanPersonal();
            }
        });
        tvFecha.setText(obtenerFechaActual("GMT-5"));
        btnRegistrar.setOnClickListener(view ->{
            switch (valor){
                case 1:
                    registrarBus();
                    break;
                case 2:
                    registrarPersonal();
                    break;
                case 3:
                    registrarUnidad();
                    break;
            }
        });

        switch (valor){
            case 1:
                iniciarScanBus();
                break;
            case 2:
                etPlaca.setVisibility(View.GONE);
                iniciarScanPersonal();
                break;
            case 3:
                etPlaca.setVisibility(View.GONE);
                iniciarScanUnidad();
                break;
            default:
                break;
        }
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

    private void recibirDatos(){
        Bundle bundle = getIntent().getExtras();
        zona = bundle.getString("zona", "");
        fundo = bundle.getString("fundo", "");
        valor = bundle.getInt("valor", 0);
        tipo = bundle.getString("tipo", "");
    }

    private void iniciarScanBus(){
        valorScan = 1;
        IntentIntegrator integrator = new IntentIntegrator(SegundoNivelRegistrarPersonal.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("LECTOR QR BUS");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void iniciarScanPersonal(){
        valorScan = 2;
        IntentIntegrator integrator = new IntentIntegrator(SegundoNivelRegistrarPersonal.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("LECTOR QR PERSONAL");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void iniciarScanUnidad(){
        valorScan = 3;
        IntentIntegrator integrator = new IntentIntegrator(SegundoNivelRegistrarPersonal.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("LECTOR QR UNIDAD");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void registrarBus(){
        if (lvData.getCount()==0){
            Toast.makeText(this, "No hay personal", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_GARITA_NIVEL1_5 + " WHERE " + Utilidades.CAMPO_GARITA_ANEXO_PERSONAL_NIVEL1_5 + "=" + "'"+etPlaca.getText().toString()+"'", null);

        idGrupo = UUID.randomUUID().toString();

        if (cursor.getCount()<0){
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_ANEXO_PERSONAL_NIVEL1_5, idGrupo);
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_CONTADOR_NIVEL1_5, 1);
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_PLACA_NIVEL1_5, etPlaca.getText().toString().trim());
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1_5, "0");
        }else{
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_ANEXO_PERSONAL_NIVEL1_5, idGrupo);
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_CONTADOR_NIVEL1_5, cursor.getCount()+1);
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_PLACA_NIVEL1_5, etPlaca.getText().toString().trim());
            valuesIntermedio.put(Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1_5, "0");
        }

        database.insert(Utilidades.TABLA_GARITA_NIVEL1_5, Utilidades.CAMPO_GARITA_ID_NIVEL1_5, valuesIntermedio);

        for (int i=0; i<lvData.getCount(); i++){
            values.put(Utilidades.CAMPO_GARITA_ZONA_NIVEL1, zona);
            values.put(Utilidades.CAMPO_GARITA_FUNDO_NIVEL1, fundo);
            values.put(Utilidades.CAMPO_GARITA_ANEXO_PLACA_NIVEL1, idGrupo);
            values.put(Utilidades.CAMPO_GARITA_PERSONAL_NIVEL1, arrayDataPersonal.get(i));
            values.put(Utilidades.CAMPO_GARITA_FECHA_NIVEL1, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_GARITA_HORA_NIVEL1, arrayHorasPersonal.get(i));
            values.put(Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL1, "0");

            database.insert(Utilidades.TABLA_GARITA_NIVEL1, Utilidades.CAMPO_GARITA_ID_NIVEL1, values);
        }

        Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("REGISTRO DE BUS");
        builder.setMessage("¿Continuarás registrando?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etPlaca.setText("");
                arrayDataPersonal.clear();
                arrayHorasPersonal.clear();
                adapter.clear();
                iniciarScanBus();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                regresarPrimerActividad();
            }
        });

        builder.create().show();
    }

    private void registrarPersonal(){
        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<lvData.getAdapter().getCount(); i++){
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_GARITA_ZONA_NIVEL2, zona);
            values.put(Utilidades.CAMPO_GARITA_FUNDO_NIVEL2, fundo);
            values.put(Utilidades.CAMPO_GARITA_PERSONAL_NIVEL2, arrayDataPersonal.get(i));
            values.put(Utilidades.CAMPO_GARITA_TIPO_HORA_NIVEL2, tipo);
            values.put(Utilidades.CAMPO_GARITA_FECHA_NIVEL2, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_GARITA_HORA_NIVEL2, arrayHorasPersonal.get(i));
            values.put(Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL2, "0");

            database.insert(Utilidades.TABLA_GARITA_NIVEL2, Utilidades.CAMPO_GARITA_ID_NIVEL2, values);

            Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE PERSONAL");
            builder.setMessage("¿Continuarás registrando?");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    arrayDataPersonal.clear();
                    arrayHorasPersonal.clear();
                    adapter.clear();
                    iniciarScanPersonal();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    regresarPrimerActividad();
                }
            });

            builder.create().show();
        }
    }

    private void registrarUnidad(){
        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<lvData.getAdapter().getCount(); i++){
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_GARITA_ZONA_NIVEL3, zona);
            values.put(Utilidades.CAMPO_GARITA_FUNDO_NIVEL3, fundo);
            values.put(Utilidades.CAMPO_GARITA_PERSONAL_NIVEL3, arrayDataUnidad.get(i));
            values.put(Utilidades.CAMPO_GARITA_TIPO_HORA_NIVEL3, tipo);
            values.put(Utilidades.CAMPO_GARITA_FECHA_NIVEL3, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_GARITA_HORA_NIVEL3, arrayHorasUnidad.get(i));
            values.put(Utilidades.CAMPO_GARITA_SINCRONIZADO_NIVEL3, "0");

            database.insert(Utilidades.TABLA_GARITA_NIVEL3, Utilidades.CAMPO_GARITA_ID_NIVEL3, values);

            Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE UNIDAD");
            builder.setMessage("¿Continuarás registrando?");
            builder.setCancelable(false);
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    arrayDataUnidad.clear();
                    arrayHorasUnidad.clear();
                    adapter.clear();
                    iniciarScanUnidad();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    regresarPrimerActividad();
                }
            });

            builder.create().show();
        }
    }

    public void regresarPrimerActividad(){
        Intent intent = new Intent(SegundoNivelRegistrarPersonal.this, PrimerNivelWelcomeGarita.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            String scanFormat = intentResult.getFormatName();

            if (!TextUtils.isEmpty(scanFormat)) {
                if (scanFormat.equals("QR_CODE") || scanFormat.equals("CODE_39")) {

                    if (intentResult.getContents() == null){
                        //Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_SHORT).show();
                    }else{
                        switch (valorScan){
                            case 1:
                                Toast.makeText(this, "PLACA REGISTRADA", Toast.LENGTH_SHORT).show();
                                etPlaca.setText(intentResult.getContents());
                                iniciarScanPersonal();
                                break;
                            case 2:
                            case 3:
                                switch (valor){
                                    case 1:
                                        if (etPlaca.getText().toString().equals(intentResult.getContents())){
                                            //Toast.makeText(this, "PLACA NO PERMITIDA", Toast.LENGTH_SHORT).show();
                                            iniciarScanPersonal();
                                        }else if (lvData.getCount()>0){
                                            if (arrayDataPersonal.contains(intentResult.getContents())){
                                                //Toast.makeText(this, "PERSONAL YA INGRESADO", Toast.LENGTH_SHORT).show();
                                                iniciarScanPersonal();
                                            }else{
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                                                String dato = intentResult.getContents().substring(11);
                                                voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle,null);
                                                //Toast.makeText(this, "PERSONAL REGISTRADO", Toast.LENGTH_SHORT).show();
                                                arrayDataPersonal.add(intentResult.getContents());

                                                arrayHorasPersonal.add(obtenerHoraActual("GMT-5"));
                                                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataPersonal);
                                                lvData.setAdapter(adapter);

                                                iniciarScanPersonal();
                                            }
                                        }else{
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                                            String dato = intentResult.getContents().substring(11);
                                            voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle,null);
                                            //Toast.makeText(this, "PERSONAL REGISTRADO", Toast.LENGTH_SHORT).show();
                                            arrayDataPersonal.add(intentResult.getContents());

                                            arrayHorasPersonal.add(obtenerHoraActual("GMT-5"));
                                            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataPersonal);
                                            lvData.setAdapter(adapter);

                                            iniciarScanPersonal();
                                        }
                                        break;
                                    case 2:
                                        if (lvData.getCount()>0){
                                            if (arrayDataPersonal.contains(intentResult.getContents())){
                                                //Toast.makeText(this, "PERSONAL YA INGRESADO", Toast.LENGTH_SHORT).show();
                                                iniciarScanPersonal();
                                            }else{
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                                                String dato = intentResult.getContents().substring(11);
                                                voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle,null);
                                                //Toast.makeText(this, "PERSONAL REGISTRADO", Toast.LENGTH_SHORT).show();
                                                arrayDataPersonal.add(intentResult.getContents());

                                                arrayHorasPersonal.add(obtenerHoraActual("GMT-5"));
                                                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataPersonal);
                                                lvData.setAdapter(adapter);

                                                iniciarScanPersonal();
                                            }
                                        }else{
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC);
                                            String dato = intentResult.getContents().substring(11);
                                            voz.speak(dato, TextToSpeech.QUEUE_FLUSH, bundle,null);
                                            //Toast.makeText(this, "PERSONAL REGISTRADO", Toast.LENGTH_SHORT).show();
                                            arrayDataPersonal.add(intentResult.getContents());

                                            arrayHorasPersonal.add(obtenerHoraActual("GMT-5"));
                                            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataPersonal);
                                            lvData.setAdapter(adapter);

                                            iniciarScanPersonal();
                                        }
                                        break;
                                    case 3:
                                        if (lvData.getCount()>0){
                                            if (arrayDataUnidad.contains(intentResult.getContents())){
                                                //Toast.makeText(this, "UNIDAD YA INGRESADA", Toast.LENGTH_SHORT).show();
                                                iniciarScanUnidad();
                                            }else{
                                                //Toast.makeText(this, "UNIDAD REGISTRADA", Toast.LENGTH_SHORT).show();
                                                arrayDataUnidad.add(intentResult.getContents());

                                                arrayHorasUnidad.add(obtenerHoraActual("GMT-5"));
                                                adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataUnidad);
                                                lvData.setAdapter(adapter);

                                                iniciarScanUnidad();
                                            }
                                        }else{
                                            //Toast.makeText(this, "UNIDAD REGISTRADA", Toast.LENGTH_SHORT).show();
                                            arrayDataUnidad.add(intentResult.getContents());

                                            arrayHorasUnidad.add(obtenerHoraActual("GMT-5"));
                                            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataUnidad);
                                            lvData.setAdapter(adapter);

                                            iniciarScanUnidad();
                                        }
                                        break;
                                }
                                break;
                        }
                    }

                }
            }else{
                if (arrayDataPersonal.size()>0){
                    adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataPersonal);
                    lvData.setAdapter(adapter);
                }

                if (arrayDataUnidad.size()>0){
                    adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayDataUnidad);
                    lvData.setAdapter(adapter);
                }
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SegundoNivelRegistrarPersonal.this, PrimerNivelWelcomeGarita.class);
        startActivity(intent);
    }
}