package br.com.ada.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.ada.domain.Player;
import br.com.ada.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {

	public Role findByPlayersIn(List<Player> usuarios);

	public Optional<Role> findByRoleName(String roleName);

}