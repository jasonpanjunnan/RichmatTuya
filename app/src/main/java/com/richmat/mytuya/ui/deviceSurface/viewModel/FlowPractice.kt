package com.richmat.mytuya.ui.deviceSurface.viewModel

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class UserMessageDateSource(
    private val messageApi:MessageApi,
    private val refreshIntervalMs: Long = 5000,
) {
    val latestMessages: Flow<List<TextMessage>> = flow {
        while (true){
            val message = messageApi.fetchLatestMessages()
            emit(message)
            delay(refreshIntervalMs)
        }
    }
//    val latestMessages: Flow<List<TextMessage>()> = flow<List<TextMessage>> { }
}

class MessageApi {
   suspend fun fetchLatestMessages():List<TextMessage>{

        return listOf(TextMessage(1))
    }
}

data class TextMessage(val da:Int)
//
//val userMessages :Flow<MessageUiModel> =
//    UserMessageDateSource(messageApi = MessageApi()).latestMessages.map {
//        userMessages ->
//        userMessages.toUiModel()
//        userMessages.coll
//    }

class MessageUiModel {

}
