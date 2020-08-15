package quartz;

import java.time.LocalDate;
import java.util.Objects;

public class ParsepShem {
    private String link;
    private String description;
    private LocalDate dataPost;

    public ParsepShem() {

    }

    public ParsepShem(String link, String description, LocalDate dataPost) {
        this.link = link;
        this.description = description;
        this.dataPost = dataPost;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDataPost() {
        return dataPost;
    }

    public void setDataPost(LocalDate dataPost) {
        this.dataPost = dataPost;
    }

}
