package com.example.gerenciadoros;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrdemServico implements Serializable {
    private String cliente;
    private String descricao;
    private Long dataTimestamp;
    private String status;


    public OrdemServico(String cliente, String descricao, Long dataTimestamp, String status) {
        this.cliente = cliente;
        this.descricao = descricao;
        this.dataTimestamp = dataTimestamp;
        this.status = status;
    }


    public String getCliente() { return cliente; }
    public String getDescricao() { return descricao; }
    public Long getDataTimestamp() { return dataTimestamp; }
    public String getStatus() { return status; }

    public String getDataFormatada() {
        if (dataTimestamp == null) {
            return "Data não definida";
        }
        try {
            Date dateObj = new Date(dataTimestamp);

            SimpleDateFormat formatter = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("pt", "BR"));
            return formatter.format(dateObj);
        } catch (Exception e) {

            return "Erro ao formatar data";
        }
    }


    public String getDataFormatadaCurta() {
        if (dataTimestamp == null) {
            return "-";
        }
        try {
            Date dateObj = new Date(dataTimestamp);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formatter.format(dateObj);
        } catch (Exception e) {
            return "Erro";
        }
    }


    @Override
    public String toString() {
        return "Cliente: " + cliente + "\nServiço: " + descricao + "\nData: " + getDataFormatadaCurta() + " (" + status + ")";
    }
}
