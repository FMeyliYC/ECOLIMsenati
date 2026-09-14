package com.ecolim.app.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolim.app.R;
import com.ecolim.app.data.local.entities.RecoleccionConDetalle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecoleccionAdapter extends RecyclerView.Adapter<RecoleccionAdapter.ViewHolder> {

    private List<RecoleccionConDetalle> lista = new ArrayList<>();

    public void setRecolecciones(List<RecoleccionConDetalle> nuevasRecolecciones) {
        this.lista = nuevasRecolecciones != null ? nuevasRecolecciones : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recoleccion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecoleccionConDetalle item = lista.get(position);
        Context context = holder.itemView.getContext();

        holder.tvResiduo.setText(item.getNombreResiduo() != null ? item.getNombreResiduo() : "Residuo");
        holder.tvCantidad.setText(String.format(Locale.getDefault(), "%.1f kg", item.getCantidadKg()));
        holder.tvZona.setText(item.getNombreZona() != null ? item.getNombreZona() : "Zona general");
        holder.tvFecha.setText(item.getFechaHora());

        if (item.isSincronizado()) {
            holder.ivSyncIcon.setImageResource(R.drawable.ic_check);
            holder.tvSyncStatus.setText(R.string.estado_sincronizado);
            holder.tvSyncStatus.setTextColor(ContextCompat.getColor(context, R.color.status_synced));
        } else {
            holder.ivSyncIcon.setImageResource(R.drawable.ic_hourglass);
            holder.tvSyncStatus.setText(R.string.estado_pendiente);
            holder.tvSyncStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending));
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvResiduo, tvCantidad, tvZona, tvFecha, tvSyncStatus;
        ImageView ivSyncIcon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResiduo = itemView.findViewById(R.id.tvItemResiduo);
            tvCantidad = itemView.findViewById(R.id.tvItemCantidad);
            tvZona = itemView.findViewById(R.id.tvItemZona);
            tvFecha = itemView.findViewById(R.id.tvItemFecha);
            tvSyncStatus = itemView.findViewById(R.id.tvItemSyncStatus);
            ivSyncIcon = itemView.findViewById(R.id.ivItemSyncIcon);
        }
    }
}
