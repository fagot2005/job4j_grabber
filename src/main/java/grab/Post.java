package grab;

import java.time.LocalDateTime;

public class Post {
    private String id;
    private String name;
    private String link;
    private String description;
        private LocalDateTime created;

    public Post(String id, String name, String link, String description, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.description = description;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
