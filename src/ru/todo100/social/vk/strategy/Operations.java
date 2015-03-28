package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.AntiCaptcha;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * @author Igor Bobko
 */
public class Operations {
    protected String accessToken;

    public Operations(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getResponse(String urlString) throws IOException {
        urlString = java.net.URLDecoder.decode(urlString, "UTF-8");
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        Charset charset = Charset.forName("UTF8");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream(),charset));
        String inputLine;
        StringBuilder builder = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            builder.append(inputLine);
        }

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonResponse.has("error")) {
            return error(jsonResponse);
        }

        return builder.toString();
    }

    public StringBuilder getStringBuilder(String methodName) {
        StringBuilder urlString = new StringBuilder("https://api.vk.com/method/" + methodName + "?");
        urlString.append("&v=5.27&access_token=").append(accessToken);
        return urlString;
    }

    public String error(JSONObject response) {
        if (response == null) return null;
        try {
            JSONObject error = response.getJSONObject("error");
            int error_code = error.getInt("error_code");
            if (error_code == 15) {

            }

            if (error_code == 6) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if (error_code == 14) {
                String captchaImg = error.getString("captcha_img");
                String captcha_sid = error.getString("captcha_sid");
                URL url = new URL(captchaImg);
                URLConnection connection = url.openConnection();
                InputStream in = connection.getInputStream();

                File f = new File("/home/igor/" + captcha_sid);
                boolean newFile = f.createNewFile();

                FileOutputStream o =
                        new FileOutputStream(f);

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = in.read(bytes)) != -1) {
                    o.write(bytes, 0, read);
                }
                System.out.println("/home/igor/" + captcha_sid);

                AntiCaptcha antiCaptcha = new AntiCaptcha();


                String key = antiCaptcha.getCaptcha(f);

                f.delete();
                //{"error":{"error_msg":"Captcha needed","request_params":[{"value":"1","key":"oauth"},{"value":"groups.join","key":"method"},{"value":"51288365","key":"group_id"},{"value":"5.27","key":"v"}],"error_code":14,"captcha_sid":"789967439515","captcha_img":"http://api.vk.com/captcha.php?sid=789967439515&s=1"}}
                //Captch neede

                if (key == null) return null;
                JSONArray array = error.getJSONArray("request_params");
                JSONObject captcha_sid_json = new JSONObject();
                captcha_sid_json.put("key","captcha_sid");
                captcha_sid_json.put("value",captcha_sid);

                array.put(captcha_sid_json);

                JSONObject captcha_key_json = new JSONObject();
                captcha_sid_json.put("key","captcha_key");
                captcha_sid_json.put("value",key);

                array.put(captcha_key_json);


                return repeatRequest(array);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String repeatRequest(JSONArray params) {
        String methodName = findValue(params, "method");

        StringBuilder request = getStringBuilder(methodName);

        for (int i = 0; i < params.length(); i++) {
            try {
                if (!params.getJSONObject(i).getString("key").equals("method")) {
                    request.append("&").append(params.getJSONObject(i).getString("key")).append("=").append(params.getJSONObject(i).getString("value"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            return getResponse(request.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String findValue(JSONArray array, String name) {
        for (int i = 0; i < array.length(); i++) {
            try {
                if (array.getJSONObject(i).getString("key").equals(name)) {
                    return array.getJSONObject(i).getString("value");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
