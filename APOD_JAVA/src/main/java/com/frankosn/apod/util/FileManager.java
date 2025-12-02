package com.frankosn.apod.util;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for managing file operations (download, save, etc).
 */
public class FileManager {

    /**
     * Ensures the images directory exists.
     */
    public static void ensureImagesDir(String imagesDir) throws IOException {
        Path path = Paths.get(imagesDir);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    /**
     * Extracts filename from URL or generates a default one.
     */
    public static String extractFilename(String imageUrl, String date) {
        try {
            URI uri = new URI(imageUrl);
            String urlPath = uri.getPath();
            String filename = urlPath.substring(urlPath.lastIndexOf('/') + 1);
            if (filename.contains("?")) {
                filename = filename.split("\\?")[0];
            }
            return filename.isEmpty() ? "apod_" + date + ".jpg" : filename;
        } catch (Exception e) {
            return "apod_" + date + ".jpg";
        }
    }

    /**
     * Checks if a file already exists at the given path.
     */
    public static boolean fileExists(String filepath) {
        return Files.exists(Paths.get(filepath));
    }

    /**
     * Downloads an image from URL and saves it to the specified filepath.
     */
    public static void downloadFile(String imageUrl, String filepath) throws IOException {
        try {
            URI uri = new URI(imageUrl);
            try (InputStream in = uri.toURL().openStream();
                 FileOutputStream out = new FileOutputStream(filepath)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL: " + imageUrl, e);
        }
    }

    /**
     * Gets the file size in KB.
     */
    public static double getFileSizeKB(String filepath) throws IOException {
        return Files.size(Paths.get(filepath)) / 1024.0;
    }

    /**
     * Constructs the metadata filepath from an image filepath.
     */
    public static String getMetadataPath(String imagePath) {
        int lastDot = imagePath.lastIndexOf('.');
        if (lastDot > 0) {
            return imagePath.substring(0, lastDot) + ".json";
        }
        return imagePath + ".json";
    }

    /**
     * Lists all image files in the directory (excluding .json files).
     */
    public static java.util.List<String> listImages(String imagesDir) throws IOException {
        Path dirPath = Paths.get(imagesDir);
        if (!Files.exists(dirPath)) {
            return java.util.Collections.emptyList();
        }
        return Files.list(dirPath)
                .filter(p -> !p.getFileName().toString().endsWith(".json"))
                .map(p -> p.getFileName().toString())
                .sorted()
                .toList();
    }
}
