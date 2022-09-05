package br.com.ada.dto;

import java.util.List;

import br.com.ada.enumarator.Stats;
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
public class RoundDTO {

	private Long id;

	private PlayerDTO player;

	private Stats stats;

	private List<BattleDTO> battles;
}
