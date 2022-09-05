package br.com.ada.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.ada.assembler.RankAssembler;
import br.com.ada.controller.spec.RankControllerSpec;
import br.com.ada.domain.Rank;
import br.com.ada.dto.EventLogDTO;
import br.com.ada.dto.RankDTO;
import br.com.ada.enumarator.EventsEnum;
import br.com.ada.service.RankService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/rank")
public class RankController implements RankControllerSpec {

	private final RankService RankService;
	private final PagedResourcesAssembler<Rank> pagedResourcesAssembler;
	private final RankAssembler rankAssembler;

	@Override
	@GetMapping
	public ResponseEntity<PagedModel<RankDTO>> getAll(
			@SortDefault.SortDefaults(@SortDefault(sort = "points", direction = Sort.Direction.DESC)) Pageable page) {
		log.info(new EventLogDTO(EventsEnum.GET_ALL_RANK, "").toString());
		var rankPage = RankService.getAll(page);
		var meditationsPageDTO = pagedResourcesAssembler.toModel(rankPage, rankAssembler);

		return new ResponseEntity<>(meditationsPageDTO, HttpStatus.OK);
	}

	public ResponseEntity<CollectionModel<RankDTO>> getAll() {
		return ResponseEntity.status(HttpStatus.OK).body(rankAssembler.toCollectionModel(RankService.findAll()));
	}

}
