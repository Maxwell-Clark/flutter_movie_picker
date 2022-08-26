package scraper.db;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.utilities.Pair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreDB {
    Firestore db;
    String projectId;
    GoogleCredentials credentials;
    FirebaseOptions options;

    public FireStoreDB(String projectId) throws IOException {
        this.projectId = projectId;
        initalizeApplication();
    }

    private void initalizeApplication() throws IOException {
        credentials = GoogleCredentials.getApplicationDefault();
        options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
    }

    public void addData(double bitCoinPrice, Pair<String, String> topBitcoinSubredditPostInfo) throws ExecutionException, InterruptedException {
        //add data
        DocumentReference docRef = db.collection("bitcoin").document( getTodayDate());
        Map<String, Object> data = new HashMap<>();
        String date = getTodayDate();
        data.put("currentPrice", bitCoinPrice);
        data.put("redditLink", topBitcoinSubredditPostInfo.getFirst());
        data.put("redditPostTitle", topBitcoinSubredditPostInfo.getSecond());
        data.put("date", date);

        //asynchronously write data
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
        System.out.println("data added for " + date);
    }

    public void getData()throws ExecutionException, InterruptedException {
        References ref = new References(db);

        //get data
        ApiFuture<QuerySnapshot> query = db.collection("bitcoin").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document : documents) {
            System.out.println("User: " + document.getId());
            System.out.println("currentprice: " + document.getDouble("currentPrice"));
            System.out.println("Reddit Link: " + document.getString("redditLink"));
            System.out.println("Reddit Post Title: " + document.getString("redditPostTitle"));
        }

    }

    private String getTodayDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy");
        Date date = new Date();
        String format = formatter.format(date);
        return format;
    }

}
