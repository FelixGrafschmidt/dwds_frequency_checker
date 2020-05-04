package group.spontaneous.dwds_frequency_checker;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import group.spontaneous.dwds_frequency_checker.models.DWDSFrequencyResponse;

@Component
public class Application {

	private RestTemplate restTemplate = new RestTemplate();

	@PostConstruct
	private void init() {
		var words = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				words.add(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		var result = new ArrayList<String>();
		var url = "https://www.dwds.de/api/frequency?q=";
		var mapper = new ObjectMapper();
		var tempResult = new ArrayList<String>();
		var errors = "";

		for (var word : words) {
			try {
				var response = restTemplate.getForObject(url + word, DWDSFrequencyResponse.class);
				if (response.getFrequency() >= 3) {
					result.add(word);
					tempResult.add(word);
					System.out.println(word);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				errors += word;
				e.printStackTrace();
			}
			var tempIndex = 0;
			if (result.size() % 100 == 0) {
				try {
					var fileContent = mapper.writeValueAsString(tempResult);

					var path = Paths.get("words_temp_" + tempIndex++ + ".json");
					var strToBytes = fileContent.getBytes();

					Files.write(path, strToBytes);
					tempResult = new ArrayList<String>();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			var fileContent = mapper.writeValueAsString(result);

			var path = Paths.get("words.json");
			var strToBytes = fileContent.getBytes();

			Files.write(path, strToBytes);

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			var fileContent = mapper.writeValueAsString(errors);

			var path = Paths.get("errors.txt");
			var strToBytes = fileContent.getBytes();

			Files.write(path, strToBytes);
			tempResult = new ArrayList<String>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(
				"###########################\n###########################\n###########################\n###########################\n###########################\n");
		System.exit(0);
	}
}
