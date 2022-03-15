package io.my.rsocket.server;

import io.my.rsocket.service.SocketAcceptImpl;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;

public class Server {
    public static void main(String[] args) {
        RSocketServer socketServer = RSocketServer.create(new SocketAcceptImpl());

        CloseableChannel closeableChannel = socketServer.bindNow(TcpServerTransport.create(6565));

        // Keep listening
        closeableChannel.onClose().block();
    }
}
