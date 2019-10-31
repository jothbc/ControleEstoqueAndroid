package jcr.br.controleestoque;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import jcr.br.controleestoque.Bean.Produto;

public class HTTPServiceInsert extends AsyncTask<Produto, Void, String> {
    private final Produto produto;

    public HTTPServiceInsert(Produto produto) {
        this.produto = produto;
    }

    @Override
    protected String doInBackground(Produto... produtos) {
        StringBuilder resposta = new StringBuilder();
        try {
            URL url = new URL("http://187.4.229.36:9999/mercadows/webresources/ws/Produto/insert/"+produto.codigo+"/"+produto.quantidade+"/"+produto.tabela);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept","text/html");
            connection.setConnectTimeout(5000);
            connection.connect();

            Scanner scanner = new Scanner (url.openStream());
            while (scanner.hasNext()){
                resposta.append(scanner.next());
            }

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
