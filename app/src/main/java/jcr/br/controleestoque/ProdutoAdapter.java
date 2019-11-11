package jcr.br.controleestoque;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jcr.br.controleestoque.Bean.Produto;

public class ProdutoAdapter extends RecyclerView.Adapter<ProdutoAdapter.ViewHolderProduto> {

    public List<Produto> produtos;

    public ProdutoAdapter() {
        this.produtos = BaixaActivity.lista_produtos;
    }

    @NonNull
    @Override
    public ProdutoAdapter.ViewHolderProduto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.lista_lancados, parent, false);
        ViewHolderProduto holderProduto = new ViewHolderProduto(view);

        return holderProduto;
    }

    @Override
    public void onBindViewHolder(@NonNull ProdutoAdapter.ViewHolderProduto holder, int position) {
        if (!produtos.isEmpty()) {
            Produto produto = produtos.get(position);
            holder.codigo.setText("Código: " + produto.codigo);
            holder.descricao.setText("Desc: " + produto.descricao);
            holder.quantidade.setText("QTD: " + Double.toString(produto.quantidade));
        }
    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ViewHolderProduto extends RecyclerView.ViewHolder {

        public TextView codigo, descricao, quantidade;

        public ViewHolderProduto(@NonNull View itemView) {
            super(itemView);
            codigo = itemView.findViewById(R.id.txt_codigo_lista);
            codigo = itemView.findViewById(R.id.txt_descricao_lista);
            codigo = itemView.findViewById(R.id.txt_quantidade_lista);
        }
    }
}
