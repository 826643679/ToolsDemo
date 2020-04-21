package TongLINK8.base;

import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class SendMsg {
	private String myQcuName;
	private String myQueName;
	private String myMsgType;
	private int myCount;
	private String myFileName;
	private TlqConnection tlqConnection = null;
	private TlqQCU tlqQcu = null;

	static int id = 0;

	public SendMsg(String QcuName, String QName, String MsgType, String FileName)
			throws TlqException {
		myQcuName = QcuName;
		myQueName = QName;
		myMsgType = MsgType;
		myFileName = FileName;
		myCount = 2;

		tlqConnection = new TlqConnection();
		tlqQcu = tlqConnection.openQCU(myQcuName);

	}

	public static synchronized String createID() {
		StringBuffer sb = new StringBuffer();
		sb.append(System.currentTimeMillis());
		sb.append("_");
		sb.append(id);
		++id;
		return sb.toString();
	}

	public static byte[] createBytes(int size) { // 构造Buffer消息内容
		StringBuffer sb = new StringBuffer(size);
		for (int i = 0; i < size; i++) {
			sb.append('a');
		}
		return sb.toString().getBytes(); 
	}

	void sendBuffMsg(int size) throws TlqException { // 发送Buffer消息 size 消息大小
		TlqMessage msgInfo = new TlqMessage();
		TlqMsgOpt msgOpt = new TlqMsgOpt();
		msgInfo.MsgType = TlqMessage.BUF_MSG; // 消息类型
		msgInfo.MsgSize = size; // 消息大小
		byte[] msgContent = createBytes(msgInfo.MsgSize); // 消息内容
		msgInfo.setMsgData(msgContent);

		msgInfo.Persistence = TlqMessage.TLQPER_Y; // 持久性
		msgInfo.Priority = TlqMessage.TLQPRI_NORMAL; // 优先级
		msgInfo.Expiry = 1000; // 生命周期
		msgOpt.QueName = myQueName; // 队列名

		tlqQcu.putMessage(msgInfo, msgOpt);
	}

	void sendFileMsg(String fName) throws TlqException { // 发送文件消息
		TlqMessage msgInfo = new TlqMessage();
		TlqMsgOpt msgOpt = new TlqMsgOpt();

		File file = new File(myFileName);
		if (file.isAbsolute()) {
			fName = myFileName + " " + createID() + "_" + file.getName();
		} else {
			int pos = file.getPath().lastIndexOf(File.separator);
			String startStr = file.getPath().substring(0, pos + 1);
			String endStr = file.getPath().substring(pos + 1);
			fName = myFileName + " " + startStr + createID() + "_" + endStr;
		}

		msgInfo.MsgType = TlqMessage.FILE_MSG;
		msgInfo.MsgSize = fName.getBytes().length;
		msgInfo.setMsgData(fName.getBytes());
		msgOpt.RemoveFileFlag = TlqMsgOpt.NOTREMOVEFILE; // 是否删除源文件标志

		msgInfo.Persistence = TlqMessage.TLQPER_Y;
		msgInfo.Priority = TlqMessage.TLQPRI_NORMAL;
		msgInfo.Expiry = 1000;
		msgOpt.QueName = myQueName;

		tlqQcu.putMessage(msgInfo, msgOpt);
	}

	public void sendMsg() {
		System.out.println("myMsgType is :" + myMsgType);
		try {
			for (int i = 0; i < myCount; i++) {
				if (myMsgType.equals("B") == true) {
					sendBuffMsg(10);
				} else {
					sendFileMsg(myFileName);
				}

			}

			System.out.println("--------------共发送消息" + myCount
					+ "条!-----------");
			System.out.println("-----------sendmsg over!!-----------");

		} catch (TlqException tlqEx) {
			tlqEx.printStackTrace();
		} finally {
			try {
				tlqQcu.close();
				tlqConnection.close();
			} catch (TlqException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] argv) throws Exception {
		String QcuName;
		String QName;
		String FileName;
		String MsgType;

		argv=new String[] {"sendqcu","lq","B","no"};

		if (argv.length < 1) {
			System.out.println("---------请输入参数！---------");
			System.out.println("---------注：如果消息类型为B,则FileName输入为no！---------");
			System.out.println("SendMsg QcuName QueName MsgType(B/F) FileName");
			return;

		}
		if (argv.length != 4) {
			System.out.println("---------您输入的参数格式不对，请重新输入！---------");
			System.out.println("---------注：如果消息类型为B,则FileName输入为no！---------");
			System.out.println("SendMsg QcuName QueName MsgType(B/F) FileName");
			return;
		} else {
			QcuName = argv[0];
			QName = argv[1];
			MsgType = argv[2];
			FileName = argv[3];
		}

		SendMsg sm = new SendMsg(QcuName, QName, MsgType, FileName);
		sm.sendMsg();
	}
}
