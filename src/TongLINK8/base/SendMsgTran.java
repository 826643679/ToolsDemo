package TongLINK8.base;

import com.tongtech.tlq.base.*;

import java.util.*;
import java.io.*;


public class SendMsgTran {
    private String myQcuName;
    private String myQueName;
    private String myMsgType;
    private int myCount;
    private String myFileName;
    private TlqConnection tlqConnection = null;
    private TlqQCU tlqQcu = null;
    static int id = 0;


    public SendMsgTran(String QcuName, String QName, String MsgType,
                       String FileName) throws TlqException {
        myQcuName = QcuName;
        myQueName = QName;
        myMsgType = MsgType;
        myFileName = FileName;
        myCount = 2;

        tlqConnection = new TlqConnection();
        tlqQcu = tlqConnection.openQCU(myQcuName);

    }

    public static synchronized String createID() {
        StringBuffer sb = new StringBuffer();
        sb.append(System.currentTimeMillis());
        sb.append("_");
        sb.append(id);
        ++id;
        return sb.toString();
    }

    public static byte[] createBytes(int size) { //构造Buffer消息内容
        StringBuffer sb = new StringBuffer(size);
        for (int i = 0; i < size; i++) {
            sb.append('a');
        }
        return sb.toString().getBytes();
    }

    void sendBuffMsg(int size, TlqMessage msgInfo, TlqMsgOpt msgOpt) throws
            TlqException { //发送Buffer消息 size 消息大小
        msgInfo.MsgType = TlqMessage.BUF_MSG; //消息类型
        msgInfo.MsgSize = size; //消息大小
        byte[] msgContent = createBytes(msgInfo.MsgSize); //消息内容
        msgInfo.setMsgData(msgContent);

        msgInfo.Persistence = TlqMessage.TLQPER_Y; //持久性
        msgInfo.Priority = TlqMessage.TLQPRI_NORMAL; //优先级
        msgInfo.Expiry = 1000; //生命周期
        msgOpt.QueName = myQueName; //队列名

        tlqQcu.putMessage(msgInfo, msgOpt);
    }

    void sendFileMsg(String fName, TlqMessage msgInfo, TlqMsgOpt msgOpt) throws
            TlqException { //发送文件消息
        msgInfo.MsgType = TlqMessage.FILE_MSG;

        fName = myFileName + " " + createID() + "_" + myFileName;
        msgInfo.MsgSize = fName.getBytes().length;
        msgInfo.setMsgData(fName.getBytes());
        msgOpt.RemoveFileFlag = TlqMsgOpt.NOTREMOVEFILE; //是否删除源文件标志

        msgInfo.Persistence = TlqMessage.TLQPER_Y;
        msgInfo.Priority = TlqMessage.TLQPRI_NORMAL;
        msgInfo.Expiry = 1000;
        msgOpt.QueName = myQueName;

        tlqQcu.putMessage(msgInfo, msgOpt);
    }

    public void sendMsgTran() {
        System.out.println("myMsgType is :" + myMsgType);
        try {
            tlqQcu.txBegin(); //事务开始
            TlqMessage msgInfo = new TlqMessage();
            TlqMsgOpt msgOpt = new TlqMsgOpt();
            for (int i = 0; i < myCount; i++) {
                if (myMsgType.equals("B") == true) {
                    sendBuffMsg(10, msgInfo, msgOpt);
                } else {
                    sendFileMsg(myFileName, msgInfo, msgOpt);
                }

            }
            System.out.println("--------------共发送" + myCount +
                    "条消息!-----------");
            tlqQcu.txCommit(); //事务提交

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
        String QName;
        String FileName;
        String MsgType;
        argv=new String[] {"sendqcu","lq","B","no"};
        if (argv.length < 1) {
            System.out.println("---------请输入参数！---------");
            System.out.println(
                    "---------注：如果消息类型为B,则FileName输入为no！---------");
            System.out.println(
                    "SendMsgTran QcuName QueName MsgType(B/F) FileName");
            return;

        }
        if (argv.length != 4) {
            System.out.println("---------您输入的参数格式不对，请重新输入！---------");
            System.out.println(
                    "---------注：如果消息类型为B,则FileName输入为no！---------");
            System.out.println(
                    "SendMsgTran QcuName QueName MsgType(B/F) FileName");
            return;
        } else {
            QcuName = argv[0];
            QName = argv[1];
            MsgType = argv[2];
            FileName = argv[3];
        }
        SendMsgTran smt = new SendMsgTran(QcuName, QName, MsgType, FileName);
        smt.sendMsgTran();
        System.out.println("-----------sendmsgtran over!!-----------");
    }

}
