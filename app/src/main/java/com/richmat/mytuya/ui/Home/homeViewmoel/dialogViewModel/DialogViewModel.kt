package com.richmat.mytuya.ui.Home.homeViewmoel.dialogViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.data.posts.PostsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DialogViewModel(
    val postsRepository: PostsRepository = FakePostsRepository()
) : ViewModel() {
    private val _isShowWarning = MutableStateFlow(false)
    val isShowWarning: StateFlow<Boolean> = _isShowWarning

    init {
        viewModelScope.launch {
            postsRepository.observeIsShowDialog().collect {
                _isShowWarning.value = it
            }
        }
    }
}