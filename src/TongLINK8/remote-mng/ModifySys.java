

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
		this.port = port;   //远程控制台服务器的端口号 请参考\etc\tlsys.conf文件中 [SuperviseBroker]字段
		this.ip = hostIP;   //远程控制台服务器所在的IP地址
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
		opt.addQCU(obj);   //增加一个QCU
		if(opt.getResourceType()==TLQOptBaseObj.OPER_RESOURCE_MEMROY)
			Thread.sleep(20000);
		
	}
	
	protected void deleteQCU() throws Exception {
		
		TLQOptQCU opt=tlqFac.getTLQOptQCU();
		opt.deleteQCUByNormal(operationName); //删除一个QCU
	}
	
	protected void addProgram() throws Exception {
		
		TLQOptProgram opt=tlqFac.getTLQOptProgram();
		Program obj = new Program();
		obj.setPrgID(Integer.parseInt(operationName));
		opt.addProgram(obj);     //增加一个Program
	}
	
	protected void deleteProgram() throws Exception {
		
		TLQOptProgram opt=tlqFac.getTLQOptProgram();
		opt.deleteProgram(operationName);   //删除一个Program
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
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("ModifySys HostIP Port OperationType OperationName\n");
			System.out.println( "---------注: OperationType = 1 增加一个队列控制单元qcu ---------");
			System.out.println( "	     OperationType = 2 删除一个队列控制单元qcu ---------");
			System.out.println( "            OperationType = 3 增加一个程序单元Program ---------");
			System.out.println( "            OperationType = 4 删除一个程序单元Program---------");
			return;
			}
		if (argv.length != 4) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("ModifySys HostIP Port OperationType OperationName\n");
			System.out.println( "---------注: OperationType = 1 增加一个队列控制单元qcu ---------");
			System.out.println( "	     OperationType = 2 删除一个队列控制单元qcu ---------");
			System.out.println( "            OperationType = 3 增加一个程序单元Program ---------");
			System.out.println( "            OperationType = 4 删除一个程序单元Program---------");
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			operationType = Integer.parseInt(argv[2]);
			operationName = argv[3];
			System.out.println(
					"--------------------ModifySys Operation begin------------------\n\n");
			
			qcuTestSuite = new ModifySys(hostIP, port, operationType, operationName);
			qcuTestSuite.connect();  //连接TLQ控制台服务器
			
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
			qcuTestSuite.close();  //关闭连接
			System.out.println(
                 "\n\n--------------------ModifySys Operation end------------------");
		}
	}

}

