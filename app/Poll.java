import java.util.HashMap;
import java.util.UUID;

public class Poll {

    private UUID id;
    private String title;
    private HashMap<String, Answer> answer;

    public Poll(UUID id, String title, HashMap<String, Answer> answer) {
        this.id = id;
        this.title = title;
        this.answer = answer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Answer> getAnswer() {
        return answer;
    }

    public void setAnswer(HashMap<String, Answer> answer) {
        this.answer = answer;
    }
}
