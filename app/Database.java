import org.postgresql.core.QueryExecutor;
import play.db.*;
import play.mvc.Controller;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Singleton
public class Database {

    private play.db.Database db;
    private Connection con;

    @Inject
    public Database(play.db.Database db) {
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

    public Poll getPoll(UUID id) {
        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Poll WHERE id = ?");
            stmt.setString(1, id.toString());

            ResultSet result = stmt.executeQuery();
            if(result.isBeforeFirst())
                throw new NullPointerException("No Poll entry found for " + id.toString());

            String title = result.getString("title");
            HashMap<String, Answer> answers = getAnswers(id);

            return new Poll(id, title, answers);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private HashMap<String, Answer> getAnswers(UUID poll) {
        HashMap<String, Answer> answers = new HashMap<>();

        try {
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM Answer WHERE poll = ?");
            stmt.setString(1, poll.toString());

            ResultSet result = stmt.executeQuery();
            while(result.next()) {
                String desc = result.getString("desc");
                int votes = result.getInt("votes");

                answers.put(desc, new Answer(desc, votes));
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

}
