SendMsg.java :发送消息应用
		输入参数：QcuName QueName MsgType(B/F) FileName GroupFlag(0/1) GroupCommitflag(0/1)
		说明：QcuName--队列控制单元名字 QueName--队列名字 MsgType(B/F)--消息类型(Buf消息输入B file类型输入F) FileName--要发送文件名(Buf消息时 输入no)
              GroupFlag--是否为组消息（1：是，0：否） GroupCommitflag--组消息是否提交（1：是，0：否）

GetMsg.java ：接收消息应用
		输入参数：QcuName QueName WaitInterval
		说明：QcuName--队列控制单元名字  QueName--本地队列名字  WaitInterval--等待接收时间
		
DelMsg.java ：删除消息应用
		输入参数：QcuName QueName
		说明：QcuName--队列控制单元名字  QueName--队列名字

SendMsgTran.java ：事务方式发送消息应用
		输入参数：QcuName QueName FileName
		说明：QcuName--队列控制单元名字 QueName--队列名字 FileName--要发送文件名
		
GetMsgTran.java ：事务方式接收消息应用
		输入参数：QcuName QueName WaitInterval
		说明：QcuName--队列控制单元名字  QueName--本地队列名字  WaitInterval--等待接收时间

PubMsg.java ：发布消息应用
		输入参数：QcuName topic cancelpubFlag(0/1)
		说明：QcuName--队列控制单元名字 topic--发布的主题 cancelpubFlag(0/1)--取消发布标志(0 不取消 1 取消)
		
SubMsg.java ：订阅消息应用
		输入参数：QcuName topic cancelsubFlag(0/1)
		说明：QcuName--队列控制单元名字 topic--订阅的主题 cancelsubFlag(0/1)--取消订阅标志(0 不取消 1 取消)

GetQueList.java ：获取某种类型的队列个数及队列名字应用
		输入参数：QcuName QueType
		说明：QcuName--队列控制单元名字 QueType--队列类型(0 远程队列 1 集群队列 2 发送队列 3 本地队列)
		
TestLine.java：查询发送连接状态应用(本地)
		输入参数：QCUName ConnName
		说明：QcuName--队列控制单元名字 ConnName 连接名称
		
TestLineRemote.java：查询发送连接状态应用(远程)
		输入参数：QCUName ConnName
		说明：QcuName--队列控制单元名字 ConnName 连接名称