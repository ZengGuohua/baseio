package com.gifisan.nio.component.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import com.gifisan.nio.component.EndPoint;
import com.gifisan.nio.component.IOReadFuture;
import com.gifisan.nio.server.selector.ServiceAcceptorJob;
import com.gifisan.nio.server.session.Session;
import com.gifisan.nio.service.MultiReadFuture;

public class MultiDecoder extends AbstractDecoder {

	public MultiDecoder(Charset charset) {
		super(charset);
	}
	
	public IOReadFuture decode(EndPoint endPoint, byte[] header) throws IOException {
		
		byte sessionID = gainSessionID(header);
		
		int textLength = gainTextLength(header);
		
		int dataLength = gainStreamLength(header);

		Session session = endPoint.getSession(sessionID);
		
		ByteBuffer textBuffer = ByteBuffer.allocate(textLength);
		
		String serviceName = gainServiceName(endPoint, header);
		
		MultiReadFuture future = new MultiReadFuture(textBuffer, session, serviceName,dataLength);
		
		ServiceAcceptorJob acceptorJob = session.getServiceAcceptorJob();
		
		acceptorJob.accept(session, future);
		
		if (!future.hasOutputStream()) {
			endPoint.endConnect();
		}
		
		return future;
	}

}