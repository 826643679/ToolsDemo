

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
		// ��ʾ2 - ʹ�ù������ȡMBean�ӿ�,�������װ��JMX��׼�ӿ�
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
//			4.��TLQ�������󹤳��л�ȡ����TLQ�ľ����������(TLQOptNodeSystem)
			TLQOptNodeSystem tlqSystem= tlqFac.getTLQOptNodeSystem();
			//5.��������TLQ�Ĳ��������ǲ����ڴ滹���ļ���Ĭ�����ڴ�
			tlqSystem.setResourceType(TLQOptBaseObj.OPER_RESOURCE_FILE);
			//6.�Ӳ�������(TLQOptNodeSystem)�л�ȡ�������ڵ��������Ϣ(NodeSystemInfo)
			NodeSystemInfo nodeInfo = tlqSystem.getNodeSystemInfo();
			NodeSystemInfo nodeInfoBak = tlqSystem.getNodeSystemInfo();
			System.out.println("NodeName="+nodeInfo.getNodeName().getValue());
			System.out.println("LogSize="+nodeInfo.getLogSize().valueToInt());
			//7.�����޸�������Ϣ(NodeSystemInfo)
			nodeInfo.setNodeName("TestNode");
			nodeInfo.setLogSize(102400);
			//8.�ж��ֶζ�̬�޸ĺ��Ƿ���������Ч
			if(nodeInfo.getNodeName().dyModify==TLQOptNodeSystem.DYNAMIC_FLAG_YES){
				System.out.println("���ֶ��޸ĺ��ܹ�������Ч");
			}else{
				System.out.println("���ֶ��޸ĺ󣬲���������Ч");
			}
			tlqSystem.setNodeSystemInfo(nodeInfo);
			
			tlqSystem.setNodeSystemInfo(nodeInfoBak);
			/*
			 * 9.������Ϣ(NodeSystemInfo)���浽Զ�̽ڵ㣬���������������޸��ڴ�����������Ч���ܷ�������Ч����(7.)�жϿ�֪��
			 * 9.1 ������ԴΪ�ļ�ʱ��ֻ���ļ������޸ġ�
			 * 9.2 ������ԴΪ�ڴ�ʱ����ʱ�����ǽڵ㴦�ڹ�����QCU���ڹ���״̬�����ܶ��ڴ���в�����
			 * ֻ����������Ч��������޸ĺ������Ч��
			 * ��֮�������Ƕ��ڴ���ļ������������������޸Ķ��ᱣ�浽�ļ��У��´νڵ��QCU������Ҳ����Ч��
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
