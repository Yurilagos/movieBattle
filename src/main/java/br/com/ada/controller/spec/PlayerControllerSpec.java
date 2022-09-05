package br.com.ada.controller.spec;

import org.springframework.http.ResponseEntity;

import br.com.ada.dto.PlayerDTO;
import br.com.ada.erro.ApiExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api(value = "Player Spec", tags = "Player")
public interface PlayerControllerSpec {

	@ApiOperation(value = "Create new Player ", nickname = "create", response = PlayerDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<PlayerDTO> create(@RequestBody PlayerDTO playerDTO);
}
