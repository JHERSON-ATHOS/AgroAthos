package com.example.agroathos;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.RRHH_DESTAJO_AR.PrimerNivelWelcomeDestajo;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.RRHH_TAREO_AR.SegundoNivelWelcome;
import com.example.agroathos.TRANSPORTE_GARITA.PrimerNivelWelcomeGarita;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnGarita, btnTareo, btnDestajo;
    ConstraintLayout layout;
    ArrayList<String> usuariosList = new ArrayList<>();

    SharedPreferences preferences;
    String dni_login = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedirPermisos();
        preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);

        layout = findViewById(R.id.clMainActivity);
        btnGarita = findViewById(R.id.btnLauncherGarita);
        btnTareo = findViewById(R.id.btnLauncherTareo);
        btnDestajo = findViewById(R.id.btnLauncherDestajos);

        btnGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnDestajo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));

        if (preferences.getString("dni", "").isEmpty()){
            solicitarAcceso();
            btnGarita.setEnabled(false);
            btnTareo.setEnabled(false);
            btnDestajo.setEnabled(false);
        }else{
            btnGarita.setEnabled(true);
            btnTareo.setEnabled(true);
            btnDestajo.setEnabled(true);
        }

    }

    public void solicitarAcceso(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ACCESO DE PERSONAL ATHOS");
        builder.setCancelable(false);

        final View view = getLayoutInflater().inflate(R.layout.content_login, null, false);
        EditText etUser = view.findViewById(R.id.etUserLoginMAINACTIVITY);

        builder.setPositiveButton("VALIDAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                dni_login = etUser.getText().toString().trim();
                validarAcceso(dni_login);
            }
        });

        builder.setView(view);
        builder.create().show();
    }

    private void validarAcceso(String username){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://agroathos.com/api/login", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("users");

                    for (int i=0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        usuariosList.add(data.getString("dni"));
                        usuariosList.add(data.getString("usuario"));

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("dni", dni_login);
                        editor.commit();

                    }

                    if (usuariosList.contains(username)){
                        Toast.makeText(MainActivity.this, "Bienvenido: "+usuariosList.get(1), Toast.LENGTH_SHORT).show();
                    }else{
                        solicitarAcceso();
                    }

                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "ERROR: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjectRequest);
    }

    private void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    public void iniciarActividad(Class aClass){
        Intent intent = new Intent(MainActivity.this, aClass);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}