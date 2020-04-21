import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.tongtech.jms.FileMessage;
import com.tongtech.tmqi.Queue;
import com.tongtech.tmqi.QueueConnectionFactory;
import com.tongtech.tmqi.jmsclient.MessageProducerImpl;
import com.tongtech.tmqi.jmsclient.SessionImpl;

public class FileMesageControl {
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
		if (args.length != 4) {
			System.out.println("请输入正确的参数");
			System.out.println("文件大小（单位M，比如400) ，预设中断百分比（0 - 100之间,大于100表示不中断）, 中断后是否删除消息(true/false)，如消息发送完成是否强制删除(true/false)");
			return;
		}
		int length = Integer.valueOf(args[0]);
		int percent = Integer.valueOf(args[1]);
		boolean needRemove = Boolean.valueOf(args[2]);
		boolean needForce = Boolean.valueOf(args[3]);
		try {
			setUp();
			File file = createFile("tempFileTestFilecontrol", length);
			FileMessage message = ((SessionImpl) session).createFileMessage(file
					.getAbsolutePath());
			TimoutInterrupt checkPercent = new TimoutInterrupt(
					(MessageProducerImpl) producer, percent, needRemove, needForce);
			new Thread(checkPercent).start();
			try {
				producer.send(message);
				System.out.println("发送完成 。。。 ");
			} catch (Exception e) {
				System.out.println("扑捉到中断异常,发送停止");
			}
			synchronized(FileMesageControl.class){
				try {
					FileMesageControl.class.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			producer.close();
			session.close();
			conn.close();
			file.delete();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

class TimoutInterrupt implements Runnable {
	private MessageProducerImpl producer;
	private int percent;
	private boolean needRemove;
	private boolean needForce;

	public TimoutInterrupt(MessageProducerImpl producer, int percent, boolean needRemove, boolean needForce) {
		this.producer = producer;
		this.percent = percent;
		this.needRemove = needRemove;
		this.needForce = needForce;
	}

	public void run() {
		int currentPercent = 0 ;
		try {
			while(currentPercent != 100){
				Thread.sleep(1000);
				currentPercent = producer.getFileProgressPercentage();
				System.out.println("已经发送"+currentPercent+"%");
				if(currentPercent >= percent){
					System.out.println("中断...");
					producer.interruptFileSending(producer.getMessageId());
					break;
				}
			}
			Thread.sleep(2000);
			if(needRemove){
			System.out.println("删除 。。。");
				try {
					producer.removeFilemessage(producer.getMessageId(), needForce);
				} catch (JMSException e) {
					if(producer.getFileProgressPercentage() == 100 && !needForce){
						System.out.println("注意 ： 对发送完成的消息，必须强制删除");
					}else{
						e.printStackTrace();
					}
				}
				System.out.println("删除完成 。。。");
			}		
			synchronized(FileMesageControl.class){
				FileMesageControl.class.notifyAll();
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}

