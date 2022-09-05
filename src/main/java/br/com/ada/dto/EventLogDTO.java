package br.com.ada.dto;

import br.com.ada.enumarator.EventsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@Builder
public class EventLogDTO {

	private final EventsEnum event;
	private final Object body;

}