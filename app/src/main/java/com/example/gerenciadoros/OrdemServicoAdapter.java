package com.example.gerenciadoros;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.OrdemViewHolder> {

    private List<OrdemServico> ordens;
    private Context context;
    private OnItemClickListener listener;
    private boolean isSelectionMode = false;
    private Set<Integer> selectedItems = new HashSet<>(); // Store positions of selected items

    public interface OnItemClickListener {
        void onItemClick(OrdemServico ordem);
        void onItemLongClick(int position);
    }

    public OrdemServicoAdapter(Context context, List<OrdemServico> ordens, OnItemClickListener listener) {
        this.context = context;
        // Create a new list to avoid modifying the original list directly if needed elsewhere
        this.ordens = new ArrayList<>(ordens);
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ordem, parent, false);
        return new OrdemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdemViewHolder holder, int position) {
        OrdemServico ordem = ordens.get(position);
        holder.bind(ordem, position);
    }

    @Override
    public int getItemCount() {
        return ordens.size();
    }

    public void updateData(List<OrdemServico> newOrdens) {
        this.ordens.clear();
        if (newOrdens != null) {
            this.ordens.addAll(newOrdens);
        }
        notifyDataSetChanged();
        // Clear selection when data is updated
        exitSelectionMode();
    }

    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!isSelectionMode) {
            selectedItems.clear();
        }
        notifyDataSetChanged(); // Redraw items to show/hide checkboxes
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<OrdemServico> getSelectedOrdens() {
        List<OrdemServico> selectedOrdensList = new ArrayList<>();
        for (int position : selectedItems) {
            if (position >= 0 && position < ordens.size()) {
                selectedOrdensList.add(ordens.get(position));
            }
        }
        return selectedOrdensList;
    }

    public Set<Integer> getSelectedItemPositions() {
        // Return a copy to prevent external modification
        return new HashSet<>(selectedItems);
    }

    public void exitSelectionMode() {
        isSelectionMode = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }

    class OrdemViewHolder extends RecyclerView.ViewHolder {
        TextView txtCliente, txtDescricao, txtData, txtStatus;
        CheckBox chkSelecionar;

        OrdemViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCliente = itemView.findViewById(R.id.txtItemCliente);
            txtDescricao = itemView.findViewById(R.id.txtItemDescricao);
            txtData = itemView.findViewById(R.id.txtItemData);
            txtStatus = itemView.findViewById(R.id.txtItemStatus);
            chkSelecionar = itemView.findViewById(R.id.chkSelecionarOrdem);
        }

        void bind(final OrdemServico ordem, final int position) {
            txtCliente.setText(ordem.getCliente());
            txtDescricao.setText(ordem.getDescricao());
            txtData.setText(ordem.getDataFormatadaCurta());
            txtStatus.setText(ordem.getStatus());
            // TODO: Set status background based on status value

            if (isSelectionMode) {
                chkSelecionar.setVisibility(View.VISIBLE);
                chkSelecionar.setChecked(selectedItems.contains(position));

                // Handle clicks only on checkbox in selection mode
                chkSelecionar.setOnClickListener(v -> {
                    toggleSelection(position);
                    // Notify activity about selection change if needed (e.g., update counter)
                    if (listener != null) listener.onItemLongClick(position); // Reusing long click for selection updates
                });
                // Disable item click listener in selection mode to avoid conflict
                itemView.setOnClickListener(v -> chkSelecionar.performClick());
                itemView.setOnLongClickListener(null); // Disable long click in selection mode

            } else {
                chkSelecionar.setVisibility(View.GONE);
                chkSelecionar.setChecked(false);

                // Normal click listener
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(ordem);
                    }
                });
                // Long click listener to enter selection mode
                itemView.setOnLongClickListener(v -> {
                    if (listener != null) {
                        listener.onItemLongClick(position);
                        return true; // Consume the long click
                    }
                    return false;
                });
            }
        }
    }
}
