package grab;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface Parse {
    Map<String, Post> list(String link) throws IOException, ParseException;

    Post detail(String link, String datePost, String description) throws IOException, ParseException;

    List<String> resources();
}
