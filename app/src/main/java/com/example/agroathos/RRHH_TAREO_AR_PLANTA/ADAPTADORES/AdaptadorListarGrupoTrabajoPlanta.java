package com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_GruposPlanta;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.TercerNivelConfiguracionGrupo;

import java.util.ArrayList;

public class AdaptadorListarGrupoTrabajoPlanta extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    ArrayList<E_GruposPlanta> datos;

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

        final View vista = inflater.inflate(R.layout.content_rrhh_tareo_arandano_listar_grupo_trabajo, null);
        TextView tvid = vista.findViewById(R.id.tvIdCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvidgrupo = vista.findViewById(R.id.tvIdGrupoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvdni = vista.findViewById(R.id.tvDniCONTENT_RRHH_TAREO_ARANDANO);
        Button btnEditar = vista.findViewById(R.id.btnEditarGrupoCONTENT_RRHH_TAREO_ARANDANO);
        Button btnDuplicar = vista.findViewById(R.id.btnDuplicarGrupoCONTENT_RRHH_TAREO_ARANDANO);
        Button btnConfigurar = vista.findViewById(R.id.btnConfigurarGrupoCONTENT_RRHH_TAREO_ARANDANO);

        tvid.setText("GRUPO ".concat(e_grupos.getContadorGrupo()));
        tvidgrupo.setText(e_grupos.getId_grupo());
        tvdni.setText("SUPERVISOR: ".concat(e_grupos.getAnexo_supervisor()));

        if (e_grupos.getEstado().equals("1")){
            btnEditar.setOnClickListener(view->{
                Intent intent = new Intent(vista.getContext(), TercerNivelConfiguracionGrupo.class);
                Bundle bundle= new Bundle();
                bundle.putInt("validacion", 1);
                bundle.putInt("valor", 1);
                bundle.putString("idGrupo", e_grupos.getId_grupo());
                bundle.putString("dni", e_grupos.getAnexo_supervisor());
                intent.putExtras(bundle);
                vista.getContext().startActivity(intent);
            });
            btnConfigurar.setEnabled(false);
        }else{
            btnEditar.setOnClickListener(view->{
                Intent intent = new Intent(vista.getContext(), TercerNivelConfiguracionGrupo.class);
                Bundle bundle= new Bundle();
                bundle.putInt("valor", 1);
                bundle.putString("idGrupo", e_grupos.getId_grupo());
                bundle.putString("dni", e_grupos.getAnexo_supervisor());
                intent.putExtras(bundle);
                vista.getContext().startActivity(intent);
            });
            btnDuplicar.setOnClickListener(view -> {
                Intent intent = new Intent(vista.getContext(), TercerNivelConfiguracionGrupo.class);
                Bundle bundle= new Bundle();
                bundle.putInt("valor", 2);
                bundle.putString("idGrupo", e_grupos.getId_grupo());
                bundle.putString("dni", e_grupos.getAnexo_supervisor());
                intent.putExtras(bundle);
                vista.getContext().startActivity(intent);
            });
            btnConfigurar.setOnClickListener(view -> {
                Intent intent = new Intent(vista.getContext(), TercerNivelConfiguracionGrupo.class);
                Bundle bundle= new Bundle();
                bundle.putInt("valor", 3);
                bundle.putString("tvNombreGrupo", tvid.getText().toString());
                bundle.putString("idGrupo", e_grupos.getId_grupo());
                bundle.putString("dni", e_grupos.getAnexo_supervisor());
                intent.putExtras(bundle);
                vista.getContext().startActivity(intent);
            });
        }

        return vista;
    }
}
