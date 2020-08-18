package grab;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    Post post = null;
    List<Post> posts = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse();
        String startLink = "https://www.sql.ru/forum/job-offers";
        sqlRuParse.list(startLink);
        System.out.println("Parsing successful");
    }

    @Override
    public List<Post> list(String link) throws IOException, ParseException {
        Document doc;
        int childNodeFirstDataPost = 11;
        int childNodeSecondDataPost = 0;
        for (int i = 1; i <= 5; i++) {
            doc = Jsoup.connect(link + "/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                String dataPost = td.parentNode().childNode(childNodeFirstDataPost).childNode(childNodeSecondDataPost).toString();
                Element href = td.child(0);
                String description = href.text();
                post = detail(href.attr("href"), dataPost, description);
                posts.add(post);
            }
        }
        return posts;
    }

    @Override
    public Post detail(String link, String datePost, String description) throws IOException, ParseException {
        int index = 1;
        String id = String.valueOf(posts.size() + 1);
        Document doc = Jsoup.connect(link).get();
        String text= doc.select(".msgBody").get(index).text();
        Post post = new Post(id, link, description, text, new ParsLocalData().transformData(datePost));
        return post;
    }
}
