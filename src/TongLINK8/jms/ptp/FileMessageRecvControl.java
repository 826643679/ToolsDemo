import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.tongtech.jms.FileMessage;
import com.tongtech.tmqi.Queue;
import com.tongtech.tmqi.QueueConnectionFactory;
import com.tongtech.tmqi.jmsclient.MessageConsumerImpl;
import com.tongtech.tmqi.jmsclient.SessionImpl;

public class FileMessageRecvControl {
	private static QueueConnectionFactory qcf;
	private static Queue queue;
	private static Connection conn;
	private static Session session;
	private static MessageProducer producer;

	private static void setUp() throws JMSException {
		qcf = new QueueConnectionFactory();
		qcf.setProperty("tmqiAddressList", "tlkq://localhost:10024");
		queue = new Queue("lq1");
		conn = qcf.createConnection();
		conn.start();
		session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		producer = session.createProducer(queue);
	}

	private static File createFile(String name, int fileLength) {
		File file = new File(name);
		try {
			if (!file.exists()) {
				file.createNewFile();
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(file));
				byte[] b = new byte[1024 * 1024];
				for (int i = 0; i < fileLength; i++) {
					os.write(b);
				}
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("请输入正确的参数");
			System.out
					.println("文件大小（单位M，比如400) ");
			return;
		}
		int length = Integer.valueOf(args[0]);
		try {
			setUp();
			File file = createFile("tempFileTestFilecontrol", length);
			FileMessage message = ((SessionImpl) session)
					.createFileMessage(file.getAbsolutePath());
			try {
				producer.send(message);
				System.out.println("发送完成 。。。 ");
			} catch (Exception e) {
				System.out.println("扑捉到中断异常,发送停止");
				e.printStackTrace();
			}
			MessageConsumer consumer = session.createConsumer(queue);
			CheckRecvPercentage checkPercent = new CheckRecvPercentage(
					(MessageConsumerImpl) consumer);
			new Thread(checkPercent).start();
			Message msg = consumer.receive();
			System.out.println("收到消息:+"+msg.getJMSMessageID());
			consumer.close();
			producer.close();
			session.close();
			conn.close();
			file.delete();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}

class CheckRecvPercentage implements Runnable {
	private MessageConsumerImpl consumer;

	public CheckRecvPercentage(MessageConsumerImpl consumer) {
		this.consumer = consumer;
	}

	public void run() {
		int currentPercent = 0;
			try {
				while (currentPercent != 100) {
					Thread.sleep(1000);
					currentPercent = consumer.getFileProgressPercentage();
					System.out.println("已经接收了" + currentPercent + "%");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
