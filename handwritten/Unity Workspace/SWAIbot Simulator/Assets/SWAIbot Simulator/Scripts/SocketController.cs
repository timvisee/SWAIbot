using UnityEngine;
using System.Collections;
using System;
using System.IO;
using System.Net.Sockets;

public class SocketController : MonoBehaviour {

	internal Boolean socketReady = false;
	
	TcpClient mySocket;
	NetworkStream theStream;
	StreamWriter theWriter;
	StreamReader theReader;
	String Host = "localhost";
	Int32 Port = 5111;

	PacketHandler ph = new PacketHandler();

	public Transform robotControllerObj;
	private static RobotController rc;

	private float lastHandshake;
	private float lastMotorUpdate;

	void Start () {
		// Set up the connection
		SetupSocket();

		Packet p = new Packet(0, 0);
		p.appendString("Mah ");
		SendPacket(p);

		rc = robotControllerObj.GetComponent<RobotController>();
	}

	void Update () {
		if(lastHandshake + 1.0f <= Time.time) {
			lastHandshake = Time.time;
			Packet p = new Packet(0, PacketType.HEARTHBEAT);
			SendPacket(p);
		}

		// Send the current motor angle, each second
		if(lastMotorUpdate + .5f <= Time.time) {
			lastMotorUpdate = Time.time;
			Packet p = new Packet(0, PacketType.MOTOR_STATUS);
			p.appendInteger((int) rc.GetMotorAngle(1));
			p.appendInteger((int) rc.GetMotorAngle(2));
			p.appendInteger((int) rc.GetMotorAngle(3));
			p.appendInteger((int) rc.GetMotorAngle(4));
			SendPacket(p);
		}

		if(socketReady)
			if(theStream.DataAvailable)
				ph.received(ReadSocket());
	}

	public void SendPacket(Packet p) {
		theWriter.Write(Protocol.serialize(p));
		theWriter.Flush();
	}

	public static void OnReceived(Packet p) {
		if(p.getPacketId() == PacketType.SET_MOTOR)
			for(int i = 0; i < p.getIntegers().Count - 1; i += 2)
				rc.SetMotorTargetAngle(p.getIntegers()[i], p.getIntegers()[i + 1]);
	}



	public void SetupSocket() {
		try {
			mySocket = new TcpClient(Host, Port);
			theStream = mySocket.GetStream();
			theWriter = new StreamWriter(theStream);
			theReader = new StreamReader(theStream);
			socketReady = true;

		} catch (Exception e) {
			Debug.Log("Socket error: " + e);
		}
	}

	public void WriteSocket(string theLine) {
		if (!socketReady)
			return;

		String foo = theLine + "\r\n";
		theWriter.Write(foo);
		theWriter.Flush();
	}

	public int ReadSocket() {
		if(!socketReady)
			return -1;
		if(theStream.DataAvailable)
			return theStream.ReadByte();
		return -1;
	}

	public void CloseSocket() {
		if (!socketReady)
			return;
		theWriter.Close();
		theReader.Close();
		mySocket.Close();
		socketReady = false;
	}
	
	public void OnGUI() {
		if(!socketReady)
			GUI.Label(new Rect(20, 20, 300, 20), "Waiting for client to connect...");
	}
}