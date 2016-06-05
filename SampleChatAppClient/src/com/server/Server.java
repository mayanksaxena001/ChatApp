package com.server;

public class Server {

	private int port;
	
	private String serverIp;

	public Server(int serverPort, String ip) {
		this.port=serverPort;
		this.serverIp=ip;
	}

	public Server() {
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
}
