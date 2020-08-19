package grab;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParsLocalDataTest {
    @Test
    public void whenTodayLocalData() throws ParseException {
        ParsLocalDataTime parsLocalDataTime = new ParsLocalDataTime();
        assertThat(parsLocalDataTime.transformDataTime("сегодня, 11:07"),
                is(LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 7))));
    }

    @Test
    public void whenYesterdayLocalData() throws ParseException {
        ParsLocalDataTime parsLocalDataTime = new ParsLocalDataTime();
        assertThat(parsLocalDataTime.transformDataTime("вчера, 11:07"),
                is(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(11, 7))));
    }

    @Test
    public void whenEmptyLocalData() throws ParseException {
        ParsLocalDataTime parsLocalDataTime = new ParsLocalDataTime();
        assertThat(parsLocalDataTime.transformDataTime(""),
                IsNull.nullValue());
    }

    @Test
    public void whenUsualLocalData() throws ParseException {
        ParsLocalDataTime parsLocalDataTime = new ParsLocalDataTime();
        assertThat(parsLocalDataTime.transformDataTime("29 июл 20, 15:09"),
                is(LocalDateTime.of(LocalDate.of(2020, 7, 29),
                        LocalTime.of(15, 9))));
    }

}