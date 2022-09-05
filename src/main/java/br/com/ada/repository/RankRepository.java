package br.com.ada.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ada.domain.Rank;

public interface RankRepository extends JpaRepository<Rank, Long> {

}
