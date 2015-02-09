package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
import ru.todo100.social.vk.datas.GroupData;
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
 * Created by igor on 08.02.15.
 */
public class GroupsOperations extends Operations {
    public GroupsOperations(String accessToken) {
        super(accessToken);
    }

    public int join(int group_id) {
        try {
            URL url = new URL("https://api.vk.com/method/groups.join?group_id=" + group_id
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
//            JSONObject object = new JSONObject(builder.toString());
//            JSONObject response = object.getJSONObject("response");
//            JSONArray items = response.getJSONArray("items");
//            List<PostData> posts = new ArrayList<>();
//            for (int i = 0; i < items.length(); i++) {
//                PostData post = new PostData();
//                post.setId(items.getJSONObject(i).getLong("id"));
//                post.setText(items.getJSONObject(i).getString("text"));
//                posts.add(post);
//            }
//            return posts;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }/* catch (JSONException e) {
            e.printStackTrace();
        }
//        return null;*/
            return 1;
    }


    public List<GroupData> get() {
        try {
            StringBuilder urlString = new StringBuilder("https://api.vk.com/method/groups.get?");
            urlString.append("&extended=1");
            urlString.append("&fields=can_post");

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
            JSONObject response = object.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");
            List<GroupData> groups = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                GroupData group = new GroupData();
                group.setId(items.getJSONObject(i).getLong("id"));
                group.setName(items.getJSONObject(i).getString("name"));
                group.setCanPost(items.getJSONObject(i).getInt("can_post"));
                groups.add(group);
            }
            return groups;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//    groups.isMemberВозвращает информацию о том, является ли пользователь участником сообщества.
//    groups.getByIdВозвращает информацию о заданном сообществе или о нескольких сообществах.
//    groups.getВозвращает список сообществ указанного пользователя.
//    groups.getMembersВозвращает список участников сообщества.

//    groups.leaveДанный метод позволяет выходить из группы, публичной страницы, или встречи.
//    groups.searchОсуществляет поиск сообществ по заданной подстроке.
//    groups.getInvitesДанный метод возвращает список приглашений в сообщества и встречи текущего пользователя.
//    groups.getInvitedUsersВозвращает список пользователей, которые были приглашены в группу.
//    groups.banUserДобавляет пользователя в черный список группы.
//    groups.unbanUserУбирает пользователя из черного списка сообщества.
//    groups.getBannedВозвращает список забаненных пользователей в сообществе.
//    groups.createПозволяет создавать новые сообщества.
//    groups.editПозволяет редактировать информацию групп.
//    groups.editPlaceПозволяет редактировать информацию о месте группы.
//    groups.getSettingsПозволяет получать данные, необходимые для отображения страницы редактирования данных сообщества.
//    groups.getRequestsВозвращает список заявок на вступление в сообщество.
//    groups.editManagerПозволяет назначить/разжаловать руководителя в сообществе или изменить уровень его полномочий.
//    groups.inviteПозволяет приглашать друзей в группу.
//    groups.addLinkПозволяет добавлять ссылки в сообщество.
//    groups.deleteLinkПозволяет удалить ссылки из сообщества.
//    groups.editLinkПозволяет редактировать ссылки в сообществе.
//    groups.reorderLinkПозволяет менять местоположение ссылки в списке.
//    groups.removeUserПозволяет исключить пользователя из группы.
//    groups.approveRequestПозволяет одобрить заявку в группу от пользователя.
}
