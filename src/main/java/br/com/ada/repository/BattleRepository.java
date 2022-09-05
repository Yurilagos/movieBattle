package br.com.ada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ada.domain.Battle;

public interface BattleRepository extends JpaRepository<Battle, Long> {

}
