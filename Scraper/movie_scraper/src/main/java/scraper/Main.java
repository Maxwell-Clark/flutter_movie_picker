package scraper;


import java.util.*;
import java.util.logging.Logger;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.functions.Context;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.utilities.Pair;
import com.google.firebase.messaging.Message;
import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import scraper.db.FireStoreDB;
//import scraper.Main.PubSubMessage;
import com.google.cloud.functions.BackgroundFunction;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import scraper.models.Media;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class Main  {
//    implements BackgroundFunction<Main.PubSubMessage>
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    static List<String> cast = new ArrayList<>();
    static String description = "";
    static String movieRating = "";
    static List<Media> mostPopularMovies = new ArrayList<>();

    public static void main( String[] args ) throws IOException, ExecutionException, InterruptedException {
//        Pair<String, String> topBitcoinSubredditPostInfo = app.getTopBitcoinSubredditPost();
          addBackToDB();

//        db.collection("mostPopularMovies").
//        FireStoreDB db = new FireStoreDB("testing1-882d1");
//        db.addData(currentBitcoinPrice, topBitcoinSubredditPostInfo);
    }
    private Pair<String, String> getTopBitcoinSubredditPost() {
        final String httpsUrl = "https://www.reddit.com/r/Bitcoin/top/";
        String topPostBody = "";
        String topPostLink = "";

        try {
            final HttpClient client = new HttpClient();
            client.start();

            final ContentResponse res = client.GET(httpsUrl);

            final Document doc = Jsoup.parse(res.getContentAsString());

            final Elements topPost = doc.select("a[data-click-id]");
            topPostBody = topPost.select("h3").first().text();
            topPostLink = topPost.attr("href");


            System.out.println("****** Content of the URL ********");
            System.out.println(topPostLink);
            System.out.println(topPostBody);

            //this is now pulling only the top post on the bitcoin subreddit.
            //next we need to pull the current bitcoin price.
            //then we need to store them both in a db. maybe as key-value pairs?
            //finally we need to build a UI that we can display the data on a plotted chart over time.
            client.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Pair<>(topPostLink, topPostBody);
    }


    private List<String> getMovieCast(String imdbId) {
        final String httpsUrl = "https://www.imdb.com/title/"+imdbId;
        String castMember = "";
        List<String> castMembers = new ArrayList<>();

        try {
            final HttpClient client = new HttpClient();
            client.start();

            final ContentResponse res = client.GET(httpsUrl);

            final Document doc = Jsoup.parse(res.getContentAsString());

            final Elements castList = doc.select("section[data-testid='title-cast']");
            castMember = castList.select("a[data-testid='title-cast-item__actor']").text();


            System.out.println("****** Content of the cast ********");
            //need to make sure that the cast members are not being looped over twice. so
//            System.out.println(castMember);
            if(!castMembers.contains(castMember)){
                castMembers.add(castMember);
            }
            System.out.println(castMembers);
            client.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return castMembers;
    }

    private String getMovieRatings(String imdbId) {
        final String httpsUrl = "https://www.imdb.com/title/"+imdbId+"/ratings";
        String ratingsText = "";
        String rating = "";

        try {
            final HttpClient client = new HttpClient();
            client.start();

            final ContentResponse res = client.GET(httpsUrl);

            final Document doc = Jsoup.parse(res.getContentAsString());

            final Elements listParent = doc.select("section.listo");
            ratingsText = listParent.select("div.title-ratings-sub-page").select("div.allText").select("div.allText").first().text();
            int startActualRatingIndex = ratingsText.indexOf("vote of");
            int endActualRatingIndex = ratingsText.indexOf(" / 10");
            rating = ratingsText.substring(startActualRatingIndex+8, endActualRatingIndex);


            System.out.println("****** Content of the ratings ********");
            System.out.println(rating);
            client.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rating;
    }

    private String getMovieDescription(String imdbId) {
        final String httpsUrl = "https://www.imdb.com/title/"+imdbId+"/plotsummary";
        String description = "";

        try {
            final HttpClient client = new HttpClient();
            client.start();

            final ContentResponse res = client.GET(httpsUrl);

            final Document doc = Jsoup.parse(res.getContentAsString());

            final Elements listParent = doc.select("section.listo");
            description = listParent.select("ul.ipl-zebra-list").select("li.ipl-zebra-list__item").first().text();

            System.out.println("****** Content of the description ********");
            System.out.println(description);
            client.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return description;
    }

    private double getBitcoinPrice() {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("https://data.messari.io/api/v1/assets/bitcoin/metrics");
        Response response = target.request().get();
        String stringedResponse = response.readEntity(String.class);

        logger.info(stringedResponse);
        //This response is giving so much more than just the price of bitcoin. can definately look deeper into it.
        Any parsedResponse = null;
//         try{
        parsedResponse = JsonIterator.deserialize(stringedResponse);
        System.out.println("Read: "+ parsedResponse.get("data").get("market_data").get("price_usd"));
//         } catch (IllegalStateException exception) {
//             System.out.println("Error: " + exception);
//         }
//        assert parsedResponse != null;
        double PriceUsd = parsedResponse.get("data").get("market_data").get("price_usd").toDouble();

        System.out.println("Current price of Bitcoin in US dollars: " + PriceUsd);
        return PriceUsd;

    }

    private Firestore getDatabase() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId("movie-picker-6f264")
                .build();
        FirebaseApp.initializeApp(options);

        return FirestoreClient.getFirestore();
    }
    //need to get the movie details
    //details include the cast, description, title, link to a trailer, and ratings.
    //most of this can be found on imdb

    private static void addBackToDB() throws IOException, ExecutionException, InterruptedException {
        Main main = new Main();
        Firestore db = main.getDatabase();
        ApiFuture<QuerySnapshot> query = db.collection("mostPopularTV").get();
        QuerySnapshot querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot document: documents) {
            Media popularMovie = new Media(document.getString("crew"), document.getString("fullTitle"), document.getString("image"), document.getString("imdbID"), document.getString("title"), document.getString("videoId"), document.getString("year"));
            mostPopularMovies.add(popularMovie);
        }

        for (Media movie: mostPopularMovies) {
            cast = main.getMovieCast(movie.imdbID);
            description = main.getMovieDescription(movie.imdbID);
            movieRating = main.getMovieRatings(movie.imdbID);
            Map<String, Object> data = new HashMap<>();
            data.put("cast", cast);
            data.put("description", description);
            data.put("rating", movieRating);
            db.collection("mostPopularTV").document(movie.title).update(data);
        }
    }

//    @Override
//    public void accept(PubSubMessage message, Context context) throws Exception {
//        String data = message.data != null
//                ? new String(Base64.getDecoder().decode(message.data))
//                : "Hello, World";
//        logger.info(data);
//        logger.info(String.format("Message Received"));
//        Pair<String, String> topBitcoinSubredditPostInfo = getTopBitcoinSubredditPost();
//        double currentBitcoinPrice = getBitcoinPrice();
//        FireStoreDB db = new FireStoreDB("testing1-882d1");
//        db.addData(currentBitcoinPrice, topBitcoinSubredditPostInfo);
//    }

//    public static class PubSubMessage {
//        String data;
//        Map<String, String> attributes;
//        String messageId;
//        String publishTime;
//    }
}