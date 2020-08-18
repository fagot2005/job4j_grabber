package quartz;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ParsLocalData {
    String today = "сегодня";
    String yesterday = "вчера";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
    LocalDate localDate = null;
    String localDataCeng;

    private final static Map<String, String> MONTH_MAPER = new HashMap<>() {
        {
            put("янв", "01");
            put("фев", "02");
            put("мар", "03");
            put("апр", "04");
            put("май", "05");
            put("июн", "06");
            put("июл", "07");
            put("авг", "08");
            put("сен", "09");
            put("окт", "10");
            put("ноя", "11");
            put("дек", "12");
        }
    };

    public LocalDate transformData(String dataOfTransform) throws ParseException {
        if (dataOfTransform.length() == 0) {
            return localDate;
        } else {
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
                for (Map.Entry<String, String> month: MONTH_MAPER.entrySet()
                ) {
                    if (localDataArray[1].equals(month.getKey())) {
                        localDataCeng= parsMonth(localDataArray, month.getValue());
                        break;
                    }
                }
                localDate = LocalDate.parse(localDataCeng, formatter);
            }
        }
        return localDate;
    }

    private String parsMonth(String[] localDataArray, String month) {
        String localDataCeng;
        localDataCeng = localDataArray[0] + "/" + month + "/" + localDataArray[2];
        return localDataCeng;
    }
}

