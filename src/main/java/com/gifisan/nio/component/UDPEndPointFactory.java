package com.gifisan.nio.component;

import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.channels.SelectionKey;

public interface UDPEndPointFactory {

	public abstract UDPEndPoint getUDPEndPoint(NIOContext context, SelectionKey selectionKey, InetSocketAddress remote)
			throws SocketException;

	public abstract void removeUDPEndPoint(UDPEndPoint endPoint);

}