import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class SubMsg {

    private String myQcuName;
private String myTopic;
private int myFlag;
private TlqConnection tlqConnection = null;
private TlqQCU tlqQcu = null;

    public SubMsg(String QcuName, String Topic, int CancelsubFlag) throws TlqException{

        myQcuName = QcuName;
myTopic = Topic;
myFlag = CancelsubFlag;
tlqConnection = new TlqConnection();
tlqQcu = tlqConnection.openQCU(myQcuName);

    }

    public void subMsg() {
       try {

           TlqMessage msgInfo = new TlqMessage();
           TlqMsgOpt msgOpt = new TlqMsgOpt();
           msgInfo.MsgSize = 10; //消息大小


           msgInfo.Persistence = TlqMessage.TLQPER_Y; //持久性
           msgInfo.Priority = TlqMessage.TLQPRI_NORMAL; //优先级
           msgInfo.Expiry = -1; //生命周期  -1无限期
           msgOpt.OperateType = TlqMsgOpt.TLQOT_SUB;
           msgOpt.PubSubScope = TlqMsgOpt.TLQPSS_GLOBAL |
                                TlqMsgOpt.TLQPSS_LOCAL; // 订阅范围 全局发布

           msgOpt.Topic = myTopic;

           if (myFlag == 0) { //订阅
               tlqQcu.putMessage(msgInfo, msgOpt);
               System.out.println("---------------Sub Msg OK!!-----------");
           } else if (myFlag == 1) {
               msgOpt.OperateType = TlqMsgOpt.TLQOT_DELSUB;
           	 String CorrMsgId = "ID:11764565810000000000000000000";
               msgInfo.CorrMsgId = CorrMsgId.getBytes();
               tlqQcu.putMessage(msgInfo, msgOpt);
               System.out.println(
                       "---------------CancelSub Msg OK!!-----------");
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
          int CancelsubFlag;

          if (argv.length < 1) {
              System.out.println("--------------请输入参数！--------------\n");
              System.out
                      .println("SubMsg QcuName Topic CancelsubFlag");
              return;
          }
          if (argv.length != 3) {
              System.out.println("---------您输入的参数格式不对，请重新输入！---------");
              System.out
                      .println("SubMsg QcuName Topic CancelsubFlag");
          } else {
              QcuName = argv[0];
              Topic = argv[1];
              CancelsubFlag = Integer.parseInt(argv[2]);
              System.out.println(
                      "--------------------sub message begin------------------");
              SubMsg SM = new SubMsg(QcuName, Topic, CancelsubFlag);
              SM.subMsg();
              System.out.println(
                      "--------------------sub message end------------------");

          }
    }
}
