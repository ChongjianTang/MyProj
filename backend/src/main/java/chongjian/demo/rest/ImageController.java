package chongjian.demo.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import com.azure.storage.blob.*;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.io.ByteStreams;

import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Classifier;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Domain;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.DomainType;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.ImageFileCreateBatch;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.ImageFileCreateEntry;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Iteration;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Project;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Region;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.TrainProjectOptionalParameter;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.CustomVisionTrainingClient;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.Trainings;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.CustomVisionTrainingManager;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.ImagePrediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.models.Prediction;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.CustomVisionPredictionClient;
import com.microsoft.azure.cognitiveservices.vision.customvision.prediction.CustomVisionPredictionManager;
import com.microsoft.azure.cognitiveservices.vision.customvision.training.models.Tag;


@RestController
@CrossOrigin(origins = "http://localhost:8000")
public class ImageController {

    @PostMapping("/images")
    public ResponseEntity<Object> saveImage(@RequestBody String image) {
        String base64 = image.replace("data:image/png;base64,", "");
        byte[] imageByte = Base64.getDecoder().decode(base64);
        String imageName = UUID.randomUUID() + ".png";

        File imageFolder = new File("./images");
        if (!imageFolder.exists()) {
            imageFolder.mkdir();
        }
        try {
            Files.write(new File("./images/" + imageName).toPath(), imageByte);
            saveToCloud(imageName);
            CustomVisionProject.uploadImages("myFace", imageName);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Success!", HttpStatus.OK);
    }

    public void saveToCloud(String imageName) {
        String connectStr = "DefaultEndpointsProtocol=https;AccountName=mystoragetcj;AccountKey=V9TQnATmgaKtFLQTiWEIqjEIT9TLlhiGrSmFySfHOEOhY07Rekze88QI1pmxy6sKTMhGBysEx2Uq+ASty0KNWA==;EndpointSuffix=core.windows.net";
// Create a BlobServiceClient object which will be used to create a container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectStr).buildClient();

//Create a unique name for the container

// Create the container and return a container client object
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("images");

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(imageName);

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

// Upload the blob
        blobClient.uploadFromFile("./images/" + imageName);
    }
}
