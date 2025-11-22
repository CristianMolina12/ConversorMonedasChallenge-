import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConversionApi {

    public Conversion buscarConversion(String baseCurrency) {

        // 1. Crear la URL dinámica
        URI direccion = URI.create("https://v6.exchangerate-api.com/v6/1be5333f70622cd2a8ce75aa/latest/" + baseCurrency);

        // 2. Crear cliente
        HttpClient client = HttpClient.newHttpClient();

        // 3. Crear request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        try {
            // 4. Enviar petición
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // 5. Convertir JSON a tu record con Gson
            Gson gson = new Gson();
            return gson.fromJson(response.body(), Conversion.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error consultando la API", e);
        }
    }
}
