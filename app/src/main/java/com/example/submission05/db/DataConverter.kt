package com.example.submission05.db

import com.example.submission03.model.Movie
import com.example.submission03.model.MovieEntity

class DataConverter {
    companion object {
        fun movieToEntity(movie: Movie): MovieEntity {
            return MovieEntity(
                movie.id,
                movie.adult,
                movie.backdropPath,
                movie.originalLanguage,
                movie.originalTitle,
                movie.overview,
                movie.popularity,
                movie.posterPath,
                movie.releaseDate,
                movie.title,
                movie.video,
                movie.voteAverage,
                movie.voteCount
            )
        }

        fun entityToMovie(movieEntity: MovieEntity): Movie {
            return Movie(
                movieEntity.id,
                movieEntity.adult,
                movieEntity.backdropPath,
                movieEntity.originalLanguage,
                movieEntity.originalTitle,
                movieEntity.overview,
                movieEntity.popularity,
                movieEntity.posterPath,
                movieEntity.releaseDate,
                movieEntity.title,
                movieEntity.video,
                movieEntity.voteAverage,
                movieEntity.voteCount
            )
        }
    }
}