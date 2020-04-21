import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class PubMsg {
    private String myQcuName;
    private String myTopic;
    private int myFlag;
    private TlqConnection tlqConnection = null;
    private TlqQCU tlqQcu = null;

    public PubMsg(String QcuName, String Topic, int CancelpubFlag) throws
            TlqException {

        myQcuName = QcuName;
        myTopic = Topic;
        myFlag = CancelpubFlag;
        tlqConnection = new TlqConnection();
        tlqQcu = tlqConnection.openQCU(myQcuName);

    }

    public static byte[] createBytes(int size) { //构造消息内容
        StringBuffer sb = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            sb.append('a');
        }
        return sb.toString().getBytes();
    }

    public void pubMsg() {
        try {

            TlqMessage msgInfo = new TlqMessage();
            TlqMsgOpt msgOpt = new TlqMsgOpt();
            msgInfo.MsgSize = 10; //消息大小
            byte[] msgContent = createBytes(msgInfo.MsgSize); //消息内容
            msgInfo.setMsgData(msgContent);

            msgInfo.Persistence = TlqMessage.TLQPER_Y; //持久性
            msgInfo.Priority = TlqMessage.TLQPRI_NORMAL; //优先级
            msgInfo.Expiry = -1; //生命周期  -1无限期
            msgOpt.OperateType = TlqMsgOpt.TLQOT_PUB;
            msgOpt.PubSubScope = TlqMsgOpt.TLQPSS_GLOBAL |
                                 TlqMsgOpt.TLQPSS_LOCAL; // 发布范围 全局发布
            msgOpt.QueName = "TLQ.SYS.BROKER.SYN";
            msgOpt.Topic = myTopic;

            if (myFlag == 0) { //发布
                tlqQcu.putMessage(msgInfo, msgOpt);
                System.out.println("---------------Pub Msg OK!!-----------");
            } else if (myFlag == 1) {
                msgOpt.OperateType = TlqMsgOpt.TLQOT_DELPUB;
            	  String CorrMsgId = "ID:11764565810000000000000000000";
                msgInfo.CorrMsgId = CorrMsgId.getBytes();
                tlqQcu.putMessage(msgInfo, msgOpt);
                System.out.println(
                        "---------------CancelPub Msg OK!!-----------");
            }

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
        String Topic;
        int CancelpubFlag;

        if (argv.length < 1) {
            System.out.println("--------------请输入参数！--------------\n");
            System.out
                    .println("PubMsg QcuName Topic CancelpubFlag");
            return;
        }
        if (argv.length != 3) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out
                    .println("PubMsg QcuName Topic CancelpubFlag");
        } else {
            QcuName = argv[0];
            Topic = argv[1];
            CancelpubFlag = Integer.parseInt(argv[2]);
            System.out.println(
                    "--------------------pub message begin------------------");
            PubMsg PM = new PubMsg(QcuName, Topic, CancelpubFlag);
            PM.pubMsg();
            System.out.println(
                    "--------------------pub message end------------------");

        }
    }

}
