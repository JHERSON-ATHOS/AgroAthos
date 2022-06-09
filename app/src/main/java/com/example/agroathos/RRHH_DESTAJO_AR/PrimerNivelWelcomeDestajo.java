package com.example.agroathos.RRHH_DESTAJO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelRegistrarGrupoTrabajo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrimerNivelWelcomeDestajo extends AppCompatActivity {

    TextView tvFecha;
    ListView lvJarras;
    Button btnRegistrar;

    ArrayList<String> arrayJarras = new ArrayList<>();
    ArrayList<String> arrayHoras = new ArrayList<>();
    ArrayList<String> arrayInfo = new ArrayList<>();

    ConexionSQLiteHelper conn;

    String dniObtenido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_destajo);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        tvFecha = findViewById(R.id.tvFechaRRHH_DESTAJO_AR);
        lvJarras = findViewById(R.id.lvJarrasRRHH_DESTAJO_AR);
        btnRegistrar = findViewById(R.id.btnRegistrarJarrasRRHH_DESTAJO_AR);

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

        btnRegistrar.setOnClickListener(view -> registrarJarras());
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

    private void registrarJarras(){
        SQLiteDatabase database = conn.getWritableDatabase();

        for (int i=0; i<lvJarras.getAdapter().getCount(); i++){
            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_DESTAJO_JARRA_NIVEL1, arrayJarras.get(i));
            values.put(Utilidades.CAMPO_DESTAJO_FECHA_NIVEL1, tvFecha.getText().toString());
            values.put(Utilidades.CAMPO_DESTAJO_HORA_NIVEL1, arrayHoras.get(i));

            Long idResultante = database.insert(Utilidades.TABLA_DESTAJO_NIVEL1, Utilidades.CAMPO_DESTAJO_ID_NIVEL1, values);

            if (idResultante > 0){
                Toast.makeText(this, "Registro Exitoso!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarScan(){
        IntentIntegrator integrator = new IntentIntegrator(PrimerNivelWelcomeDestajo.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Lector QR Jarras");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(true);
        //flash-integrator.setTorchEnabled(true);
        integrator.initiateScan();
    }

    private void consultarJarraPersonal(String jarra){
        SQLiteDatabase database = conn.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_JARRA1_NIVEL2 + "=" + "'"+jarra+"' OR " + Utilidades.CAMPO_JARRA2_NIVEL2 + "=" + "'"+jarra+"'" , null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                dniObtenido = cursor.getString(6);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null){
            if (intentResult.getContents() == null){
                Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_SHORT).show();
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayInfo);
                lvJarras.setAdapter(adapter);
            }else{
                Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show();
                consultarJarraPersonal(intentResult.getContents());
                arrayInfo.add(intentResult.getContents().concat(" - DNI= ").concat(dniObtenido));
                arrayJarras.add(intentResult.getContents());
                arrayHoras.add(obtenerHoraActual("AMERICA/Lima"));
                iniciarScan();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}