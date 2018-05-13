package objects;

import play.db.Databases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class VewoteDatabase {

    public static VewoteDatabase INSTANCE = new VewoteDatabase(
                    Databases.createFrom("org.postgresql.Driver", "jdbc:postgresql://localhost:5432/vewote?user=postgres"));

    private play.db.Database db;
    private Connection con;

    public VewoteDatabase(play.db.Database db) {
        this.db = db;
        this.con = db.getConnection();

        configure();
    }

    public void storePoll(Poll poll) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Poll VALUES(?, ?)");
            stmt.setString(1, poll.getId().toString());
            stmt.setString(2, poll.getTitle());

            stmt.executeUpdate();
            con.commit();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    private void storeAnswers(UUID poll, List<Answer> answers) {
        try {
            PreparedStatement stmt = con.prepareStatement("INSERT INTO Answer VALUES(?, ?, ?)");
            stmt.setString(1, poll.toString());

            for(Answer answer : answers) {
                stmt.setString(2, poll.toString());
                stmt.setString(3, answer.desc);
                stmt.setInt(4, answer.votes);

                stmt.executeUpdate();
            }

            con.commit();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Poll> getPolls() {
        List<Poll> polls = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Poll");

            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                UUID id = UUID.fromString(result.getString("id"));
                String title = result.getString("title");
                List<Answer> answers = getAnswers(id);

                polls.add(new Poll(id, title, answers));
            }
            return polls;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Answer> getAnswers(UUID poll) {
        List<Answer> answers = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Answer WHERE poll = ?");
            stmt.setString(1, poll.toString());

            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                String desc = result.getString("desc");
                int votes = result.getInt("votes");

                answers.add(new Answer(desc, votes));
            }

            return answers;
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void configure() {
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
