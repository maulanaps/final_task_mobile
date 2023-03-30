package com.example.submission05.utils

import com.example.submission03.model.MovieAndTvShow
import com.example.submission03.model.MovieAndTvShowEntity

class DataConverter {
    companion object {
        fun movieTvShowToEntity(movieAndTvShow: MovieAndTvShow): MovieAndTvShowEntity {
            return MovieAndTvShowEntity(
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

        fun entityToMovieTvShow(movieAndTvShowEntity: MovieAndTvShowEntity): MovieAndTvShow {
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