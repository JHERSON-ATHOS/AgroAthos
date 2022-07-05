package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroathos.ENTIDADES.E_PersonalTrabajo;
import com.example.agroathos.R;

import java.util.ArrayList;

public class NO_USADO extends RecyclerView.Adapter<NO_USADO.ListaPersonalTrabajoViewHolder> {

    Context context;
    ArrayList<E_PersonalTrabajo> listaPersonalTrabajo;

    public NO_USADO(Context context, ArrayList<E_PersonalTrabajo> listaPersonalTrabajo) {
        this.context = context;
        this.listaPersonalTrabajo = listaPersonalTrabajo;
    }

    @NonNull
    @Override
    public ListaPersonalTrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rrhh_tareo_arandano_listar_personal_grupo, parent, false);
        return new ListaPersonalTrabajoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaPersonalTrabajoViewHolder holder, int position) {
        holder.tvId.setText(listaPersonalTrabajo.get(position).getId());
        holder.tvPersonal.setText(listaPersonalTrabajo.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return listaPersonalTrabajo.size();
    }

    public class ListaPersonalTrabajoViewHolder extends RecyclerView.ViewHolder{

        TextView tvId, tvPersonal, tvJarras;

        public ListaPersonalTrabajoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tvIdListadoCONTENT_RRHH_TAREO_ARANDANO);
            tvPersonal = itemView.findViewById(R.id.tvPersonalListadoCONTENT_RRHH_TAREO_ARANDANO);
        }
    }
}
