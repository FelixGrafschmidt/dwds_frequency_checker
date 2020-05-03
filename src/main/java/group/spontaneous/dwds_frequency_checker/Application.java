package group.spontaneous.dwds_frequency_checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import group.spontaneous.dwds_frequency_checker.models.DWDSFrequencyResponse;

@Component
public class Application {

	private RestTemplate restTemplate = new RestTemplate();

	@PostConstruct
	private void init() {

		List<String> words = new ArrayList<>();
		try {
			try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
				String line;
				while ((line = br.readLine()) != null) {
					words.add(line);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		var result = new ArrayList<String>();
		var url = "https://www.dwds.de/api/frequency";

		for (var word : words) {

			var response = restTemplate.getForObject(url + "?q=" + word, DWDSFrequencyResponse.class);

			if (response.getFrequency() >= 3) {
				result.add(word);
			}
		}

		System.out.println(result);
	}
}
