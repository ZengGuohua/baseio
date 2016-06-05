package com.likemessage.server;

import java.sql.SQLException;

import com.alibaba.fastjson.JSONObject;
import com.gifisan.database.DataBaseContext;
import com.gifisan.nio.common.BeanUtil;
import com.gifisan.nio.component.Parameters;
import com.gifisan.nio.component.future.ServerReadFuture;
import com.gifisan.nio.plugin.jms.MapMessage;
import com.gifisan.nio.plugin.jms.server.MQContext;
import com.gifisan.nio.plugin.jms.server.MQContextFactory;
import com.gifisan.nio.server.IOSession;
import com.gifisan.nio.server.RESMessage;
import com.likemessage.bean.T_MESSAGE;

public class MessageServlet extends LMServlet {

	public static final String	SERVICE_NAME		= MessageServlet.class.getSimpleName();

	public static final String	ACTION_ADD_MESSAGE	= "ACTION_ADD_MESSAGE";

	protected AbstractService getAbstractService(DataBaseContext context) throws SQLException {
		return new MessageService(context);
	}

	protected void doAccept(IOSession session, ServerReadFuture future, AbstractService _service) throws Exception {

//		if (future.hasOutputStream()) {
//
//			OutputStream outputStream = future.getOutputStream();
//
//			if (outputStream == null) {
//				future.setOutputIOEvent(new BufferedOutputStream(future.getStreamLength()), null);
//				return;
//			}
//		}
		
		
		MessageService service = (MessageService) _service;

		Parameters parameters = future.getParameters();

		String action = parameters.getParameter(ACTION);

		if (ACTION_ADD_MESSAGE.equals(action)) {
			addMessage(session, future, parameters, service);
		} else {
			actionNotFound(session, future, _service);
		}

	}

	private void addMessage(IOSession session, ServerReadFuture future, Parameters parameters, MessageService service)
			throws Exception {

		JSONObject object = parameters.getJSONObject("t_message");

		T_MESSAGE tMessage = (T_MESSAGE) BeanUtil.map2Object(object, T_MESSAGE.class);

		if (service.addMessage(tMessage)) {

		}

		MapMessage _message = new MapMessage("mmm", parameters.getParameter("UUID"));

		_message.put("eventName", "lMessage");
		_message.put("fromUserID", tMessage.getFromUserID());
		_message.put("message", tMessage.getMessage());
		
		MQContext context = MQContextFactory.getMQContext();
		
		context.offerMessage(_message);

		RESMessage message = RESMessage.SUCCESS;

		future.write(message.toString());

		session.flush(future);
	}

}