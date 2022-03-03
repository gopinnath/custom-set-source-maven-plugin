import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Example {

	private static Logger logger = Logger.getLogger("Example");
	
	public static void main(String[] args) {
		logger.info(ResourceBundle.getBundle("sample").getString("prop1"));
		logger.info(ResourceBundle.getBundle("sample").getString("prop2"));
	}
}