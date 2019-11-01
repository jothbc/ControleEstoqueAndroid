package jcr.br.controleestoque.Bean;

import java.io.Serializable;

public class Produto implements Serializable {
    public String codigo;
    public String descricao;
    public double quantidade;
    public String tabela;
    public String usuario;

    public Produto() {
        this.codigo = "0";
    }

    public Produto(String codigo, String descricao,double quantidade){
        this.codigo=codigo;
        this.descricao=descricao;
        this.quantidade=quantidade;
    }
}
