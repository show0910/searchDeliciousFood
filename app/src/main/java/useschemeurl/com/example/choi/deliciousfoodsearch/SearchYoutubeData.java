package useschemeurl.com.example.choi.deliciousfoodsearch;

/**
 * Created by Choi on 2016-11-04.
 */

public class SearchYoutubeData {

    String videoId;
    String title;
    String url;
    String publishedAt;
    String description;

    public SearchYoutubeData(String videoId, String title, String url,
                             String publishedAt, String description) {
        super();
        this.videoId = videoId;
        this.title = title;
        this.url = url;
        this.publishedAt = publishedAt;
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
