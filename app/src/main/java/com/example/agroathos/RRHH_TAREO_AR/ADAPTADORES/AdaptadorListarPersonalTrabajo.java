package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agroathos.ENTIDADES.E_Cultivos;
import com.example.agroathos.ENTIDADES.E_Grupos;
import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.TercerNivelAgregarPersonal;

import java.util.ArrayList;

public class AdaptadorListarPersonalTrabajo extends ArrayAdapter<E_PersonalTrabajo> {

    public AdaptadorListarPersonalTrabajo(@NonNull Context context, ArrayList<E_PersonalTrabajo> personalTrabajoArrayList) {
        super(context, 0, personalTrabajoArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return view(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (position == 0){
            return viewInicial(position, convertView, parent);
        }

        return view(position, convertView, parent);
    }

    private View view(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_rrhh_tareo_arandano_listar_personal_grupo, parent, false);

        TextView tvId = convertView.findViewById(R.id.tvIdListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvPersonal = convertView.findViewById(R.id.tvPersonalListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvJarras = convertView.findViewById(R.id.tvJarrasListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvHinicio = convertView.findViewById(R.id.tvHinicioListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvHfinal = convertView.findViewById(R.id.tvHfinalListadoCONTENT_RRHH_TAREO_ARANDANO);

        E_PersonalTrabajo e_personalTrabajo = getItem(position);

        tvId.setText(e_personalTrabajo.getId());
        tvPersonal.setText(e_personalTrabajo.getNombre());
        tvJarras.setText(e_personalTrabajo.getJarras());
        tvHinicio.setText(e_personalTrabajo.getHora_inicio());
        tvHfinal.setText(e_personalTrabajo.getHora_final());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_rrhh_tareo_arandano_listar_personal_grupo, null);

        E_PersonalTrabajo e_personalTrabajo = getItem(position);

        TextView tvId = convertView.findViewById(R.id.tvIdListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvPersonal = convertView.findViewById(R.id.tvPersonalListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvJarras = convertView.findViewById(R.id.tvJarrasListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvHinicio = convertView.findViewById(R.id.tvHinicioListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvHfinal = convertView.findViewById(R.id.tvHfinalListadoCONTENT_RRHH_TAREO_ARANDANO);

        tvId.setText(e_personalTrabajo.getId());
        tvPersonal.setText(e_personalTrabajo.getNombre());
        tvJarras.setText(e_personalTrabajo.getJarras());
        tvHinicio.setText(e_personalTrabajo.getHora_inicio());
        tvHfinal.setText(e_personalTrabajo.getHora_final());

        return convertView;
    }
}
