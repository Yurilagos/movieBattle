package br.com.ada.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ada.controller.spec.PlayerControllerSpec;
import br.com.ada.domain.Role;
import br.com.ada.dto.EventLogDTO;
import br.com.ada.dto.PlayerDTO;
import br.com.ada.enumarator.EventsEnum;
import br.com.ada.service.PlayerService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/player")
public class PlayerController implements PlayerControllerSpec {

	private final PlayerService playerService;

	@Override
	@PostMapping
	public ResponseEntity<PlayerDTO> create(@RequestBody PlayerDTO playerDTO) {
		log.info(new EventLogDTO(EventsEnum.POST_PLAYER, playerDTO.toString()).toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(playerService.create(playerDTO, Role.ROLE_PLAYER));
	}

}
