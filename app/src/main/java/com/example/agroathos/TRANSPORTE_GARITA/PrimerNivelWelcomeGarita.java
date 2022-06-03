package com.example.agroathos.TRANSPORTE_GARITA;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.agroathos.ENTIDADES.E_Fundos;
import com.example.agroathos.ENTIDADES.E_Zonas;
import com.example.agroathos.MainActivity;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorFundos;
import com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES.AdaptadorZonas;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class PrimerNivelWelcomeGarita extends AppCompatActivity {

    Button btnBus, btnPersonal, btnUnidad;
    Spinner spZona, spFundo;

    ArrayList<E_Zonas> zonasArrayListPersonalizado = new ArrayList<>();
    ArrayList<E_Fundos> fundosArrayListPersonalizado = new ArrayList<>();
    AdaptadorZonas adaptadorZonasPersonalizado;
    AdaptadorFundos adaptadorFundosPersonalizado;

    String zona = "";
    String fundo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primer_nivel_welcome_garita);

        btnBus = findViewById(R.id.btnRegistrarBusGARITA);
        btnPersonal = findViewById(R.id.btnRegistrarPersonalGARITA);
        btnUnidad = findViewById(R.id.btnRegistrarUnidadGARITA);
        spZona = findViewById(R.id.spZonaTRANSPORTE_GARITA);
        spFundo = findViewById(R.id.spFundoTRANSPORTE_GARITA);

        validarAccion();
        cargarZonas();

        btnBus.setOnClickListener(view ->
                {
                    if (zona.equals("-- Selecciona una Zona --")){
                        Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (fundo.equals("-- Selecciona un Fundo --")){
                        Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    enviarDatos(1, "");
                });
        btnPersonal.setOnClickListener(view -> {
            if (zona.equals("-- Selecciona una Zona --")){
                Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fundo.equals("-- Selecciona un Fundo --")){
                Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE HORAS");
            builder.setPositiveButton("INGRESO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(2, "INGRESO");
                }
            });
            builder.setNegativeButton("SALIDA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(2, "SALIDA");
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
        btnUnidad.setOnClickListener(view -> {
            if (zona.equals("-- Selecciona una Zona --")){
                Toast.makeText(this, "Secciona una Zona", Toast.LENGTH_SHORT).show();
                return;
            }

            if (fundo.equals("-- Selecciona un Fundo --")){
                Toast.makeText(this, "Secciona un Fundo", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("REGISTRO DE HORAS");
            builder.setPositiveButton("INGRESO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(3, "INGRESO");
                }
            });
            builder.setNegativeButton("SALIDA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enviarDatos(3, "SALIDA");
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        spZona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Zonas e_zonas = zonasArrayListPersonalizado.get(position);
                zona = e_zonas.getNombre();

                switch (zona){
                    case "ICA":
                        fundosArrayListPersonalizado.clear();
                        fundosArrayListPersonalizado.add(new E_Fundos("-- Selecciona un Fundo --"));
                        fundosArrayListPersonalizado.add(new E_Fundos("PLANTA SANTIAGO"));
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

                adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeGarita.this, fundosArrayListPersonalizado);
                spFundo.setAdapter(adaptadorFundosPersonalizado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spFundo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                E_Fundos e_fundos = fundosArrayListPersonalizado.get(position);
                fundo = e_fundos.getNombre();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void validarAccion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("SOLICITUD");
        builder.setMessage("¿Qué acción vas a realizar?");
        builder.setCancelable(false);
        builder.setPositiveButton("REGISTRAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("VER REGISTROS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PrimerNivelWelcomeGarita.this, TercerNivelListarRegistrosGarita.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.create().show();
    }

    public void cargarZonas(){
        zonasArrayListPersonalizado.add(new E_Zonas("-- Selecciona una Zona --"));
        fundosArrayListPersonalizado.add(new E_Fundos( "-- Selecciona un Fundo --"));

        zonasArrayListPersonalizado.add(new E_Zonas( "CARAZ"));
        zonasArrayListPersonalizado.add(new E_Zonas( "ICA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "PISCO"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NEPEÑA"));
        zonasArrayListPersonalizado.add(new E_Zonas( "NAZCA"));

        adaptadorZonasPersonalizado = new AdaptadorZonas(PrimerNivelWelcomeGarita.this, zonasArrayListPersonalizado);
        adaptadorFundosPersonalizado = new AdaptadorFundos(PrimerNivelWelcomeGarita.this, fundosArrayListPersonalizado);

        spZona.setAdapter(adaptadorZonasPersonalizado);
        spFundo.setAdapter(adaptadorFundosPersonalizado);
    }

    public void enviarDatos(int valor, String tipo){
        Intent intent = new Intent(PrimerNivelWelcomeGarita.this, SegundoNivelRegistrarPersonal.class);

        Bundle bundle = new Bundle();
        bundle.putString("zona", zona);
        bundle.putString("fundo", fundo);
        bundle.putInt("valor", valor);
        bundle.putString("tipo", tipo);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PrimerNivelWelcomeGarita.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}