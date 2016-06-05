package com.profile;

public class Profile {

	private String name;

	private String hostName;

	public Profile(String name, String ipAddress) {
		this.name = name;
		this.hostName = ipAddress;
	}

	public Profile() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String toString() {
		return "Profile [name : " + name + "  hostName= : " + hostName + "]";
	}
	
}
