package com.frankosn.apod.cli;

import com.frankosn.apod.config.ConfigLoader;
import com.frankosn.apod.model.ApodData;
import com.frankosn.apod.service.ApodService;
import com.frankosn.apod.storage.ApodStorage;
import com.frankosn.apod.util.DateUtil;
import com.frankosn.apod.util.FileManager;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * CLI interface for the NASA APOD Downloader application.
 */
public class ApodCli {

    private static final Logger logger = LoggerFactory.getLogger(ApodCli.class);
    private static final Gson gson = new Gson();
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private final String imagesDir;

    public ApodCli() {
        this.imagesDir = ConfigLoader.getImagesDirectory();
    }

    /**
     * Runs the main CLI loop.
     */
    public void run() throws IOException {
        try {
            FileManager.ensureImagesDir(imagesDir);
        } catch (IOException e) {
            System.err.println("❌ Failed to create images directory: " + e.getMessage());
            throw e;
        }

        while (true) {
            showMenu();
            String choice = readInput("Select an option (1-5): ").strip();

            switch (choice) {
                case "1" -> optionDownloadByDate();
                case "2" -> optionDownloadRange();
                case "3" -> optionViewImages();
                case "4" -> optionDownloadToday();
                case "5" -> {
                    System.out.println("\n👋 Goodbye!");
                    return;
                }
                default -> System.out.println("\n❌ Invalid option. Try again.");
            }
        }
    }

    /**
     * Option 1: Download APOD by specific date.
     */
    private void optionDownloadByDate() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Download APOD by date");
        System.out.println("=".repeat(50));

