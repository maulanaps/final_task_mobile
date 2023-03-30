package com.example.submission05.utils

import com.example.submission03.model.MovieAndTvShow

class DataConverter {
    companion object {
        fun movieToEntity(movieAndTvShow: MovieAndTvShow): MovieAndTvShow {
            return MovieAndTvShow(
                movieAndTvShow.id,
                movieAndTvShow.adult,
                movieAndTvShow.backdropPath,
                movieAndTvShow.originalLanguage,
                movieAndTvShow.originalTitle,
                movieAndTvShow.overview,
                movieAndTvShow.popularity,
                movieAndTvShow.posterPath,
                movieAndTvShow.releaseDate,
                movieAndTvShow.title,
                movieAndTvShow.video,
                movieAndTvShow.voteAverage,
                movieAndTvShow.voteCount
            )
        }

        fun entityToMovie(movieAndTvShowEntity: MovieAndTvShow): MovieAndTvShow {
            return MovieAndTvShow(
                movieAndTvShowEntity.id,
                movieAndTvShowEntity.adult,
                movieAndTvShowEntity.backdropPath,
                movieAndTvShowEntity.originalLanguage,
                movieAndTvShowEntity.originalTitle,
                movieAndTvShowEntity.overview,
                movieAndTvShowEntity.popularity,
                movieAndTvShowEntity.posterPath,
                movieAndTvShowEntity.releaseDate,
                movieAndTvShowEntity.title,
                movieAndTvShowEntity.video,
                movieAndTvShowEntity.voteAverage,
                movieAndTvShowEntity.voteCount
            )
        }
    }
}