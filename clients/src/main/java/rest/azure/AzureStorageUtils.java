package main.java.rest.azure;

import com.azure.core.http.rest.PagedIterable;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.TaggedBlobItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class AzureStorageUtils {
    //search for a folder in Azure Blob Storage that contains file with specific tag
    public static boolean checkFolderWithFileAndTag(String fileName, String tagName, String tagValue) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be null or empty");
        }
        if (tagName == null || tagName.isEmpty()) {
            throw new IllegalArgumentException("Tag name must not be null or empty");
        }

        String connectionString;

        try (FileInputStream input = new FileInputStream("src/test/resources/azure.properties")) {
            Properties properties = new Properties();
            properties.load(input);
            connectionString = properties.getProperty("azure.connection.string");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration: " + e.getMessage(), e);
        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();


        tagValue = tagValue == null ? "*" : tagValue.replace("'", "\\'");
        String tagFilter = String.format("\"%s\" = '%s'", tagName, tagValue);

        System.out.println("Searching for folder containing file: " + fileName + " with tag: " + tagName + " and value: " + tagValue);

        try {
            PagedIterable<TaggedBlobItem> blobs = blobServiceClient.findBlobsByTags(tagFilter);

            for (TaggedBlobItem blob : blobs) {
                String blobName = blob.getName();


                if (blobName.endsWith("/" + fileName)) {
                    String folderName = blobName.substring(0, blobName.lastIndexOf('/'));
                    System.out.printf("Folder found: %s containing file: %s with the specified tag%n", folderName, fileName);
                    return true;
                }
            }

            System.out.println("No folder found containing the specified file and tag.");
        } catch (Exception e) {
            System.err.println("Error during tag-based search: " + e.getMessage());
        }

        return false;
    }
}
