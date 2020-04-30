package TongLINK8.base;
/**
 * TLQ配置信息
 * @author liyiaa
 *
 */
public class TlqConfing {
	
	//连接工厂
	public static final String tcf = "tongtech.jms.jndi.JmsContextFactory";
	
	//连接地址
	public static final String url = "127.0.0.1";
	
	//连接端口
	
	public static final String port = "10252";
	
	//队列名称
	public static final String MyQueue = "sendqcu";
	
	//remoteURL
	public static final String remoteURL = "tlq://"+url+":"+port;
	
	//remoteFactory
    public static final String remoteFactory = "RemoteConnectionFactory";

    //队列控制单元名称
	public static final String QcuName = "sendqcu";

	//队列名称
	public static final String QName = "lq";

	//文件名
	public static final String FileName = "no";    //注：如果消息类型为B,则FileName输入为no！

	//消息类型
	public static final String MsgType = "B";
	

}
