

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import com.tongtech.tlq.admin.remote.jmx.TLQConnector;
import com.tongtech.tlq.admin.remote.jmx.mbean.TLQJMXConnectMBean;
import com.tongtech.tlq.admin.remote.jmx.mbean.TLQJMXLicenseMBean;
import com.tongtech.tlq.admin.remote.jmx.qcu.TLQMatchOption;

public class TestJMXStand {
	public static void main(String[] args) {
		// 演示1 - 使用JMX标准接口查找获取MBean接口
		try {
			// 使用工厂类获取JMX MBeanServerConnection
			JMXServiceURL url = new JMXServiceURL(
					"service:jmx:rmi://localhost/jndi/rmi://localhost:12099/myconnector");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();
		
			TLQJMXConnectMBean tlqConnectMBean = (TLQJMXConnectMBean)MBeanServerInvocationHandler
					.newProxyInstance(mbsc, new ObjectName(
							"bean:name=TLQJMXConnect"),
							TLQJMXConnectMBean.class, false);
			TLQConnector connAttr = new TLQConnector("127.0.0.1",
					10252);
			connAttr.setRecvDataTmOut(300);
			TLQConnector serverConnect = tlqConnectMBean.connect(connAttr);

			TLQJMXLicenseMBean license = (TLQJMXLicenseMBean)MBeanServerInvocationHandler
					.newProxyInstance(mbsc, new ObjectName(
							"bean:name=TLQJMXLicense"),
							TLQJMXLicenseMBean.class, false);
			Properties p = license.getLicense(serverConnect);
			echoProperties(p);
			/*
			MBeanInfo mbeanInfo=mbsc.getMBeanInfo(new ObjectName("bean:name=TLQJMXQCU"));
			MBeanOperationInfo[] opers=mbeanInfo.getOperations();
			for(int i=0;i<opers.length;i++){
				echo(""+opers[i].getReturnType()+" "+opers[i].getName()+" ");
				MBeanParameterInfo[] infos=opers[i].getSignature();
				for(int j=0;j<infos.length;j++){
					echo(""+infos[j].getType());
				}
				echo("============");
			}
			*/
			ObjectName objName =new ObjectName("bean:name=TLQJMXQCU");
			HashMap map =new HashMap();
			map.put("TLQMATCH_STATUS", TLQMatchOption.TLQSTATE_READY);
			Object[] params=new Object[]{serverConnect,"qcu1","lq",map};
			String[] signature=new String[]{serverConnect.getClass().getName(),String.class.getName(),String.class.getName(),Map.class.getName()};
			mbsc.invoke(objName, "getQueMsgNum", params, signature);
			//mbsc.queryMBeans(new ObjectName("bean:name=TLQJMXQCU"), null);
			// 关闭连接
			tlqConnectMBean.close(serverConnect);
			echo("=======================");
			jmxc.close();
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
}
