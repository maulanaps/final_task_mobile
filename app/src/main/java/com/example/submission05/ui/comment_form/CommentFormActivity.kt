package com.example.submission05.ui.comment_form

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.example.submission03.databinding.ActivityCommentFormBinding
import com.example.submission05.constant.Constants.Companion.EDIT_COMMENT
import com.example.submission05.constant.Constants.Companion.WRITE_COMMENT
import com.example.submission05.data.local.room.comment.CommentDatabase
import com.example.submission05.data.local.entity.CommentEntity


class CommentFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentFormBinding

    // VIEW MODEL
    private val viewModel: CommentViewModel by viewModels {
        val commentDao = CommentDatabase.getInstance(this).CommentDao()
        CommentViewModel.provideFactory(commentDao, this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get intent
        val comment: CommentEntity? = intent.getParcelableExtra(COMMENT_DATA)
        val mode = intent.getStringExtra(MODE)
        val movieId = intent.getIntExtra(MOVIE_ID, 0)

        binding.apply {

            Log.d("coms", "onCreate: mode = $mode")
            if (mode == WRITE_COMMENT) {
                btnSubmitComment.text = "Submit"
                btnDeleteComment.visibility = GONE

                btnSubmitComment.setOnClickListener {
                    val commentWriter = etWriterName.text.toString()
                    val commentContent = etCommentContent.text.toString()

                    if (commentWriter.isEmpty()) {
                        etWriterName.error = "Please fill your name"
                        etWriterName.requestFocus()
                    } else if (commentContent.isEmpty()) {
                        etCommentContent.error = "Please write your comment"
                        etCommentContent.requestFocus()
                    } else {
                        val newComment = CommentEntity(
                            movieTvShowId = movieId,
                            writer = commentWriter,
                            content = commentContent
                        )
                        viewModel.insertComment(newComment)
                        finish()
                    }
                }
            }

            if (mode == EDIT_COMMENT) {
                etWriterName.setText(comment!!.writer)
                etCommentContent.setText(comment.content)
                btnSubmitComment.text = "Update"
                btnDeleteComment.visibility = VISIBLE

                btnSubmitComment.setOnClickListener {
                    comment.writer = etWriterName.text.toString()
                    comment.content = etCommentContent.text.toString()
                    viewModel.updateComment(comment)
                    finish()
                }

                btnDeleteComment.setOnClickListener {
                    viewModel.deleteComment(comment)
                    finish()
                }
            }
        }
    }

    companion object {
        private const val COMMENT_DATA = "comment_data"
        private const val MODE = "mode"
        private const val MOVIE_ID = "movie_id"

        fun open(
            activity: AppCompatActivity,
            movieId: Int,
            mode: String,
            commentEntity: CommentEntity?
        ) {
            val intent = Intent(activity, CommentFormActivity::class.java)
            intent.putExtra(COMMENT_DATA, commentEntity)
            intent.putExtra(MODE, mode)
            intent.putExtra(MOVIE_ID, movieId)
            ActivityCompat.startActivity(activity, intent, null)
        }
    }
}