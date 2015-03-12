package ru.todo100.social.vk.strategy;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by igor on 08.02.15.
 */
public class Operations {
    protected String accessToken;

    public Operations(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String inputLine;
        StringBuilder builder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }
        return builder.toString();
    }

    public StringBuilder getStringBuilder(String methodName) {
        StringBuilder urlString = new StringBuilder("https://api.vk.com/method/" + methodName + "?");
        urlString.append("&v=5.27&access_token=").append(accessToken);
        return urlString;
    }

}
