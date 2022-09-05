package br.com.ada.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.ada.domain.Rank;
import br.com.ada.dto.RankDTO;
import br.com.ada.repository.RankRepository;
import br.com.ada.util.MapperUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RankService {

	private final RankRepository rankRepository;
	private final MapperUtil mapperUtil;

	public RankDTO saveRankPoints(Rank rank) {
		return mapperUtil.convertTo(rankRepository.save(rank), RankDTO.class);
	}

	public Page<Rank> getAll(Pageable page) {
		return rankRepository.findAll(page);
	}

	public List<Rank> findAll() {
		return rankRepository.findAll();
	}

}
