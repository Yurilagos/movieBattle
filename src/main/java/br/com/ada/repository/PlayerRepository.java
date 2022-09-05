package br.com.ada.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.ada.domain.Player;

@Repository
public interface PlayerRepository extends CrudRepository<Player, String>{
	
	Optional<Player> findByLogin(String login);

}
