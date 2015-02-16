package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import ru.todo100.social.vk.datas.PostData;
import ru.todo100.social.vk.datas.UserData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 08.02.15.
 */
public class UserOperations extends Operations {
    public UserOperations(String accessToken) {
        super(accessToken);
    }

    public UserData get() {
        return get("").get(0);
    }

    public List<UserData> get(String user_ids) {
        try {
            StringBuilder urlString = new StringBuilder("https://api.vk.com/method/users.get?");
            if (!StringUtils.isEmpty(user_ids)) {
                urlString.append("user_ids=" + user_ids);
            }
            urlString.append("&v=5.27&access_token=" + accessToken);


            URL url = new URL(urlString.toString());
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
            JSONArray response = object.getJSONArray("response");

            List<UserData> users = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                UserData user = new UserData();
                user.setId(response.getJSONObject(i).getInt("id"));
                user.setFirstName(response.getJSONObject(i).getString("first_name"));
                user.setLastName(response.getJSONObject(i).getString("last_name"));
                users.add(user);
            }
            return users;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
