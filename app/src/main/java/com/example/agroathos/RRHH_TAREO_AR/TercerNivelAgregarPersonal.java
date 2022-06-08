package com.example.agroathos.RRHH_TAREO_AR;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.R;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

public class TercerNivelAgregarPersonal extends AppCompatActivity {

    ListView lvListadoPersonal;
    FloatingActionButton fbAgregarPersonal;
    Button btnEditarPersona, btnAgregarPersonal;

    ArrayList<String> arrayPersonalList;

    ConexionSQLiteHelper conn;

    String idGrupo = "";
    String dni = "";
    int valor = 0;

    /*VALORES OBTENIDOS BD*/
    String BDid_nivel2 = "";
    String BDgrupo = "";
    String BDfundo = "";
    String BDmodulo = "";
    String BDlote = "";
    String BDlabor = "";
    String BDdniPersonal = "";
    String BDdniSupervisor = "";

    ContentValues valuesEditarPersonal = new ContentValues();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_asignar_jarras);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        lvListadoPersonal = findViewById(R.id.lvListadoPersonalACTTercerlNivelRRHH_TAREO_ARANDANO);
        fbAgregarPersonal = findViewById(R.id.fbAgregarPersonalNuevoRRHH_TAREO_AR);
        btnEditarPersona = findViewById(R.id.btnEditarPersonalNuevoRRHH_TAREO_AR);
        btnAgregarPersonal = findViewById(R.id.btnRegistrarPersonalNuevoRRHH_TAREO_AR);

        Bundle bundle = getIntent().getExtras();
        idGrupo = bundle.getString("idGrupo");
        dni = bundle.getString("dni");
        valor = bundle.getInt("valor");

        if (valor == 1){
            btnEditarPersona.setVisibility(View.VISIBLE);
        }
        if (valor == 2){
            btnAgregarPersonal.setVisibility(View.VISIBLE);
        }

        consultarGruposTrabajo();

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayPersonalList);
        lvListadoPersonal.setAdapter(adapter);

        lvListadoPersonal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obtenerDatosPersonal(BDdniPersonal);
            }
        });

        //fbAgregarPersonal.setOnClickListener(view->iniciarScanPersonal());
        //btnEditarPersona.setOnClickListener(view->actualizarPersonal());
        //btnAgregarPersonal.setOnClickListener(view->registrarNuevoPersonal());
    }

    private void consultarGruposTrabajo(){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_ANEXO_GRUPO_NIVEL2 + "=" + "'"+idGrupo+"'", null);

        if (cursor != null){
            while (cursor.moveToNext()) {
                arrayPersonalList.add(cursor.getString(6));
                BDid_nivel2 = cursor.getString(0);
                BDgrupo = cursor.getString(1);
                BDfundo = cursor.getString(2);
                BDmodulo = cursor.getString(3);
                BDlote = cursor.getString(4);
                BDlabor = cursor.getString(5);
                BDdniPersonal = cursor.getString(6);
                BDdniSupervisor = cursor.getString(7);
            }
        }
    }

    private void obtenerDatosPersonal(String dni_personal){
        SQLiteDatabase database = conn.getReadableDatabase();

        arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_PERSONAL_NIVEL2 + "=" + "'"+dni_personal+"'", null);

        if (cursor != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = getLayoutInflater().inflate(R.layout.content_rrhh_tareo_arandano_especificacion_personal, null, false);
            EditText etHoraInicio = view.findViewById(R.id.etHoraInicioEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            EditText etHoraFinal = view.findViewById(R.id.etHoraFinalEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbAbierto = view.findViewById(R.id.rbEstadoAbiertoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbCerrado = view.findViewById(R.id.rbEstadoCerradoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);

            etHoraFinal.setOnClickListener(v ->{
                final Calendar c = Calendar.getInstance();
                int hora = c.get(Calendar.HOUR_OF_DAY);
                int minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        etHoraFinal.setText(hourOfDay+":"+minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.show();
            });

            if (cursor.moveToFirst()){
                etHoraInicio.setText(cursor.getString(11));
                etHoraFinal.setText(cursor.getString(12));

                if (cursor.getString(13).equals("ABIERTO")){
                    rbAbierto.setChecked(true);
                }else{
                    rbCerrado.setChecked(true);
                }

            }
            builder.setView(view);

            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (rbAbierto.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");

                        String[] parametro = {BDid_nivel2};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);

                        Toast.makeText(TercerNivelAgregarPersonal.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                    }
                    if (rbCerrado.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, etHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "CERRADO");

                        String[] parametro = {BDid_nivel2};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);

                        Toast.makeText(TercerNivelAgregarPersonal.this, "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TercerNivelAgregarPersonal.this, SegundoNivelWelcome.class);
        startActivity(intent);
    }
}