package com.frankosn.apod.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Configuration loader for environment variables using .env file support.
 */
public class ConfigLoader {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    /**
     * Gets the NASA API key from environment or .env file.
     * Throws an exception if not configured.
     */
    public static String getNasaApiKey() {
        String apiKey = System.getenv("NASA_API_KEY");
        if (apiKey == null) {
            apiKey = dotenv.get("NASA_API_KEY");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException(
                "NASA_API_KEY is not configured. Set it in your .env file or environment variables.");
        }
        return apiKey;
    }

    /**
     * Gets the images directory path, defaulting to "images" inside the APOD_JAVA module.
     */
    public static String getImagesDirectory() {
        String baseDir = System.getenv("APOD_JAVA_HOME");
        if (baseDir != null && !baseDir.isEmpty()) {
            return baseDir + "/images";
        }
        // Default to current directory's images folder
        // When running from JAR in APOD_JAVA, use relative path
        return "./images";
    }
}
