package tx.api;

import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LicencaValidator {

    private static final String API_URL = "https://0ic91lyxu6.execute-api.us-east-2.amazonaws.com/test/validarLicenca?licenca=";

    public static boolean validarLicenca(String licenca) {
        try {
            URL url = new URL(API_URL + licenca);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Bukkit.getLogger().info("Código de resposta da API: " + responseCode); // Log para verificar o código de resposta

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Log para verificar a resposta da API
                Bukkit.getLogger().info("Resposta da API: " + response.toString());

                // Ajustar a verificação para a resposta correta
                return response.toString().contains("Licenca Valida");
            } else {
                return false; // Licença inválida ou erro na requisição
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Erro na requisição
        }
    }
}
