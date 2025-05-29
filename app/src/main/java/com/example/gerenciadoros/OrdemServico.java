package com.example.gerenciadoros;

import java.io.Serializable;


public class OrdemServico implements Serializable {
    private String cliente;
    private String descricao;
    private String data;
    private String status;

    public OrdemServico(String cliente, String descricao, String data, String status) {
        this.cliente = cliente;
        this.descricao = descricao;
        this.data = data;
        this.status = status;
    }

    public String getCliente() { return cliente; }
    public String getDescricao() { return descricao; }
    public String getData() { return data; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return cliente + " - " + descricao + " (" + status + ")";
    }
}
