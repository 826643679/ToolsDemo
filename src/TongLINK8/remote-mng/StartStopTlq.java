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
		this.port = port;   //Զ�̿���̨�������Ķ˿ں� ��ο�\etc\tlsys.conf�ļ��� [SuperviseBroker]�ֶ�
		this.ip = hostIP;   //Զ�̿���̨���������ڵ�IP��ַ
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
			System.out.println("--------------�����������--------------\n");
			System.out.println("StartStopTlq HostIP Port OperationType QCUName\n");
			System.out.println( "---------ע: OperationType = 0 ֹͣTLQ ---------");
			System.out.println( "	     OperationType = 1 ����TLQ ---------");
			return;
			}
		if (argv.length != 4) {
			System.out.println("--------------�����������--------------\n");
			System.out.println("StartStopTlq HostIP Port OperationType QCUName\n");
			System.out.println( "---------ע: OperationType = 0 ֹͣTLQ ---------");
			System.out.println( "	     OperationType = 1 ����TLQ ---------");
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			operationType = Integer.parseInt(argv[2]);
			qcuName = argv[3];
			
			System.out.println(
					"--------------------StartStopTlq Operation begin------------------\n\n");
			
			qcuTestSuite = new StartStopTlq(hostIP, port, operationType, qcuName);
			qcuTestSuite.connect();  //����TLQ����̨������
			
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
			qcuTestSuite.close();  //�ر�����
			System.out.println(
                 "\n\n--------------------StartStopTlq Operation end------------------");
		}
	}

}


