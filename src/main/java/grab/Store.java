package grab;

import grab.Post;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public interface Store {
    void save (Post post) throws SQLException;
    void saveAll(Iterable<Post> posts);
    List<Post> getAll();
    Post FondBiId(String id);
    Timestamp lastItem();
}
