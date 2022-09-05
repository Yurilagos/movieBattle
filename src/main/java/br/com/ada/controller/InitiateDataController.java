package br.com.ada.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ada.domain.Role;
import br.com.ada.dto.PlayerDTO;
import br.com.ada.service.MovieService;
import br.com.ada.service.PlayerService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Controller
@RequestMapping("/init")
public class InitiateDataController {

	private final PlayerService playerService;
	private final MovieService movieService;

	@GetMapping("/create-data")
	public ResponseEntity<?> createData() {

		playerService
				.create(PlayerDTO.builder().fullName("Player 1").login("player1").password("1234").build(), Role.ROLE_PLAYER);
		playerService.create(PlayerDTO.builder().fullName("Player 2").login("player2").password("4321").build(), Role.ROLE_PLAYER);
		playerService.create(PlayerDTO.builder().fullName("admin").login("admin").password("admin").build(), Role.ROLE_ADMIN);

		movieService.create("titanic");
		movieService.create("the rock");
		movieService.create("2 fast 2 furious");
		movieService.create("moon");
		movieService.create("Blue Jasmine");
		movieService.create("The Invisible");
		movieService.create("Spider Man");
		movieService.create("Star Wars");
		movieService.create("iron man");
		movieService.create("today");
		movieService.create("Today I Hang");
		movieService.create("My Neighbor Totoro");
		movieService.create("How to Train Your Dragon");
		movieService.create("Madagascar");
		movieService.create("Robin Hood");

		return ResponseEntity.ok(null);
	}

}
