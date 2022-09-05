package br.com.ada.controller.spec;

import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.ada.dto.BattleDTO;
import br.com.ada.erro.ApiExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Battle Spec", tags = "Battle")
public interface BattleControllerSpec {

	@ApiOperation(value = "Create new Battle ", nickname = "create", response = BattleDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<BattleDTO> create(@PathVariable Long firstMovie, @PathVariable Long secondMovie);

	@ApiOperation(value = "Choose movie", nickname = "choose")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<String> choose(@PathVariable @NotNull(message = "M1001") Long movieId);

	@ApiOperation(value = "Stop Round Game", nickname = "Stop Round")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<String> stopRound();

}
