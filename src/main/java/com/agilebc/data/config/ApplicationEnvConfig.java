package com.agilebc.data.config;

import com.agilebc.data.AbstractAgilebcData;

public class ApplicationEnvConfig extends AbstractAgilebcData {

	private ServerEnvType serverType = null;

	public ServerEnvType getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = ServerEnvType.valueOf(serverType);
	}
}
