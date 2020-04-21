
import javax.jms.*; // The JMS classes
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.naming.*; // The JNDI classes

class PtoP {

    public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";/* initial context factory*/
   
    public static final String remoteURL = "tlq://127.0.0.1:10025";
    public static final String remoteFactory = "RemoteConnectionFactory";

    public static void main(String args[]) {
    
        java.util.Properties prop = new java.util.Properties();
        /*初始化上下文*/
        Context ictx = null;
        Queue queue = null;
        QueueConnectionFactory qcf = null;
        
		
        prop.put("java.naming.factory.initial", tcf);
        prop.put("java.naming.provider.url", remoteURL);		        
        try {
            ictx = new InitialContext(prop);
        } catch (NamingException ne) {
            System.out.println("error:" + ne);
            System.exit(-1);
        }
        try {
            // Look up connection factory and queue.
            qcf = (QueueConnectionFactory) ictx.lookup(remoteFactory);
            queue = (Queue) ictx.lookup("MyQueue");
        } catch (NamingException ne) {
            System.out.println("naming error:" + ne);
            System.exit(-1);
        }
        try {
            //Establish QueueConnection and QueueSession
            QueueConnection conn = qcf.createQueueConnection();
            conn.start();
            QueueSession sess = conn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
           
            /*send message*/
            QueueSender queueSender =  sess.createSender(queue);
            Message message = sess.createTextMessage("A message body");
           queueSender.send(message);
           
            /*receive message*/
            QueueReceiver receiver =  sess.createReceiver(queue);
     
           
          Message  messagerev =  receiver.receive();
            if (messagerev instanceof TextMessage) {
                String text = ((TextMessage) messagerev).getText();
                System.out.println(text);
            } else {
                System.out.println("message is not TextMessage");
            }
            // 关闭使用完毕的对象
            queueSender.close();
            receiver.close();
            sess.close();
            conn.close();

        } catch (JMSException je) {
            System.out.println("jms error:" + je);
            System.exit(-1);
        }
    }


}
