ModifyQcu.java: �޸�qcu�����ļ�Ӧ��
		���������HostIP Port OperationType LocalQueName Value
		˵����HostIP--TLQ�������˵�IP��ַ Port--��ش�������˿ں� OperationType--�������� LocalQueName--Ҫ�����ı���                       �������� Value-- ֻ��OperationType =4 ��ʱ������ã���Ҫ�޸ĵı��ض����е�MsgNum�ֶ���׼��������ֵ����		              ���������ֵ0��

		ע��  OperationType = 1 ����һ�����ض���
		      OperationType = 2 ɾ��һ�����ض���
		      OperationType = 3 ��ȡָ�����ض�����MsgNum��ֵ
		      OperationType = 4 �޸�ָ�����ض�����MsgNum��ֵ
                ����  java ModifyQcu 127.0.0.1 10252 1 lq8 0          ����һ�����ض���lq8


ModifySys.java: �޸�sys�����ļ�Ӧ��
		���������HostIP Port OperationType OperationName
		˵����HostIP--TLQ�������˵�IP��ַ Port--��ش�������˿ں� OperationType--�������� OperationName--׼��Ҫ����		              /ɾ���Ķ��п��Ƶ�Ԫ�����Ԫ�����ơ�		              

		ע:   OperationType = 1 ����һ�����п��Ƶ�Ԫqcu 
   		      OperationType = 2 ɾ��һ�����п��Ƶ�Ԫqcu
   		      OperationType = 3 ����һ������ԪProgram 
   		      OperationType = 4 ɾ��һ������ԪProgram
                ����  java ModifySys 127.0.0.1 10252 1 qcu2          ����һ�����п��Ƶ�Ԫqcu2
		      java ModifySys 127.0.0.1 10252 3 556           ����һ������Ԫ 556


StartStopTlq.java:��ͣTLQ��Ӧ��
		���������HostIP Port OperationType QCUName
		˵����HostIP--TLQ�������˵�IP��ַ Port--��ش�������˿ں� OperationType--�������� QCUName--׼��Ҫ��ͣ��TLQ��		              ���ơ�		              

		ע:   OperationType = 0 ֹͣTLQ
		      OperationType = 1 ����TLQ
		����  java StartStopTlq 127.0.0.1 10252 0 qcu1          ֹͣqcu1

Monitor.java:   ��ض�������Ϣ��Ӧ��
		���������HostIP Port QCUName LocalQueName
		˵����HostIP--TLQ�������˵�IP��ַ Port--��ش�������˿ں� QCUName--׼��Ҫ��ص�QCU���� LocalQueName--׼��Ҫ		        ��صĶ������ơ�
	
		ע:  �����������ǰҪ�ȱ�֤��׼����صĶ�����������һ����Ϣ              
		
		���� java Monitor 127.0.0.1 10252 qcu1 lq1       ���lq1��������Ϣ����Ϣ   
		     
