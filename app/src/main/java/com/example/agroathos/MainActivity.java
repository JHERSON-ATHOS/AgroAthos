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
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
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
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.PrimerNivelWelcomeTareoPlanta;
import com.example.agroathos.TRANSPORTE_GARITA.PrimerNivelWelcomeGarita;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btnGarita, btnTareo, btnDestajo, btnPlanta;
    Button btnGaritaUsuarioGarita, btnTareoUsuarioAuxiliar, btnDestajoUsuarioProductividad, btnPlantaUsuarioPlanta;
    TextView tvUsuario, tvVersion;
    ConstraintLayout layout;
    LinearLayout layoutAdministrador, layoutGarita, layoutAuxiliar, layoutProductividad, layoutPlanta;

    SharedPreferences preferences;
    String dni_login = "";

    String dni_access = "";
    String usuario_access = "";
    String tipo_usuario_access = "";

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
        layoutPlanta = findViewById(R.id.llVistaPlantaMAINACTIVITY);
        tvUsuario = findViewById(R.id.tvNombreUsuarioLogueadoACTIVITYMAIN);
        tvVersion = findViewById(R.id.tvVersion);
        btnGarita = findViewById(R.id.btnLauncherGarita);
        btnTareo = findViewById(R.id.btnLauncherTareo);
        btnPlanta = findViewById(R.id.btnLauncherPlanta);
        btnDestajo = findViewById(R.id.btnLauncherDestajos);
        btnGaritaUsuarioGarita = findViewById(R.id.btnLauncherGaritaUsuarioGaritaMAINACTIVITY);
        btnTareoUsuarioAuxiliar = findViewById(R.id.btnLauncherTareoUsuarioAxuliarMAINACTIVITY);
        btnPlantaUsuarioPlanta = findViewById(R.id.btnLauncherPlantaUsuarioPlantaMAINACTIVITY);
        btnDestajoUsuarioProductividad = findViewById(R.id.btnLauncherDestajosUsuarioProductividadMAINACTIVITY);

        btnGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnPlanta.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareoPlanta.class));
        btnDestajo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));
        btnGaritaUsuarioGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareoUsuarioAuxiliar.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnPlantaUsuarioPlanta.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareoPlanta.class));
        btnDestajoUsuarioProductividad.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));

        obtenerVersionApp();

        if (dni_preferences.isEmpty()){
            solicitarAcceso();
        }else{
            validarAccesoSecundario();
        }

        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            200);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public String obtenerVersionApp(){
        try {
            PackageInfo paquete = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionDeAplicacion = paquete.versionName;

            tvVersion.setText("V ".concat(versionDeAplicacion));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://agroathos.com/api/login/"+dni,
                null, new Response.Listener<JSONObject>() {
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
                case "5":
                    layoutPlanta.setVisibility(View.VISIBLE);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Necesitas actualizar la versión", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}