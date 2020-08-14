package quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
//        Elements row = doc.select(".postslisttopic");
//        System.out.println(row);
//        for (Element td : row) {
//            Element href = td.child(0);
//            System.out.println(href.attr("href"));
//            System.out.println(href.text());
//        }
        Elements data_job = doc.select(".altCol");
        //System.out.println(data_job);
        for (Element td : data_job) {
            Element href = td.after("style=\"text-align:center\" class=\"altCol\"");
            System.out.println(href.after("style=\"text-align:center\" class=\"altCol\""));
            System.out.println(href.text());
        }
    }
}
