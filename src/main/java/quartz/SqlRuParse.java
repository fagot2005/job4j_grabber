package quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        Post post = new Post(id, link, description, text, transformData(datePost));
        return post;
    }

    public LocalDate transformData(String dataOfTransform) throws ParseException {
        String today = "сегодня";
        String yesterday = "вчера";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate localDate;
        String localDataCeng;
        String[] dataArray = dataOfTransform.split(",");
        if (dataArray[0].equals(today)) {
            localDate = LocalDate.now();
        } else if (dataArray[0].equals(yesterday)) {
            localDate = LocalDate.now().minusDays(1);
        } else {
            String[] localDataArray = dataArray[0].split(" ");
            localDataCeng = "";
            if (localDataArray[0].length() == 1) {
                localDataArray[0] = "0" + localDataArray[0];
            }
            if (localDataArray[1].equals("янв")) {
                localDataCeng = parsMonth(localDataArray, "01");
            } else if (localDataArray[1].equals("фев")) {
                localDataCeng = parsMonth(localDataArray, "02");
            } else if (localDataArray[1].equals("мар")) {
                localDataCeng = parsMonth(localDataArray, "03");
            } else if (localDataArray[1].equals("апр")) {
                localDataCeng = parsMonth(localDataArray, "04");
            } else if (localDataArray[1].equals("май")) {
                localDataCeng = parsMonth(localDataArray, "05");
            } else if (localDataArray[1].equals("июн")) {
                localDataCeng = parsMonth(localDataArray, "06");
            } else if (localDataArray[1].equals("июл")) {
                localDataCeng = parsMonth(localDataArray, "07");
            } else if (localDataArray[1].equals("авг")) {
                localDataCeng = parsMonth(localDataArray, "08");
            } else if (localDataArray[1].equals("сен")) {
                localDataCeng = parsMonth(localDataArray, "09");
            } else if (localDataArray[1].equals("окт")) {
                localDataCeng = parsMonth(localDataArray, "10");
            } else if (localDataArray[1].equals("ноя")) {
                localDataCeng = parsMonth(localDataArray, "11");
            } else if (localDataArray[1].equals("дек")) {
                localDataCeng = parsMonth(localDataArray, "08");
            }
            localDate = LocalDate.parse(localDataCeng, formatter);
        }
        return localDate;
    }

    private String parsMonth(String[] localDataArray, String s) {
        String localDataCeng;
        localDataCeng = localDataArray[0] + "/" + s + "/" + localDataArray[2];
        return localDataCeng;
    }
}
