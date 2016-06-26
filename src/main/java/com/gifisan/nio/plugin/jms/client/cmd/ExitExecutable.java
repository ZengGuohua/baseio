package com.gifisan.nio.plugin.jms.client.cmd;

import java.util.HashMap;

import com.gifisan.nio.common.CloseUtil;
import com.gifisan.nio.common.cmd.CmdResponse;
import com.gifisan.nio.common.cmd.CommandContext;
import com.gifisan.nio.connector.IOConnector;

public class ExitExecutable extends JMSCommandExecutor {

	public CmdResponse exec(CommandContext context, HashMap<String, String> params) {

		CmdResponse response = new CmdResponse();

		IOConnector connector = getClientConnector(context);
		
		if (connector == null) {
			response.setResponse("请先登录！");
			return response;
		}
		
		//FIXME logout
//		connector.logout();
		
		CloseUtil.close(connector);
		
		setMessageBrowser(context, null);
		setClientConnector(context, null);
		
		response.setContinue(false);
		response.setResponse("系统退出！");
		
		return response;
	}
}
