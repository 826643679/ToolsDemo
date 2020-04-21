
import java.util.ArrayList;
import java.util.Map;


import com.tongtech.tlq.admin.common.TlqPage;
import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.conf.Program;
import com.tongtech.tlq.admin.conf.QCU;
import com.tongtech.tlq.admin.remote.api.*;
import com.tongtech.tlq.admin.remote.api.node.TLQOptProgram;
import com.tongtech.tlq.admin.remote.api.qcu.*;


public class Monitor {
	
	
	public static TLQConnect tlqConnect = null;
	public static TLQOptObjFactory tlqFac = null;
	
	String ip, qcuName,localQueName;
	Integer port;
		
	public Monitor(String hostIP, int port, String qcuName, String localQueName){
		this.port = port;   //远程控制台服务器的端口号 请参考\etc\tlsys.conf文件中 [SuperviseBroker]字段
		this.ip = hostIP;   //远程控制台服务器所在的IP地址
		this.qcuName = qcuName;
		this.localQueName = localQueName;
		
	}
	
	protected void connect()
	{
		try
		{
			tlqConnect = new TLQConnect(ip, port);
			tlqConnect.setIsDebug(TLQConnect.DEBUG_NO);
			tlqConnect.connect();
			tlqFac = new TLQOptObjFactory(tlqConnect);
	
		}
		catch(TLQParameterException e)
		{
			e.printStackTrace();
		}
		catch(TLQRemoteException e2)
		{
			e2.printStackTrace();
		}
		
	}
		
	protected void monitorMessage() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue(qcuName);
		TlqPage tlqPage = new TlqPage();
		Map map = opt.getSingleMessages(localQueName,TLQOptLocalQue.MSG_STATUS_READY, tlqPage);
		ArrayList listFieldName = (ArrayList)map.get(TLQOptLocalQue.GET_FIELDNAME_LIST);
		ArrayList listFieldValue = (ArrayList)map.get(TLQOptLocalQue.GET_FIELDVALUE_LIST);
		ArrayList oneValue  = (ArrayList)listFieldValue.get(0);
		int i = 0;
		String name, value = null;
		while( 1==1 )
		{
			System.out.println("------------------------------DispQue  Information------------------------------");
			for(i=0;i<listFieldName.size();i++)
			{
				name = (String)listFieldName.get(i);
				value = (String)oneValue.get(i);
				System.out.println(name + "=" + value);
			}
			System.out.println("-------------------------------------------------------------------------");
			Thread.sleep(3000);
		}
			
	}
		
	protected void close(){
		try
		{
			if(null!=tlqConnect)
				tlqConnect.close();
		} catch (TLQRemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] argv)
	{
		String hostIP,qcuName,localQueName;
		int port;
		Monitor qcuTestSuite;
		
		if (argv.length < 1) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("Monitor HostIP Port QCUName LocalQueName\n");
			
			return;
			}
		if (argv.length != 4) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("Monitor HostIP Port QCUName LocalQueName\n");
			
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			qcuName = argv[2];
			localQueName = argv[3];
			System.out.println(
					"--------------------Monitor Operation begin------------------\n\n");
			
			qcuTestSuite = new Monitor(hostIP, port, qcuName, localQueName);
			qcuTestSuite.connect();  //连接TLQ控制台服务器
			
			try
			{
				qcuTestSuite.monitorMessage();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
						
			qcuTestSuite.close();  //关闭连接
			System.out.println(
                 "\n\n--------------------Monitor Operation end------------------");
		}
	}

}

