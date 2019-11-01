package jcr.br.controleestoque;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import jcr.br.controleestoque.Bean.Produto;

public class HTTPServicePost extends AsyncTask<Produto, Void, String> {
    private final Produto produto;

    public HTTPServicePost(Produto produto) {
        this.produto = produto;
    }

    @Override
    protected String doInBackground(Produto... produtos) {
        /*
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
        */
         return sendPost("http://192.168.1.158:9999/mercadows/webresources/ws/Produto/post", new Gson().toJson(produto), "POST");
    }

    public String sendPost(String url, String json,String metodo) {

        try {
            // Cria um objeto HttpURLConnection:
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();

            try {
                // Define que a conexão pode enviar informações e obtê-las de volta:
                request.setDoOutput(true);
                request.setDoInput(true);

                // Define o content-type:
                request.setRequestProperty("Content-Type", "application/json");
                // Define o tempo máximo
                request.setConnectTimeout(4000);

                // Define o método da requisição:
                request.setRequestMethod(metodo);

                // Conecta na URL:
                request.connect();

                // Escreve o objeto JSON usando o OutputStream da requisição:
                try (OutputStream outputStream = request.getOutputStream()) {
                    outputStream.write(json.getBytes("UTF-8"));
                }

                // Caso você queira usar o código HTTP para fazer alguma coisa, descomente esta linha.
                //int response = request.getResponseCode();
                return readResponse(request);
            } finally {
                request.disconnect();
            }
        } catch (IOException ex) {
            System.err.println(ex);
            return null;
        }
    }

    private String readResponse(HttpURLConnection request) throws IOException {
        ByteArrayOutputStream os;
        try (InputStream is = request.getInputStream()) {
            os = new ByteArrayOutputStream();
            int b;
            while ((b = is.read()) != -1) {
                os.write(b);
            }
        }
        return new String(os.toByteArray());
    }

    @Deprecated
    public static class MinhaException extends Exception {

        private static final long serialVersionUID = 1L;

        public MinhaException(Throwable cause) {
            super(cause);
        }
    }


}
