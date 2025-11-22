import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Scanner;

public class Principal {

    public static void main(String[] args) {

        // Creamos un objeto que permite conectarnos a la API
        ConversionApi api = new ConversionApi();

        // Donde se guardará la respuesta (tasas de conversión)
        Conversion data;

        try {
            // Llamamos a la API para obtener todas las tasas de cambio usando USD como base
            data = api.buscarConversion("USD");
        } catch (Exception e) {
            // Si algo falla al llamar la API, se imprime el error y salimos del programa
            System.err.println("Error al obtener tasas desde la API: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Extraemos las tasas de conversión (conversion_rates) del record Conversion
        Map<String, Double> rates = data.conversionRates();

        // Formato bonito para imprimir valores (ej: 1,234.50)
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Scanner para leer datos del usuario
        Scanner sc = new Scanner(System.in);

        // Variable que controla el loop del menú
        boolean running = true;

        // ==============================
        //           LOOP DEL MENÚ
        // ==============================
        while (running) {

            // Menú principal
            System.out.println("\n=== Sistema de conversion de monedas ===");
            System.out.println("1) Peso Colombiano a Dolares");
            System.out.println("2) Dolares a Peso Colombiano");
            System.out.println("3) Peso Colombiano a Euro");
            System.out.println("4) Euro a Peso Colombiano");
            System.out.println("5) Peso Colombiano a Yen");
            System.out.println("6) Yen a Peso colombiano");
            System.out.println("7) Salir");
            System.out.print("Elige una opción (número): ");

            // Leemos la opción
            String entrada = sc.nextLine().trim();
            int opcion;

            try {
                opcion = Integer.parseInt(entrada);
            } catch (NumberFormatException ex) {
                System.out.println("Opción inválida. Intenta de nuevo.");
                continue; // vuelve al menú
            }

            // Si elige salir, rompemos el loop
            if (opcion == 7) {
                running = false;
                System.out.println("Saliendo. ¡Hasta luego!");
                break;
            }

            // Variables para definir el tipo de conversión
            String from = null;  // moneda origen
            String to = null;    // moneda destino

            // Switch para identificar la conversión según la opción
            switch (opcion) {
                case 1 -> { from = "COP"; to = "USD"; }
                case 2 -> { from = "USD"; to = "COP"; }
                case 3 -> { from = "COP"; to = "EUR"; }
                case 4 -> { from = "EUR"; to = "COP"; }
                case 5 -> { from = "COP"; to = "JPY"; }
                case 6 -> { from = "JPY"; to = "COP"; }
                default -> {
                    System.out.println("Opción fuera de rango. Intenta de nuevo.");
                    continue; // vuelve al menú
                }
            }

            // Buscamos en el map las tasas necesarias
            Double rateFrom = rates.get(from);
            Double rateTo = rates.get(to);

            // Verificamos que existan las tasas
            if (rateFrom == null || rateTo == null) {
                System.out.printf("No se encontró la tasa para %s o %s.%n", from, to);
                continue;
            }

            // Pedir cantidad al usuario
            System.out.printf("Ingresa la cantidad en %s a convertir (ej: 100.50): ", from);
            String amountStr = sc.nextLine().trim();

            BigDecimal amount;

            try {
                // Convertir lo ingresado a BigDecimal (más preciso que double)
                amount = new BigDecimal(amountStr);
            } catch (Exception e) {
                System.out.println("Cantidad inválida. Usa número con decimales.");
                continue;
            }

            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                System.out.println("La cantidad debe ser positiva.");
                continue;
            }

            // Convertimos las tasas Double a BigDecimal
            BigDecimal bdRateFrom = BigDecimal.valueOf(rateFrom);
            BigDecimal bdRateTo = BigDecimal.valueOf(rateTo);

            // Fórmula general:
            // amount * (rateTo / rateFrom)
            BigDecimal factor = bdRateTo.divide(bdRateFrom, 12, RoundingMode.HALF_UP);

            // Multiplicamos la cantidad por el factor de conversión
            BigDecimal converted =
                    amount.multiply(factor).setScale(2, RoundingMode.HALF_UP);

            // Mostramos el resultado final
            System.out.printf("%s %s = %s %s%n",
                    df.format(amount),
                    from,
                    df.format(converted),
                    to);
        }

        // Cerramos scanner
        sc.close();
    }
}
