package dennois.springmatchfinder.bootstrap;

import dennois.springmatchfinder.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    private final MatchService matchService;

    @Autowired
    public StartupRunner(MatchService matchService) {
        this.matchService = matchService;
    }

    @Override
    public void run(String... args) {
        String result = matchService.fetchMatches();
        System.out.println(result);
    }
}