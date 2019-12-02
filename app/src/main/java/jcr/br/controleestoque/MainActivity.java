package jcr.br.controleestoque;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import jcr.br.controleestoque.Bean.MyException;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static boolean autenticado = false;
    public static String login_string;
    private Button btnAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MyException exception = new MyException();
        btnAuth = findViewById(R.id.btn_login);
        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticarViaMacWifi();
            }
        });
        autenticarViaMacWifi();
    }

    private void autenticarViaMacWifi() {
        String caminho = "Usuario/get/";
        String param = getMacWifi();
        System.out.println("MAC WIFIIIIIIIIIIIIII: " + param);
        try {
            HTTPService service = new HTTPService(caminho, param);
            String request = service.execute().get();
            if (request != null) {
                autenticado = true;
                login_string = request;
                Intent intent = new Intent(MainActivity.this, BaixaActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "ERRO: " + String.valueOf(MyException.code) + "\nO Dispositivo deve estar cadastrado no aplicativo Desktop.", Toast.LENGTH_LONG).show();
                autenticado = false;
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.message_erro_operacao, Toast.LENGTH_LONG).show();
            autenticado = false;
        }
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
        switch (id) {
            case R.id.action_exit:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    public String getMacWifi() {
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }
}
