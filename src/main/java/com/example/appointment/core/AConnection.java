
//�ͻ��˵������࣬�����ӹ��ܵĺ���

package com.example.appointment.core;


import com.example.appointment.message.AMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

//�Ժ��Ĵ�����г�ȡ��һ�����ĸ������ķ������ֱ������ӣ��Ͽ����ӣ�������Ϣ��������Ϣ
public class AConnection 
{

	private String host = "";
	private int port = 8080;
	Socket client;
	private DataInputStream reader;
	private DataOutputStream writer;
	private WaitThread waitThread;
	public boolean isWaiting;

	// new��QQConnection�����ʱ���ʼ��IP��ַ�Ͷ˿�
	public AConnection(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}


	//�����������֮�������
	public void connect() throws UnknownHostException, IOException {
		// ��������
		client = new Socket(host, port);
		reader = new DataInputStream(client.getInputStream());
		writer = new DataOutputStream(client.getOutputStream());
		// �������ӵ�ʱ�����ȴ��߳�
		isWaiting = true;
		waitThread = new WaitThread();
		waitThread.start();

	}

	 //�Ͽ�������ڼ������
	public void disConnect() throws IOException {
		// �ر����Ӿ����ͷ���Դ
		client.close();
		reader.close();
		writer.close();
		isWaiting = false;
	}

	//����xml��Ϣ
	public void sendMessage(String xml) throws IOException {
		// ������ϢҪ�õ������������������Ϊ��ĳ�Ա�������ڴ������ӵ�ʱ���ʼ�����Ͽ����ӵ�ʱ���ͷ���Դ
		// ������Ϣ��ʵ���ǰ���Ϣд��ȥ
		writer.writeUTF(xml);

	}

	//����java������Ϣ
	public void sendMessage(AMessage msg) throws IOException {
		writer.writeUTF(msg.toXml());
	}

	//�ȴ��߳� ������Ϣ,���ڲ�֪����Ϣʲôʱ�򵽴��Ҫһֱ�ȴ��ţ��ȴ���Ϣ�ĵ���
	private class WaitThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (isWaiting) {
				// ������Ϣ��ʵ���ǽ���Ϣ��ȡ��
				try {
					String xml = reader.readUTF();// ��ȡ��Ϣ
					// ����Ϣת��Java����
					AMessage msg = new AMessage();
					msg = (AMessage) msg.fromXml(xml);
					// ������յ���Ϣ��������Ϣ�б����type�ֶ��������¼����ȡ��ϵ���б��ǳ��Ȳ���������һ���ֲ�����ȡ����һ���ӿڣ������ڰ�ť�ĵ���¼����������յ���Ϣ��������
					/*
					 * ���յ���Ϣ֮�����ε���ÿ����������onReceive����
					 */
					for (OnMessageListener listener : listeners) {
						listener.onReveive(msg);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}

	// �������ᾭ�����ͻ��˷�����Ϣ���ͻ��˻��в�ͬ����Ϣ�����������½�һ���������ļ��ϣ������������һ���������͵���һ��onReveive������
	//�������о͵��ã�������û�оͲ�����
	private List<OnMessageListener> listeners = new ArrayList<OnMessageListener>();

	public void addOnMessageListener(OnMessageListener listener) {
		listeners.add(listener);
	}

	public void removeOnMessageListener(OnMessageListener listener) {
		listeners.remove(listener);
	}

	//��Ϣ�ļ������ӿڣ�������Ϣ������ʱ��͵���һ��onReceive����
	public static interface OnMessageListener {
		public void onReveive(AMessage msg);
	}

}
