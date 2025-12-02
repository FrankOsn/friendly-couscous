package com.frankosn.apod;

import com.frankosn.apod.cli.ApodCli;

/**
 * Main entry point for the NASA APOD Downloader application.
 * Java 21 port of the original Python CLI application.
 */
public class ApodApplication {

    public static void main(String[] args) {
        try {
            ApodCli cli = new ApodCli();
            cli.run();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
