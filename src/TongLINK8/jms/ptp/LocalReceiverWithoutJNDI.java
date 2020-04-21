import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;

public class LocalReceiverWithoutJNDI {
	
	public static void main(String[] args) {
		com.tongtech.tmqi.ConnectionFactory connectionFactory = null;
		Connection conn = null;
		Session session = null;
		String url = "tlqlocal://localhost/qcu1";
		try {
			
			connectionFactory = new com.tongtech.tmqi.ConnectionFactory();
			connectionFactory.setProperty("tmqiAddressList", url);
			conn = connectionFactory.createConnection();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			Queue myQueue = new com.tongtech.tmqi.Queue("lq1");
			MessageProducer producer = session.createProducer(myQueue);
			TextMessage msg = session.createTextMessage();
			msg.setText("this a textMessage .");
			producer.send(msg);
			MessageConsumer consumer = session.createConsumer(myQueue);
			conn.start();
			Message message = consumer.receive();
			if(message instanceof TextMessage){
				TextMessage recvMsg = (TextMessage)msg;
				System.out.println("receiv a textMessage : " + recvMsg.getText());
			}
			session.close();
			conn.close();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (session != null) {
					session.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
