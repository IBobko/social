package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.PostData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 26.01.15.
 */
public class WallOperations {
    private String accessToken;

    public WallOperations(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<PostData> get(int owner_id, String domain, int offset, int count, String filter, Short extended) {
        try {
            URL url = new URL("https://api.vk.com/method/wall.get?owner_id=" + owner_id
                    + "&domain=" + domain
                    + "&offset=" + offset
                    + "&count=" + count
                    + "&filter=" + filter
                    + "&extended=" + extended
                    + "&v=5.27&access_token=" + accessToken);


            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            JSONObject object = new JSONObject(builder.toString());
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<PostData> posts = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                PostData post = new PostData();
                post.setId(items.getJSONObject(i).getLong("id"));
                post.setText(items.getJSONObject(i).getString("text"));
                posts.add(post);
            }
            return posts;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean delete(int owner_id, Long post_id) {
        try {
            URL url = new URL("https://api.vk.com/method/wall.delete?owner_id=" + owner_id
                    + "&post_id=" + post_id
                    + "&v=5.27&access_token=" + accessToken);

            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            JSONObject object = new JSONObject(builder.toString());
            if(object.has("error")) {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean post(Long owner_id, int friends_only,int from_group,String message) {
        try {
            URL url = new URL("https://api.vk.com/method/wall.post?owner_id=" + owner_id
                    + "&message=" + message
                    + "&v=5.27&access_token=" + accessToken);

            URLConnection connection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            JSONObject object = new JSONObject(builder.toString());
            if(object.has("error")) {
                return false;
            }
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
