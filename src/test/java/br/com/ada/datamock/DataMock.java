package br.com.ada.datamock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.ada.domain.Battle;
import br.com.ada.domain.Movie;
import br.com.ada.domain.Player;
import br.com.ada.domain.Role;
import br.com.ada.domain.Round;
import br.com.ada.dto.BattleDTO;
import br.com.ada.dto.MovieDTO;
import br.com.ada.dto.MovieOmdbDTO;
import br.com.ada.dto.PlayerDTO;
import br.com.ada.dto.RoleDTO;
import br.com.ada.dto.RoundDTO;
import br.com.ada.enumarator.Results;
import br.com.ada.enumarator.Stats;

public class DataMock {

	private static DataMock uniqueInstance;

	private DataMock() {
	}

	public static synchronized DataMock getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new DataMock();
		}

		return uniqueInstance;
	}
	
	
	public Movie getMovie() {
        return Movie.builder()
        		.id(1l)
                .title("titanic")
                .year("1997")
                .director("James Cameron")
                .imdbRating(7.9d)
                .imdbVotes(1154441d)
                .build();
    }
	
	public MovieDTO getMovieDTO() {
        return MovieDTO.builder()
                .title("titanic")
                .year("1997")
                .director("James Cameron")
                .imdbRating(7.9d)
                .imdbVotes(1154441d)
                .build();
    }
	public Movie getMovieIronMan() {
        return Movie.builder()
        		.id(3l)
                .title("Iron Man")
                .year("2008")
                .director("Jon Favreau")
                .imdbRating(7.9d)
                .imdbVotes(1043875d)
                .build();
    }
	public MovieDTO getMovieIronManDTO() {
        return MovieDTO.builder()
        		.id(3l)
                .title("Iron Man")
                .year("2008")
                .director("Jon Favreau")
                .imdbRating(7.9d)
                .imdbVotes(1043875d)
                .build();
    }
	
	public Movie getMovieTheRock() {
        return Movie.builder()
        		.id(4l)
                .title("The Rock")
                .year("1996")
                .director("Michael Bay")
                .imdbRating(7.4d)
                .imdbVotes(336386d)
                .build();
    }
	
	public MovieDTO getMovieTheRockDTO() {
        return MovieDTO.builder()
        		.id(4l)
                .title("The Rock")
                .year("1996")
                .director("Michael Bay")
                .imdbRating(7.4d)
                .imdbVotes(336386d)
                .build();
    }
	
	public Movie getMovie2() {
        return Movie.builder()
        		.id(2l)
                .title("Robin Hood")
                .year("2010")
                .director("Ridley Scott")
                .imdbRating(6.6d)
                .imdbVotes(268179d)
                .build();
    }
	
	public MovieDTO getMovieDTO2() {
		return MovieDTO.builder()
        		.id(2l)
                .title("Robin Hood")
                .year("2010")
                .director("Ridley Scott")
                .imdbRating(6.6d)
                .imdbVotes(268179d)
                .build();
    }
	
	public MovieOmdbDTO getMovieOmdbDTO() {
        return MovieOmdbDTO.builder()
                .title("titanic")
                .year("1997")
                .director("James Cameron")
                .imdbRating(7.9d)
                .imdbVotes("1,154,441")
                .build();
    }
	
	public Player getPlayer() {
        return Player.builder()
                .login("player1")
                .fullName("player 1")
                .password("1234")
                .build();
    }
	
	
	public PlayerDTO getPlayerDTO() {
        return PlayerDTO.builder()
                .login("player1")
                .fullName("player 1")
                .password("1234")
                .build();
    }
	
	public Role getRole() {
        return Role.builder()
                .roleName(Role.ROLE_PLAYER)
                .build();
    }
	public RoleDTO getRoleDTO() {
        return RoleDTO.builder()
                .roleName(Role.ROLE_PLAYER)
                .build();
    }
	
	public Round getOpenRound() {
		return Round.builder()
				.id(1l)
				.player(getPlayer())
				.stats(Stats.OPEN)
				.build();
	}
	public Round getOpenRoundWithBattle() {
		return Round.builder()
				.id(1l)
				.player(getPlayer())
				.stats(Stats.OPEN)
				.battles(Arrays.asList(getBattle()))
				.build();
	}
	public Round getOpenRoundWithBattleResult() {
		return Round.builder()
				.id(1l)
				.player(getPlayer())
				.stats(Stats.OPEN)
				.battles(getBattleWithResultList())
				.build();
	}
	
	public RoundDTO getOpenRoundDTO() {
		return RoundDTO.builder()
				.id(1l)
				.player(getPlayerDTO())
				.stats(Stats.OPEN)
				.build();
	}
	
	
	public Battle getBattle() {
		return Battle.builder()
				.id(1l)
				.firstMovie(getMovie())
				.secondMovie(getMovie2())
				.build();
	}
	public Battle getBattleWithIronMandAndTheRock() {
		return Battle.builder()
				.id(2l)
				.firstMovie(getMovieIronMan())
				.secondMovie(getMovieTheRock())
				.build();
	}
	public BattleDTO getBattleWithIronMandAndTheRockDTO() {
		return BattleDTO.builder()
				.id(2l)
				.firstMovie(getMovieIronManDTO())
				.secondMovie(getMovieTheRockDTO())
				.build();
	}
	public Battle getBattleWithResult() {
		return Battle.builder()
				.id(1l)
				.firstMovie(getMovie())
				.secondMovie(getMovie2())
				.results(Results.RIGHT)
				.build();
	}
	public List<Battle> getBattleWithResultList() {
		List<Battle> list = new ArrayList<>();
		list.add(Battle.builder()
				.id(1l)
				.firstMovie(getMovie())
				.secondMovie(getMovie2())
				.results(Results.RIGHT)
				.build());
		return list;
	}
	
	public BattleDTO getBattleWithResultDTO() {
		return BattleDTO.builder()
				.id(1l)
				.firstMovie(getMovieDTO())
				.secondMovie(getMovieDTO2())
				.results(Results.RIGHT)
				.build();
	}
	
	
	public BattleDTO getBattleDTO() {
		return BattleDTO.builder()
				.id(1l)
				.firstMovie(getMovieDTO())
				.secondMovie(getMovieDTO2())
				.build();
	}
	
	

}