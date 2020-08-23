package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    private Connection cn;
    private static final String GREATE_TABLE = "create table rabbit (id serial primary key, data date);";
    private static final String INSERT_DATE = "insert into rabbit values (1, CURRENT_DATE);";

    public static void main(String[] args) {
        AlertRabbit alertRabbit = new AlertRabbit();
        int interval = alertRabbit.readProperties("./src/main/resources/rabbit.properties");
        alertRabbit.initConnection();
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }

    private int readProperties(String patch) {
        int resalt = 0;
        String[] temp = new String[2];
        try (BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/rabbit.properties"))) {
            do {
                temp = in.readLine().split("=");
                if (temp[0].equals("rabbit.interval")) {
                    resalt = Integer.parseInt(temp[1]);
                    break;
                }
            } while (temp.length != 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return resalt;
    }

    private void initConnection() {
        try (InputStream in = Rabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
