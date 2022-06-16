package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.agroathos.ENTIDADES.E_Cultivos;
import com.example.agroathos.ENTIDADES.E_Lotes;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorLotes extends ArrayAdapter<E_Lotes> {

    public AdaptadorLotes(@NonNull Context context, ArrayList<E_Lotes> lotesArrayList) {
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

        E_Lotes e_lotes = getItem(position);

        tvCultivo.setText(e_lotes.getNombre());

        return convertView;
    }

    private View viewInicial(int position, View convertView, ViewGroup parent){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_inicial_datos_rrhhtareoar, null);

        E_Lotes e_lotes = getItem(position);

        TextView tvCultivoInicial = convertView.findViewById(R.id.tvNombreDatosInicialRRHH_TAREO_AR);
        tvCultivoInicial.setText(e_lotes.getNombre());

        return convertView;
    }
}
