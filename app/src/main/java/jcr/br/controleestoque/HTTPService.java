package jcr.br.controleestoque;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPService extends AsyncTask<String, Void, String> {
    private final String codigo;
    private final URL url;

    public HTTPService(String url, String codigo) throws MalformedURLException {
        this.codigo = codigo;
        this.url = new URL(url + codigo);
    }

    @Override
    protected String doInBackground(String... parametros) {
        StringBuilder resposta = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                resposta.append(inputLine);
            }
            in.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return resposta.toString();
    }
}
