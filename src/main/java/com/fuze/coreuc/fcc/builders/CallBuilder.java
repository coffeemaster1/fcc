package com.fuze.coreuc.fcc.builders;

import com.fuze.coreuc.fcc.models.Call;
import com.google.inject.Inject;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.CommandAction;
import org.asteriskjava.manager.action.GetVarAction;
import org.asteriskjava.manager.response.CommandResponse;
import org.asteriskjava.manager.response.GetVarResponse;
import org.asteriskjava.manager.response.ManagerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class CallBuilder {

	private static final Logger LOGGER = LoggerFactory.getLogger(CallBuilder.class);


	private ManagerConnection amiConn;

	@Inject
	public CallBuilder(ManagerConnection amiConn){
		this.amiConn = amiConn;
	}

	public Map<String, Call> buildCalls (List<String> accountCodes, Map<String, Call> globalCalls) throws IOException, TimeoutException {

		Map<String, Call> callList = new HashMap<>();

		for (Call call : globalCalls.values()) {
			String accountCode = call.getAccountCode();
			if (accountCodes.contains(accountCode)) {
				callList.put(call.getUniqueID(), call);
			}
		}
		return callList;
	}

	public Map<String, Call> buildGlobalCallList () throws IOException, TimeoutException {

		Map<String, Call> callList = new HashMap<>();

		List<String> channels;
		CommandAction command = new CommandAction();
		CommandResponse commandResponse;

		command.setCommand("core show channels concise");
		commandResponse = (CommandResponse) amiConn.sendAction(command, 30000);

		channels = commandResponse.getResult();

		for (String channel : channels) {
			List<String> channelInfo = new ArrayList<>();
			channelInfo.addAll(Arrays.asList(channel.split("!")));

			GetVarAction getVar = new GetVarAction();
			GetVarResponse varResponse;

			getVar.setChannel(channelInfo.get(0));
			getVar.setVariable("CONNECTEDLINE(all)");

			varResponse = (GetVarResponse) amiConn.sendAction(getVar, 30000);
			String connectedLine = varResponse.getValue();

			String connectedLineName = connectedLine.substring(connectedLine.indexOf("\"") + 1, connectedLine.lastIndexOf("\""));
			String connectedLineNum = connectedLine.substring(connectedLine.indexOf("<") + 1, connectedLine.lastIndexOf(">"));

			channelInfo.add(connectedLineName);
			channelInfo.add(connectedLineNum);

			callList.put(channelInfo.get(channelInfo.size() - 3), new Call(channelInfo));
		}

		return callList;
	}

}
