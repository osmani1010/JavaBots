package es.osmani.weather;

import es.osmani.weather.Model;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import static es.osmani.core.Constants.getWeatherToken;

public class Weather {

    public static String getWeather(String msg, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="
                + msg + "&units=metric&lang=es&appid=" + getWeatherToken());

        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONArray getArray = object.getJSONArray("weather");

        /*
        for (JSONArray ga: getArray){


            
        }
        */
        
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setMain((String) obj.get("main"));
        }

        return "Ciudad: " + model.getName() + "\n" +
                "Temperatura: " + model.getTemp() + "C" + "\n" +
                "Humedad: " + model.getHumidity() + "%" + "\n" +
                "Prediccion: " + model.getMain() + "\n" +
                "Descripcion: " + model.getDescription() + "\n" +
                "http://openweathermap.org/img/w/" + model.getIcon() + ".png";
    }
}
