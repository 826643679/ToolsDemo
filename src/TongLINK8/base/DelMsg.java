package TongLINK8.base;

import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class DelMsg {
    static int MyMsgCount = 0;
    private String myQcuName;
    private String myQueName;
    private TlqConnection tlqConnection = null;
    private TlqQCU tlqQcu = null;
    private int mynum = 0;

    public DelMsg(String QcuName, String QueName) throws
            TlqException {
        myQcuName = QcuName;
        myQueName = QueName;
        tlqConnection = new TlqConnection();
        tlqQcu = tlqConnection.openQCU(myQcuName);
    }

    public void delMsg() {

        try {

            TlqMessage msgInfo = new TlqMessage();
            TlqMsgOpt msgOpt = new TlqMsgOpt();

            msgOpt.QueName = myQueName;
            /* msgOpt.MatchOption = TlqMsgOpt.TLQMATCH_PRIORITY; //条件删除
             msgInfo.Priority = 5;*/
            msgOpt.DelState = TlqMsgOpt.TLQSTATE_ALL; //删除所有状态的消息

            int num = tlqQcu.delMessage(msgInfo, msgOpt);
            mynum = num;

        } catch (TlqException e) {
            e.printStackTrace();

        } finally {
            try {
                tlqQcu.close();
                tlqConnection.close();
            } catch (TlqException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-----------共删除 " + mynum + " 条消息---------");

    }

    public static void main(String[] argv) throws Exception {
        String QcuName;
        String QueName;

        if (argv.length < 1) {
            System.out.println("--------------请输入参数！--------------\n");
            System.out
                    .println("DelMsg QcuName QueName ");
            return;
        }
        if (argv.length != 2) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out
                    .println("DelMsg QcuName QueName ");
        } else {
            QcuName = argv[0];
            QueName = argv[1];
            System.out.println(
                    "--------------------delete message begin------------------");
            DelMsg DM = new DelMsg(QcuName, QueName);
            DM.delMsg();

        }
        System.out.println("----------DelMsg is over!------------");
    }
}
