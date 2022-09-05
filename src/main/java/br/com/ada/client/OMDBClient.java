package br.com.ada.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.ada.dto.MovieOmdbDTO;

@FeignClient(value = "omdb", url = "${omdb-api.url}")
public interface OMDBClient {

	@GetMapping
	MovieOmdbDTO getMovie(@RequestParam("t") String title, @RequestParam("apikey") String key);

}
