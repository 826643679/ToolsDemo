import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.conf.Program;
import com.tongtech.tlq.admin.conf.QCU;
import com.tongtech.tlq.admin.remote.api.*;
import com.tongtech.tlq.admin.remote.api.node.TLQOptProgram;
import com.tongtech.tlq.admin.remote.api.qcu.*;


public class StartStopTlq {
	
	
	public static TLQConnect tlqConnect = null;
	public static TLQOptObjFactory tlqFac = null;
	
	String ip, qcuName;
	Integer port, operationType;
		
	public StartStopTlq(String hostIP, int port, int operationType, String qcuName){
		this.port = port;   //远程控制台服务器的端口号 请参考\etc\tlsys.conf文件中 [SuperviseBroker]字段
		this.ip = hostIP;   //远程控制台服务器所在的IP地址
		this.operationType = operationType;
		this.qcuName = qcuName;
	}
	
	protected void connect()
	{
		try
		{
			tlqConnect = new TLQConnect(ip, port);
			tlqConnect.setIsDebug(TLQConnect.DEBUG_YES);
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
	
	protected void startQCU() throws Exception {
		
		TLQOptQCU opt=tlqFac.getTLQOptQCU();
		opt.startQCU(qcuName);
		Thread.sleep(20000);
		
	}
	
	protected void stopQCU() throws Exception {
		
		TLQOptQCU opt=tlqFac.getTLQOptQCU();
		opt.stopQCUByAbort(qcuName);
		Thread.sleep(20000);
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
		String hostIP,qcuName;
		int port,operationType;
		StartStopTlq qcuTestSuite;
		
		if (argv.length < 1) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("StartStopTlq HostIP Port OperationType QCUName\n");
			System.out.println( "---------注: OperationType = 0 停止TLQ ---------");
			System.out.println( "	     OperationType = 1 启动TLQ ---------");
			return;
			}
		if (argv.length != 4) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("StartStopTlq HostIP Port OperationType QCUName\n");
			System.out.println( "---------注: OperationType = 0 停止TLQ ---------");
			System.out.println( "	     OperationType = 1 启动TLQ ---------");
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			operationType = Integer.parseInt(argv[2]);
			qcuName = argv[3];
			
			System.out.println(
					"--------------------StartStopTlq Operation begin------------------\n\n");
			
			qcuTestSuite = new StartStopTlq(hostIP, port, operationType, qcuName);
			qcuTestSuite.connect();  //连接TLQ控制台服务器
			
			switch(operationType)
			{
			case 0:
				try
				{
					qcuTestSuite.stopQCU();  
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 1:
				try
				{
					qcuTestSuite.startQCU();  
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			}
			qcuTestSuite.close();  //关闭连接
			System.out.println(
                 "\n\n--------------------StartStopTlq Operation end------------------");
		}
	}

}


