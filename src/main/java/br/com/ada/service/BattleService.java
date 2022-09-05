package br.com.ada.service;

import static java.util.Objects.isNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.ada.domain.Battle;
import br.com.ada.domain.Movie;
import br.com.ada.domain.Player;
import br.com.ada.domain.Rank;
import br.com.ada.domain.Round;
import br.com.ada.dto.BattleDTO;
import br.com.ada.dto.EventLogDTO;
import br.com.ada.dto.MovieDTO;
import br.com.ada.enumarator.EventsEnum;
import br.com.ada.enumarator.Results;
import br.com.ada.enumarator.Stats;
import br.com.ada.erro.ApiBusinessException;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.BattleRepository;
import br.com.ada.repository.RoundRepository;
import br.com.ada.util.MapperUtil;
import br.com.ada.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class BattleService {

	private final BattleRepository battleRepository;
	private final MovieService movieService;
	private final MapperUtil mapperUtil;
	private final PlayerService playerService;
	private final RoundRepository roundRepository;
	private final RankService rankService;

	public BattleDTO fight() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String login = principal.toString();

		return getOpenRoundOrCreateNew(login);
	}

	private BattleDTO getOpenRoundOrCreateNew(String login) {
		var entity = new Round();
		var battleDTO = new BattleDTO();
		Player player = playerService.findPlayerByLogin(login);

		Optional<Round> optionalRound = roundRepository.findByPlayerLoginAndStatsEquals(player.getLogin(), Stats.OPEN);

		if (optionalRound.isPresent()) {
			entity = optionalRound.get();
			Optional<Battle> noResultsBattle = entity.getBattles().stream()
					.filter(battle -> isNull(battle.getResults())).findFirst();

			if (noResultsBattle.isPresent()) {
				return mapperUtil.convertTo(noResultsBattle.get(), BattleDTO.class);
			} else {
				List<Movie> movieList = movieService.getRandomMovieWithoutMatchers(entity.getBattles());
				if (CollectionUtils.isNotEmpty(movieList)) {
					Battle battle = battleRepository.save(Battle.builder().firstMovie(movieList.get(0)).secondMovie(movieList.get(1)).build());
					entity.getBattles().add(battle);
					battleDTO = mapperUtil.convertTo(battle, BattleDTO.class);
				} else {
					entity.setStats(Stats.CLOSED);
					Rank rank = Rank.builder().player(player).points(getPoints(entity)).round(entity).build();
					rankService.saveRankPoints(rank);
					throw new ApiErrorException("M1008", HttpStatus.BAD_REQUEST);
				}
				roundRepository.save(entity);
			}

		} else {
			battleDTO = initRound(entity, player);
		}

		return battleDTO;

	}

	private BattleDTO initRound(Round entity, Player player) {
		entity.setPlayer(player);
		entity.setStats(Stats.OPEN);
		Movie firstMovie = movieService.getRandomMovie();
		Movie secondMovie = movieService.getRandomMovie();

		while (firstMovie.getId() == secondMovie.getId()) {
			secondMovie = movieService.getRandomMovie();
		}
		
		Battle battle = battleRepository.save(Battle.builder().firstMovie(firstMovie).secondMovie(secondMovie).build());
		entity.setBattles(Arrays.asList(battle));
		roundRepository.save(entity);
		return mapperUtil.convertTo(battle, BattleDTO.class);
	}

	public BattleDTO create(Long firstMovie, Long secondMovie) {
		MovieDTO firstMovieDTO = movieService.findMovieById(firstMovie);
		MovieDTO secondMovieDTO = movieService.findMovieById(secondMovie);

		BattleDTO battleDTO = BattleDTO.builder().firstMovie(firstMovieDTO).secondMovie(secondMovieDTO).build();
		Battle battle = battleRepository.save(mapperUtil.convertTo(battleDTO, Battle.class));

		return mapperUtil.convertTo(battle, BattleDTO.class);
	}

	public String choose(Long movieId) {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String login = principal.toString();

		var entity = new Round();
		Player player = playerService.findPlayerByLogin(login);

		Optional<Round> optionalRound = roundRepository.findByPlayerLoginAndStatsEquals(player.getLogin(), Stats.OPEN);

		if (optionalRound.isPresent()) {
			entity = optionalRound.get();
			Battle noResultsBattle = entity.getBattles().stream().filter(battle -> isNull(battle.getResults()))
					.findFirst().orElseThrow(() -> new ApiErrorException("M1007", HttpStatus.BAD_REQUEST));

			return getBattleResult(entity, movieId, noResultsBattle, player);
		}
		throw new ApiErrorException("M1007", HttpStatus.BAD_REQUEST);
	}

	private String getBattleResult(Round entity, Long movieId, Battle noResultsBattle, Player player) {
		Movie firstMovie = noResultsBattle.getFirstMovie();
		Movie secondMovie = noResultsBattle.getSecondMovie();

		if (firstMovie.getId() != movieId && secondMovie.getId() != movieId) {
			throw new ApiErrorException("M1010", HttpStatus.BAD_REQUEST);
		}

		Long winnerId = getWinnerId(firstMovie, secondMovie);

		if (winnerId == movieId) {
			noResultsBattle.setResults(Results.RIGHT);
		} else {
			noResultsBattle.setResults(Results.WRONG);
			entity.plusOneErrorAmount();

			if (entity.getErrorAmount() >= 3) {
				entity.setStats(Stats.CLOSED);
				Rank rank = Rank.builder().player(player).points(getPoints(entity)).round(entity).build();
				rankService.saveRankPoints(rank);
			}

			roundRepository.save(entity);
		}
		battleRepository.save(noResultsBattle);

		return String.format("Your Answer is %s", noResultsBattle.getResults());
	}

	private Long getWinnerId(Movie firstMovie, Movie secondMovie) {
		return firstMovie.getScore() > secondMovie.getScore() ? firstMovie.getId() : secondMovie.getId();
	}

	public String stop() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String login = principal.toString();

		var entity = new Round();
		Player player = playerService.findPlayerByLogin(login);

		Optional<Round> optionalRound = roundRepository.findByPlayerLoginAndStatsEquals(player.getLogin(), Stats.OPEN);

		if (optionalRound.isPresent()) {
			entity = optionalRound.get();
			Optional<Battle> noResultsBattle = entity.getBattles().stream()
					.filter(battle -> isNull(battle.getResults())).findFirst();
			if (noResultsBattle.isPresent()) {
				Battle battle = noResultsBattle.get();
				battle.setResults(Results.WRONG);
				battleRepository.save(battle);
			}
			entity.setStats(Stats.CLOSED);
			roundRepository.save(entity);
		} else {
			throw new ApiBusinessException("M1009", HttpStatus.BAD_REQUEST);
		}

		Rank rank = Rank.builder().player(player).points(getPoints(entity)).round(entity).build();
		rankService.saveRankPoints(rank);

		return String.format("Game Over, you got %s Points", rank.getPoints());
	}

	private Double getPoints(Round round) {
		double quantity = round.getBattles().size();
		double correct = round.getBattles().stream().filter(battle -> battle.getResults().equals(Results.RIGHT))
				.count();
		double percent = (correct / quantity) * 100d;
		log.info("Quantity -> " + quantity);
		log.info("correct -> " + correct);
		log.info("percent -> " + percent);
		log.info(new EventLogDTO(EventsEnum.RANK_POINTS, "POINTS = " + (percent * quantity)).toString());

		return Utils.trunc(percent * quantity);
	}

}
