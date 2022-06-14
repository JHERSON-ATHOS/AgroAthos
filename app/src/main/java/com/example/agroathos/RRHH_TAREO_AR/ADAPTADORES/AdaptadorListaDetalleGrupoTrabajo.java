package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroathos.ENTIDADES.E_DetalleGrupoTrabajo;
import com.example.agroathos.R;

import java.util.ArrayList;

public class AdaptadorListaDetalleGrupoTrabajo extends RecyclerView.Adapter<AdaptadorListaDetalleGrupoTrabajo.DetalleGrupoTrabajoViewHolder> {

    ArrayList<E_DetalleGrupoTrabajo> listaDetalleGrupo;

    public AdaptadorListaDetalleGrupoTrabajo(ArrayList<E_DetalleGrupoTrabajo> listaDetalleGrupo) {
        this.listaDetalleGrupo = listaDetalleGrupo;
    }

    @NonNull
    @Override
    public DetalleGrupoTrabajoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_rrhh_tareo_arandano_listar_detalle_grupo_trabajo, parent, false);
        return new DetalleGrupoTrabajoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleGrupoTrabajoViewHolder holder, int position) {
        holder.tvid.setText(listaDetalleGrupo.get(position).getId());
        holder.tvnombre.setText(listaDetalleGrupo.get(position).getPersonal());
        holder.tvjarra1.setText("1ra JARRA: ".concat(listaDetalleGrupo.get(position).getJarra_uno()));
        holder.tvjarra2.setText("2da JARRA: ".concat(listaDetalleGrupo.get(position).getJarra_dos()));
        holder.tvestado.setText(listaDetalleGrupo.get(position).getEstado());

        holder.btnEditar.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), listaDetalleGrupo.get(position).getPersonal(), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaDetalleGrupo.size();
    }

    public class DetalleGrupoTrabajoViewHolder extends RecyclerView.ViewHolder{

        TextView tvid, tvnombre, tvjarra1, tvjarra2, tvestado;
        Button btnEditar;

        public DetalleGrupoTrabajoViewHolder(@NonNull View itemView) {
            super(itemView);

            tvid = itemView.findViewById(R.id.tvIdPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            tvnombre = itemView.findViewById(R.id.tvDatosPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            tvjarra1 = itemView.findViewById(R.id.tvJarra1PersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            tvjarra2 = itemView.findViewById(R.id.tvJarra2PersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            tvestado = itemView.findViewById(R.id.tvEstadoPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            btnEditar = itemView.findViewById(R.id.btnEditarPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
        }
    }
}
