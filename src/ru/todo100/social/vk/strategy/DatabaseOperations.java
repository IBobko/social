package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.DatabaseData;
import ru.todo100.social.vk.datas.GroupData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 06.03.15.
 */
public class DatabaseOperations extends Operations {
    public DatabaseOperations(String accessToken) {
        super(accessToken);
    }

    public List<DatabaseData> getCountries(){
        try {
            URL url = new URL("https://api.vk.com/method/database.getCountries?"
                    + "&v=5.27&access_token=" + accessToken);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            System.out.println(builder.toString());

            JSONObject object = new JSONObject(builder.toString());
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<DatabaseData> countries = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                DatabaseData country = new DatabaseData();
                country.setId(items.getJSONObject(i).getInt("id"));
                country.setTitle(items.getJSONObject(i).getString("title"));
                countries.add(country);
            }
            return countries;
        } catch (IOException e/* | JSONException e*/) {
          //  e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DatabaseData> getCities(Integer country_id){
        try {
            URL url = new URL("https://api.vk.com/method/database.getCities?"
                    + "&v=5.27&country_id="+country_id+"&access_token=" + accessToken);
            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            System.out.println(builder.toString());

            JSONObject object = new JSONObject(builder.toString());
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<DatabaseData> countries = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                DatabaseData country = new DatabaseData();
                country.setId(items.getJSONObject(i).getInt("id"));
                country.setTitle(items.getJSONObject(i).getString("title"));
                countries.add(country);
            }
            return countries;
        } catch (IOException e/* | JSONException e*/) {
            //  e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
