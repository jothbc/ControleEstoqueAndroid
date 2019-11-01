package jcr.br.controleestoque;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jcr.br.controleestoque.Bean.Produto;

public class ListLancadosActivity extends AppCompatActivity {

    private RecyclerView lstProdutos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_lancados);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstProdutos = findViewById(R.id.lstProdutos);
        lstProdutos.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        lstProdutos.setLayoutManager(linearLayoutManager);
        lstProdutos.setAdapter(new ProdutoAdapter());
    }

}
