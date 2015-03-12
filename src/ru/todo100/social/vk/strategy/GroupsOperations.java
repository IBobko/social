package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.GroupData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Bobko on 08.02.15.
 * GroupsOperations
 */
public class GroupsOperations extends Operations {
    public GroupsOperations(String accessToken) {
        super(accessToken);
    }

    public int join(Long group_id) {
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
            JSONObject object = new JSONObject(builder.toString());
            if (object.has("response") && object.getInt("response") == 1) {
                return 1;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @SuppressWarnings("StringBufferReplaceableByString")
    public List<GroupData> get() {
        try {
            StringBuilder urlString = new StringBuilder("https://api.vk.com/method/groups.get?");
            urlString.append("&extended=1");
            urlString.append("&fields=can_post,members_count");

            urlString.append("&v=5.27&access_token=").append(accessToken);

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
                group.setMemberCount(items.getJSONObject(i).getInt("members_count"));
                groups.add(group);
            }
            return groups;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GroupData> search(String search, Integer offset, Integer count, Integer country_id, Integer city_id) {
        try {
            StringBuilder urlString = new StringBuilder("https://api.vk.com/method/groups.search?");
            urlString.append("&q=").append(search);
            urlString.append("&fields=can_post");
            if (offset != null) {
                urlString.append("&offset=").append(offset);
            }
            if (count != null) {
                urlString.append("&count=").append(count);
            }

            if (country_id != null) {
                urlString.append("&country_id=").append(country_id);
            }

            if (city_id != null) {
                urlString.append("&city_id=").append(city_id);
            }

            urlString.append("&v=5.27&access_token=").append(accessToken);

            URL url = new URL(urlString.toString());
            URLConnection connection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            System.out.println(builder.toString());
            JSONObject o = new JSONObject(builder.toString());
            JSONObject response = o.getJSONObject("response");
            JSONArray items = response.getJSONArray("items");

            List<GroupData> groups = new ArrayList<>();
            for (int i = 0; i < items.length(); i++) {
                if (items.get(i) instanceof JSONObject) {
                    GroupData group = new GroupData();
                    group.setId(items.getJSONObject(i).getLong("id"));
                    group.setName(items.getJSONObject(i).getString("name"));
                    group.setCanPost(items.getJSONObject(i).getInt("can_post"));

                    groups.add(group);
//
//                    //groupData.setGid(array.getJSONObject(i).getLong("gid"));
//                    groupData.setName(array.getJSONObject(i).getString("name"));
//                    groupData.setScreenName(array.getJSONObject(i).getString("screen_name"));
//                    groupData.setIsClosed(Boolean.parseBoolean(String.valueOf(array.getJSONObject(i).getInt("is_closed"))));
//                    groupData.setType(array.getJSONObject(i).getString("type"));

                    //groupData.setName(array.getJSONObject(i).getString("photo"));
                    //groupData.setName(array.getJSONObject(i).getString("photo_medium"));
                    //groupData.setName(array.getJSONObject(i).getString("photo_big"));


                }
            }
            //groupsList.setItems(data);
            return groups;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void leave(Integer group_id) {
        try {
            StringBuilder urlString = new StringBuilder("https://api.vk.com/method/groups.leave?");


            if (group_id != null) {
                urlString.append("&group_id=").append(group_id);
            }
            urlString.append("&v=5.27&access_token=").append(accessToken);

            URL url =new URL(urlString.toString());



            URLConnection connection = url.openConnection();

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                builder.append(inputLine);

            System.out.println(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
