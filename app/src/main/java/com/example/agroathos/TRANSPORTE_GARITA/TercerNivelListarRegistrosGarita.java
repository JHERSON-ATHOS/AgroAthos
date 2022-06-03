package com.example.agroathos.TRANSPORTE_GARITA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

    TextView tvPlaca;
    ListView lvData;
    Button btnBus, btnPersonal, btnUnidad;

    ConexionSQLiteHelper conn;
    ArrayList<String> arrayListData = new ArrayList<>();

    int valor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tercer_nivel_listar_registros_garita);

        conn = new ConexionSQLiteHelper(getApplicationContext(), "athos0", null, 1);

        tvPlaca = findViewById(R.id.tvPlacaRegistradaGARITA);
        lvData = findViewById(R.id.lvDataRegistradaGARITA);
        btnBus = findViewById(R.id.btnListarBusGARITA);
        btnPersonal = findViewById(R.id.btnListarPersonalGARITA);
        btnUnidad = findViewById(R.id.btnListarUnidadGARITA);

        btnBus.setOnClickListener(view->listarRegistroBus());
        btnPersonal.setOnClickListener(view->listarRegistroPersonal());
        btnUnidad.setOnClickListener(view->listarRegistroUnidad());

        listarRegistroBus();

        if (valor==1){
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(TercerNivelListarRegistrosGarita.this, "funciona", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void listarRegistroBus(){
        valor = 1;
        Toast.makeText(this, "Listando el Registro de Bus", Toast.LENGTH_SHORT).show();
        arrayListData.clear();

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT "+Utilidades.CAMPO_GARITA_PLACA_NIVEL1_5+" FROM "+Utilidades.TABLA_GARITA_NIVEL1_5, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    tvPlaca.setVisibility(View.VISIBLE);
                    tvPlaca.setText(cursor.getString(0));
                    arrayListData.add(cursor.getString(0));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListData);
                    lvData.setAdapter(adapter);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    private void listarRegistroBusPersonal(){
        Toast.makeText(this, "Listando el Personal del Bus", Toast.LENGTH_SHORT).show();
        arrayListData.clear();

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL1+" WHERE "+Utilidades.CAMPO_GARITA_FECHA_NIVEL1+"="+"'"+obtenerFechaActual("AMERICA/Lima")+"'", null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                do{
                    tvPlaca.setVisibility(View.VISIBLE);
                    tvPlaca.setText(cursor.getString(3));
                    arrayListData.add(cursor.getString(4));
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListData);
                    lvData.setAdapter(adapter);
                }while (cursor.moveToNext());
            }
        }
        cursor.close();
    }

    private void listarRegistroPersonal(){
        valor = 2;
        Toast.makeText(this, "Listando el Registro de Personal", Toast.LENGTH_SHORT).show();
        arrayListData.clear();
        tvPlaca.setVisibility(View.GONE);

        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+Utilidades.TABLA_GARITA_NIVEL2+" WHERE "+Utilidades.CAMPO_GARITA_FECHA_NIVEL2+"="+"'"+obtenerFechaActual("AMERICA/Lima")+"'", null);
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
        Toast.makeText(this, "Listando el Registro de Unidad", Toast.LENGTH_SHORT).show();
        arrayListData.clear();
        tvPlaca.setVisibility(View.GONE);

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