package com.frankosn.apod.service;

import com.frankosn.apod.config.ConfigLoader;
import com.frankosn.apod.model.ApodData;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for fetching APOD data from NASA API.
 */
public class ApodService {

    private static final Logger logger = LoggerFactory.getLogger(ApodService.class);
    private static final String BASE_URL = "https://api.nasa.gov/planetary/apod";
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    /**
     * Fetches APOD data for a specific date.
     */
    public static ApodData fetchApod(String date) throws Exception {
        String apiKey = ConfigLoader.getNasaApiKey();

        HttpUrl url = HttpUrl.parse(BASE_URL)
                .newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("date", date)
                .addQueryParameter("thumbs", "True")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Failed to fetch APOD: HTTP " + response.code());
            }

            String responseBody = response.body().string();
            ApodData apodData = gson.fromJson(responseBody, ApodData.class);
            logger.debug("Fetched APOD for date {}: {}", date, apodData);
            return apodData;
        }
    }

    /**
     * Extracts the image URL from ApodData based on media type.
     */
    public static String extractImageUrl(ApodData data) {
        if (data == null) {
            return null;
        }

        String mediaType = data.getMediaType();
        if ("image".equalsIgnoreCase(mediaType)) {
            return data.getHdurl() != null ? data.getHdurl() : data.getUrl();
        } else if ("video".equalsIgnoreCase(mediaType)) {
            return data.getThumbnailUrl() != null ? data.getThumbnailUrl() : data.getUrl();
        }

        return null;
    }
}
