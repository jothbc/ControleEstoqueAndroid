package jcr.br.controleestoque.Bean;

import java.io.Serializable;

public class Produto implements Serializable {
    public String codigo;
    public String descricao;
    public double quantidade;
    public String tabela;

    public Produto() {
        this.codigo = "0";
    }
}
