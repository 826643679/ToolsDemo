

import com.tongtech.tlq.admin.conf.LocalQue;
import com.tongtech.tlq.admin.remote.api.*;
import com.tongtech.tlq.admin.remote.api.qcu.*;


public class ModifyQcu {
	
	
	public static TLQConnect tlqConnect = null;
	public static TLQOptObjFactory tlqFac = null;
	
	String ip, localQueName;
	Integer port, operationType, value;
		
	public ModifyQcu(String hostIP, int port, int operationType, String localQueName, int value ){
		this.port = port;   //Զ�̿���̨�������Ķ˿ں� ��ο�\etc\tlsys.conf�ļ��� [SuperviseBroker]�ֶ�
		this.ip = hostIP;   //Զ�̿���̨���������ڵ�IP��ַ
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
		obj.setLocalQueName(localQueName);  //����һ�����ض���
		opt.addLocalQue(obj);
	}
	
	protected void deleteLocalQue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		opt.deleteLocalQueByNormal(localQueName); //ɾ��һ�����ض���
	}
	
	protected void getValue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		LocalQue obj= opt.getLocalQue(localQueName);
		String name =obj.getMsgNum().getValue();   //��ȡָ�����ض�����MsgNum���ֵ
		System.out.println("The MsgNum Value in " + localQueName + " queue is " + name);
	}
	
	protected void setValue() throws Exception {
		
		TLQOptLocalQue opt=tlqFac.getTLQOptLocalQue("qcu1");
		LocalQue obj= opt.getLocalQue(localQueName);
		obj.setMsgNum(value);  //����ָ�����ض�����MsgNum���ֵ
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
			System.out.println("--------------�����������--------------\n");
			System.out.println("ModifyQcu HostIP Port OperationType LocalQueName Value\n");
			System.out.println( "---------ע: OperationType = 1 ����һ�����ض��� ---------");
			System.out.println( "	     OperationType = 2 ɾ��һ�����ض��� ---------");
			System.out.println( "            OperationType = 3 ��ȡָ�����ض�����MsgNum��ֵ ---------");
			System.out.println( "            OperationType = 4 �޸�ָ�����ض�����MsgNum��ֵ---------");
			return;
			}
		if (argv.length != 5) {
			System.out.println("--------------�����������--------------\n");
			System.out.println("ModifyQcu HostIP Port OperationType LocalQueName Value\n");
			System.out.println( "---------ע: OperationType = 1 ����һ�����ض��� ---------");
			System.out.println( "	     OperationType = 2 ɾ��һ�����ض��� ---------");
			System.out.println( "            OperationType = 3 ��ȡָ�����ض�����MsgNum��ֵ ---------");
			System.out.println( "            OperationType = 4 �޸�ָ�����ض�����MsgNum��ֵ---------");
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
			qcuTestSuite.connect();  //����TLQ����̨������
			
			switch(operationType)
			{
			case 1:
				try
				{
					qcuTestSuite.addLocalQue();  //����Loacal Que����
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				break;
			case 2:
				try
				{
					qcuTestSuite.deleteLocalQue();  //ɾ��Loacal Que����
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
			qcuTestSuite.close();  //�ر�����
			System.out.println(
                 "\n\n--------------------ModifyQcu Operation end------------------");
		}
	}

}





