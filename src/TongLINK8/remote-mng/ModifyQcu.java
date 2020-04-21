

import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.remote.api.*;
import com.tongtech.tlq.admin.remote.api.qcu.*;


public class ModifyQcu {
	
	
	public static TLQConnect tlqConnect = null;
	public static TLQOptObjFactory tlqFac = null;
	
	String ip, localQueName;
	Integer port, operationType, value;
		
	public ModifyQcu(String hostIP, int port, int operationType, String localQueName, int value ){
		this.port = port;   //远程控制台服务器的端口号 请参考\etc\tlsys.conf文件中 [SuperviseBroker]字段
		this.ip = hostIP;   //远程控制台服务器所在的IP地址
		this.operationType = operationType;
		this.localQueName = localQueName;
		this.value = value;
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
	
	protected void addLocalQue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		LocalQue obj= new LocalQue();
		obj.setLocalQueName(localQueName);  //增加一个本地队列
		opt.addLocalQue(obj);
	}
	
	protected void deleteLocalQue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		opt.deleteLocalQueByNormal(localQueName); //删除一个本地队列
	}
	
	protected void getValue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		LocalQue obj= opt.getLocalQue(localQueName);
		String name =obj.getMsgNum().getValue();   //读取指定本地队列中MsgNum域的值
		System.out.println("The MsgNum Value in " + localQueName + " queue is " + name);
	}
	
	protected void setValue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		LocalQue obj= opt.getLocalQue(localQueName);
		obj.setMsgNum(value);  //设置指定本地队列中MsgNum域的值
		opt.setLocalQue(obj);
		System.out.println("The MsgNum Value in " + localQueName + " queue has been set to " + value);
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
		String hostIP,localQueName;
		int port,operationType,value;
		ModifyQcu qcuTestSuite;
		
		if (argv.length < 1) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("ModifyQcu HostIP Port OperationType LocalQueName Value\n");
			System.out.println( "---------注: OperationType = 1 增加一个本地队列 ---------");
			System.out.println( "	     OperationType = 2 删除一个本地队列 ---------");
			System.out.println( "            OperationType = 3 读取指定本地队列中MsgNum的值 ---------");
			System.out.println( "            OperationType = 4 修改指定本地队列中MsgNum的值---------");
			return;
			}
		if (argv.length != 5) {
			System.out.println("--------------请输入参数！--------------\n");
			System.out.println("ModifyQcu HostIP Port OperationType LocalQueName Value\n");
			System.out.println( "---------注: OperationType = 1 增加一个本地队列 ---------");
			System.out.println( "	     OperationType = 2 删除一个本地队列 ---------");
			System.out.println( "            OperationType = 3 读取指定本地队列中MsgNum的值 ---------");
			System.out.println( "            OperationType = 4 修改指定本地队列中MsgNum的值---------");
			} 
		else {
			hostIP = argv[0];
			port = Integer.parseInt(argv[1]);
			operationType = Integer.parseInt(argv[2]);
			localQueName = argv[3];
			value = Integer.parseInt(argv[4]);
			System.out.println(
					"--------------------ModifyQcu Operation begin------------------\n\n");
			
			qcuTestSuite = new ModifyQcu(hostIP, port, operationType, localQueName, value);
			qcuTestSuite.connect();  //连接TLQ控制台服务器
			
			switch(operationType)
			{
			case 1:
				try
				{
					qcuTestSuite.addLocalQue();  //增加Loacal Que队列
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 2:
				try
				{
					qcuTestSuite.deleteLocalQue();  //删除Loacal Que队列
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 3:
				try
				{
					qcuTestSuite.getValue(); 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			case 4:
				try
				{
					qcuTestSuite.setValue(); 
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				break;
			}
			qcuTestSuite.close();  //关闭连接
			System.out.println(
                 "\n\n--------------------ModifyQcu Operation end------------------");
		}
	}

}





