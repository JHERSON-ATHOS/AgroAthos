package com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_GruposPlanta;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.TercerNivelConfiguracionGrupo;
import com.example.agroathos.RRHH_TAREO_AR_PLANTA.TercerNivelWelcomeTareoPlantaEdicion;

import java.util.ArrayList;

public class AdaptadorListarGrupoTrabajoPlanta extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<E_GruposPlanta> datos;

    ArrayList<String> arrayListId = new ArrayList<>();
    ArrayList<String> arrayListIdGrupo = new ArrayList<>();
    ArrayList<String> arrayListProceso = new ArrayList<>();
    ArrayList<String> arrayListActividad = new ArrayList<>();
    ArrayList<String> arrayListLabor = new ArrayList<>();
    ArrayList<String> arrayListMesa = new ArrayList<>();

    public AdaptadorListarGrupoTrabajoPlanta(Context context, ArrayList<E_GruposPlanta> datos) {
        this.context = context;
        this.datos = datos;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datos.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        E_GruposPlanta e_grupos = datos.get(position);

        final View vista = inflater.inflate(R.layout.content_rrhh_tareo_arandano_listar_grupo_trabajo_planta, null);
        TextView tvid = vista.findViewById(R.id.tvIdCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        TextView tvidgrupo = vista.findViewById(R.id.tvIdGrupoCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        TextView tvproceso = vista.findViewById(R.id.tvProcesoCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        TextView tvactividad = vista.findViewById(R.id.tvActividadCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        TextView tvlabor = vista.findViewById(R.id.tvLaborCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        TextView tvmesa = vista.findViewById(R.id.tvMesaCONTENT_RRHH_TAREO_ARANDANO_PLANTA);
        Button btnEditar = vista.findViewById(R.id.btnEditarGrupoCONTENT_RRHH_TAREO_ARANDANO_PLANTA);

        obtenerDatos(e_grupos.getId_grupo());
        for (int i=0; i<arrayListId.size(); i++){
            tvid.setText("PROCESO ".concat(String.valueOf(i+1)));
            tvidgrupo.setText(arrayListIdGrupo.get(i));
            tvproceso.setText(arrayListProceso.get(i));
            tvactividad.setText(arrayListActividad.get(i));
            tvlabor.setText(arrayListLabor.get(i));
            tvmesa.setText(arrayListMesa.get(i));
        }

        if (e_grupos.getEstado().equals("1")){
            btnEditar.setEnabled(false);
        }else{
            btnEditar.setOnClickListener(view->{
                Intent intent = new Intent(vista.getContext(), TercerNivelWelcomeTareoPlantaEdicion.class);
                Bundle bundle= new Bundle();
                bundle.putString("idGrupo", e_grupos.getId_grupo());
                bundle.putString("idAnexoNivel1", e_grupos.getAnexoNivel1());
                intent.putExtras(bundle);
                vista.getContext().startActivity(intent);
            });
        }

        return vista;
    }

    private void obtenerDatos(String grupo){
        ConexionSQLiteHelper conn;
        conn = new ConexionSQLiteHelper(context.getApplicationContext(), "athos0",null, Utilidades.VERSION_APP);
        SQLiteDatabase database = conn.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_TAREO_PLANTA_NIVEL3 + " WHERE "
                + Utilidades.CAMPO_ANEXO_GRUPO_TAREO_PLANTA_NIVEL3 + "=" + "'"+grupo+"' ", null);
        if (cursor != null){
            while (cursor.moveToNext()) {
                arrayListId.add(cursor.getString(0));
                arrayListIdGrupo.add(cursor.getString(1));
                arrayListProceso.add(cursor.getString(2));
                arrayListActividad.add(cursor.getString(3));
                arrayListLabor.add(cursor.getString(4));
                arrayListMesa.add(cursor.getString(5));
            }
            cursor.close();
        }
    }
}
