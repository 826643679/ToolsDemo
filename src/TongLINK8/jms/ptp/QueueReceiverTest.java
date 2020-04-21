import java.util.Properties;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;
import javax.naming.Context;

import com.tongtech.jms.FileMessage;


public class QueueReceiverTest {
    public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";/* initial context factory*/
 
    public static final String remoteURL = "tlq://127.0.0.1:10025";
    public static final String remoteFactory = "RemoteConnectionFactory";
	
	public static void main(String[] args) {
 
		ConnectionFactory testConnFactory = null;
		Connection myConn = null;
		Session mySession = null;
		Queue testQueue = null;
		MessageConsumer testConsumer = null;
		MessageProducer testProducer = null;
		
		try {
			
			 Properties pro=new Properties();
	
		  pro.setProperty("java.naming.factory.initial", tcf);
		  pro.setProperty("java.naming.provider.url", remoteURL);
		        
		     
		  Context context = new javax.naming.InitialContext(pro);

			testConnFactory = (ConnectionFactory) context.lookup(remoteFactory);
			testQueue = (Queue)context.lookup("MyQueue");;
		
			myConn = testConnFactory.createConnection();
			
			mySession = myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			testProducer = mySession.createProducer(testQueue);
			testConsumer = mySession.createConsumer(testQueue);
			myConn.start();
			TextMessage tMsg = mySession.createTextMessage("TEST");
			testProducer.send(tMsg);
			Message message = testConsumer.receive(2000);
			
			if(message != null){
				if(message instanceof TextMessage){
					TextMessage textMessage = (TextMessage)message;
					System.out.println("�յ�һ��Text��Ϣ:"+textMessage.getText());
				}else if(message instanceof MapMessage ){
					System.out.println("�յ�һ��Map��Ϣ");
				}else if(message instanceof StreamMessage ){
					System.out.println("�յ�һ��Text��Ϣ");
				}else if(message instanceof BytesMessage ){
					System.out.println("�յ�һ��Bytes��Ϣ");
				}else if(message instanceof ObjectMessage ){
					System.out.println("�յ�һ��Object��Ϣ");
				}else if(message instanceof FileMessage ){
					System.out.println("�յ�һ���ļ���Ϣ");
				}
			}else{
				System.out.println("û���յ���Ϣ");
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
				System.out.println("�˳�ʱ��������");
				e.printStackTrace();
			}
		}
	}


}
