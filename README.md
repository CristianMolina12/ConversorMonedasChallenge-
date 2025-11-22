# 💱 Conversor de Monedas en Consola (Java)

Este es un proyecto en Java que realiza conversiones de monedas usando la API pública **ExchangeRate API**.  
El programa funciona desde la consola e incluye un menú interactivo donde el usuario puede convertir entre:

- 🇨🇴 Peso Colombiano ⇄ 🇺🇸 Dólar
- 🇨🇴 Peso Colombiano ⇄ 🇪🇺 Euro
- 🇨🇴 Peso Colombiano ⇄ 🇯🇵 Yen Japonés

El sistema obtiene las tasas de conversión en tiempo real desde la API.

---

## 🚀 Características principales

- Llamada HTTP a la API `https://v6.exchangerate-api.com/`
- Conversión automática entre pares de monedas
- Menú en consola que funciona en bucle
- Manejo de errores de usuario y API
- Uso de:
  - `HttpClient`
  - `Gson`
  - `Records` de Java para mapear JSON
  - `BigDecimal` para conversiones precisas


