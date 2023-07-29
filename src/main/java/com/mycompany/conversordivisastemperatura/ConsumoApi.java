/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.conversordivisastemperatura;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * @author anderson
 */
public class ConsumoApi {
    private String sourceCurrencyCode;
    private String targetCurrencyCode;
    private double conversionRate;

    public ConsumoApi(String sourceCurrencyCode, String targetCurrencyCode) {
        this.sourceCurrencyCode = sourceCurrencyCode.toUpperCase();
        this.targetCurrencyCode = targetCurrencyCode.toUpperCase();
        fetchConversionRate();
    }

    private void fetchConversionRate() {
        try {
            String apiUrl = "https://v6.exchangerate-api.com/v6/a27a9e51fd9604e0cf929a3b/latest/";
            URL url = new URL(apiUrl + sourceCurrencyCode);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("Ocurrió un error: " + responseCode);
            } else {
                StringBuilder informationString = new StringBuilder();
                Scanner scanner = new Scanner(url.openStream());

                while (scanner.hasNext()) {
                    informationString.append(scanner.nextLine());
                }

                scanner.close();

                JSONObject jsonObject = new JSONObject(informationString.toString());

                if (jsonObject.getString("result").equals("success")) {
                    JSONObject conversionRates = jsonObject.getJSONObject("conversion_rates");
                    conversionRate = conversionRates.getDouble(targetCurrencyCode);
                } else {
                    throw new RuntimeException("Error en la respuesta de la API");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double convert(double value) {
        return value * conversionRate;
    }
}
