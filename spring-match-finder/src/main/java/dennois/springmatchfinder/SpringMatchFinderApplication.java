package dennois.springmatchfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


//TODO remove exclude and setup DB Connection
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class SpringMatchFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMatchFinderApplication.class, args);

	}

}
