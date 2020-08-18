package grab;

import grab.Post;

import java.util.List;

public interface Store {
    void save (Post post);
    List<Post> getAll();
}
