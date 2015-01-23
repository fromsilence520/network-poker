package com.hj.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.Disposable;
import com.hj.tool.Card;
import com.hj.tool.Comm;

import android.util.Log;

public class Server extends Thread implements Disposable{

	Constant con=Constant.getCons();
	public boolean stop=false;
	public static Server s=null;
	public static Server getServer(){if(s==null) {s=new Server();s.start();}return s;}
	public void run() {
		// ��Ϣѭ��
	DatagramSocket server=null;
	 try {  
         //����UDP����  
         server = new DatagramSocket(Proxys.PORT_RECEIVE + con.Max);  
         //���建����  
         byte[] buffer=new byte[1024];  
         //����������ݰ�  
         DatagramPacket packet=new DatagramPacket(buffer,buffer.length);  
         while (!stop) {  
             //��������  
             server.receive(packet);  
             //�ж��Ƿ��յ����ݣ�Ȼ������ַ���  
             if(packet.getLength()>0){  
                 String str = new String(buffer,0,packet.getLength());  
                 //out(str);
                 parse(str.split(Proxys.div)); 
             }  
         }  
     } catch (SocketException e) {  
         e.printStackTrace();  
     } catch (IOException e) {  
         e.printStackTrace();  
     } finally{
    	 server.close();
     }
	}
	public void parse(String data[])
	{
		int cmd=Integer.parseInt(data[1]);
		Constant c= Constant.getCons();
		switch(cmd)
		{
			case Proxys.CMD_HOST_REQUEST_ROOM:// ��������
				c.u = new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				if (c.currentRoom!=null &&c.me.getIp().equals(c.currentRoom.getIp())) {
					out("���˽�����:"+c.u.getIp());
					c.replyRoom(c.u);
				}
				break;
			case Proxys.CMD_REPLY_ROOM: // �յ���������
			case Proxys.CMD_CREATE_ROOM: // �յ�����������
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (!c.isContainsRoom(c.r)) {
					c.roomList.add(c.r);
					c.updateRoomList();
				}
				break;
			case Proxys.CMD_HOST_JOIN_ROOM: // �յ������������
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("�������:"+c.u.getIp());
				if (!c.isContainsUser(c.u)) {
					c.users.add(c.u);
					c.currentRoom.userCount++;
					c.orderComputer();
					c.broadUser();
					c.updateRoom();
				}
				break;
			case Proxys.CMD_BROAD_USERLIST:// �յ��û��б����
				out("�û�����");
				int len=(data.length-4)/6;
				out("len="+len);
				c.users.clear();
				for(int i=0;i<len;i++)
				{
					c.users.add(new User(data[3+i*6],data[4+i*6],Integer.parseInt(data[5+i*6]),Integer.parseInt(data[6+i*6]),
							Integer.parseInt(data[7+i*6]),Integer.parseInt(data[8+i*6])));
				}
				c.updateUserList();
				break;
			case Proxys.CMD_HOST_READY_ROOM: // ׼��
				
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("׼��:"+c.u.getDeskId());
				for (User user : c.users) {
					if (user.getIp().equals(c.u.getIp()))
						user.setIsReady(1);
				}
				c.broadUser();
				// ��ʼ��Ϸ
				c.beginGame();
				break;
			case Proxys.CMD_UPDATE_ROOM: // �յ������б����
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (c.isContainsRoom(c.r)) {
					for (Room t : c.roomList) {
						if (t.getIp().equals(c.r.getIp()))
							t.setUserCount(c.r.getUserCount());
					}
				} else
					c.roomList.add(c.r);
				c.updateRoomList();
				break;
			case Proxys.CMD_BEGIN_CARDS:// �յ�������0
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				c.updateBeginCard();
				c.startLandlord();
				break;
			case Proxys.CMD_BEGIN_LANDLORD_CARDS: // �յ�������
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				c.endHostCard();
				break;
			case Proxys.CMD_HOST_FINISH_LANDLORD: // ���������ְ�ť
				c.u =new User(data[3],data[4],Integer.parseInt(data[5]),Integer.parseInt(data[6]),
						Integer.parseInt(data[7]),Integer.parseInt(data[8]));
				out("����:"+c.u.getDeskId());
				// �ж��������û��
				if (!con.judgeFinishLandlord())
					con.nextLandlord(c.u.getDeskId());
				break;
			case Proxys.CMD_BROAD_NEXT_LANDLORD: // ��֪ͨ��ʾ���ְ�ť
				c.updateLandlord(1);
				break;
			case Proxys.CMD_HOST_START_CARDS: // ˭�ǵ���
				byte[] buffer=Proxys.getBuffer(new String[]{
						Integer.toString(Proxys.CMD_START_CARDS),
						c.me.getIp()			
						});
				c.sendCMD(buffer, data[3]);
				break;
			case Proxys.CMD_START_CARDS: // ������ʼ����
				out("���ҳ����˳���:"+c.me.getDeskId());
				c.showCardButton();
				break;
			case Proxys.CMD_SEND_CURRENTID_CARDS: // �յ���ǰID
				out("currentId:"+data[3]);
				c.setCurrentId(Integer.parseInt(data[3]));
				break;
			case Proxys.CMD_SEND_CARDS: // �յ��㲥������
				int desk1 = (c.ns.gInfo.getCurrentPlayerId() + 2) % 3;
				out("desk1:"+desk1);
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				out("pai:"+c.cardDataList.size());
				c.updateCards(desk1);
				break;
			case Proxys.CMD_HOST_SEND_CARDS: // ���˳�����
				int desk = -1;
				for (User s : c.users) {
					if (s.getIp().equals(data[2]))
						desk = s.getDeskId();
				}
				out(desk + ":����");
				// sendID
				c.sendId(desk);
				c.cardDataList.clear();
				len=(data.length-4);
				for(int i=3;i<len+3;i++)
				{
					String s[]=data[i].split(":");
					c.cardDataList.add(new CardData(Integer.parseInt(s[1]), Integer.parseInt(s[0])));
				}
				// ���Ʒָ�������
				c.sendCards();
				// ��һ����
				if (!(c.Max == 2 && desk == 1))
					c.nextPlayer(desk);
				// Computer����
				if (c.Max == 2 && (desk == 1)) {
					// ���Գ���
					//��ֹ������������
					out("���Գ���");
					if (c.ns.gInfo.playerOutList[1] != null)
					{
						c.ns.old[1].addAll(c.ns.gInfo.playerOutList[1]);
						c.ns.gInfo.playerOutList[1].clear();
					}
					else
						c.ns.gInfo.playerOutList[1]=new ArrayList<Card>();
					for (Card c1 : c.ns.gInfo.playerList[1]) {
						for (CardData card : c.cardDataList) {
							if (card.imageId == c1.imageId) {
								c.ns.gInfo.playerOutList[1].add(c1);
							}
						}
					}
					//////////////////////////
					c.cardDataList.clear();
					c.setCurrentId(2);
					c.ns.gInfo.playerOutList[2] = Comm.getBestAI(
							c.ns.gInfo.playerList[2],
							c.ns.gInfo.getOppo());
					c.ns.gInfo.playerOutList[1].clear();
					if(c.ns.gInfo.getOppo()!=null)
						out("oppo:"+c.ns.gInfo.getOppo().get(0).value);
					else
						out("null");
					c.sendId(2);
					if (c.ns.gInfo.playerOutList[2] == null)
						out("����û�Ƴ�");
					else {
						out("���Գ�����:" + c.ns.gInfo.playerOutList[2].size());
						String s = "";
						for (Card cd : c.ns.gInfo.playerOutList[2]) {
							s += cd.value + ",";
							c.cardDataList.add(new CardData(cd.imageId, 2)); 
						}
						out("������:" + s);
					}
					out("���Գ���������");
					
					
					// ���Ʒָ�������
					c.sendCards();
					out("���Գ�������һ���˳�");
					// ��һ����
					c.nextPlayer(2);
				}
				break;
			case Proxys.CMD_HOST_LEAVE_ROOM: // �����뿪����
				c.broadleaveRoom();
				c.delRoom(c.currentRoom);
				break;
			case Proxys.CMD_LEAVE_ROOM:
				c.init();
				break;
			case Proxys.CMD_DEL_ROOM: // �յ�ɾ������
				c.r = new Room(data[3],Integer.parseInt(data[4]));
				if (c.isContainsRoom(c.r)) {
					c.removeRoom(c.r);
				}
				c.updateRoomList();
				break;
			
			
		}
		
	}
	public static void out(String s) {
		Log.v("test", s);
	}
	@Override
	public void dispose() {
		stop=true;
		s=null;
	}
}
