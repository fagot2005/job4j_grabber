package quartz;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        SqlRuParse sqlRuParse = new SqlRuParse();
        List<ParsepShem> parsepShemList = new ArrayList<>();
        Document doc;
        for (int i = 1; i <= 5; i++) {
            if (i == 1) {
                doc  = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
            } else {
                doc = Jsoup.connect("https://www.sql.ru/forum/job-offers" + "/" + i).get();
            }
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                String link = href.attr("href");
                String description = href.text();
                Node node = td.parentNode().childNode(11);
                String[] dataPostTemp = node.toString().split(">");
                String[] dataPost = dataPostTemp[1].split("<");
                ParsepShem parsepShem = new ParsepShem(link, description, sqlRuParse.transform(dataPost[0]));
                parsepShemList.add(parsepShem);
                System.out.println(link + System.lineSeparator() + description + System.lineSeparator() + parsepShemList.get(parsepShemList.size()-1).getDataPost());
            }
        }
    }

    public LocalDate transform(String dataOfTransform) throws ParseException {
        String today = "сегодня";
        String yesterday = "вчера";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate localDate;
        String[] dataArray = dataOfTransform.split(",");
        if (dataArray[0].equals(today)) {
            localDate = LocalDate.now();
        } else if (dataArray[0].equals(yesterday)) {
            localDate = LocalDate.now().minusDays(1);
        } else {
            String[] localDataArray = dataArray[0].split(" ");
            String localDataCeng = "";
            if (localDataArray[0].length() == 1) {
                localDataArray[0] = "0" + localDataArray[0];
            }
            if (localDataArray[1].equals("янв")) {
                localDataCeng = localDataArray[0] + "/" + "01" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("фев")) {
                localDataCeng = localDataArray[0] + "/" + "02" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("мар")) {
                localDataCeng = localDataArray[0] + "/" + "03" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("апр")) {
                localDataCeng = localDataArray[0] + "/" + "04" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("май")) {
                localDataCeng = localDataArray[0] + "/" + "05" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("июн")) {
                localDataCeng = localDataArray[0] + "/" + "06" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("июл")) {
                localDataCeng = localDataArray[0] + "/" + "07" + "/" + localDataArray[2];
            } else if (localDataArray[1].equals("авг")) {
                localDataCeng = localDataArray[0] + "/" + "08" + "/" + localDataArray[2];
            }else if (localDataArray[1].equals("сен")) {
                localDataCeng = localDataArray[0] + "/" + "09" + "/" + localDataArray[2];
            }else if (localDataArray[1].equals("окт")) {
                localDataCeng = localDataArray[0] + "/" + "10" + "/" + localDataArray[2];
            }else if (localDataArray[1].equals("ноя")) {
                localDataCeng = localDataArray[0] + "/" + "11" + "/" + localDataArray[2];
            }else if (localDataArray[1].equals("дек")) {
                localDataCeng = localDataArray[0] + "/" + "08" + "/" + localDataArray[2];
            }
            localDate = LocalDate.parse(localDataCeng, formatter);
        }
        return localDate;
    }
}
