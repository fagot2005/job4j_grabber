package grab;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class ParsLocalDataTime {
    private String today = "сегодня";
    private String yesterday = "вчера";
    private DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yy");
    private LocalDate localDate = null;

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

    public LocalDateTime transformDataTime(String dataAndTimeOfTransform) {
        if (dataAndTimeOfTransform.length() == 0) {
            return null;
        }
        String[] dataTimeArray = dataAndTimeOfTransform.split(",");
        LocalDate localDate = transformData(dataTimeArray[0]);
        LocalTime localTime = transformTime(dataTimeArray[1].substring(1));
        return LocalDateTime.of(localDate, localTime);
    }

    public LocalDate transformData(String dataOfTransform)  {
        if (dataOfTransform.length() == 0) {
            return localDate;
        } else {
            if (dataOfTransform.equals(today)) {
                localDate = LocalDate.now();
            } else if (dataOfTransform.equals(yesterday)) {
                localDate = LocalDate.now().minusDays(1);
            } else {
                String[] localDataArray = dataOfTransform.split(" ");
                if (localDataArray[0].length() == 1) {
                    localDataArray[0] = "0" + localDataArray[0];
                }
                for (Map.Entry<String, String> month: MONTH_MAPER.entrySet()
                ) {
                    if (localDataArray[1].equals(month.getKey())) {
                        localDate = LocalDate.parse(localDataArray[0] + "/" + month.getValue() + "/" + localDataArray[2], formatterDate);
                        break;
                    }
                }
            }
        }
        return localDate;
    }

    private LocalTime transformTime(String localTime) {
        String[] localTimeArray = localTime.split(":");
        return LocalTime.of(Integer.parseInt(localTimeArray[0]), Integer.parseInt(localTimeArray[1]));
    }
}

