

import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.conf.Program;
import com.tongtech.tlq.admin.conf.QCU;
import com.tongtech.tlq.admin.remote.api.*;
import com.tongtech.tlq.admin.remote.api.node.TLQOptProgram;
import com.tongtech.tlq.admin.remote.api.qcu.*;


public class ModifySys {
	
	
	public static TLQConnect tlqConnect = null;
	public static TLQOptObjFactory tlqFac = null;
	
	String ip, operationName;
	Integer port, operationType;
		
	public ModifySys(String hostIP, int port, int operationType, String operationName){
		this.port = port;   //Զ�̿���̨�������Ķ˿ں� ��ο�\etc\tlsys.conf�ļ��� [SuperviseBroker]�ֶ�
		this.ip = hostIP;   //Զ�̿���̨���������ڵ�IP��ַ
		this.operationType = operationType;
		this.operationName = operationName;
		
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
	
	protected void addQCU() throws Exception {
		
		TLQOptQCU opt=tlqFac.getTLQOptQCU();
		QCU obj= new QCU();
		obj.setQcuName(operationName);
		obj.setQcuStatus(0);
		opt.addQCU(obj);   //����һ��QCU
		if(opt.getResourceType()==TLQOptBaseObj.OPER_RESOURCE_MEMROY)
			Thread.sleep(20000);
		
	}
	
	protected void deleteQCU() throws Exception {
		
		TLQOptQCU opt=tlqFac.getTLQOptQCU();
		opt.deleteQCUByNormal(operationName); //ɾ��һ��QCU
	}
	
	protected void addProgram() throws Exception {
		
		TLQOptProgram opt=tlqFac.getTLQOptProgram();
		Program obj = new Program();
		obj.setPrgID(Integer.parseInt(operationName));
		opt.addProgram(obj);     //����һ��Program
	}
	
	protected void deleteProgram() throws Exception {
		
		TLQOptProgram opt=tlqFac.getTLQOptProgram();
		opt.deleteProgram(operationName);   //ɾ��һ��Program
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
		String hostIP,operationName;
		int port,operationType;
		ModifySys qcuTestSuite;
		
		if (argv.length < 1) {
			System.out.println("--------------�����������--------------\n");
			System.out.println("ModifySys HostIP Port OperationType OperationName\n");
			System.out.println( "---------ע: OperationType = 1 ����һ�����п��Ƶ�Ԫqcu ---------");
			System.out.println( "	     OperationType = 2 ɾ��һ�����п��Ƶ�Ԫqcu ---------");
			System.out.println( "            OperationType = 3 ����һ������ԪProgram ---------");
			System.out.println( "            OperationType = 4 ɾ��һ������ԪProgram---------");
			return;
			}
		if (argv.length != 4) {
			System.out.println("--------------�����������--------------\n");
			System.out.println("ModifySys HostIP Port OperationType OperationName\n");
			System.out.println( "---------ע: OperationType = 1 ����һ�����п��Ƶ�Ԫqcu ---------");
			System.out.println( "	     OperationType = 2 ɾ��һ�����п��Ƶ�Ԫqcu ---------");
			System.out.println( "            OperationType = 3 ����һ������ԪProgram ---------");
			System.out.println( "            OperationType = 4 ɾ��һ������ԪProgram---------");
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			operationType = Integer.parseInt(argv[2]);
			operationName = argv[3];
			System.out.println(
					"--------------------ModifySys Operation begin------------------\n\n");
			
			qcuTestSuite = new ModifySys(hostIP, port, operationType, operationName);
			qcuTestSuite.connect();  //����TLQ����̨������
			
			switch(operationType)
			{
			case 1:
				try
				{
					qcuTestSuite.addQCU();  
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 2:
				try
				{
					qcuTestSuite.deleteQCU();  
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 3:
				try
				{
					qcuTestSuite.addProgram(); 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 4:
				try
				{
					qcuTestSuite.deleteProgram(); 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
			qcuTestSuite.close();  //�ر�����
			System.out.println(
                 "\n\n--------------------ModifySys Operation end------------------");
		}
	}

}

