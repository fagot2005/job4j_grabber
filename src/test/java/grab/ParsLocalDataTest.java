package grab;

import grab.ParsLocalData;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDate;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ParsLocalDataTest {
    @Test
    public void whenTodayLocalData() throws ParseException {
        ParsLocalData parsLocalData = new ParsLocalData();
        assertThat(parsLocalData.transformData("сегодня, 11:07"),
                is(LocalDate.now()));
    }

    @Test
    public void whenYesterdayLocalData() throws ParseException {
        ParsLocalData parsLocalData = new ParsLocalData();
        assertThat(parsLocalData.transformData("вчера, 11:07"),
                is(LocalDate.now().minusDays(1)));
    }

    @Test
    public void whenEmptyLocalData() throws ParseException {
        ParsLocalData parsLocalData = new ParsLocalData();
        assertThat(parsLocalData.transformData(""),
                IsNull.nullValue());
    }

    @Test
    public void whenUsualLocalData() throws ParseException {
        ParsLocalData parsLocalData = new ParsLocalData();
        assertThat(parsLocalData.transformData("29 июл 20, 15:09"),
                is(LocalDate.of(2020, 7, 29)));
    }

}