import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class GetQueList {
    private String myQcuName;
    private int myQueType;
    private TlqConnection tlqConnection = null;
    private TlqQCU tlqQcu = null;

    static int id = 0;

    public GetQueList(String QcuName, int queType) throws TlqException {
        myQcuName = QcuName;
        myQueType = queType;
        tlqConnection = new TlqConnection();
        tlqQcu = tlqConnection.openQCU(myQcuName);       
    }

    public static synchronized String createID(){
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append("_");
        sb.append(id);
        ++id;
        return sb.toString();
    }

    public void getQueList() {
    	int num,i;
        try {
						switch(myQueType){
        		case 0:
        			myQueType = tlqQcu.TLQ_REMOTE_QUE;
        			break;
        		case 1:
        			myQueType = tlqQcu.TLQ_CLUSTER_QUE;
        			break; 
        		case 2:
        			myQueType = tlqQcu.TLQ_SEND_QUE;
        			break; 
        		case 3:
        			myQueType = tlqQcu.TLQ_LOCAL_QUE;
        			break; 
      /*  		default:
        			System.out.println("输入quetype错误"); 
        			return ;
       	*/
       		 }     
           num = tlqQcu.getQueList(myQueType);
           if(num < 0)
           {
           	  return ;
           }
           System.out.println("quenum: " + tlqQcu.queNum);
           for(i=0; i<num; i++)
           {
           			System.out.print(tlqQcu.queNames[i]+" ");
           }
           System.out.println("");
        } catch (TlqException tlqEx) {
            tlqEx.printStackTrace();
        } finally {
            try {
                tlqQcu.close();
                tlqConnection.close();
            } catch (TlqException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] argv) throws Exception {
        String QcuName;
        int queType;

        if (argv.length != 2) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out.println(
                    "注：QueType 0:TLQ_REMOTE_QUE  1:TLQ_CLUSTER_QUE  2:TLQ_SEND_QUE  3:TLQ_LOCAL_QUE");
            System.out.println(
                    "GetQueList QcuName QueType");
            return;
        } else {
            QcuName = argv[0];
            queType = Integer.parseInt(argv[1]);
        }
        	
        GetQueList gql = new GetQueList(QcuName, queType);
        gql.getQueList();
    }
}
