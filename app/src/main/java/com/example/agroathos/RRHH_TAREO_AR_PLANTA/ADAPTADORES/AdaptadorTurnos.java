package com.example.agroathos.RRHH_TAREO_AR_PLANTA.ADAPTADORES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agroathos.ENTIDADES.E_Naves;
import com.example.agroathos.ENTIDADES.E_Turnos;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorTurnos extends ArrayAdapter<E_Turnos> {

    public AdaptadorTurnos(@NonNull Context context, ArrayList<E_Turnos> lotesArrayList) {
        super(context, 0, lotesArrayList);
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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_datos_rrhhtareoar, parent, false);

        TextView tvCultivo = convertView.findViewById(R.id.tvNombreDatoRRHH_TAREO_AR);

        E_Turnos e_turnos = getItem(position);

        tvCultivo.setText(e_turnos.getNombre());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_inicial_datos_rrhhtareoar, null);

        E_Turnos e_turnos = getItem(position);

        TextView tvCultivoInicial = convertView.findViewById(R.id.tvNombreDatosInicialRRHH_TAREO_AR);
        tvCultivoInicial.setText(e_turnos.getNombre());

        return convertView;
    }
}
