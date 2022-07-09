package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.ENTIDADES.E_Cultivos;
import com.example.agroathos.ENTIDADES.E_Fundos;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_Zonas;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorCultivos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorFundos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorZonas;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class PrimerNivelWelcomeTareo extends AppCompatActivity {

    Spinner spZona, spFundo, spCultivo;
    EditText etDNI;
    Button btnIngresar;
    ConstraintLayout layout;

    String zona = "";
    String fundo = "";
    String cultivo = "";

    //ADAPTADOR PARA SP PERSONALIZADO
    ArrayList<E_Zonas> zonasArrayListPersonalizado = new ArrayList<>();
    ArrayList<E_Fundos> fundosArrayListPersonalizado = new ArrayList<>();
    ArrayList<E_Cultivos> cultivosArrayListPersonalizado = new ArrayList<>();
    AdaptadorZonas adaptadorZonasPersonalizado;
    AdaptadorFundos adaptadorFundosPersonalizado;
    AdaptadorCultivos adaptadorCultivosPersonalizado;

    ConexionSQLiteHelper conn;
    SharedPreferences preferences;
    SharedPreferences preferencesLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        preferencesLogin = getSharedPreferences("Acceso", Context.MODE_PRIVATE);

        spZona = findViewById(R.id.spZonaRRHH_TAREO_ARANDANO_PRIMER_NIVEL);
        spFundo = findViewById(R.id.spFundoRRHH_TAREO_ARANDANO_PRIMER_NIVEL);
        spCultivo = findViewById(R.id.spCultivoRRHH_TAREO_ARANDANO_PRIMER_NIVEL);
        etDNI = findViewById(R.id.etDNIRRHH_TAREO_ARANDANO_PRIMER_NIVEL);
        btnIngresar = findViewById(R.id.btnIngresasrRRHH_TAREO_ARANDANO_PRIMER_NIVEL);
        layout = findViewById(R.id.layoutPrincipalPrimerNivelWelcomeTareoArRRHH_TAREO_AR);

        cargarDatos();

        if (!preferencesLogin.getString("dni", "").isEmpty()){
            etDNI.setEnabled(false);
            etDNI.setText(preferencesLogin.getString("dni", ""));
        }

        if (!preferences.getString("dniSupervisor", "").isEmpty()){
            Intent intent = new Intent(PrimerNivelWelcomeTareo.this, SegundoNivelWelcome.class);
            startActivity(intent);
        }

        spZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Zonas e_zonas = zonasArrayListPersonalizado.get(i);
                zona = e_zonas.getNombre();

                switch (zona){
                    case "ICA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos("-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LDN"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LPO"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MAC"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MEN"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "PAR"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SAT"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SNA"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SOJ"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "STF"));
                        break;
                    case "PISCO":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos("-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos("LIN"));
                        break;
                    case "NAZCA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "MAT"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "LUC"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SOI"));
                        break;
                    case "CARAZ":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "CHI"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "POM"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SCA"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "SLA"));
                        break;
                    case "NEPEÑA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos( "CAY"));
                        break;
                }

                adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeTareo.this, fundosArrayListPersonalizado);
                spFundo.setAdapter(adaptadorFundosPersonalizado);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        spFundo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Fundos e_fundos = fundosArrayListPersonalizado.get(i);
                fundo = e_fundos.getNombre();

                switch (fundo){
                    case "SAT":
                    case "LPO":
                    case "PAR":
                    case "SCA":
                    case "SOJ":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "GRANADA"));
                        break;
                    case "CAY":
                    case "LIN":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "HIGO"));
                        break;
                    case "CHI":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ARANDANO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "AGUAYMANTO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "CEREZO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "PITIHAYA"));
                        break;
                    case "LDN":
                    case "SLA":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ARANDANO"));
                        break;
                    case "LUC":
                    case "MAT":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "DATIL"));
                        break;
                    case "MAC":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "AGUAYMANTO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "DATIL"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ESPARRAGO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "GRANADA"));
                        break;
                    case "MEN":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ESPARRAGO"));
                        break;
                    case "POM":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ARANDANO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "CEREZO"));
                        break;
                    case "SNA":
                    case "STF":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "DATIL"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "GRANADA"));
                        break;
                    case "SOI":
                        cultivosArrayListPersonalizado.clear();
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ARANDANO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "DATIL"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "ESPARRAGO"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "GRANADA"));
                        cultivosArrayListPersonalizado.add(new E_Cultivos( "PALTA"));
                        break;
                }
                adaptadorCultivosPersonalizado = new AdaptadorCultivos(PrimerNivelWelcomeTareo.this, cultivosArrayListPersonalizado);
                spCultivo.setAdapter(adaptadorCultivosPersonalizado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spCultivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                E_Cultivos e_cultivos = cultivosArrayListPersonalizado.get(i);
                cultivo = e_cultivos.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnIngresar.setOnClickListener(view -> consultarDatos(zona, fundo, cultivo, etDNI.getText().toString()));
    }

    private void cargarDatos(){
        zonasArrayListPersonalizado.add(new E_Zonas("-- Selecciona una Zona --"));
        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));
        cultivosArrayListPersonalizado.add(new E_Cultivos( "-- Selecciona un Cultivo --"));

        zonasArrayListPersonalizado.add(new E_Zonas( "CARAZ"));
        zonasArrayListPersonalizado.add(new E_Zonas( "ICA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "PISCO"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NEPEÑA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NAZCA"));

        adaptadorZonasPersonalizado = new AdaptadorZonas(PrimerNivelWelcomeTareo.this, zonasArrayListPersonalizado);
        adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeTareo.this, fundosArrayListPersonalizado);
        adaptadorCultivosPersonalizado = new AdaptadorCultivos(PrimerNivelWelcomeTareo.this, cultivosArrayListPersonalizado);

        spZona.setAdapter(adaptadorZonasPersonalizado);
        spFundo.setAdapter(adaptadorFundosPersonalizado);
        spCultivo.setAdapter(adaptadorCultivosPersonalizado);
    }

    private void enviarDatos(){

        if (zona.equals("-- Selecciona una Zona --")){
            Snackbar.make(layout, "Selecciona una Zona", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (fundo.equals("-- Selecciona un Fundo --")){
            Snackbar.make(layout, "Selecciona un Fundo", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (cultivo.equals("-- Selecciona un Cultivo --")){
            Snackbar.make(layout, "Selecciona un Cultivo", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(etDNI.getText().toString())){
            Snackbar.make(layout, "Ingresa un DNI", Snackbar.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = conn.getWritableDatabase();

        String id_nivel_uno = UUID.randomUUID().toString();

        ContentValues values = new ContentValues();
        values.put(Utilidades.CAMPO_IDNIVEL1_NIVEL1, id_nivel_uno);
        values.put(Utilidades.CAMPO_ZONA_NIVEL1, zona);
        values.put(Utilidades.CAMPO_FUNDO_NIVEL1, fundo);
        values.put(Utilidades.CAMPO_CULTIVO_NIVEL1, cultivo);
        values.put(Utilidades.CAMPO_DNI_NIVEL1, etDNI.getText().toString());
        values.put(Utilidades.CAMPO_FECHA_NIVEL1, obtenerFechaActual("AMERICA/Lima"));
        values.put(Utilidades.CAMPO_HORA_NIVEL1, obtenerHoraActual("GMT-5"));
        values.put(Utilidades.CAMPO_SINCRONIZADO_NIVEL1, "0");

        Long idResultante = database.insert(Utilidades.TABLA_NIVEL1, Utilidades.CAMPO_ID_NIVEL1, values);

        if (idResultante > 0){
            Toast.makeText(this, "Registrado!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(PrimerNivelWelcomeTareo.this, SegundoNivelWelcome.class);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("dniSupervisor", etDNI.getText().toString());
            editor.putString("dni", etDNI.getText().toString());
            editor.putString("idNivel1", id_nivel_uno);
            editor.putString("zona", zona);
            editor.putString("fundo", fundo);
            editor.putString("cultivo", cultivo);
            editor.commit();

            database.close();
            startActivity(intent);
        }
    }

    public void consultarDatos(String zona, String fundo, String cultivo, String dni){
        SQLiteDatabase database = conn.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_NIVEL1+" WHERE "+Utilidades.CAMPO_ZONA_NIVEL1+"="
                +"'"+zona+"' AND "+Utilidades.CAMPO_FUNDO_NIVEL1+"="+ "'"+fundo+"' AND "+Utilidades.CAMPO_CULTIVO_NIVEL1+"="
                +"'"+cultivo+"' AND "+Utilidades.CAMPO_DNI_NIVEL1+"="+"'"+dni+"'", null);

        if (cursor != null){
            if (cursor.moveToFirst()){
                String idNivel1BD = cursor.getString(1);
                String zonaBD = cursor.getString(2);
                String fundoBD = cursor.getString(3);
                String cultivoBD = cursor.getString(4);
                String dniBD = cursor.getString(5);

                Intent intent = new Intent(PrimerNivelWelcomeTareo.this, SegundoNivelWelcome.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("dniSupervisor", etDNI.getText().toString());
                editor.putString("dni", dniBD);
                editor.putString("idNivel1", idNivel1BD);
                editor.putString("zona", zonaBD);
                editor.putString("fundo", fundoBD);
                editor.putString("cultivo", cultivoBD);
                editor.commit();

                startActivity(intent);

            }else{
                enviarDatos();
            }
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PrimerNivelWelcomeTareo.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}