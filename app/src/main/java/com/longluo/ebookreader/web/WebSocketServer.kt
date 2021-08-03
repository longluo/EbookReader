package com.longluo.ebookreader.web

import fi.iki.elonen.NanoWSD

class WebSocketServer(port: Int) : NanoWSD(port) {

    override fun openWebSocket(handshake: IHTTPSession): WebSocket? {
        return if (handshake.uri == "/sourceDebug") {
            SourceDebugWebSocket(handshake)
        } else null
    }
}
