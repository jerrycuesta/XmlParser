package saxfeedparser;
import java.util.List;

public interface FeedParser {
	List<Message> parse();
}