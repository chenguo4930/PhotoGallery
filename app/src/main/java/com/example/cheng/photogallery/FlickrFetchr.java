package com.example.cheng.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 基本网络链接代码
 * Created by cheng on 2016/9/24.
 */

public class FlickrFetchr {

    private static final String TAG = "FlickFetchr";
    private static final String API_KEY = "8e6e4f1fbf7a7d17d0c4962dd0da0ff0";

    public byte[] getUrlBytes(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //只有调用getInputStream()方法，post为getOutputStream()方法时才会链接到url地址
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ":with" + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    /**
     * 负责将获取到的字节数据转换为string
     *
     * @param urlSpec
     * @return
     * @throws IOException
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems() {

        List<GalleryItem> items = new ArrayList<>();
        try {
            /*
              这里使用Uri.Builder构建了Url，Uri.Builder可创建正确转义的参数化URL，appendQueryParameter(String,String)
              可自动转移查询字符串
             */
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build()
                    .toString();

            String jsonString = getUrlString(url);
            JSONObject jsongBody = new JSONObject(jsonString);
            parseItems(items,jsongBody);
            Log.i(TAG,"Received JSON:"+jsonString);
        } catch (IOException e) {
            Log.e(TAG,"Failed to fetch items",e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException{
        try {
            JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
            JSONArray photoJsnoArra = photosJsonObject.getJSONArray("photo");

            for (int i = 0; i < photoJsnoArra.length(); i++) {
                JSONObject photoJsonObject = photoJsnoArra.getJSONObject(i);

                GalleryItem item = new GalleryItem();
                item.setmId(photoJsonObject.getString("id"));
                item.setmCption(photoJsonObject.getString("title"));

                if (!photoJsonObject.has("url_s")){
                    continue;
                }

                item.setmUrl(photoJsonObject.getString("url_s"));
                items.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
