package natcash.news;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NewsApplication {
	private static final Logger logger = LoggerFactory.getLogger(NewsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(NewsApplication.class, args); 

	}
}
