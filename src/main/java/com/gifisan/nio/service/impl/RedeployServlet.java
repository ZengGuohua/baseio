package com.gifisan.nio.service.impl;

import com.gifisan.nio.component.Configuration;
import com.gifisan.nio.component.Parameters;
import com.gifisan.nio.component.RESMessage;
import com.gifisan.nio.server.NIOContext;
import com.gifisan.nio.server.ServerContext;
import com.gifisan.nio.server.session.NIOSession;
import com.gifisan.nio.service.NIOServlet;

public class RedeployServlet extends NIOServlet {

	private String	username	= null;
	private String	password	= null;
	
	public void accept(NIOSession session) throws Exception {
		Parameters param = session.getParameters();
		String username = param.getParameter("username");
		String password = param.getParameter("password");

		boolean result = this.username.equals(username) && this.password.equals(password);
		if (result) {
			ServerContext context = (ServerContext) session.getContext();
			RESMessage message =  context.redeploy()  ? RESMessage.R_SUCCESS : RESMessage.R_FAIL;
			session.write(message.toString());
		} else {
			session.write(RESMessage.R_UNAUTH.toString());
		}
		session.flush();

	}

	public void initialize(NIOContext context, Configuration config) throws Exception {
		this.username = config.getProperty("username");
		this.password = config.getProperty("password");
	}

}