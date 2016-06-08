package com.server;

public class Server {

	private int port;
	
	private String serverIp;
	
	private String hostName;

	public Server(int serverPort, String ip,String hostName) {
		this.port=serverPort;
		this.serverIp=ip;
		this.hostName=hostName;
	}
 
	public Server() {
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
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

	@Override
	public String toString() {
		return "Server [port=" + port + ", serverIP=" + serverIp + "]";
	}
	
}
