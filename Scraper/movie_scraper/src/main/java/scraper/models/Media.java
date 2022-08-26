package scraper.models;

public class Media {
    public String crew;
    public String fullTitle;
    public String imageURL;
    public String imdbID;
    public String title;
    public String videoId;
    public String year;

    public Media(String crew, String fullTitle, String imageURL, String imdbID, String title, String videoId, String year) {
        this.crew = crew;
        this.fullTitle = fullTitle;
        this.imageURL = imageURL;
        this.imdbID = imdbID;
        this.title = title;
        this.videoId = videoId;
        this.year = year;
    }

    public Media(String crew, String fullTitle, String imageURL, String imdbID, String title, String year) {
        this.crew = crew;
        this.fullTitle = fullTitle;
        this.imageURL = imageURL;
        this.imdbID = imdbID;
        this.title = title;
        this.year = year;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
