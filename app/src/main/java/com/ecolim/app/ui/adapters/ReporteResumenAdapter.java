package com.ecolim.app.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecolim.app.R;
import com.ecolim.app.data.local.entities.ResumenTipoResiduo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReporteResumenAdapter extends RecyclerView.Adapter<ReporteResumenAdapter.ViewHolder> {

    private List<ResumenTipoResiduo> items = new ArrayList<>();

    public void setItems(List<ResumenTipoResiduo> nuevosItems) {
        this.items = nuevosItems != null ? nuevosItems : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<ResumenTipoResiduo> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reporte_resumen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResumenTipoResiduo item = items.get(position);
        holder.tvNombreResiduo.setText(item.getNombreResiduo());
        holder.tvCantidadKg.setText(String.format(Locale.getDefault(), "%.0f kg", item.getTotalKg()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreResiduo, tvCantidadKg;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreResiduo = itemView.findViewById(R.id.tvNombreResiduo);
            tvCantidadKg = itemView.findViewById(R.id.tvCantidadKg);
        }
    }
}
