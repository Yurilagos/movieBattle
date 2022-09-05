package br.com.ada.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.ada.datamock.DataMock;
import br.com.ada.domain.Battle;
import br.com.ada.domain.Movie;
import br.com.ada.domain.Player;
import br.com.ada.domain.Rank;
import br.com.ada.domain.Round;
import br.com.ada.dto.BattleDTO;
import br.com.ada.enumarator.Stats;
import br.com.ada.erro.ApiBusinessException;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.BattleRepository;
import br.com.ada.repository.RoundRepository;
import br.com.ada.util.MapperUtil;

@ExtendWith(MockitoExtension.class)
public class BattleServiceTest {

	@InjectMocks
	private BattleService battleService;

	@Mock
	private BattleRepository battleRepository;
	@Mock
	private PlayerService playerService;
	@Mock
	private MovieService movieService;
	@Mock
	private RoundRepository roundRepository;
	@Mock
	private MapperUtil mapperUtil;
	@Mock
	private Authentication authentication;
	@Mock
	private SecurityContext securityContext;

	@Mock
	private RankService rankService;

	private final DataMock dataMock = DataMock.getInstance();

	@DisplayName("should create new Round with successful")
	@Test
	void shouldCreateNewRoundWithSuccessful() {
		Player player = dataMock.getPlayer();
		Movie movie = dataMock.getMovie();
		Movie movie2 = dataMock.getMovie2();
		Battle battle = dataMock.getBattle();
		BattleDTO battleDTO = dataMock.getBattleDTO();
		Round roundWithBattle = dataMock.getOpenRoundWithBattle();

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);

		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.empty());
		when(movieService.getRandomMovie()).thenReturn(movie, movie2);
		when(battleRepository.save(any(Battle.class))).thenReturn(battle);
		when(roundRepository.save(any(Round.class))).thenReturn(roundWithBattle);
		when(mapperUtil.convertTo(battle, BattleDTO.class)).thenReturn(battleDTO);

		BattleDTO result = battleService.fight();

		assertNotNull(result);
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(roundRepository, atLeastOnce()).save(any(Round.class));

	}

	@DisplayName("should return the same battle when have not reuslt")
	@Test
	void shouldReturnTheSameBattleWhenHaveNotResult() {
		Player player = dataMock.getPlayer();
		Battle battle = dataMock.getBattle();
		BattleDTO battleDTO = dataMock.getBattleDTO();
		Round roundWithBattle = dataMock.getOpenRoundWithBattle();

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);

		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));
		when(mapperUtil.convertTo(battle, BattleDTO.class)).thenReturn(battleDTO);

		BattleDTO result = battleService.fight();

		assertNotNull(result);
		assertEquals(roundWithBattle.getBattles().get(0).getFirstMovie().getTitle(), result.getFirstMovie().getTitle());
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(playerService, atLeastOnce()).findPlayerByLogin(anyString());

	}

	@DisplayName("should return new battle when the last battle has resilt")
	@Test
	void shouldReturnNewBattleWhenTheLastBattleHasResult() {
		Player player = dataMock.getPlayer();
		Battle battle = dataMock.getBattleWithIronMandAndTheRock();
		BattleDTO battleDTO = dataMock.getBattleWithIronMandAndTheRockDTO();
		Round roundWithBattle = dataMock.getOpenRoundWithBattleResult();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);

		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));
		when(movieService.getRandomMovieWithoutMatchers(roundWithBattle.getBattles())).thenReturn(movies);
		when(battleRepository.save(any(Battle.class))).thenReturn(battle);
		when(mapperUtil.convertTo(battle, BattleDTO.class)).thenReturn(battleDTO);

		BattleDTO result = battleService.fight();

		assertNotNull(result);
		assertEquals(roundWithBattle.getBattles().get(1).getFirstMovie().getTitle(), result.getFirstMovie().getTitle());
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(playerService, atLeastOnce()).findPlayerByLogin(anyString());
		verify(battleRepository, atLeastOnce()).save(any(Battle.class));
		verify(movieService, atLeastOnce()).getRandomMovieWithoutMatchers(roundWithBattle.getBattles());

	}

	@DisplayName("should throw exception when game over")
	@Test
	void shouldThrowExceptionWhenGameOver() {
		Player player = dataMock.getPlayer();
		Round roundWithBattle = dataMock.getOpenRoundWithBattleResult();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);

		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));
		when(rankService.saveRankPoints(any(Rank.class))).thenReturn(null);

		ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {

			when(movieService.getRandomMovieWithoutMatchers(roundWithBattle.getBattles())).thenReturn(null);

			battleService.fight();
		});
		assertThat(exception.getCode()).isEqualToIgnoringCase("M1008");
	}

	@DisplayName("should return right Result")
	@Test
	void shouldReturnRightResult() {
		Player player = dataMock.getPlayer();
		Battle battle = dataMock.getBattleWithIronMandAndTheRock();
		Round roundWithBattle = dataMock.getOpenRoundWithBattle();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);
		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));
		when(battleRepository.save(any(Battle.class))).thenReturn(battle);

		String result = battleService.choose(1l);

		assertNotNull(result);
		assertEquals(result, "Your Answer is RIGHT");
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(playerService, atLeastOnce()).findPlayerByLogin(anyString());
		verify(battleRepository, atLeastOnce()).save(any(Battle.class));

	}

	@DisplayName("should return wrong Result")
	@Test
	void shouldReturnWrongResult() {
		Player player = dataMock.getPlayer();
		Battle battle = dataMock.getBattleWithIronMandAndTheRock();
		Round roundWithBattle = dataMock.getOpenRoundWithBattle();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);
		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));
		when(battleRepository.save(any(Battle.class))).thenReturn(battle);
		roundWithBattle.setErrorAmount(1);
		when(roundRepository.save(any(Round.class))).thenReturn(roundWithBattle);

		String result = battleService.choose(2l);

		assertNotNull(result);
		assertEquals(result, "Your Answer is WRONG");
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(playerService, atLeastOnce()).findPlayerByLogin(anyString());
		verify(battleRepository, atLeastOnce()).save(any(Battle.class));

	}

	@DisplayName("should stop battle")
	@Test
	void shouldStopBattle() {
		Player player = dataMock.getPlayer();
		Round roundWithBattle = dataMock.getOpenRoundWithBattleResult();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);
		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
				.thenReturn(Optional.of(roundWithBattle));

		roundWithBattle.setStats(Stats.CLOSED);
		when(roundRepository.save(any(Round.class))).thenReturn(roundWithBattle);

		when(rankService.saveRankPoints(any(Rank.class))).thenReturn(null);

		String result = battleService.stop();

		assertNotNull(result);
		assertEquals(result, "Game Over, you got 100.0 Points");
		verify(roundRepository, atLeastOnce()).findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class));
		verify(playerService, atLeastOnce()).findPlayerByLogin(anyString());
	}

	@DisplayName("should throw exception when try to stop game that dont exist")
	@Test
	void shouldThrowExceptionWhenTryToStopGameThatDontExist() {
		Player player = dataMock.getPlayer();
		List<Movie> movies = new ArrayList<>();
		movies.add(dataMock.getMovieIronMan());
		movies.add(dataMock.getMovieTheRock());

		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(securityContext.getAuthentication().getPrincipal()).thenReturn("player1");
		SecurityContextHolder.setContext(securityContext);
		when(playerService.findPlayerByLogin(anyString())).thenReturn(player);
		when(roundRepository.findByPlayerLoginAndStatsEquals(anyString(), any(Stats.class)))
		.thenReturn(Optional.empty());
		
		ApiBusinessException exception = assertThrows(ApiBusinessException.class, () -> battleService.stop());
		
		assertThat(exception.getCode()).isEqualToIgnoringCase("M1009");
	}

}
