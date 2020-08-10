package quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.quartz.JobBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        int interval = readProperties("./src/main/resources/rabbit.properties");
        try {
            List<Long> store = new ArrayList<>();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            JobDetail job = newJob(Rabbit.class).usingJobData(data).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(interval)
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(5000);
            scheduler.shutdown();
            System.out.println(store);
        } catch (SchedulerException | InterruptedException | FileNotFoundException se) {
            se.printStackTrace();
        }
    }

    private static int readProperties(String patch) {
       int resalt = 0;
        String[] temp;
        try (BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/rabbit.properties"))) {
            do {
                temp = in.readLine().split("=");
                if (temp[0].equals("rabbit.interval")) {
                    resalt = Integer.parseInt(temp[1]);
                }
            } while (temp.length != 0);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } return resalt;
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            List<Long> store = (List<Long>) context.getJobDetail().getJobDataMap().get("store");
            store.add(System.currentTimeMillis());
        }
    }
}
