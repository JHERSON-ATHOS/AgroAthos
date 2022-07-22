package com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorListarPersonalTrabajoPlanta extends ArrayAdapter<E_PersonalTrabajo> {

    public AdaptadorListarPersonalTrabajoPlanta(@NonNull Context context, ArrayList<E_PersonalTrabajo> personalTrabajoArrayList) {
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

        E_PersonalTrabajo e_personalTrabajo = getItem(position);

        tvId.setText(e_personalTrabajo.getId());
        tvPersonal.setText(e_personalTrabajo.getNombre());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.content_rrhh_tareo_arandano_listar_personal_grupo, null);

        E_PersonalTrabajo e_personalTrabajo = getItem(position);

        TextView tvId = convertView.findViewById(R.id.tvIdListadoCONTENT_RRHH_TAREO_ARANDANO);
        TextView tvPersonal = convertView.findViewById(R.id.tvPersonalListadoCONTENT_RRHH_TAREO_ARANDANO);

        tvId.setText(e_personalTrabajo.getId());
        tvPersonal.setText(e_personalTrabajo.getNombre());

        return convertView;
    }
}
