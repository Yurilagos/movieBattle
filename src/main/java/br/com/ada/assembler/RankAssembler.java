package br.com.ada.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import br.com.ada.controller.RankController;
import br.com.ada.domain.Rank;
import br.com.ada.dto.RankDTO;

@Component
public class RankAssembler extends RepresentationModelAssemblerSupport<Rank, RankDTO> {

	public RankAssembler() {
        super(RankController.class, RankDTO.class);
    }

	@Override
	public RankDTO toModel(Rank entity) {
		RankDTO rankDTO = instantiateModel(entity);
		rankDTO.setId(entity.getId());
		rankDTO.setPlayerLogin(entity.getPlayer().getLogin());
		rankDTO.setPoints(entity.getPoints());
		rankDTO.setRoundId(entity.getRound().getId());
		return rankDTO;
	}

	@Override
	public CollectionModel<RankDTO> toCollectionModel(Iterable<? extends Rank> entities) {
		CollectionModel<RankDTO> orders = super.toCollectionModel(entities);
		orders.add(linkTo(methodOn(RankController.class).getAll()).withSelfRel());
		return orders;
	}

}
