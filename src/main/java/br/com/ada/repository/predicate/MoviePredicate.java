package br.com.ada.repository.predicate;

import java.util.List;

import com.querydsl.core.types.Predicate;

import br.com.ada.domain.QMovie;

public class MoviePredicate {

	public static Predicate getMovieWhereIdNotIn(List<Long> idList) {
		return QMovie.movie.id.notIn(idList);
	}

}
