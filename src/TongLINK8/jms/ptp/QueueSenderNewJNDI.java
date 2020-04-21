import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Queue;
import javax.jms.TextMessage;

public class QueueSenderNewJNDI {
    public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";/* initial context factory*/
   
    public static final String remoteURL = "tlq://127.0.0.1:10025";   
    public static final String remoteFactory = "RemoteConnectionFactory";

	public static void main(String[] args) {
   
		ConnectionFactory testConnFactory = null;
		Connection myConn = null;
		Session mySession = null;
		Queue testQueue = null;
		MessageProducer testProducer = null;
		MessageConsumer testConsumer = null;
		TextMessage testMessage = null;

		try {
			Properties pro = new Properties();
	
			pro.put("java.naming.factory.initial", tcf);
			pro.setProperty("java.naming.provider.url", remoteURL);
			javax.naming.Context ctx = new javax.naming.InitialContext(pro);
			testConnFactory = (javax.jms.ConnectionFactory) ctx.lookup(remoteFactory);
			testQueue = (javax.jms.Queue) ctx.lookup("MyQueue");

			myConn = testConnFactory.createConnection();
			mySession = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			testProducer = mySession.createProducer(testQueue);
			testConsumer = mySession.createConsumer(testQueue);
			testMessage = mySession	.createTextMessage("QueueSenderJNDI Message");
			myConn.start();
			
			System.out.println("发送消息...");
			testProducer.send(testMessage);
			System.out.println("发送完成...");
			TextMessage msg = (TextMessage)testConsumer.receive(2000);
			System.out.println(msg.getText());

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
