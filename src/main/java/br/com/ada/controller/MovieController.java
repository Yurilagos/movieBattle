package br.com.ada.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ada.controller.spec.MovieControllerSpec;
import br.com.ada.dto.MovieDTO;
import br.com.ada.service.MovieService;

@Controller
@RequestMapping("/movie")
public class MovieController implements MovieControllerSpec {

	@Autowired
	private MovieService movieService;

	@GetMapping("/find/{title}")
	@PreAuthorize("hasAnyRole('ADMIN', 'PLAYER')")
	public ResponseEntity<MovieDTO> getMovie(String title) {
		return ResponseEntity.status(HttpStatus.OK).body(movieService.getMovie(title));
	}

	@PostMapping("/create/{title}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<MovieDTO> createMovie(String title) {
		return ResponseEntity.status(HttpStatus.CREATED).body(movieService.create(title));
	}

}
