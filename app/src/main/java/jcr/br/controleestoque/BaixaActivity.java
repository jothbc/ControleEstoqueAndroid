package jcr.br.controleestoque;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import jcr.br.controleestoque.Bean.MyException;
import jcr.br.controleestoque.Bean.Produto;


public class BaixaActivity extends AppCompatActivity {

    private Produto produto;
    public static List<Produto> lista_produtos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baixa);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.setTitle(getString(R.string.title_usuario) + " " + MainActivity.login_string.toUpperCase());
        lista_produtos = new ArrayList<>();
        produto = new Produto();

        EditText codigo = findViewById(R.id.edtCodigo);
        codigo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    getQuantidade(v);
                }
            }
        });

        FloatingActionButton scan = findViewById(R.id.float_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarScan(view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_baixa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_lancados:
                Intent actLancados = new Intent(BaixaActivity.this, ListLancadosActivity.class);
                startActivity(actLancados);
                break;
            case R.id.action_consultar_preco:
                Intent actConsulta = new Intent(BaixaActivity.this, ConsultaPrecoActivity.class);
                startActivity(actConsulta);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void iniciarScan(View view) {
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
                EditText codigo = findViewById(R.id.edtCodigo);
                codigo.setText(result.getContents());
                getQuantidade(findViewById(R.id.layout_content_baixa));
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getQuantidade(View view) {
        EditText descricao = findViewById(R.id.edtDescricao);
        EditText quantidade_banco = findViewById(R.id.edtQuantidade);
        EditText quantidade = findViewById(R.id.edtQuantidade2);
        try {
            EditText codigo = findViewById(R.id.edtCodigo);
            if (codigo.getText().toString().trim().equals("")) {
                Snackbar.make(view, getString(R.string.message_informar_codigo), Snackbar.LENGTH_SHORT).show();
                return;
            }
            String caminho = "Produto/get/";
            String param = codigo.getText().toString();
            String request =  new HTTPService(caminho, param).execute().get();

            if (request!=null) {
                try {
                    produto = new Gson().fromJson(request, Produto.class);
                }catch (JsonSyntaxException e){
                    Toast.makeText(this,"ALGO DEU ERRADO",Toast.LENGTH_LONG).show();
                    return;
                }
                descricao.setText(produto.descricao);
                quantidade_banco.setText(String.valueOf(produto.quantidade));
                quantidade.setText("1");
            } else {
                descricao.setText("");
                quantidade_banco.setText("");
                quantidade.setText("");
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle((R.string.title_erro));
                dialog.setMessage(getString(R.string.message_produto_null)+"\n"+String.valueOf(MyException.code));
                dialog.show();
            }
        } catch (Exception e) {
            descricao.setText("");
            quantidade_banco.setText("");
            Toast.makeText(this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void lancarQuantidade(View view) {
        EditText descricao = findViewById(R.id.edtDescricao);
        EditText quantidade_banco = findViewById(R.id.edtQuantidade);
        EditText quantidade = findViewById(R.id.edtQuantidade2);
        EditText codigo = findViewById(R.id.edtCodigo);
        if (produto.codigo.equals("0") || quantidade.getText().toString().trim().isEmpty() || descricao.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), getString(R.string.message_faltam_campos), Toast.LENGTH_LONG).show();
            return;
        }
        double qtd = Double.valueOf(quantidade.getText().toString().replaceAll(",", "\\."));
        RadioGroup group = findViewById(R.id.groupRadio);
        RadioButton id = findViewById(group.getCheckedRadioButtonId());

        switch (id.getId()) {
            case R.id.radioUso:
                produto.tabela = "consumption";
                break;
            case R.id.radioPadaria:
                produto.tabela = "bakery";
                break;
            case R.id.radioTroca:
                produto.tabela = "exchange";
                break;
            case R.id.radioQuebra:
                produto.tabela = "break";
                break;
        }
        produto.quantidade = qtd;
        produto.usuario = MainActivity.login_string;

        try {
            String caminho = "Produto/post";
            String json = new Gson().toJson(produto);
            HTTPServicePost service = new HTTPServicePost(json, caminho, "POST");
            String response = service.execute().get();
            if (response != null) {
                if (response.equals("true")) {
                    codigo.setText("");
                    codigo.requestFocus();
                    descricao.setText("");
                    quantidade_banco.setText("");
                    quantidade.setText("");
                    Toast.makeText(this.getApplicationContext(), getString(R.string.message_concluido), Toast.LENGTH_LONG).show();
                    lista_produtos.add(new Produto(produto.codigo, produto.descricao, produto.quantidade));
                }
            } else {
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle((R.string.title_erro));
                dialog.setMessage(getString(R.string.message_erro_operacao) + "\n" + String.valueOf(MyException.code));
                dialog.show();
            }
        } catch (Exception e) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.title_erro);
            dialog.setMessage(getString(R.string.message_erro_operacao));
            dialog.show();
        }
    }

}
