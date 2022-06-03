package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agroathos.ENTIDADES.E_Zonas;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorZonas extends ArrayAdapter<E_Zonas> {

    public AdaptadorZonas(@NonNull Context context, ArrayList<E_Zonas> zonasArrayList) {
        super(context, 0, zonasArrayList);
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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_zonas_rrhhtareoar, parent, false);

        TextView tvCultivo = convertView.findViewById(R.id.tvNombreZonaRRHH_TAREO_AR);

        E_Zonas e_zonas = getItem(position);

        tvCultivo.setText(e_zonas.getNombre());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_inicial_zonas_rrhhtareoar, null);

        E_Zonas e_zonas = getItem(position);

        TextView tvCultivoInicial = convertView.findViewById(R.id.tvNombreZonaInicialRRHH_TAREO_AR);
        tvCultivoInicial.setText(e_zonas.getNombre());

        return convertView;
    }
}