        while (true) {
            String date = readInput("Enter date (YYYY-MM-DD) or 'back': ").strip();

            if ("back".equalsIgnoreCase(date)) {
                return;
            }

            if (!DateUtil.isValidDate(date)) {
                System.out.println("❌ Invalid format. Use YYYY-MM-DD");
                continue;
            }

            downloadAndDisplay(date);
            break;
        }
    }

    /**
     * Option 2: Download APOD for a range of dates.
     */
    private void optionDownloadRange() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Download APOD - Date Range");
        System.out.println("=".repeat(50));

        while (true) {
            String startDateStr = readInput("Start date (YYYY-MM-DD) or 'back': ").strip();

            if ("back".equalsIgnoreCase(startDateStr)) {
                return;
            }

            if (!DateUtil.isValidDate(startDateStr)) {
                System.out.println("❌ Invalid format. Use YYYY-MM-DD");
                continue;
            }

            String endDateStr = readInput("End date (YYYY-MM-DD): ").strip();

            if (!DateUtil.isValidDate(endDateStr)) {
                System.out.println("❌ Invalid format. Use YYYY-MM-DD");
                continue;
            }

            LocalDate startDate = DateUtil.parseDate(startDateStr);
            LocalDate endDate = DateUtil.parseDate(endDateStr);

            if (startDate.isAfter(endDate)) {
                System.out.println("❌ Start date must be before end date");
                continue;
            }

            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;

            if (daysBetween > 100) {
                String confirm = readInput(
                    String.format("⚠️  This will download %d images. Continue? (y/n): ", daysBetween)
                ).strip().toLowerCase();
                if (!confirm.equals("y")) {
                    return;
                }
            }

            downloadRange(startDate, endDate, daysBetween);
            break;
        }
    }

    /**
     * Option 3: View downloaded images.
     */
    private void optionViewImages() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Downloaded Images");
        System.out.println("=".repeat(50));

        try {
            List<String> images = FileManager.listImages(imagesDir);

            if (images.isEmpty()) {
                System.out.println("No images downloaded yet.");
                return;
            }

            System.out.println("\nTotal: " + images.size() + " image(s)\n");

            int idx = 1;
            for (String filename : images) {
                String filepath = imagesDir + "/" + filename;
                double fileSizeKB = FileManager.getFileSizeKB(filepath);
                String metadataPath = FileManager.getMetadataPath(filepath);

                String title = "No title";
                String date = "Unknown";

                try {
                    String metadataJson = new String(java.nio.file.Files.readAllBytes(
                        java.nio.file.Paths.get(metadataPath)));
                    ApodStorage.ApodMetadataFile metadata = gson.fromJson(metadataJson,
                        ApodStorage.ApodMetadataFile.class);
                    title = metadata.title != null ? metadata.title : "No title";
                    date = metadata.date != null ? metadata.date : "Unknown";
                } catch (Exception ignored) {
                }

                System.out.printf("%d. %s\n", idx, filename);
                System.out.printf("   Size: %.1f KB\n", fileSizeKB);
                System.out.printf("   Date: %s\n", date);
                System.out.printf("   Title: %s\n\n", title);

                idx++;
            }
        } catch (IOException e) {
            System.err.println("❌ Error listing images: " + e.getMessage());
        }
    }

    /**
     * Option 4: Download today's APOD.
     */
    private void optionDownloadToday() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("Download Today's APOD");
        System.out.println("=".repeat(50));

        String today = DateUtil.getTodayFormatted();
        System.out.println("Downloading APOD for " + today + "...");
        downloadAndDisplay(today);
    }

    /**
     * Downloads and displays APOD for a specific date.
     */
    private void downloadAndDisplay(String date) {
        try {
            ApodData data = ApodService.fetchApod(date);
            String imageUrl = ApodService.extractImageUrl(data);

            if (imageUrl == null) {
                System.out.println("❌ No image found for " + date);
                return;
            }

            String filename = FileManager.extractFilename(imageUrl, date);
            String filepath = imagesDir + "/" + filename;

            if (FileManager.fileExists(filepath)) {
                System.out.println("✓ Already downloaded: " + filename);
            } else {
                System.out.println("Downloading: " + filename);
                FileManager.downloadFile(imageUrl, filepath);
                System.out.println("✓ Downloaded: " + filename);
            }

            ApodStorage.saveMetadata(data, filepath);
            System.out.println("📁 Saved at: " + filepath);
            System.out.println("📋 Title: " + data.getTitle());

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            logger.error("Error downloading APOD for date " + date, e);
        }
    }

    /**
     * Downloads a range of APODs.
     */
    private void downloadRange(LocalDate startDate, LocalDate endDate, long totalDays) {
        int successCount = 0;

        System.out.println("Downloading " + totalDays + " APOD(s)...");

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            String dateStr = DateUtil.formatDate(current);
            try {
                ApodData data = ApodService.fetchApod(dateStr);
                String imageUrl = ApodService.extractImageUrl(data);

                if (imageUrl != null) {
                    String filename = FileManager.extractFilename(imageUrl, dateStr);
                    String filepath = imagesDir + "/" + filename;

                    if (!FileManager.fileExists(filepath)) {
                        FileManager.downloadFile(imageUrl, filepath);
                        ApodStorage.saveMetadata(data, filepath);
                    }

                    successCount++;
                    System.out.println("  ✓ " + dateStr + ": " + filename);
                } else {
                    System.out.println("  ✗ " + dateStr + ": No image found");
                }
            } catch (Exception e) {
                System.out.println("  ✗ " + dateStr + ": " + e.getMessage());
                logger.debug("Error downloading APOD for date " + dateStr, e);
            }

            current = current.plusDays(1);
        }

        System.out.printf("\n✅ Completed: %d/%d successful downloads\n", successCount, totalDays);
    }

    /**
     * Displays the main menu.
     */
    private void showMenu() {
        System.out.println("\n" + "╔" + "═".repeat(48) + "╗");
        System.out.println("║" + " ".repeat(12) + "NASA APOD - Local Downloader" + " ".repeat(9) + "║");
        System.out.println("╚" + "═".repeat(48) + "╝");
        System.out.println("\n1. Download APOD by specific date");
        System.out.println("2. Download APOD for a date range");
        System.out.println("3. View downloaded images");
        System.out.println("4. Download today's APOD");
        System.out.println("5. Exit");
        System.out.println();
    }

    /**
     * Reads user input from console.
     */
    private String readInput(String prompt) {
        System.out.print(prompt);
        try {
            return reader.readLine();
        } catch (IOException e) {
            logger.error("Error reading input", e);
            return "";
        }
    }
}
