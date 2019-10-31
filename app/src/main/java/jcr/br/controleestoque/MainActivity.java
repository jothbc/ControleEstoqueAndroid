package jcr.br.controleestoque;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static boolean autenticado = false;
    public static String login_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText login, senha;
        login = findViewById(R.id.editLogin);
        senha = findViewById(R.id.editSenha);
        login.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);
        senha.setTextAlignment(EditText.TEXT_ALIGNMENT_CENTER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void autenticar(View view) {
        EditText login, senha;
        login = findViewById(R.id.editLogin);
        senha = findViewById(R.id.editSenha);

        if (login.getText().toString().trim().isEmpty() || senha.getText().toString().trim().isEmpty()) {
            return;
        }
        String url = "http://187.4.229.36:9999/mercadows/webresources/ws/Usuario/get/";
        String parametro = login.getText().toString().toUpperCase().trim() + "/" + senha.getText().toString();
        try {
            HTTPService service = new HTTPService(url, parametro);
            String resposta = service.execute(url, parametro).get();
            if (resposta.equals("autenticado")) {
                autenticado = true;
                login_string = login.getText().toString();
                Toast.makeText(this.getApplicationContext(), getString(R.string.message_bem_vindo) + " " + login.getText(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, BaixaActivity.class);
                startActivity(intent);
            } else {
                autenticado = false;
                Snackbar.make(view, getString(R.string.message_login_failed), Snackbar.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }

    }
}
