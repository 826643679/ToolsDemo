package TongLINK8.base;

import com.tongtech.tlq.base.TlqConnection;
import com.tongtech.tlq.base.TlqException;
import com.tongtech.tlq.base.TlqQCU;


public class TestLine {
	private static TlqConnection tlqConnection = null;
    private static TlqQCU tlqQcu = null;
    private static String qcuName;
    private static String connName;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 2){
			System.out.println("Usage:TestLine <qcuName> <connName>");
			return;
		}
		qcuName = args[0];
		connName = args[1];
		TestLine testLine = new TestLine();
		int status = testLine.testLine(connName);
		System.out.println("testLine���ص�״̬Ϊ:"+status);
	}
	public TestLine(){
		try {
			tlqConnection = new TlqConnection();
			tlqQcu = tlqConnection.openQCU(qcuName);
		} catch (TlqException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int testLine(String connName){
		int maxTestWaitTime=3;
		try {
			return tlqQcu.tlqTestLine(connName,maxTestWaitTime);
		} catch (TlqException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -2;
		}finally{
			try {
				tlqQcu.close();
				tlqConnection.close();
			} catch (TlqException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
