package br.com.ada.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

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
public class PlayerDTO {

	@NotEmpty(message = "M1003")
	private String login;
	private String fullName;
	@NotEmpty(message = "M1004")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private List<RoleDTO> roles;

}
