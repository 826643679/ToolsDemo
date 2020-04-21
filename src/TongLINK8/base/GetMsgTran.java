package TongLINK8.base;

import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;

public class GetMsgTran {

    static int MyMsgCount = 0;
    private String myQcuName;
    private String myQueName;
    private int myWaitInterval;
    private TlqConnection tlqConnection = null;
    private TlqQCU tlqQcu = null;

    public GetMsgTran(String QcuName, String QueName, int WaitInterval) throws
            TlqException {
        myQcuName = QcuName;
        myQueName = QueName;
        myWaitInterval = WaitInterval;
        tlqConnection = new TlqConnection();
        tlqQcu = tlqConnection.openQCU(myQcuName);

    }

    static public void printMsgInfo(TlqMessage msgInfo) {
        if ((int) msgInfo.MsgType == 1) {
            System.out.println("Received a File Msg");
            System.out.print("msgInfo.MsgId=" + new String(msgInfo.MsgId));
            System.out.println("   msgInfo.MsgSize=" + (int) msgInfo.MsgSize);

        } else {
            System.out.println("Received a Buffer Msg");
            System.out.print("msgInfo.MsgId=" + new String(msgInfo.MsgId));
            System.out.println("   msgInfo.MsgSize=" + (int) msgInfo.MsgSize);
        }
    }

    public void recvMsgTran() {
        int msgCount = 0;
        try {

            tlqQcu.txBegin(); //事务开始
            while (true) {
                TlqMessage msgInfo = new TlqMessage();
                TlqMsgOpt msgOpt = new TlqMsgOpt();

                msgOpt.QueName = myQueName;
                msgOpt.WaitInterval = myWaitInterval;
                /*  msgOpt.MatchOption = TlqMsgOpt.TLQMATCH_PRIORITY; //条件接收
                  msgInfo.Priority = 5;*/

                msgOpt.OperateType = TlqMsgOpt.TLQOT_GET;
                tlqQcu.getMessage(msgInfo, msgOpt);
                msgCount = msgCount + 1;

                printMsgInfo(msgInfo);

            }

        } catch (TlqException e) {
            e.printStackTrace();

        } finally {
            MyMsgCount = msgCount;
            try {
                tlqQcu.txCommit(); //事务提交
                tlqQcu.close();
                tlqConnection.close();
            } catch (TlqException e) {
                e.printStackTrace();
            }
        }
        System.out.println("----------GetMsg is over!------------\n");
    }

    public static void main(String[] argv) throws Exception {
        String QcuName;
        String QueName;
        int WaitInterval = 0;
        argv=new String[] {"sendqcu","lq","0"};
        if (argv.length < 1) {
            System.out.println("--------------请输入参数！--------------\n");
            System.out
                    .println("GetMsgTran QcuName QueName WaitInterval");
            return;
        }
        if (argv.length != 3) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out
                    .println("GetMsgTran QcuName QueName WaitInterval");
        } else {
            QcuName = argv[0];
            QueName = argv[1];
            WaitInterval = Integer.parseInt(argv[2]);
            System.out.println(
                    "--------------------receive message begin------------------");
            GetMsgTran GMT = new GetMsgTran(QcuName, QueName, WaitInterval);
            GMT.recvMsgTran();

        }
        System.out.println("-------共接收消息" + MyMsgCount + "条-------");
    }
}
