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
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroathos.RRHH_DESTAJO_AR.PrimerNivelWelcomeDestajo;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.TRANSPORTE_GARITA.PrimerNivelWelcomeGarita;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnGarita, btnTareo, btnDestajo;
    Button btnGaritaUsuarioGarita, btnTareoUsuarioAuxiliar, btnDestajoUsuarioProductividad;
    TextView tvUsuario;
    ConstraintLayout layout;
    LinearLayout layoutAdministrador, layoutGarita, layoutAuxiliar, layoutProductividad;

    SharedPreferences preferences;
    String dni_login = "";

    String dni_access = "";
    String usuario_access = "";
    String tipo_usuario_access = "";

    private int currentProgress = 0;

    String dni_preferences = "";
    String usuario_preferences = "";
    String tipo_usuario_preferences = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedirPermisos();
        preferences = getSharedPreferences("Acceso", Context.MODE_PRIVATE);
        dni_preferences = preferences.getString("dni", "");
        usuario_preferences = preferences.getString("usuario", "");
        tipo_usuario_preferences = preferences.getString("tipo_usuario", "");

        layout = findViewById(R.id.clMainActivity);
        layoutAdministrador = findViewById(R.id.llVistaAdminMAINACTIVITY);
        layoutGarita = findViewById(R.id.llVistaGaritaMAINACTIVITY);
        layoutAuxiliar = findViewById(R.id.llVistaAuxiliarMAINACTIVITY);
        layoutProductividad = findViewById(R.id.llVistaProductividadMAINACTIVITY);
        tvUsuario = findViewById(R.id.tvNombreUsuarioLogueadoACTIVITYMAIN);
        btnGarita = findViewById(R.id.btnLauncherGarita);
        btnTareo = findViewById(R.id.btnLauncherTareo);
        btnDestajo = findViewById(R.id.btnLauncherDestajos);
        btnGaritaUsuarioGarita = findViewById(R.id.btnLauncherGaritaUsuarioGaritaMAINACTIVITY);
        btnTareoUsuarioAuxiliar = findViewById(R.id.btnLauncherTareoUsuarioAxuliarMAINACTIVITY);
        btnDestajoUsuarioProductividad = findViewById(R.id.btnLauncherDestajosUsuarioProductividadMAINACTIVITY);

        btnGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnDestajo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));
        btnGaritaUsuarioGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareoUsuarioAuxiliar.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnDestajoUsuarioProductividad.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));

        if (dni_preferences.isEmpty()){
            solicitarAcceso();
        }else{
            validarAccesoSecundario();

            /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);

            final View view = getLayoutInflater().inflate(R.layout.load_transporte_garita_clonacion_grupos, null, false);
            ProgressBar progressBar = view.findViewById(R.id.progressBarClonandoGrupoTRANSPORTE_GARITA);
            TextView tvTitulo = view.findViewById(R.id.tvTituloProgressBar);

            tvTitulo.setText("Estamos configurando tu entorno");

            builder.setView(view).create();
            AlertDialog dialog = builder.show();

            Timer timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    currentProgress = currentProgress + 10;
                    progressBar.setProgress(currentProgress);
                    progressBar.setMax(100);

                    if (currentProgress == 100){
                        timer.cancel();
                        dialog.dismiss();
                    }

                }
            };
            timer.schedule(timerTask, 2000, 100);*/
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

    private void validarAcceso(String dni){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://agroathos.com/api/login/"+dni, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("data_unitaria");
                    dni_access = object.getString("dni");
                    usuario_access = object.getString("usuario");
                    tipo_usuario_access = object.getString("tipo_usuario_id");

                    if (!TextUtils.isEmpty(dni_access)) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("dni", dni_access);
                        editor.putString("usuario", usuario_access);
                        editor.putString("tipo_usuario", tipo_usuario_access);
                        editor.commit();

                        tvUsuario.setVisibility(View.VISIBLE);
                        tvUsuario.setText("BIENVENID@\n"+usuario_access);

                        switch (tipo_usuario_access){
                            case "1":
                                layoutAdministrador.setVisibility(View.VISIBLE);
                                break;
                            case "2":
                                layoutGarita.setVisibility(View.VISIBLE);
                                break;
                            case "3":
                                layoutAuxiliar.setVisibility(View.VISIBLE);
                                break;
                            case "4":
                                layoutProductividad.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                    //LEIDA DE DATOS MASIVOS
                    //JSONArray jsonArray = response.getJSONArray("data_unitaria");
                    /*for (int i=0; i<jsonArray.length(); i++){
                        JSONObject data = jsonArray.getJSONObject(i);
                        usuariosList.add(data.getString("dni"));
                        usuariosList.add(data.getString("usuario"));
                        usuariosList.add(data.getString("tipo_usuario"));
                    }

                    if (usuariosList.contains(dni)){

                        Toast.makeText(MainActivity.this, "BIENVENIDO: "+usuariosList.get(1), Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("dni", dni_login);
                        editor.commit();

                        if (usuariosList.contains("ADMINISTRADOR")){
                            btnGarita.setVisibility(View.VISIBLE);
                            btnTareo.setVisibility(View.VISIBLE);
                            btnDestajo.setVisibility(View.VISIBLE);
                        }
                    }else{
                        solicitarAcceso();
                    }*/
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Error: No existe usuario", Toast.LENGTH_SHORT).show();
                    solicitarAcceso();
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

    private void validarAccesoSecundario(){

        if (dni_preferences != ""){

            tvUsuario.setVisibility(View.VISIBLE);
            tvUsuario.setText("BIENVENID@\n"+usuario_preferences);

            switch (tipo_usuario_preferences){
                case "1":
                    layoutAdministrador.setVisibility(View.VISIBLE);
                    break;
                case "2":
                    layoutGarita.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    layoutAuxiliar.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    layoutProductividad.setVisibility(View.VISIBLE);
                    break;
            }
        }
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