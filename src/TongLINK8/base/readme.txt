SendMsg.java :������ϢӦ��
		���������QcuName QueName MsgType(B/F) FileName GroupFlag(0/1) GroupCommitflag(0/1)
		˵����QcuName--���п��Ƶ�Ԫ���� QueName--�������� MsgType(B/F)--��Ϣ����(Buf��Ϣ����B file��������F) FileName--Ҫ�����ļ���(Buf��Ϣʱ ����no)
              GroupFlag--�Ƿ�Ϊ����Ϣ��1���ǣ�0���� GroupCommitflag--����Ϣ�Ƿ��ύ��1���ǣ�0����

GetMsg.java ��������ϢӦ��
		���������QcuName QueName WaitInterval
		˵����QcuName--���п��Ƶ�Ԫ����  QueName--���ض�������  WaitInterval--�ȴ�����ʱ��
		
DelMsg.java ��ɾ����ϢӦ��
		���������QcuName QueName
		˵����QcuName--���п��Ƶ�Ԫ����  QueName--��������

SendMsgTran.java ������ʽ������ϢӦ��
		���������QcuName QueName FileName
		˵����QcuName--���п��Ƶ�Ԫ���� QueName--�������� FileName--Ҫ�����ļ���
		
GetMsgTran.java ������ʽ������ϢӦ��
		���������QcuName QueName WaitInterval
		˵����QcuName--���п��Ƶ�Ԫ����  QueName--���ض�������  WaitInterval--�ȴ�����ʱ��

PubMsg.java ��������ϢӦ��
		���������QcuName topic cancelpubFlag(0/1)
		˵����QcuName--���п��Ƶ�Ԫ���� topic--���������� cancelpubFlag(0/1)--ȡ��������־(0 ��ȡ�� 1 ȡ��)
		
SubMsg.java ��������ϢӦ��
		���������QcuName topic cancelsubFlag(0/1)
		˵����QcuName--���п��Ƶ�Ԫ���� topic--���ĵ����� cancelsubFlag(0/1)--ȡ�����ı�־(0 ��ȡ�� 1 ȡ��)

GetQueList.java ����ȡĳ�����͵Ķ��и�������������Ӧ��
		���������QcuName QueType
		˵����QcuName--���п��Ƶ�Ԫ���� QueType--��������(0 Զ�̶��� 1 ��Ⱥ���� 2 ���Ͷ��� 3 ���ض���)
		
TestLine.java����ѯ��������״̬Ӧ��(����)
		���������QCUName ConnName
		˵����QcuName--���п��Ƶ�Ԫ���� ConnName ��������
		
TestLineRemote.java����ѯ��������״̬Ӧ��(Զ��)
		���������QCUName ConnName
		˵����QcuName--���п��Ƶ�Ԫ���� ConnName ��������