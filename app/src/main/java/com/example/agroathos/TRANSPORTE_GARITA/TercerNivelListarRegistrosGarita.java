package com.example.agroathos.TRANSPORTE_GARITA;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TercerNivelListarRegistrosGarita extends AppCompatActivity {

    ListView lvData, lvDataBus;
    Button btnBus, btnPersonal, btnUnidad;

    ConexionSQLiteHelper conn;
    ArrayList<String> arrayListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_listar_registros_garita);

        conn = new ConexionSQLiteHelper(this,"athos0",null,Utilidades.VERSION_APP);

        lvData = findViewById(R.id.lvDataRegistradaGARITA);
        lvDataBus = findViewById(R.id.lvDataBusRegistradaGARITA);
        btnBus = findViewById(R.id.btnListarBusGARITA);
        btnPersonal = findViewById(R.id.btnListarPersonalGARITA);
        btnUnidad = findViewById(R.id.btnListarUnidadGARITA);

        btnBus.setOnClickListener(view->listarRegistroBus());
        btnPersonal.setOnClickListener(view->listarRegistroPersonal());
        btnUnidad.setOnClickListener(view->listarRegistroUnidad());

        listarRegistroBus();

        lvDataBus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listarRegistroBusPersonal(lvDataBus.getItemAtPosition(position).toString());
            }
        });

    }

    private void listarRegistroBus(){
        lvData.setVisibility(View.GONE);
        lvDataBus.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Listando el Registro de Bus", Toast.LENGTH_SHORT).show();
        arrayListData.clear();

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1_5+" GROUP BY placa HAVING COUNT(*)>=1", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    arrayListData.add(cursor.getString(3));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListData);
                    lvDataBus.setAdapter(adapter);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    private void listarRegistroBusPersonal(String placa){
        SQLiteDatabase database = conn.getReadableDatabase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = getLayoutInflater().inflate(R.layout.content_transporte_garita_listar_personal_bus, null, false);
        ListView lvDataPersonal = view.findViewById(R.id.lvDataPersonalBusTRANSPORTE_GARITA);
        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1_5+" WHERE "+Utilidades.CAMPO_GARITA_PLACA_NIVEL1_5+"="+"'"+placa+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do {
                    String anexo = cursor.getString(1);
                    Cursor cursor2 = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1+" WHERE "+Utilidades.CAMPO_GARITA_ANEXO_PLACA_NIVEL1+"="+"'"+anexo+"'", null);
                    if (cursor2 != null){
                        if (cursor2.moveToFirst()){
                            do{
                                arrayList.add(cursor2.getString(4));
                            }while (cursor2.moveToNext());
                        }

                    }
                }while (cursor.moveToNext());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
                lvDataPersonal.setAdapter(adapter);
                adapter.notifyDataSetInvalidated();

                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }
        cursor.close();
    }

    private void listarRegistroPersonal(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SELECCIONA LA HORA DE REGISTRO");
        builder.setCancelable(false);
        builder.setPositiveButton("INGRESO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listarRegistroPersonalHoras("INGRESO");
            }
        });
        builder.setNegativeButton("SALIDA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listarRegistroPersonalHoras("SALIDA");
            }
        });
        builder.create().show();
    }

    private void listarRegistroPersonalHoras(String tipo){
        arrayListData.clear();
        lvData.setAdapter(null);
        lvDataBus.setVisibility(View.GONE);
        lvData.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Listando el Registro de Personal", Toast.LENGTH_SHORT).show();

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL2+" WHERE "+Utilidades.CAMPO_GARITA_FECHA_NIVEL2+"="+"'"+obtenerFechaActual("GMT-5")+"' AND "+Utilidades.CAMPO_GARITA_TIPO_HORA_NIVEL2+"="+"'"+tipo+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    arrayListData.add(cursor.getString(3));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListData);
                    lvData.setAdapter(adapter);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    private void listarRegistroUnidad(){
        arrayListData.clear();
        lvData.setAdapter(null);
        lvDataBus.setVisibility(View.GONE);
        lvData.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Listando el Registro de Unidad", Toast.LENGTH_SHORT).show();

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL3+" WHERE "+Utilidades.CAMPO_GARITA_FECHA_NIVEL3+"="+"'"+obtenerFechaActual("AMERICA/Lima")+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    arrayListData.add(cursor.getString(3));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListData);
                    lvData.setAdapter(adapter);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
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
        Intent intent = new Intent(TercerNivelListarRegistrosGarita.this, PrimerNivelWelcomeGarita.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}