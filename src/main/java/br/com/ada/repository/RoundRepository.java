package br.com.ada.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import br.com.ada.domain.Round;
import br.com.ada.enumarator.Stats;

public interface RoundRepository extends JpaRepository<Round, Long> {

	Optional<Round> findByPlayerLoginAndStatsEquals(String playerLogin, Stats stats);
	

}
