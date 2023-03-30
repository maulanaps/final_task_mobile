package com.example.submission05.ui.comment_form

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.app.ActivityCompat
import com.example.submission03.databinding.ActivityCommentFormBinding
import com.example.submission05.constant.Constants.Companion.EDIT_COMMENT
import com.example.submission05.constant.Constants.Companion.WRITE_COMMENT
import com.example.submission05.data.room.comment.CommentDatabase
import com.example.submission05.data.model.CommentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private lateinit var binding: ActivityCommentFormBinding

class CommentFormActivity : AppCompatActivity() {
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

            val commentDb = CommentDatabase.getInstance(this@CommentFormActivity)
            val commentDao = commentDb.CommentDao()
            Log.d("coms", "onCreate: mode = $mode")
            if (mode == WRITE_COMMENT) {
                btnSubmitComment.text = "Submit"
                btnDeleteComment.visibility = GONE

                btnSubmitComment.setOnClickListener {
                    val commentWriter = etWriterName.text.toString()
                    val commentContent = etCommentContent.text.toString()
                    val newComment = CommentEntity(movieId = movieId, writer= commentWriter, content= commentContent)
                    CoroutineScope(Dispatchers.IO).launch {
                        commentDao.insert(newComment)
                    }
                    finish()
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
                    CoroutineScope(Dispatchers.IO).launch {
                        commentDao.update(comment)
                    }
                    finish()
                }

                btnDeleteComment.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        commentDao.delete(comment)
                    }
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