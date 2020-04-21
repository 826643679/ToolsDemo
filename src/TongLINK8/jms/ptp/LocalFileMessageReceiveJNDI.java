import java.util.Properties;
import java.io.File;
import javax.jms.Connection;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

import com.tongtech.jms.FileMessage;
import com.tongtech.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class LocalFileMessageReceiveJNDI {

	public static void main(String[] args) {
		Context ctx = null;
		Connection conn = null;
		Session session = null;
		try {
			Properties pro = new Properties();
			pro.setProperty(Context.INITIAL_CONTEXT_FACTORY, "tongtech.jms.jndi.JmsContextFactory");
			pro.setProperty(Context.PROVIDER_URL, "tlq://127.0.0.1:10025");
			ctx = new InitialContext(pro);

			QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup("LocalQueueConnectionFactory");
			Queue queue = (Queue) ctx.lookup("lq1");
			conn = connFactory.createConnection();
			session = (Session) conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			MessageProducer producer = session.createProducer(queue);
			MessageConsumer consumer = session.createConsumer(queue);

			FileMessage fileMsg = session.createFileMessage();
			File file = new File("text.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			fileMsg.setFile(file.getAbsolutePath());
			producer.send(fileMsg);
			System.out.println("发送了一条文件消息: " + fileMsg.getFile());
			conn.start();
			FileMessage recvMsg = (FileMessage) consumer.receive(5000);
			System.out.println("收到一条文件消息 : " + recvMsg.getFile());
		} catch (Exception e) {
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
				System.out.println("退出时发生错误。");
				e.printStackTrace();
			}
		}
	}
}
