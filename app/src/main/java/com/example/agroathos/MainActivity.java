package com.example.agroathos;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.Toast;
import com.example.agroathos.RRHH_DESTAJO_AR.PrimerNivelWelcomeDestajo;
import com.example.agroathos.RRHH_TAREO_AR.PrimerNivelWelcomeTareo;
import com.example.agroathos.TRANSPORTE_GARITA.PrimerNivelWelcomeGarita;
import com.example.agroathos.TRANSPORTE_GARITA.SegundoNivelRegistrarPersonal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {

    Button btnGarita, btnBus, btnTareo, btnDestajo;

    /*String url = "https://192.168.1.40:8001/personal";
    ArrayList<String> arrayList = new ArrayList<>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pedirPermisos();

        btnGarita = findViewById(R.id.btnLauncherGarita);
        btnTareo = findViewById(R.id.btnLauncherTareo);
        btnDestajo = findViewById(R.id.btnLauncherDestajos);

        btnGarita.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeGarita.class));
        btnTareo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeTareo.class));
        btnDestajo.setOnClickListener(view -> iniciarActividad(PrimerNivelWelcomeDestajo.class));

        //consultarBD();
    }

    private void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    public Connection conexionBD(){
        Connection cnn = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn = DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.41;databaseName=AthosMobile;user=sistemas;password=Athos021;");
        }catch (Exception e){
            Toast.makeText(this, "Error BD=== "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return cnn;
    }

    public void consultarBD(){
        try {
            Statement stm = conexionBD().createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM TEST");

            if (rs.next()){
                Toast.makeText(this, rs.getString(1), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No hay data", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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