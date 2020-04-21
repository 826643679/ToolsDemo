ModifyQcu.java: 修改qcu配置文件应用
		输入参数：HostIP Port OperationType LocalQueName Value
		说明：HostIP--TLQ服务器端的IP地址 Port--监控代理监听端口号 OperationType--操作类型 LocalQueName--要操作的本地                       队列名称 Value-- 只在OperationType =4 的时候才有用，对要修改的本地队列中的MsgNum字段所准备赋的数值，其		              它情况均赋值0。

		注：  OperationType = 1 增加一个本地队列
		      OperationType = 2 删除一个本地队列
		      OperationType = 3 读取指定本地队列中MsgNum的值
		      OperationType = 4 修改指定本地队列中MsgNum的值
                例：  java ModifyQcu 127.0.0.1 10252 1 lq8 0          增加一个本地队列lq8


ModifySys.java: 修改sys配置文件应用
		输入参数：HostIP Port OperationType OperationName
		说明：HostIP--TLQ服务器端的IP地址 Port--监控代理监听端口号 OperationType--操作类型 OperationName--准备要增加		              /删除的队列控制单元或程序单元的名称。		              

		注:   OperationType = 1 增加一个队列控制单元qcu 
   		      OperationType = 2 删除一个队列控制单元qcu
   		      OperationType = 3 增加一个程序单元Program 
   		      OperationType = 4 删除一个程序单元Program
                例：  java ModifySys 127.0.0.1 10252 1 qcu2          增加一个队列控制单元qcu2
		      java ModifySys 127.0.0.1 10252 3 556           增加一个程序单元 556


StartStopTlq.java:启停TLQ的应用
		输入参数：HostIP Port OperationType QCUName
		说明：HostIP--TLQ服务器端的IP地址 Port--监控代理监听端口号 OperationType--操作类型 QCUName--准备要启停的TLQ的		              名称。		              

		注:   OperationType = 0 停止TLQ
		      OperationType = 1 启动TLQ
		例：  java StartStopTlq 127.0.0.1 10252 0 qcu1          停止qcu1

Monitor.java:   监控队列中信息的应用
		输入参数：HostIP Port QCUName LocalQueName
		说明：HostIP--TLQ服务器端的IP地址 Port--监控代理监听端口号 QCUName--准备要监控的QCU名称 LocalQueName--准备要		        监控的队列名称。
	
		注:  运行这个用例前要先保证所准备监控的队列中至少有一条消息              
		
		例： java Monitor 127.0.0.1 10252 qcu1 lq1       监控lq1队列中消息的信息   
		     
