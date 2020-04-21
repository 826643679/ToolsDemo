

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.remote.JMXServiceURL;

import com.tongtech.tlq.admin.common.TlqPage;
import com.tongtech.tlq.admin.conf.NodeSystemInfo;
import com.tongtech.tlq.admin.dynamic.ConstDefine;
import com.tongtech.tlq.admin.remote.jmx.node.TLQOptNodeSystem;
import com.tongtech.tlq.admin.remote.jmx.qcu.TLQOptLocalQue;
import com.tongtech.tlq.admin.remote.jmx.TLQConnect;
import com.tongtech.tlq.admin.remote.jmx.TLQConnector;
import com.tongtech.tlq.admin.remote.jmx.TLQOptBaseObj;
import com.tongtech.tlq.admin.remote.jmx.TLQOptObjFactory;

public class TestJMX {
	public static void main(String[] args) {
		// 演示2 - 使用工厂类获取MBean接口,工厂类封装了JMX标准接口
		try {
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi://localhost/jndi/rmi://localhost:12099/myconnector");
			TLQConnector tlqAttr=new TLQConnector("127.0.0.1", 10252);
			tlqAttr.setLocale(Locale.ENGLISH);
			tlqAttr.setIsDebug(ConstDefine.LOG_DEBUG_YES);
			tlqAttr.setRecvDataTmOut(300000);
			TLQOptObjFactory tlqFac = new TLQOptObjFactory(url, null,
					//new TLQConnectAttribute("127.0.0.1", 10252));
					tlqAttr);
			TLQConnect tlqConnect = tlqFac.getTLQConnect();
			tlqConnect.connect();
//			4.从TLQ操作对象工厂中获取操作TLQ的具体操作对象(TLQOptNodeSystem)
			TLQOptNodeSystem tlqSystem= tlqFac.getTLQOptNodeSystem();
			//5.可以设置TLQ的操作对象是操作内存还是文件，默认是内存
			tlqSystem.setResourceType(TLQOptBaseObj.OPER_RESOURCE_FILE);
			//6.从操作对象(TLQOptNodeSystem)中获取服务器节点的配置信息(NodeSystemInfo)
			NodeSystemInfo nodeInfo = tlqSystem.getNodeSystemInfo();
			NodeSystemInfo nodeInfoBak = tlqSystem.getNodeSystemInfo();
			System.out.println("NodeName="+nodeInfo.getNodeName().getValue());
			System.out.println("LogSize="+nodeInfo.getLogSize().valueToInt());
			//7.可以修改配置信息(NodeSystemInfo)
			nodeInfo.setNodeName("TestNode");
			nodeInfo.setLogSize(102400);
			//8.判断字段动态修改后是否能立即生效
			if(nodeInfo.getNodeName().dyModify==TLQOptNodeSystem.DYNAMIC_FLAG_YES){
				System.out.println("此字段修改后，能够立即生效");
			}else{
				System.out.println("此字段修改后，不能立即生效");
			}
			tlqSystem.setNodeSystemInfo(nodeInfo);
			
			tlqSystem.setNodeSystemInfo(nodeInfoBak);
			/*
			 * 9.配置信息(NodeSystemInfo)保存到远程节点，不是所有配置项修改内存后就能立即生效。能否立即生效根据(7.)判断可知。
			 * 9.1 操作资源为文件时，只对文件进行修改。
			 * 9.2 操作资源为内存时，此时必须是节点处于工作或QCU处于工作状态，才能对内存进行操作。
			 * 只有能立即生效的配置项，修改后才能生效。
			 * 总之，不管是对内存或文件操作，所有配置项修改都会保存到文件中，下次节点或QCU重启后，也会生效。
			 */
			Properties prop=tlqSystem.getNodeSpvInfo();
			Set set = prop.keySet();
			TLQOptLocalQue toLocQue = tlqFac.getTLQOptLocalQue("qcu1");
			TlqPage tlqPage = new TlqPage();
			Map map = toLocQue.getSingleMessages("lq",TLQOptBaseObj.MSG_STATUS_READY, tlqPage);
			ArrayList listFieldName =(ArrayList) map.get(TLQOptBaseObj.GET_FIELDNAME_LIST);
			ArrayList listFieldValue = (ArrayList)map.get(TLQOptBaseObj.GET_FIELDVALUE_LIST);
			Properties statInfo = (Properties)map.get(TLQOptBaseObj.GET_STATINFO_PROPERTIES);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void echo(String msg) {
		System.out.println(msg);
	}

	private static void echoProperties(Properties p) {
		Set set = p.keySet();
		for (Iterator i = set.iterator(); i.hasNext();) {
			Object key = i.next();
			Object value = p.get(key);
			echo(key + " = " + value);
		}
	}
	private static void echoMap(Map p) {
		Set set = p.keySet();
		for (Iterator i = set.iterator(); i.hasNext();) {
			Object key = i.next();
			Object value = p.get(key);
			echo(key + " = " + value);
		}
	}
}
