package com.example.agroathos.RRHH_TAREO_AR.ADAPTADORES;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroathos.BD_SQLITE.ConexionSQLiteHelper;
import com.example.agroathos.BD_SQLITE.UTILIDADES.Utilidades;
import com.example.agroathos.ENTIDADES.E_DetalleGrupoTrabajo;
import com.example.agroathos.R;
import com.example.agroathos.RRHH_TAREO_AR.TercerNivelConfiguracionGrupo;

import java.util.ArrayList;
import java.util.Calendar;

public class AdaptadorListaDetalleGrupoTrabajo extends RecyclerView.Adapter<AdaptadorListaDetalleGrupoTrabajo.DetalleGrupoTrabajoViewHolder> {

    Context context;
    ArrayList<E_DetalleGrupoTrabajo> listaDetalleGrupo;
    int hora = 0;
    int minutos = 0;

    public AdaptadorListaDetalleGrupoTrabajo(Context context, ArrayList<E_DetalleGrupoTrabajo> listaDetalleGrupo) {
        this.context = context;
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
        holder.tvestado.setText(listaDetalleGrupo.get(position).getEstado());

        holder.btnEditar.setOnClickListener(view -> {
            obtenerDatosPersonal(listaDetalleGrupo.get(position).getId(),listaDetalleGrupo.get(position).getPersonal());
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
            tvestado = itemView.findViewById(R.id.tvEstadoPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
            btnEditar = itemView.findViewById(R.id.btnEditarPersonalDetalleGrupoTrabajoRRHH_TAREO_AR);
        }
    }

    private void obtenerDatosPersonal(String id, String dni_personal){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(context.getApplicationContext(),"athos0",null,Utilidades.VERSION_APP);
        SQLiteDatabase database = conn.getReadableDatabase();
        ContentValues valuesEditarPersonal = new ContentValues();

        //arrayPersonalList = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Utilidades.TABLA_NIVEL2 + " WHERE " + Utilidades.CAMPO_PERSONAL_NIVEL2 + "=" + "'"+dni_personal+"'", null);

        if (cursor != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);

            LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());
            final View view = inflater.inflate(R.layout.content_rrhh_tareo_arandano_especificacion_personal, null, false);
            TextView tvHoraInicio = view.findViewById(R.id.tvHoraInicioEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            TextView tvHoraFinal = view.findViewById(R.id.tvHoraFinalEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            Button btnHoraInicio = view.findViewById(R.id.btnHoraInicioEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            Button btnHoraFinal = view.findViewById(R.id.btnHoraFinalEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbAbierto = view.findViewById(R.id.rbEstadoAbiertoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);
            RadioButton rbCerrado = view.findViewById(R.id.rbEstadoCerradoEditarRRHH_TAREO_ARANDANO_ESPECIFICACION_PERSONAL);

            btnHoraInicio.setOnClickListener(v ->{
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hora = hourOfDay;
                        minutos = minute;
                        tvHoraInicio.setText(hourOfDay+":"+minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hora, minutos);
                timePickerDialog.show();
            });
            btnHoraFinal.setOnClickListener(v ->{
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hora = hourOfDay;
                        minutos = minute;
                        tvHoraFinal.setText(hourOfDay+":"+minute);
                    }
                }, hora, minutos, false);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.updateTime(hora, minutos);
                timePickerDialog.show();
            });

            if (cursor.moveToFirst()){
                tvHoraInicio.setText(cursor.getString(9));
                tvHoraFinal.setText(cursor.getString(10));

                if (cursor.getString(11).equals("ABIERTO")){
                    rbAbierto.setChecked(true);
                }else{
                    rbCerrado.setChecked(true);
                }
            }

            builder.setView(view);

            builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (rbAbierto.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, tvHoraInicio.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, tvHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "ABIERTO");

                        String[] parametro = {id};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);
                        Toast.makeText(context.getApplicationContext(), "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    if (rbCerrado.isChecked()){
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_INICIO_NIVEL2, tvHoraInicio.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_HORA_FIN_NIVEL2, tvHoraFinal.getText().toString());
                        valuesEditarPersonal.put(Utilidades.CAMPO_ESTADO_NIVEL2, "CERRADO");

                        String[] parametro = {id};
                        database.update(Utilidades.TABLA_NIVEL2, valuesEditarPersonal, Utilidades.CAMPO_ID_NIVEL2+"=?", parametro);
                        Toast.makeText(context.getApplicationContext(), "Datos Actualizados!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });

            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
