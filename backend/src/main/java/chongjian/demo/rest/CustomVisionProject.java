package chongjian.demo.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CustomVisionProject {


    static RestTemplate restTemplate = new RestTemplate();
    static String projectName = "AI";
    static String trainingEndpoint = "https://westus2.api.cognitive.microsoft.com/";
    static String trainingApiKey = "04eaac80b3f74737a3e112417f047034";
    static String predictionResourceId = "/subscriptions/20f43a2b-f269-4410-9185-4d675628f7f4/resourceGroups/MyFaceRecognition/providers/Microsoft.CognitiveServices/accounts/AI";
    static String tagName1 = "myFace";
    static String tagName2 = "others";
    static String projectId = "7c746f5e-6763-47e6-9f2f-f5800e94d589";


    public static void main(String[] args) throws JSONException, IOException {

//        RestTemplate restTemplate = new RestTemplate();
//        String projectName = "AI";
//        String trainingEndpoint = "https://westus2.api.cognitive.microsoft.com/";
//        String trainingApiKey = "04eaac80b3f74737a3e112417f047034";
//        String predictionResourceId = "/subscriptions/20f43a2b-f269-4410-9185-4d675628f7f4/resourceGroups/MyFaceRecognition/providers/Microsoft.CognitiveServices/accounts/AI";
////        String publishName = "imageClassifier2Prediction";
//        String tagName1 = "myFace";
//        String tagName2 = "others";
//        String projectId = "7c746f5e-6763-47e6-9f2f-f5800e94d589";

//        Map<String, String> tagsId = getTags(restTemplate, trainingEndpoint, projectId, trainingApiKey);
//
//        String projectID = createProject(restTemplate, trainingEndpoint, projectName, trainingApiKey);
//        System.out.println(projectID);


//        uploadImages(restTemplate, trainingEndpoint, projectId, trainingApiKey, tagsId.get(tagName1), "1b62dda0-f19f-479d-ab4d-eed2a2962999.png");
//        String projectID = "f422b129-09bb-4b06-9fe0-e9657ea0b4ad";
//        //System.out.println("Project Created Status " + !projectID.isEmpty());
//
//        // String tagID1 = addTags(restTemplate, trainingEndpoint, projectID, tagName1, trainingApiKey);
//        // String tagID2 = addTags(restTemplate, trainingEndpoint, projectID, tagName2, trainingApiKey);
//        String tagID1 = "98af3bf0-7c4d-4c68-adb6-425d4e9fdc21";
//        String tagID2 = "441d8f85-4e41-4768-8a78-fbe3e7ca72ee";
//        //System.out.println(tagName1 + " Tag Added Status " + !tagID1.isEmpty());
//        //System.out.println(tagName2 + " Tag Added Status " + !tagID2.isEmpty());
//
//        // String uploadStatus1 = uploadImages(restTemplate, trainingEndpoint, projectID, trainingApiKey, tagID1);
//        // System.out.println("Image Upload Status Of " + tagName1 + " Tag: " + uploadStatus1);
//        // String uploadStatus2 = uploadImages(restTemplate, trainingEndpoint, projectID, trainingApiKey, tagID2);
//        //System.out.println("Image Upload Status Of " + tagName2 + " Tag: " + uploadStatus2);
//
//        // String imageName = "photo_" + UUID.randomUUID() + ".png";
//        // String filename = "/Users/euniceyuan/backend-service/images/" + imageName;
//        // uploadImages(restTemplate, trainingEndpoint, projectID, trainingApiKey, tagID2, filename);
//
//        // String iterationID = trainProject(restTemplate, trainingEndpoint, projectID, trainingApiKey, tagID1);
//        //System.out.println("Project Training Status " + !iterationID.isEmpty());
//
//        String iterationID = "ba651aba-a5c6-4593-97bd-632b69bc59bf";
//        // publishIteration(restTemplate, trainingEndpoint, projectID, iterationID, publishName, predictionResourceId, trainingApiKey);
//        //System.out.println("Project Publish Status " + publishStatus);
//
//        String imageName = "photo_0b02b371-e41e-405c-8359-9e03a79adb7c.png";
//        String filename = "/Users/euniceyuan/Desktop/fruits/" + imageName;
//        // JSONArray predictions = testPredictionEndpoint(restTemplate, trainingEndpoint, projectID, iterationID, publishName, predictionResourceId, trainingApiKey, filename);
//        // System.out.println(predictions);


    }

    public static String createProject() throws JSONException {
        String url = trainingEndpoint + "/customvision/v3.4-preview/training/projects?name=" + projectName;


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<>(headers);


        ResponseEntity<String> response = restTemplate.postForEntity(URI.create(url), request, String.class);
        // System.out.println(response.getBody())

        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("id");
    }

    public static String addTags(String tagName) throws JSONException {
        String url = "https://{endpoint}/customvision/v3.3/Training/projects/{projectId}/tags?name={name}";
        // String url = "https://imageclassifier1.cognitiveservices.azure.com/customvision/v3.3/Training/projects/f422b129-09bb-4b06-9fe0-e9657ea0b4ad/tags?name=eunice1";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectId);
        params.put("name", tagName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        // System.out.println(response.getBody());

        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("id");
    }

    public static Map<String, String> getTags() throws JSONException, IOException {
        String url = trainingEndpoint + "/customvision/v3.4-preview/training/projects/" + projectId + "/tags?Training-key=" + trainingApiKey;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

        ResponseEntity<String> response = restTemplate.getForEntity(builder.build().toUri(), String.class);
        JSONArray jsonArray = new JSONArray(response.getBody());
        Map<String, String> tagsId = new HashMap<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            tagsId.put(jsonObject.getString("name"), jsonObject.getString("id"));
        }
        return tagsId;
    }

    public static void uploadImages(String tagName, String fileName) throws JSONException, IOException {
//        String url = "https://{endpoint}/customvision/v3.3/training/projects/{projectId}/images?tagIds={tagIds}";

        Map<String, String> tagsId = getTags();


        String url = trainingEndpoint + "/customvision/v3.4-preview/training/projects/" + projectId + "/images?tagIds=" + tagsId.get(tagName);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);

        Path path = Paths.get("./images/" + fileName);
        System.out.println(path);
        byte[] imageFile = Files.readAllBytes(path);

        HttpEntity<byte[]> request = new HttpEntity<>(imageFile, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.build().toUri(), request, String.class);
        System.out.println(response.getBody());
    }

    public static String trainProject(RestTemplate restTemplate, String trainingEndpoint, String projectID, String trainingApiKey, String tagID) throws JSONException {
        String url = "https://{endpoint}/customvision/v3.3/Training/projects/{projectId}/train";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<String>("{}", headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        System.out.println(response.getBody());

        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("id");
    }

    public static void publishIteration(RestTemplate restTemplate, String trainingEndpoint, String projectID, String iterationID, String publishName, String predictionId, String trainingApiKey) throws JSONException {
        String url = "https://{endpoint}/customvision/v3.3/training/projects/{projectId}/iterations/{iterationId}/publish?publishName={publishName}&predictionId={predictionId}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("iterationId", iterationID);
        params.put("publishName", publishName);
        params.put("predictionId", predictionId);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Training-key", trainingApiKey);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        System.out.println(response.getBody());
    }

    public static JSONArray testPredictionEndpoint(RestTemplate restTemplate, String trainingEndpoint, String projectID, String iterationID, String publishName, String predictionId, String trainingApiKey, byte[] imageFile) throws JSONException, IOException {
        // String url = "https://{endpoint}/customvision/v3.3/Training/projects/{projectId}/iterations/{iterationId}/publish?publishName={publishName}&predictionId={predictionId}";
        String url = "https://{endpoint}/customvision/v3.3/training/projects/{projectId}/quicktest/image?iterationId={iterationId}";

        Map<String, String> params = new HashMap<>();
        params.put("endpoint", trainingEndpoint);
        params.put("projectId", projectID);
        params.put("iterationId", iterationID);

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        // System.out.println(builder.buildAndExpand(params).toUri());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Training-key", trainingApiKey);

        // Path path = Paths.get(fileName);
        // byte[] imageFile = Files.readAllBytes(path);

        HttpEntity<byte[]> request = new HttpEntity<>(imageFile, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(builder.buildAndExpand(params).toUri(), request, String.class);
        // System.out.println(response.getBody());

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray predictions = jsonObject.getJSONArray("predictions");
        // String tagName = predictions.getJSONObject(0).getString("tagName");
        // String probability = predictions.getJSONObject(0).getString("probability");

        return predictions;
    }


}
