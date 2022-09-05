package br.com.ada.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ada.controller.spec.BattleControllerSpec;
import br.com.ada.dto.BattleDTO;
import br.com.ada.dto.EventLogDTO;
import br.com.ada.enumarator.EventsEnum;
import br.com.ada.service.BattleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/battle")
@PreAuthorize("hasRole('PLAYER')")
public class BattleController implements BattleControllerSpec {

	private final BattleService battleService;

	@Override
	@PostMapping("/create/fristMovie/{firstMovie}/secondMovie/{secondMovie}")
	public ResponseEntity<BattleDTO> create(Long firstMovie, Long secondMovie) {
		log.info(new EventLogDTO(EventsEnum.POST_BATTLE, "fristMovie = " + firstMovie + "secondMovie = " + secondMovie)
				.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(battleService.create(firstMovie, secondMovie));
	}

	@PostMapping("/fight")
	public ResponseEntity<BattleDTO> fight() {
		log.info(new EventLogDTO(EventsEnum.POST_FIGHT_BATTLE, "").toString());
		return ResponseEntity.status(HttpStatus.OK).body(battleService.fight());
	}

	@PatchMapping("/choose/{movieId}")
	public ResponseEntity<String> choose(Long movieId) {
		log.info(new EventLogDTO(EventsEnum.PATCH_CHOOSE_BATTLE, "movieId = " + movieId).toString());
		return ResponseEntity.status(HttpStatus.OK).body(battleService.choose(movieId));
	}

	@Override
	@PatchMapping("/stop")
	public ResponseEntity<String> stopRound() {
		log.info(new EventLogDTO(EventsEnum.PATCH_STOP_BATTLE, "").toString());
		return ResponseEntity.status(HttpStatus.OK).body(battleService.stop());
	}

}
