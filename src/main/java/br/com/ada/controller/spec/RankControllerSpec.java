package br.com.ada.controller.spec;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import br.com.ada.dto.RankDTO;
import br.com.ada.erro.ApiExceptionHandler;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface RankControllerSpec {

	@ApiOperation(value = "Get all Rank", nickname = "getAll")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Unexpected Error", response = ApiExceptionHandler.class) })
	ResponseEntity<PagedModel<RankDTO>> getAll(Pageable page);

}
