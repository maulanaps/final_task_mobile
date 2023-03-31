package com.example.submission05.ui.comment_form

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.example.submission05.data.local.entity.CommentEntity
import com.example.submission05.data.local.room.comment.CommentDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentViewModel(private val commentDao: CommentDao) : ViewModel() {

    companion object {
        fun provideFactory(
            commentDao: CommentDao,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null,
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return CommentViewModel(commentDao) as T
                }
            }
    }

    fun insertComment(commentEntity: CommentEntity){
        CoroutineScope(Dispatchers.IO).launch {
            commentDao.insert(commentEntity)
        }
    }

    fun updateComment(commentEntity: CommentEntity){
        CoroutineScope(Dispatchers.IO).launch {
            commentDao.update(commentEntity)
        }
    }

    fun deleteComment(commentEntity: CommentEntity){
        CoroutineScope(Dispatchers.IO).launch {
            commentDao.delete(commentEntity)
        }
    }
}