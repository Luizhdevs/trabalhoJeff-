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

// Define o adapter que conecta a lista de Ordens de Serviço com o RecyclerView.
public class OrdemServicoAdapter extends RecyclerView.Adapter<OrdemServicoAdapter.OrdemViewHolder> {
    //ordens: lista de ordens a serem exibidas.
    private List<OrdemServico> ordens;
    //contexto da aplicação.
    private Context context;
    // trata cliques no item.
    private OnItemClickListener listener;
    //indica se o modo de seleção múltipla está ativado.
    private boolean isSelectionMode = false;
    //guarda as posições selecionadas.
    private Set<Integer> selectedItems = new HashSet<>(); 

    // Interface que permite responder aos cliques e long clicks na ordem.
    public interface OnItemClickListener {
        void onItemClick(OrdemServico ordem);
        void onItemLongClick(int position);
    }
    //Construtor do adapter, recebe contexto, lista de ordens, listener para cliques 
    public OrdemServicoAdapter(Context context, List<OrdemServico> ordens, OnItemClickListener listener) {
        this.context = context;
        this.ordens = new ArrayList<>(ordens);
        this.listener = listener;
    }

    //Cria a visualização de cada item (usando o XML list_item_ordem.xml).
    @NonNull
    @Override
    public OrdemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_ordem, parent, false);
        return new OrdemViewHolder(view);
    }

    //Liga (bind) os dados da ordem ao visual da ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull OrdemViewHolder holder, int position) {
        OrdemServico ordem = ordens.get(position);
        holder.bind(ordem, position);
    }

    // Retorna o número total de ordens para o RecyclerView.
    @Override
    public int getItemCount() {
        return ordens.size();
    }

    // Atualiza a lista de ordens e recarrega a tela. Sai do modo seleção ao atualizar.
    public void updateData(List<OrdemServico> newOrdens) {
        this.ordens.clear();
        if (newOrdens != null) {
            this.ordens.addAll(newOrdens);
        }
        notifyDataSetChanged();
        exitSelectionMode();
    }

    //Ativa ou desativa o modo de seleção múltipla.
    public void setSelectionMode(boolean selectionMode) {
        isSelectionMode = selectionMode;
        if (!isSelectionMode) {
            selectedItems.clear();
        }
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return isSelectionMode;
    }
    //Marca ou desmarca um item quando o checkbox é clicado.
    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(position);
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    //Retorna o número de ordens selecionadas.
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    //Retorna uma lista com as ordens que estão selecionadas.
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
        return new HashSet<>(selectedItems);
    }

    //Sai do modo seleção e limpa os itens marcados.
    public void exitSelectionMode() {
        isSelectionMode = false;
        selectedItems.clear();
        notifyDataSetChanged();
    }
    //ViewHolder: classe interna que representa cada item da lista (cada card).
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
        //Preenche os campos com os dados da ordem de serviço.
        void bind(final OrdemServico ordem, final int position) {
            txtCliente.setText(ordem.getCliente());
            txtDescricao.setText(ordem.getDescricao());
            txtData.setText(ordem.getDataFormatadaCurta());
            txtStatus.setText(ordem.getStatus());
            
            //Se o modo seleção estiver ativo, mostra o checkbox e marca se estiver selecionado.
            if (isSelectionMode) {
                chkSelecionar.setVisibility(View.VISIBLE);
                chkSelecionar.setChecked(selectedItems.contains(position));

                
                chkSelecionar.setOnClickListener(v -> {
                    toggleSelection(position);
                    
                    if (listener != null) listener.onItemLongClick(position); 
                });

                // Clicar no card também aciona o clique do checkbox.
                itemView.setOnClickListener(v -> chkSelecionar.performClick());
                itemView.setOnLongClickListener(null); 

            } else {
                //Se o modo seleção estiver desativado, oculta e desmarca o checkbox.
                chkSelecionar.setVisibility(View.GONE);
                chkSelecionar.setChecked(false);

                //Se o modo seleção não estiver ativo, o clique abre os detalhes da ordem.
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onItemClick(ordem);
                    }
                });
                // Um clique longo ativa o modo seleção.
                itemView.setOnLongClickListener(v -> {
                    if (listener != null) {
                        listener.onItemLongClick(position);
                        return true; 
                    }
                    return false;
                });
            }
        }
    }
}
