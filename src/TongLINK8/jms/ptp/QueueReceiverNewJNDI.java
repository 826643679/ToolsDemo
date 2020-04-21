import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import com.tongtech.jms.FileMessage;


public class QueueReceiverNewJNDI {
    public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";/* initial context factory*/
   
    public static final String remoteURL = "tlq://127.0.0.1:10025";
    public static final String remoteFactory = "RemoteConnectionFactory";
	
	public static void main(String[] args) {
   
		ConnectionFactory testConnFactory = null;
		Connection myConn = null;
		Session mySession = null;
		Queue testQueue = null;
		MessageConsumer testConsumer = null;
		
		try {
			Properties pro = new Properties();
		
			pro.put("java.naming.factory.initial", tcf);
			pro.setProperty("java.naming.provider.url", remoteURL);
			javax.naming.Context ctx = new javax.naming.InitialContext(pro);
			
			testConnFactory = (javax.jms.ConnectionFactory) ctx.lookup(remoteFactory);
			testQueue = (javax.jms.Queue) ctx.lookup("MyQueue");
		
			myConn = testConnFactory.createConnection();
			mySession = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			testConsumer = mySession.createConsumer(testQueue);
			myConn.start();
			
			Message message = testConsumer.receive(2000);
			
			if(message != null){
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage)message;
					System.out.println("收到一条Text消息:"+textMessage.getText());
				}else if(message instanceof MapMessage ){
					System.out.println("收到一条Map消息");
				}else if(message instanceof StreamMessage ){
					System.out.println("收到一条Text消息");
				}else if(message instanceof BytesMessage ){
					System.out.println("收到一条Bytes消息");
				}else if(message instanceof ObjectMessage ){
					System.out.println("收到一条Object消息");
				}else if(message instanceof FileMessage ){
					System.out.println("收到一条文件消息");
				}
			}else{
				System.out.println("没有收到消息");
			}
		} catch (Exception jmse) {
			System.out.println("Exception oxxurred :" + jmse.toString());
			jmse.printStackTrace();
		} finally {
			try {
				if (mySession != null) {
					mySession.close();
				}
				if (myConn != null) {
					myConn.close();
				}
			} catch (Exception e) {
				System.out.println("退出时发生错误。");
				e.printStackTrace();
			}
		}
	}

	
}
