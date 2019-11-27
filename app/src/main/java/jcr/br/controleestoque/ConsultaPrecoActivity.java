package jcr.br.controleestoque;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import jcr.br.controleestoque.Bean.Produto;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ConsultaPrecoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_preco);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getString(R.string.title_consulta_preco));

        FloatingActionButton cameraScan = findViewById(R.id.btn_consulta_preco);
        cameraScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               iniciarScan(view);
            }
        });
    }


    private void iniciarScan(View view) {
        EditText codigo = findViewById(R.id.editCodigoConsulta);
        if(!codigo.getText().toString().trim().isEmpty()){
            getPreco(codigo.getText().toString().trim());
            return;
        }
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Camera Scan");
        integrator.setCameraId(0);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
               getPreco(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getPreco(String codigo){
        TextView descricao = findViewById(R.id.editDescricao);
        TextView valor = findViewById(R.id.editValor);
        try {
            HTTPService service = new HTTPService("Produto/get/preco/", codigo);
            String request = service.execute().get();
            Produto produto = new Gson().fromJson(request,Produto.class);
            descricao.setText(produto.descricao);
            valor.setText(new DecimalFormat("0.00").format(produto.venda));
        }catch (Exception e){
            descricao.setText("");
            valor.setText("");
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle((R.string.title_erro));
            dialog.setMessage(getString(R.string.message_produto_null));
            dialog.show();
        }
    }
}
