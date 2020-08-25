package grab;

import com.sun.net.httpserver.HttpServer;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab{

    private ConfigManager cfg;

    public Store store() throws IOException {
        return new PsqlStore(cfg);
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg () {
        cfg =  new ConfigManager("psqlstore.properties");
    }

    private Predicate<String> searchCondition() {
        return name -> {
            String[] include = cfg.get("lenguage-include").split(",");
            String[] exclude = cfg.get("lenguage-exclude").split(",");
            String lowerName = name.toLowerCase();
            boolean resalt = true;
            for (String in : include) {
                if (!lowerName.contains(in)) {
                    resalt = false;
                }
            }
            if (resalt) {
                for (String ex : exclude) {
                    if (lowerName.contains(ex)) {
                        resalt = false;
                        break;
                    }
                }
            }
            return resalt;
        };
    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        data.put("condition", searchCondition());
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(cfg.get("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            Predicate<String> condition = (Predicate<String>) map.get("conditiom");
            Timestamp lastData = store.lastItem();
            Predicate<Timestamp> until = (data) -> data.before(lastData);
            List<Post> posts = new LinkedList<>();

            for (String resourse : parse.resources()) {
                List<Post> urlPosts = null;
                try {
                    urlPosts = parse.list(resourse);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (urlPosts.isEmpty()) {
                    break;
                }
                posts.addAll(urlPosts.stream()
                        .filter(p -> condition.test(p.getLink()))
                        .collect(Collectors.toList()));
            }
            posts.forEach(p -> {
                try {
                    p.setDescription(parse.detail(p.getLink(), p.getCreated(), p.getDescription()).getDescription());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            });
            store.saveAll(posts);
        }
    }

    public void web(Store store) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 9000), 0);
            server.createContext("/vacancies", exchange -> {
                List<Post> posts = store.getAll();
                StringJoiner html = new StringJoiner(System.lineSeparator());
                html.add("<!DOCTYPE html>");
                html.add("<html>");
                html.add("<head>");
                html.add("<meta charset=\"UTF-8\">");
                html.add("<title>Vacancies</title>");
                html.add("</head>");
                html.add("<body>");

                html.add("<table style=\"border: 1px solid black;\">");
                html.add("<tr style=\"border: 1px solid black;\">");
                html.add("<th style=\"border: 1px solid black;\">Name</th>");
                html.add("<th style=\"border: 1px solid black;\">Date</th>");
                html.add("<th style=\"border: 1px solid black;\">Description</th>");
                html.add("</tr>");

                for (Post post : posts) {
                    html.add("<tr style=\"border: 1px solid black;\">");
                    html.add(String.format("<td style=\"border: 1px solid black;\"><a href=\"%s\">%s</a></td>", post.getLink(), post.getLink()));
                    html.add(String.format("<td style=\"border: 1px solid black;\">%s</td>", post.getCreated()));
                    html.add(String.format("<td style=\"border: 1px solid black;\">%s</td>", post.getDescription()));
                    html.add("</tr>");
                }

                html.add("</table>");

                html.add("</body>");
                html.add("</html>");

                byte[] bytes = html.toString().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().put("Content-Type", List.of("text/html", "charset=UTF-8"));
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                    os.flush();
                }
            });
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
        grab.web(store);
    }
}
