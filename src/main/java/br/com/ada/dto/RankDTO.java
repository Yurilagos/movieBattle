package br.com.ada.dto;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class RankDTO extends RepresentationModel<RankDTO> {

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Long id;
	private String playerLogin;
	private Long roundId;
	private Double points;

}
