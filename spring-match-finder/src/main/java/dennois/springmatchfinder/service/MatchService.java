package dennois.springmatchfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class MatchService {

    private final WebClient webClient;

    @Autowired
    public MatchService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String fetchMatches() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("fdo", "Y");
        formData.add("mmfilter", "3");
        formData.add("discipline", "");
        formData.add("regval", "");
        formData.add("submit", "GO");

        return webClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
