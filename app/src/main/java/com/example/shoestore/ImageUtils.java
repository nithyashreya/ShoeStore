package com.example.shoestore;

import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtils {
    public static boolean isImageUrlValid(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return false;
        }

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000); // 3 seconds timeout
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (IOException e) {
            Log.e("ImageUtils", "Error checking image URL: " + imageUrl, e);
            return false;
        }
    }
}