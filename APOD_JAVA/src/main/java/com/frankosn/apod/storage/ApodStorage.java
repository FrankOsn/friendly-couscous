package com.frankosn.apod.storage;

import com.frankosn.apod.model.ApodData;
import com.frankosn.apod.util.FileManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Storage service for saving APOD images and metadata.
 */
public class ApodStorage {

    private static final Logger logger = LoggerFactory.getLogger(ApodStorage.class);
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Saves APOD metadata to a JSON file.
     */
    public static void saveMetadata(ApodData data, String imagePath) throws IOException {
        String metadataPath = FileManager.getMetadataPath(imagePath);

        ApodMetadataFile metadata = new ApodMetadataFile(
                data.getTitle(),
                data.getExplanation(),
                data.getDate(),
                data.getMediaType(),
                data.getUrl(),
                data.getHdurl(),
                imagePath
        );

        try (FileWriter writer = new FileWriter(metadataPath)) {
            gson.toJson(metadata, writer);
        }
        logger.info("Metadata saved to: {}", metadataPath);
    }

    /**
     * Inner class to represent metadata structure.
     */
    public static class ApodMetadataFile {
        public String title;
        public String explanation;
        public String date;
        public String media_type;
        public String url;
        public String hdurl;
        public String image_path;

        public ApodMetadataFile(String title, String explanation, String date,
                                String mediaType, String url, String hdurl, String imagePath) {
            this.title = title;
            this.explanation = explanation;
            this.date = date;
            this.media_type = mediaType;
            this.url = url;
            this.hdurl = hdurl;
            this.image_path = imagePath;
        }
    }
}
