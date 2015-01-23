package com.hj.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.os.Message;
import android.util.Log;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.hj.screen.NetScreen;
import com.hj.screen.RommScreen;
import com.hj.tool.Card;
import com.hj.tool.Comm;

public class Constant implements Disposable{

	public int Max=2;
	//single
	public static Constant constant=null;
	public boolean backTORommScreen=false;
	public NetScreen ns=null;
	public RommScreen rs=null;
	public Room currentRoom = null,r=null;
	public User me = null, u = null, computer = null;
	public static Constant getCons(){ if(constant==null) constant=new Constant(); return constant;}
	//data
	public List<Room> roomList = new ArrayList<Room>();;// �������������û�
	public List<User> users = new ArrayList<User>();;
	public List<CardData> cardDataList = new ArrayList<CardData>();
	public CardData cards[] = new CardData[56];
	//room
	public void createRoom() {
		// �����Լ���user
		me.setDeskId(users.size());
		users.add(me);
		addComputer();
		currentRoom = new Room(me.getIp(), 1);
		roomList.add(currentRoom);
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_CREATE_ROOM),
				me.getIp(),
				currentRoom.getIp(),currentRoom.getUserCount()+""});
		sendCMD(buffer, Proxys.getBroadCastIP());
		updateUserList();

	}
	public void addComputer() 
	{
		if (Max == 2) {
			computer = new User("����", "null");
			computer.setDeskId(2);
			computer.setIsReady(1);
			users.add(computer);
		}
	}
	public void orderComputer()
	{
		if (Max == 2) {
			User t1 = users.get(1), t2 = users.get(2);
			;
			users.remove(t1);
			users.remove(t2);
			users.add(t2);
			users.add(t1);
		}

	}
	public void joinRoom(Room r) 
	{
		currentRoom = r;
		me.setDeskId(r.userCount);
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_JOIN_ROOM),
				me.getIp(),
				me.getName(),me.getIp(),me.getDeskId()+"",me.getRoomId()+"",me.getIsReady()+"",me.getLandlordScore()+""});
		sendCMD(buffer, r.getIp());
	}
	public void requestRoom() 
	{
		roomList.clear();
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_REQUEST_ROOM),
				me.getIp(),
				me.getName(),me.getIp(),me.getDeskId()+"",me.getRoomId()+"",me.getIsReady()+"",me.getLandlordScore()+""});
		sendCMD(buffer, Proxys.getBroadCastIP());
	}
	public void replyRoom(User user) 
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_REPLY_ROOM),
				me.getIp(),
				currentRoom.getIp(),currentRoom.getUserCount()+""});
		sendCMD(buffer, user.getIp());
	}
	public void updateRoom() 
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_UPDATE_ROOM),
				me.getIp(),
				currentRoom.getIp(),currentRoom.getUserCount()+""});
		sendCMD(buffer, Proxys.getBroadCastIP());
	}
	public boolean isContainsRoom(Room r) {
		for (int i = 0; i < roomList.size(); i++) {
			if (roomList.get(i).getIp().equals(r.getIp()))
				return true;
		}
		return false;
	}
	public void updateRoomList() {
		sendMes1(Proxys.MES_UPDATE_ROOM, null);
	}
	//User
	public boolean isContainsUser(User u) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getIp().equals(u.getIp())) {
				return true;
			}
		}
		return false;
	}
	public void broadUser()
	{
		byte[] buffer=Proxys.getBufferUser(users);
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMDS(buffer, ip);
		
	}
	public void broadHideComputer()
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.MES_HIDE_COMPUTERCARDS),
				me.getIp()
			});
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMDS(buffer, ip);
	}
	public void updateUserList() {
		out("�����û���");
		sendMes2(Proxys.MES_UPDATE_User, null);
	}
	//Game
	public void getReady() 
	{
		me.setIsReady(1);
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_READY_ROOM),
				me.getIp(),
				me.getName(),me.getIp(),me.getDeskId()+"",me.getRoomId()+"",me.getIsReady()+"",me.getLandlordScore()+""});
		sendCMD(buffer, currentRoom.getIp());
	}
	public void beginGame() 
	{
		int count = 0;
		for (User user : users) {
			if (user.getIsReady()==1) {
				count++;
			}
		}
		if (count == 3) {
			 out("׼�����:��ʼ����");
			// ȡ��׼��ͼ��
			for (User user : users) {
				user.setIsReady(0);
			}
			broadUser();
			cardDataList.clear();
			int len = 54;
			for (int i = 1; i <= len; i++)
				cards[i] = new CardData(i, -1);
			// out("ϴ��");
			// ϴ��
			for (int i = 0; i < 100; i++) {
				Random random = new Random();
				int a = random.nextInt(54) + 1;
				int b = random.nextInt(54) + 1;
				CardData k = cards[a];
				cards[a] = cards[b];
				cards[b] = k;
			}
			// ����
			for (int i = 1; i <= 51; i++) {
				cardDataList.add(cards[i]);
				cards[i].setDeskId(i % 3);
			}
			// ������
			for (int i = 52; i <= 54; i++) {
				cardDataList.add(cards[i]);
				cards[i].setDeskId(3);
			}
			// ���Ʒָ�������
			byte[] buffer=Proxys.getBufferCard(cardDataList,Proxys.CMD_BEGIN_CARDS);
			String ip[]=new String[3];
			for (User user : users) {
				if (!user.getIp().equals("null"))
					ip[user.getDeskId()]=user.getIp();
			}
			sendCMDS(buffer, ip);
		} else
			broadUser();
	}
	public void finishLandlord() 
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_FINISH_LANDLORD),
				me.getIp(),
				me.getName(),me.getIp(),me.getDeskId()+"",me.getRoomId()+"",me.getIsReady()+"",me.getLandlordScore()+""});
		sendCMD(buffer, currentRoom.getIp());
	}
	public void sendHostCards() {
		// out("֪ͨHost������");
		cardDataList.clear();
		for (Card cd : ns.gInfo.playerOutList[me.getDeskId()]) 
			cardDataList.add(new CardData(cd.imageId, me.getDeskId()));
		// ���Ʒָ�������
		byte[] buffer=Proxys.getBufferCard(cardDataList,Proxys.CMD_HOST_SEND_CARDS);
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMD(buffer, currentRoom.getIp());
	}
	public void updateBeginCard() {
		
		for (CardData card : cardDataList) {
			Card cards = new Card(card.getImageId(), ns.atlsa);
			ns.gInfo.playerList[card.getDeskId()].add(cards);
			// netGameScreen.net.out(card.getDeskId()+":"+me.getDeskId());
			if (card.getDeskId() == me.getDeskId()) {
				cards.addListener(new ClickListener() {
					@Override
					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						Card c = (Card) event.getListenerActor();
						if(ns.gInfo.button[4].isVisible())
							c.move();
						c.touched=1;
						Log.v("test", "touch" + c.imageId);
						return super.touchDown(event, x, y, pointer, button);
					}

				});
			}
		}
		// ����
		sendMes2(Proxys.MES_UPDATE_BEGINCARD, null);
	}
	public void startLandlord() {
		if (me.getIp().equals(currentRoom.getIp())) {// ����ȷ��˭�ǵ���
			ns.gInfo.FirstLandLordId = 0;// ������������
			ns.gInfo.setCurrentPlayerId(ns.gInfo.FirstLandLordId);
			updateLandlord(1);
		}
	}
	public void updateLandlord(int visible) {
		sendMes2(Proxys.MES_UPDATE_LANDLORD, visible);
	}
	public boolean judgeFinishLandlord() {
		int count = 0;
		for (User user : users) {
			if (user.getLandlordScore() > -1)
				count++;
		}
		if (count == 3) {
			out("�������");
			int landlordId = Comm.getLandOwnerId(new int[] {
					users.get(0).getLandlordScore(),
					users.get(1).getLandlordScore(),
					users.get(2).getLandlordScore() }, 0);
			// out("����ID:"+landlordId);
			ns.gInfo.LandlordId = landlordId;
			for (User user : users) {
				user.setLandlordScore(-1);
				// out("����:"+user.getIp()+":ȡ��������ʾ");
			}
			broadUser();
			// ���������������
			cardDataList.clear();
			for (Card c : ns.gInfo.playerList[3]) {
				cardDataList.add(new CardData(c.imageId, landlordId));
			}
			// ���Ʒָ�������
			byte[] buffer=Proxys.getBufferCard(cardDataList,Proxys.CMD_BEGIN_LANDLORD_CARDS);
			String ip[]=new String[3];
			for (User user : users) {
				if (!user.getIp().equals("null"))
					ip[user.getDeskId()]=user.getIp();
			}
			sendCMDS(buffer, ip);
			// ֪ͨ��������
			startHostCard();
			return true;
		}
		return false;
	}
	public void startHostCard() {
		// out("����ID:"+users.get(netGameScreen.gInfo.LandlordId).getIp());
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_START_CARDS),
				me.getIp(),users.get(ns.gInfo.LandlordId)
				.getIp()				
				});
		sendCMD(buffer, currentRoom.getIp());
	}
	public void endHostCard()
	{
		ns.gInfo.setLandlordId(cardDataList.get(0).deskId);
		updateBeginCard();
		updateLandLordHead();
	}
	public void updateLandLordHead() {
		sendMes2(Proxys.MES_UPDATE_LANDLORDHEAD, null);
	}
	public void nextLandlord(int deskId) {
		// out("֪ͨ��һ��������:"+users.get((deskId+1)%3).getIp());
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_BROAD_NEXT_LANDLORD),
				me.getIp()				
				});
		sendCMD(buffer, users.get((deskId + 1) % 3).getIp());
	}
	public void nextPlayer(int deskId)
	{
		sendCMD(Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_START_CARDS),
				me.getIp(),users.get((deskId + 1) % 3).getIp()				
				}), currentRoom.getIp());
	}
	public void flush(List<Card> list) {
		sendMes2(Proxys.MES_FLUSH_CARDS, list);
	}
	public void updateCards(int desk) {
		// ����
		sendMes2(Proxys.MES_SEND_CARDS, desk);
	}
	public void setCurrentId(int id)
	{
		ns.gInfo.setCurrentPlayerId(id);
	}
	public void sendId(int desk)
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_SEND_CURRENTID_CARDS),
				me.getIp(),((desk + 1) % 3)+""		
				});
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMDS(buffer, ip);
	}
	public void sendCards()
	{
		byte[] b=Proxys.getBufferCard(cardDataList,Proxys.CMD_SEND_CARDS);
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMDS(b, ip); 
	}
	public void leaveRoom() {
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_HOST_LEAVE_ROOM),
				me.getIp()	
				});
		sendCMD(buffer, currentRoom.getIp());
		
	}
	public void broadleaveRoom()
	{
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_LEAVE_ROOM),
				me.getIp()	
				});
		String ip[]=new String[3];
		for (User user : users) {
			if (!user.getIp().equals("null"))
				ip[user.getDeskId()]=user.getIp();
		}
		sendCMDS(buffer, ip);
	}
	public void delRoom(Room r) {
		byte[] buffer=Proxys.getBuffer(new String[]{
				Integer.toString(Proxys.CMD_DEL_ROOM),
				me.getIp(),r.getIp(),r.getUserCount()+""	
				});
		sendCMD(buffer, Proxys.getBroadCastIP());
	}
	public void init()
	{
		users.clear();
		currentRoom = null;
		updateRoomList();
		backTORommScreen=true;
		
	}
	public void removeRoom(Room room) {

		Room t = null;
		for (Room r : roomList) {
			if (r.getIp().equals(room.getIp()))
				t = r;
		}
		roomList.remove(t);
	}
	public void showCardButton() {
		// out("��ʾ���ư�ť");
		sendMes2(Proxys.MES_SHOW_CARDBUTTON, null);
	}
	public void sendMes1(int cmd, Object o) {
		Message m = new Message();
		m.what = cmd;
		m.obj = o;
		rs.handler.sendMessage(m);

	}
	public void sendMes2(int cmd, Object o) {
		Message m = new Message();
		m.what = cmd;
		m.obj = o;
		if(ns!=null)
			ns.handler.sendMessage(m);
		else
			out("ns������");
	}
	public void sendCMD(final byte[] buffer,final String ip)
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					DatagramSocket ds = new DatagramSocket();
					DatagramPacket packet = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip),
							Proxys.PORT_RECEIVE + Max);  
			        //��������  
			        ds.send(packet);  
			        ds.close();
				}catch(Exception e){}
			}
		}).start();
	}
	public void sendCMDS(final byte[] buffer,final String ip[])
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try{
					DatagramSocket ds = new DatagramSocket();
					for(int i=0;i<3;i++)
					{
						if(ip[i]!=null)
						{
							DatagramPacket packet = new DatagramPacket(buffer,buffer.length,InetAddress.getByName(ip[i]),
									Proxys.PORT_RECEIVE + Max);  
							//��������  
					        ds.send(packet);  
						}
					}
			        ds.close();
				}catch(Exception e){}
			}
		}).start();
	}
	@Override
	public void dispose() {
		constant=null;
	}
	public static void out(String s) {
		Log.v("test", s);
	}
}
