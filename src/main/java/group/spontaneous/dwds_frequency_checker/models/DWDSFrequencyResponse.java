package group.spontaneous.dwds_frequency_checker.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DWDSFrequencyResponse {
	private int frequency;
	private int hits;
	private String total;
	private String q;
}
