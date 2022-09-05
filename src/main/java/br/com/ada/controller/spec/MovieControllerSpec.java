package br.com.ada.controller.spec;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.ada.dto.MovieDTO;
import br.com.ada.erro.ApiExceptionHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Movie Spec", tags = "Movie")
public interface MovieControllerSpec {

	@ApiOperation(value = "Get Movie ", nickname = "Get Movie", response = MovieDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<MovieDTO> getMovie(@PathVariable String title);

	@ApiOperation(value = "Create new Movie ", nickname = "create", response = MovieDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<MovieDTO> createMovie(@PathVariable String title);

}
