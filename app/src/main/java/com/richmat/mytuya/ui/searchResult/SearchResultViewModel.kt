package com.richmat.mytuya.ui.searchResult

import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.richmat.mytuya.MyApplication
import com.richmat.mytuya.data.posts.Imp.FakePostsRepository
import com.richmat.mytuya.util.data.DevResultMassage
import com.richmat.mytuya.util.jsonToDevResultMassage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val myRepository: FakePostsRepository,
) : ViewModel() {

    fun changeName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun rename() {
        viewModelScope.launch {
            var result: Boolean
            try {
                result = myRepository.rename(_uiState.value.deviceId, _uiState.value.name)
            } catch (e: Exception) {
//                throw e
                result = false
                println(e)
            }
            val toast = if (result) "修改名字成功" else "修改名字失败"
            Toast.makeText(MyApplication.context, toast, Toast.LENGTH_SHORT).show()
        }
    }

    private val saved = savedStateHandle.get<String>(DEV_RESULT)!!

    private val _uiState = MutableStateFlow(DevResultMassage("", ""))
    val uiState: StateFlow<DevResultMassage> = _uiState

    init {
        val result = jsonToDevResultMassage(saved)
        //        _uiState.update { it.copy(devResultMassage = result) }
        _uiState.value = result
    }

    companion object {
        const val DEV_RESULT = "dev_result"
    }
}