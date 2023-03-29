package com.example.submission05.model

import com.example.submission03.model.MovieAndTvShow
import com.google.gson.annotations.SerializedName

data class Movies (

    @SerializedName("page"          ) var page         : Int?               = null,
    @SerializedName("results"       ) var results      : List<MovieAndTvShow> = emptyList(),
    @SerializedName("total_pages"   ) var totalPages   : Int?               = null,
    @SerializedName("total_results" ) var totalResults : Int?               = null

)


//object Movies {
//    fun getList(): List<Movie> {
//        return listOf(
//            Movie(
//                R.drawable.avengers_bd, 299534, "Avengers: Endgame",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                315.836,
//                R.drawable.avengers_poster,
//                "2019-04-24",
//                "Avengers: Endgame",
//                8.3,
//                20247
//            ),
//            Movie(
//                R.drawable.spiderman_bd, 299534, "Spider-Man: No Way Home",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                315.836,
//                R.drawable.spiderman_poster,
//                "2019-04-24",
//                "Spider-Man: No Way Home",
//                9.1,
//                20247
//            ),
//            Movie(
//                R.drawable.thor_bd, 299534, "Thor: Ragnarok",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                315.836,
//                R.drawable.thor_poster,
//                "2019-04-24",
//                "Thor: Ragnarok",
//                7.9,
//                20247
//            ),
//            Movie(
//                R.drawable.hobbsshaw_bd, 299534, "Fast & Furious Presents: Hobbs & Shaw",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                315.836,
//                R.drawable.hobbsshaw_poster,
//                "2019-04-24",
//                "Fast & Furious Presents: Hobbs & Shaw",
//                7.9,
//                20247
//            ),
//            Movie(
//                R.drawable.conjuring_bd, 299534, "The Conjuring 2",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                315.836,
//                R.drawable.conjuring_poster,
//                "2019-04-24",
//                "The Conjuring 2",
//                7.9,
//                20247
//            )
//        )
//    }
//}