package br.com.ada.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1350813801989867921L;

	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_PLAYER = "ROLE_PLAYER";

	@Id
	@Getter
	@Setter
	private String roleName;

	@ManyToMany(mappedBy = "roles")
	@Setter
	@JsonIgnore
	private List<Player> players;

	@Override
	public String getAuthority() {
		return this.roleName;
	}

	public List<Player> getPlayers() {
		if (players != null) {
			return players;
		}
		return new ArrayList<>();
	}

}
