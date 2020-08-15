package quartz;

import java.time.LocalDate;

public class Post {
    private String idPost;
    private String body;
    private LocalDate datePost;

    public Post(String idPost, String body, LocalDate datePost) {
        this.idPost = idPost;
        this.body = body;
        this.datePost = datePost;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDate getDatePost() {
        return datePost;
    }

    public void setDatePost(LocalDate datePost) {
        this.datePost = datePost;
    }
}
