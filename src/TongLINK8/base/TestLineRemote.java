import com.tongtech.tlq.admin.remote.api.TLQConnect;
import com.tongtech.tlq.admin.remote.api.TLQOptObjFactory;
import com.tongtech.tlq.admin.remote.api.TLQParameterException;
import com.tongtech.tlq.admin.remote.api.TLQRemoteException;
import com.tongtech.tlq.admin.remote.api.qcu.TLQOptSendConn;


public class TestLineRemote {
	static TLQOptObjFactory tlqFac = null;
	private static String qcuName;
    private static String connName;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TLQConnect tlqConnect = null;
		
		if (args.length < 1) {
            System.out.println("---------请输入参数！---------");
            System.out.println(
                    "TestLineRemote QcuName ConnName");
            return;

        }
        if (args.length != 2) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out.println(
                    "TestLineRemote QcuName ConnName");
            return;
        } else {
        	qcuName = args[0];
    		connName = args[1];
        }
		try {
			tlqConnect = new TLQConnect("127.0.0.1",10252);
			tlqConnect.connect();
			tlqFac = new TLQOptObjFactory(tlqConnect);
			TestLineRemote testLineRemote = new TestLineRemote(qcuName,connName);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TLQRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				tlqConnect.close();
			} catch (TLQRemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public TestLineRemote(String qcuName,String connName){
		try {
			int maxTestWaitTime=3;
			TLQOptSendConn tlqOptSendConn = tlqFac.getTLQOptSendConn(qcuName, connName);
			int status = tlqOptSendConn.testLine(connName,maxTestWaitTime);
			System.out.println("testLine的状态为："+status);
		} catch (TLQParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TLQRemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
