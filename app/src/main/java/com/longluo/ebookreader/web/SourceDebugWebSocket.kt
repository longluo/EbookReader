package com.longluo.ebookreader.web

import com.longluo.ebookreader.R
import com.longluo.ebookreader.data.appDb
import com.longluo.ebookreader.model.Debug
import com.longluo.ebookreader.model.webBook.WebBook
import com.longluo.ebookreader.utils.GSON
import com.longluo.ebookreader.utils.fromJsonObject
import com.longluo.ebookreader.utils.isJson
import com.longluo.ebookreader.utils.runOnIO
import fi.iki.elonen.NanoHTTPD
import fi.iki.elonen.NanoWSD
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import splitties.init.appCtx
import java.io.IOException


class SourceDebugWebSocket(handshakeRequest: NanoHTTPD.IHTTPSession) :
    NanoWSD.WebSocket(handshakeRequest),
    CoroutineScope by MainScope(),
    Debug.Callback {

    private val notPrintState = arrayOf(10, 20, 30, 40)

    override fun onOpen() {
        launch(IO) {
            kotlin.runCatching {
                while (isOpen) {
                    ping("ping".toByteArray())
                    delay(30000)
                }
            }
        }
    }

    override fun onClose(
        code: NanoWSD.WebSocketFrame.CloseCode,
        reason: String,
        initiatedByRemote: Boolean
    ) {
        cancel()
        Debug.cancelDebug(true)
    }

    override fun onMessage(message: NanoWSD.WebSocketFrame) {
        launch(IO) {
            kotlin.runCatching {
                if (!message.textPayload.isJson()) {
                    send("数据必须为Json格式")
                    close(NanoWSD.WebSocketFrame.CloseCode.NormalClosure, "调试结束", false)
                    return@launch
                }
                val debugBean = GSON.fromJsonObject<Map<String, String>>(message.textPayload)
                if (debugBean != null) {
                    val tag = debugBean["tag"]
                    val key = debugBean["key"]
                    if (tag.isNullOrBlank() || key.isNullOrBlank()) {
                        send(appCtx.getString(R.string.cannot_empty))
                        close(NanoWSD.WebSocketFrame.CloseCode.NormalClosure, "调试结束", false)
                        return@launch
                    }
                    appDb.bookSourceDao.getBookSource(tag)?.let {
                        Debug.callback = this@SourceDebugWebSocket
                        Debug.startDebug(this, WebBook(it), key)
                    }
                }
            }
        }
    }

    override fun onPong(pong: NanoWSD.WebSocketFrame) {

    }

    override fun onException(exception: IOException) {
        Debug.cancelDebug(true)
    }

    override fun printLog(state: Int, msg: String) {
        if (state in notPrintState) {
            return
        }
        runOnIO {
            runCatching {
                send(msg)
                if (state == -1 || state == 1000) {
                    Debug.cancelDebug(true)
                    close(NanoWSD.WebSocketFrame.CloseCode.NormalClosure, "调试结束", false)
                }
            }.onFailure {
                it.printStackTrace()
            }
        }
    }

}
