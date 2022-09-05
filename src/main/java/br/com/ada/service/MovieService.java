package br.com.ada.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.ada.client.OMDBClient;
import br.com.ada.domain.Battle;
import br.com.ada.domain.Movie;
import br.com.ada.dto.MovieDTO;
import br.com.ada.dto.MovieOmdbDTO;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.MovieRepository;
import br.com.ada.repository.predicate.MoviePredicate;
import br.com.ada.util.MapperUtil;
import br.com.ada.util.Utils;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

	private final OMDBClient client;
	private final MovieRepository movieRepository;
	private final MapperUtil mapperUtil;

	@Value("${omdb-api.key}")
	private String apiKey;

	public MovieDTO getMovie(String title) {
		MovieOmdbDTO movieOmdbDTO = client.getMovie(title, apiKey);
		return MovieDTO.builder().director(movieOmdbDTO.getDirector()).title(movieOmdbDTO.getTitle())
				.year(movieOmdbDTO.getYear()).imdbRating(movieOmdbDTO.getImdbRating())
				.imdbVotes(Utils.convertImdbVoteToDouble(movieOmdbDTO.getImdbVotes())).build();
	}

	public MovieDTO create(String title) {
		MovieDTO movieDTO = getMovie(title);
		Movie movie = movieRepository.save(mapperUtil.convertTo(movieDTO, Movie.class));
		return mapperUtil.convertTo(movie, MovieDTO.class);
	}

	public MovieDTO findMovieById(Long id) {
		Movie optionalMovie = movieRepository.findById(id)
				.orElseThrow(() -> new ApiErrorException("M1005", HttpStatus.BAD_REQUEST));
		return mapperUtil.convertTo(optionalMovie, MovieDTO.class);
	}

	public Movie getRandomMovie() {
		Random rand = new Random();
		List<Movie> movieList = movieRepository.findAll();
		int randomIndex = rand.nextInt(movieList.size());
		return movieList.get(randomIndex);
	}

	public List<Movie> getRandomMovieWithoutMatchers(List<Battle> battleList) {
		Iterable<Movie> allMoviesDontUsedYet = null;
		List<Movie> movieList = new ArrayList<>();
		List<Long> invalidIdList = new ArrayList<>();
		do {
			movieList.clear();
			Random rand = new Random();
			List<Movie> tempMovies = new ArrayList<>();
			movieRepository.findAll(MoviePredicate.getMovieWhereIdNotIn(invalidIdList)).forEach(tempMovies::add);
			if (CollectionUtils.isEmpty(tempMovies)) {
				// End Game
				return null;
			}
			int randomIndex = rand.nextInt(tempMovies.size());
			Movie movie = tempMovies.get(randomIndex);

			invalidIdList.add(movie.getId());
			movieList.add(movie);

			List<Long> allMatchersId = new ArrayList<>();

			List<Long> firstMovieMatchersId = battleList.stream()
					.filter(battle -> battle.getSecondMovie().getId().equals(movie.getId())).map(Battle::getFirstMovie)
					.map(Movie::getId).collect(Collectors.toList());
			allMatchersId.addAll(firstMovieMatchersId);

			List<Long> secondMovieMatchersId = battleList.stream()
					.filter(battle -> battle.getFirstMovie().getId().equals(movie.getId())).map(Battle::getSecondMovie)
					.map(Movie::getId).collect(Collectors.toList());

			allMatchersId.addAll(secondMovieMatchersId);
			allMatchersId.addAll(invalidIdList);

			if (CollectionUtils.isNotEmpty(allMatchersId)) {
				allMoviesDontUsedYet = movieRepository.findAll(MoviePredicate.getMovieWhereIdNotIn(allMatchersId));
			}

		} while (IterableUtils.isEmpty(allMoviesDontUsedYet));

		movieList.add(allMoviesDontUsedYet.iterator().next());

		return movieList;

	}

}
