package grab;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PsqlStore implements Store, AutoCloseable{
    private Connection connection;
    private ConfigManager configManager;

    public PsqlStore(ConfigManager configManager) throws IOException {
        this.configManager = configManager;
        this.initConnection();
        //this.initTable();
    }

    public PsqlStore(Connection connection) {
        this.connection = connection;
    }

    private void initConnection() {
        try {
            Class.forName(configManager.get("driver-class-name"));
            connection = DriverManager.getConnection(
                    configManager.get("url"),
                    configManager.get("username"),
                    configManager.get("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private void initTable() throws IOException {
        String sql = String.join("", Files.readAllLines(Path.of("./db", "create_table.sql")));
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(Post post) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into post(name, link, description, create_date) values (?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, post.getName());
            ps.setString(2, post.getLink());
            ps.setString(3, post.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getString(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveAll(Iterable<Post> posts) {
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement ps
                         = connection.prepareStatement("insert into post(name, link, description, create_date) values (?, ?, ?, ?);")) {
                for (Post post : posts) {
                    ps.setString(1, post.getName());
                    ps.setString(2, post.getLink());
                    ps.setString(3, post.getDescription());
                    ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("select * from post;")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Post p = new Post(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("link"),
                            rs.getString("description"),
                            rs.getTimestamp("create_date").toLocalDateTime()
                    );
                    p.setId(rs.getString("id"));
                    posts.add(p);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post FondBiId(String id) {
        Post post = null;
        try (PreparedStatement ps = connection.prepareStatement("select * from post p where p.id = ?;")) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Post p = new Post(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("link"),
                            rs.getString("description"),
                            rs.getTimestamp("create_date").toLocalDateTime());
                    p.setId(rs.getString("id"));
                    post = p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public Timestamp lastItem() {
        return null;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws IOException, ParseException, SQLException {
        PsqlStore psqlStore = new PsqlStore(new ConfigManager("psqlstore.properties"));
        SqlRuParse sqlRuParse = new SqlRuParse();
        String startLink = "https://www.sql.ru/forum/job-offers";
        Map<String, Post> posts = sqlRuParse.list(startLink);
        for (Map.Entry<String, Post> post : posts.entrySet()
             ) {
            psqlStore.save(post.getValue());
        }
        System.out.println("Parsing successful");
    }
}


