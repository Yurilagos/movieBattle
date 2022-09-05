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

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.ada.client.OMDBClient;
import br.com.ada.datamock.DataMock;
import br.com.ada.domain.Movie;
import br.com.ada.dto.MovieDTO;
import br.com.ada.erro.ApiErrorException;
import br.com.ada.repository.MovieRepository;
import br.com.ada.util.MapperUtil;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

	@InjectMocks
	private MovieService movieService;

	@Mock
	private MovieRepository movieRepository;
	@Mock
	private MapperUtil mapperUtil;
	@Mock
	private OMDBClient client;

	private final DataMock dataMock = DataMock.getInstance();

	@DisplayName("should create movie with successful")
	@Test
	void shouldCreateMovieWithSuccessful() {
		var movie = dataMock.getMovie();
		var movieDTO = dataMock.getMovieDTO();
		var movieOmdbDTO = dataMock.getMovieOmdbDTO();

		when(client.getMovie(anyString(), any())).thenReturn(movieOmdbDTO);
		when(movieRepository.save(any(Movie.class))).thenReturn(movie);
		when(mapperUtil.convertTo(movieDTO, Movie.class)).thenReturn(movie);
		when(mapperUtil.convertTo(movie, MovieDTO.class)).thenReturn(movieDTO);

		MovieDTO result = movieService.create(movie.getTitle());

		assertNotNull(result);
		assertEquals(movieDTO.getTitle(), result.getTitle());
		assertEquals(movieDTO.getDirector(), result.getDirector());
		verify(movieRepository, atLeastOnce()).save(any(Movie.class));

	}

	@DisplayName("should find movie with successful")
	@Test
	void shouldFindMovieWithSuccessful() {
		var movie = dataMock.getMovie();
		var movieDTO = dataMock.getMovieDTO();

		when(movieRepository.findById(any(Long.class))).thenReturn(Optional.of(movie));
		when(mapperUtil.convertTo(movie, MovieDTO.class)).thenReturn(movieDTO);

		MovieDTO result = movieService.findMovieById(movie.getId());

		assertNotNull(result);
		assertEquals(movieDTO.getTitle(), result.getTitle());
		assertEquals(movieDTO.getDirector(), result.getDirector());
		verify(movieRepository, atLeastOnce()).findById(any(Long.class));

	}

	@DisplayName("should throw exception when searching for movie that does not exist")
	@Test
	void shouldThrowExceptionForMovieThatDoesNotExist() {
		Long id = 1l;
		ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {

			when(movieRepository.findById(id)).thenReturn(Optional.empty());

			movieService.findMovieById(id);
		});
		assertThat(exception.getCode()).isEqualToIgnoringCase("M1005");
	}

}
