package app;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ServentInfo implements Serializable {

	private static final long serialVersionUID = 5304170042791281555L;
	private final String ipAddress;
	private final int listenerPort;
	private final int chordId;


	private final String rootPath;
	private final String storagePath;
	private final int weekLimit;
	private final int strongLimit;
	private final String teamName;
	private final int teamLimit;

	public ServentInfo(String ipAddress, int listenerPort) {
		this.ipAddress = ipAddress;
		this.listenerPort = listenerPort;
		this.chordId = ChordState.chordHash(listenerPort);

		this.rootPath="";
		this.storagePath="";
		this.weekLimit=0;
		this.strongLimit=0;
		this.teamName="";
		this.teamLimit=0;
	}

	public ServentInfo(String ipAddress, int listenerPort, String rootPath, String storagePath, int weekLimit, int strongLimit, String teamName, int teamLimit) {
		this.ipAddress = ipAddress;
		this.listenerPort = listenerPort;
		this.chordId = ChordState.chordHash(listenerPort);

		this.rootPath=rootPath;
		this.storagePath=storagePath;
		this.weekLimit=weekLimit;
		this.strongLimit=strongLimit;
		this.teamName=teamName;
		this.teamLimit=teamLimit;
	}


	@Override
	public String toString() {
		return "ServentInfo{" +
				"ipAddress='" + ipAddress + '\'' +
				", listenerPort=" + listenerPort +
				", chordId=" + chordId +
				", rootPath='" + rootPath + '\'' +
				", storagePath='" + storagePath + '\'' +
				", weekLimit=" + weekLimit +
				", strongLimit=" + strongLimit +
				", teamName='" + teamName + '\'' +
				", teamLimit=" + teamLimit +
				'}';
	}
}
