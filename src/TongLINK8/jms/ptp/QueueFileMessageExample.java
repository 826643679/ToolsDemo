import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import java.io.File;
import com.tongtech.jms.FileMessage;
import com.tongtech.jms.Session;


public class QueueFileMessageExample {
    public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";/* initial context factory*/

    public static final String remoteURL = "tlq://127.0.0.1:10025";
    public static final String remoteFactory = "RemoteConnectionFactory";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ConnectionFactory testConnFactory = null;
		Connection myConn = null;
		Session mySession = null;
		Queue testQueue = null;
		MessageProducer testProducer = null;
		MessageConsumer testConsumer = null;
		FileMessage fileMessage = null;

		try {
			Properties pro = new Properties();

		
			pro.setProperty("java.naming.factory.initial", tcf);
			pro.setProperty("java.naming.provider.url", remoteURL);
			
			javax.naming.Context ctx = new javax.naming.InitialContext(pro);

			testConnFactory = (javax.jms.ConnectionFactory) ctx.lookup(remoteFactory);
			testQueue = (javax.jms.Queue) ctx.lookup("MyQueue");

			myConn = testConnFactory.createConnection();
			mySession = (com.tongtech.jms.Session)myConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			testProducer = mySession.createProducer(testQueue);
			testConsumer = mySession.createConsumer(testQueue);
			myConn.start();
			File file = new File("text.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			fileMessage = mySession.createFileMessage(file.getAbsolutePath());//�ļ���Ҫ�ڻ�������TLQSNDFILESDIRָ���Ŀ¼�д���
			System.out.println("������Ϣ...");
			testProducer.send(fileMessage);
			System.out.println("�������...");
			Message message = testConsumer.receive(3000);
			if(message != null){
				if(message instanceof FileMessage){
					FileMessage msg= (FileMessage)message;
					System.out.println("���ܵ�1���ļ���Ϣ,�ļ���:"+msg.getFile());//�ļ��洢��TLQRCVFILESDIRָ���Ŀ¼��
				}else{
					System.out.println("���ܵ�1�����ļ�������Ϣ");
				}
			}else{
					System.out.println("δ�յ���Ϣ");
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
