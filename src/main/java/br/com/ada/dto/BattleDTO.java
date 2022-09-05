package br.com.ada.dto;

import javax.validation.constraints.NotEmpty;

import br.com.ada.enumarator.Results;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BattleDTO {

	private Long id;
	@NotEmpty(message = "M1002")
	private MovieDTO firstMovie;
	@NotEmpty(message = "M1002")
	private MovieDTO secondMovie;
	private Results results;

}
