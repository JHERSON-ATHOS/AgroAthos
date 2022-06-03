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

import com.example.agroathos.ENTIDADES.E_Cultivos;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorCultivos extends ArrayAdapter<E_Cultivos> {

    public AdaptadorCultivos(@NonNull Context context, ArrayList<E_Cultivos> cultivosArrayList) {
        super(context, 0, cultivosArrayList);
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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_cultivos_rrhhtareoar, parent, false);

        TextView tvCultivo = convertView.findViewById(R.id.tvNombreCultivoRRHH_TAREO_AR);

        E_Cultivos e_cultivos = getItem(position);

        tvCultivo.setText(e_cultivos.getNombre());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_inicial_cultivos_rrhhtareoar, null);

        E_Cultivos e_cultivos = getItem(position);

        TextView tvCultivoInicial = convertView.findViewById(R.id.tvNombreCultivoInicialRRHH_TAREO_AR);
        tvCultivoInicial.setText(e_cultivos.getNombre());

        return convertView;
    }
}
