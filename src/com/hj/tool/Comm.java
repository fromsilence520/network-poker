package com.hj.tool;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.hj.net.Constant;
import com.hj.singlejoker.ScoreManager;
import com.hj.singlejoker.SoundManager;

import android.app.Activity;
import android.util.Log;

public class Comm {
	/*
	 * QQ:361106306 by:С�� ת�ش˳����뱣����Ȩ,δ������������������ҵ��;!
	 */
	public static int oppoerFlag;//������id
	public static GameInfo gInfo=null;
	public static int tipOffset=0;
	// ������-������ص�һ���зֵ�ID
	public static int getFirstScoreId() {
		Random random = new Random();
		int a = random.nextInt(3);
		return a;
	}
	// ��������
	public static int getScore(List<Card> list)
	{
		if(Constant.getCons().Max==2)
			return 0;
		if(list.get(0).color!=5)
			return 1;
		else if(list.get(1).color==5)
			return 3;
		else
			return 2;
	}
	// ѡ��˭�ǵ���
	public static int getLandOwnerId(int s[], int firstId) {
		for (int i = 3; i >= 0; i--) {
			for (int j = firstId; j < firstId + 3; j++) {
				Comm.HjLog("j%3=" + j % 3);
				if (s[j % 3] == i)
				{
					ScoreManager.baseScore=i;
					gInfo.bscores.setText(i+"");
					return j % 3;
				}
			}
		}
		return firstId;
	}

	// �趨�Ƶ�˳��
	public static void setOrder(List<Card> list) {
		Collections.sort(list, new Comparator<Card>() {
			@Override
			public int compare(Card o1, Card o2) {

				return o2.value - o1.value;
			}
		});
	}

	// �趨����˳��
	public static void setOutPosition(List<Card> list, int id) {
		SoundManager.PlaySound(list);
		float offset = 25, start = 0;
		//ScoreManager
		CardType ct=jugdeType(list);
		if(ct==CardType.c4||ct==CardType.c111222||ct==CardType.c1112223344||ct==CardType.c11122234)
		{
			ScoreManager.baseTimes*=2;
			gInfo.btimes.setText(ScoreManager.baseTimes+"");
		}
		if (id == 1) {
			start = 400 - list.size() * offset / 2;
			 Card card1=list.get(0);
			 card1.setVisible(true);
			 card1.addAction(Actions.moveTo(start + 0 * offset, 150, 1f));
			for (int i = 1, len = list.size(); i < len; i++) {
				Card card = list.get(i);
				card.setVisible(true);
			    card.setZIndex(list.get(i-1).getZIndex()+1);
				card.addAction(Actions.moveTo(start + i * offset, 150, 1f));
			}
		}
		if (id == 0) {
			start = 190 - list.size() * offset / 2;
			 Card card1=list.get(0);
			 card1.setScale(0.7f);
			 card1.setFront();
			 card1.setVisible(true);
			 card1.addAction(Actions.moveTo(start + 0 * offset, 330, 1f));
			for (int i = 1, len = list.size(); i < len; i++) {
				Card card = list.get(i);
				card.setScale(0.7f);
				card.setVisible(true);
				card.setFront();
			    card.setZIndex(list.get(i-1).getZIndex()+1);
				card.addAction(Actions.moveTo(start + i * offset, 330, 1f));
			}
		} 
		if (id == 2) {
			start = 590 - list.size() * offset / 2;
			 Card card1=list.get(0);
			 card1.setVisible(true);
			 card1.setScale(0.7f);
			card1.setFront();
			 card1.addAction(Actions.moveTo(start + 0 * offset, 330, 1f));
			for (int i = 1, len = list.size(); i < len; i++) {
				Card card = list.get(i);
				card.setScale(0.7f);
				card.setFront();
				card.setVisible(true);
			    card.setZIndex(list.get(i-1).getZIndex()+1);
				card.addAction(Actions.moveTo(start + i * offset, 330, 1f));
			}
		}
	}

	// �趨˳��������趨λ��
	public static void rePosition(List<Card> list,int id) {

		float offset = 39,start=0,top=0;
		//cards[i].addAction(Actions.moveTo(740,310,1f));2
		//cards[i].addAction(Actions.moveTo(2,310, 1f));
		if (list.size() > 17)
			offset = 33;
		if(id==1)
		{
			start = 400 - list.size() * offset / 2;
			top=20;
		}
		if(id==0)
		{
			start=2;
			top=303;
			offset=0;
		}
		if(id==2)
		{
			start=740;
			offset=0;
			top=303;
		}
		if(list.size()>0)
		{
			Card card1 = list.get(0);
			card1.addAction(Actions.moveTo(start + 0 * offset, top, 1f));
			for (int i = 1, len = list.size(); i < len; i++) {
				Card card = list.get(i);
				card.setZIndex(list.get(i - 1).getZIndex() + 1);
				card.addAction(Actions.moveTo(start + i * offset, top, 1f));
			}
		}
		

	}

	// �����õ�
	public static void HjLog(String s) {
		Log.v("test", s);
	}

	// �ж�����
	public static CardType jugdeType(List<Card> list) {
		// ��Ϊ֮ǰ��������ԱȽϺ��ж�
		if(list==null)
			return CardType.c0;
		int len = list.size();
		if(len<1)
			return CardType.c0;
		// ˫��,��Ϊը������
		if (len == 2 && list.get(1).color == 5)
			return CardType.c4;
		// ����,���ӣ�3������4��һ��ը��
		if (len <= 4) { // �����һ����������ͬ��˵��ȫ����ͬ
			if (list.size() > 0 && list.get(0).value == list.get(len - 1).value) {
				switch (len) {
				case 1:
					return CardType.c1;
				case 2:
					return CardType.c2;
				case 3:
					return CardType.c3;
				case 4:
					return CardType.c4;
				}
			}
			// ����һ����������ͬʱ,3��1
			if (len == 4 && ((list.get(0).value) == list.get(len - 2).value
					|| list.get(1).value == list.get(len - 1).value))
				return CardType.c31;
			else {
				return CardType.c0;
			}
		}
		// ��5������ʱ�����֣�3��2���ɻ���2˳��4��2�ȵ�
		if (len >= 5) {// ���ڰ���ͬ���������ִ���
			int count[] = new int[4];
			// ����������ֳ���Ƶ��
			Comm.getMax(count, list); // a[0,1,2,3]�ֱ��ʾ�ظ�1,2,3,4�ε���
			// 3��2 -----�غ��ظ�3�ε���
			if (count[2] == 1 && count[1] == 1 && len == 5)
				return CardType.c32;
			// 4��2(��,˫)
			if (count[3] == 1 && len == 6)
				return CardType.c411;
			if (count[3] == 1 && count[1] == 2 && len == 8)
				return CardType.c422;
			// ����,��֤��������
			if ((list.get(0).color != 5) && (count[0] == len)
					&& (list.get(0).value - list.get(len - 1).value == len - 1))
				return CardType.c123;
			// ����
			if (count[1] == len / 2
					&& len % 2 == 0
					&& len / 2 >= 3
					&& (list.get(0).value - list.get(len - 1).value == (len / 2 - 1)))
				return CardType.c1122;
			// �ɻ�
			if (count[2] == len / 3
					&& (len % 3 == 0)
					&& (list.get(0).value - list.get(len - 1).value == (len / 3 - 1)))
				return CardType.c111222;
			// �ɻ���n��,n/2��
			if (count[2] >= 2 && count[2] == len / 4 && len == count[2] * 4)
				return CardType.c11122234;

			// �ɻ���n˫
			if (count[2] >= 2 && count[1] == len / 5 && len == count[2] * 5)
				return CardType.c1112223344;

		}
		return CardType.c0;
	}

	// �õ������ͬ��
	public static void getMax(int a[], List<Card> list) {
		int count[] = new int[18];// 0-15����һ��,�����16��
		for (int i = 0; i < 18; i++)
			count[i] = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			count[list.get(i).value]++;
		}
		for (int i = 0; i < 18; i++) {
			switch (count[i]) {
			case 1:
				a[0]++;
				break;
			case 2:
				a[1]++;
				break;
			case 3:
				a[2]++;
				break;
			case 4:
				a[3]++;
				break;
			}
		}
	}

	// ����Ƶ��Ƿ��ܳ�
	public static int checkCards(List<Card> c, List<Card> oppo) {
		// �ҳ���ǰ���������ĸ����Գ���,c�ǵ�ѡ����
		if(oppo==null)
			return 1;
		List<Card> currentlist = oppo;
		CardType cType = jugdeType(c);
		CardType cType2 = jugdeType(currentlist);
		// ���������ֱͬ�ӹ���
		if (cType != CardType.c4 && c.size() != currentlist.size())
		{
			HjLog("������ͬ:"+c.size()+":"+currentlist.size());
			return 0;
		}
		// �Ƚ��ҵĳ�������
		if (cType != CardType.c4 && cType != cType2) {
			HjLog("���Ͳ�ͬ��ͬ");
			return 0;
		}
		// �Ƚϳ������Ƿ�Ҫ��
		// ����ը��
		if (cType == CardType.c4) {
			if (c.size() == 2)
				return 1;
			if (cType2 != CardType.c4) {
				return 1;
			}
		}

		// ����,����,3��,4ը��
		if (cType == CardType.c1 || cType == CardType.c2
				|| cType == CardType.c3 || cType == CardType.c4) {
			if (c.get(0).value <= currentlist.get(0).value) {
				HjLog(c.get(0).value+"<"+currentlist.get(0).value);
				return 0;
			} else {
				return 1;
			}
		}
		// ˳��,���ӣ��ɻ���
		if (cType == CardType.c123 || cType == CardType.c1122
				|| cType == CardType.c111222) {
			if (c.get(0).value <= currentlist.get(0).value)
				return 0;
			else
				return 1;
		}
		// ���ظ���������
		// 3��1,3��2 ,�ɻ�������˫,4��1,2,ֻ��Ƚϵ�һ�����У���һ�޶���
		if (cType == CardType.c31 || cType == CardType.c32
				|| cType == CardType.c411 || cType == CardType.c422
				|| cType == CardType.c11122234 || cType == CardType.c1112223344) {
			List<Card> a1 = getOrder2(c); // �ҳ�����
			List<Card> a2 = getOrder2(currentlist);// ��ǰ�����
			if (a1.get(0).value < a2.get(0).value)
				return 0;
		}
		return 1;
	}

	// �����ظ���������
	public static List getOrder2(List<Card> list) {
		List<Card> list2 = new ArrayList<Card>(list);
		List<Card> list3 = new ArrayList<Card>();
		List<Integer> list4 = new ArrayList<Integer>();
		int len = list2.size();
		int a[] = new int[20];
		for (int i = 0; i < 20; i++)
			a[i] = 0;
		for (int i = 0; i < len; i++) {
			a[list2.get(i).value]++;
		}
		int max = 0;
		for (int i = 0; i < 20; i++) {
			max = 0;
			for (int j = 19; j >= 0; j--) {
				if (a[j] > a[max])
					max = j;
			}

			for (int k = 0; k < len; k++) {
				if (list2.get(k).value == max) {
					list3.add(list2.get(k));
				}
			}
			list2.remove(list3);
			a[max] = 0;
		}
		return list3;
	}
	// �õ�Tips
	public static List<Card> getTipsCard(List<Card> curList, List<Card> oppo)
	{
		List<Card> oppo2=getOrder2(oppo);
		List<Card> list = new ArrayList<Card>(curList),tipList=new ArrayList<Card>();
		CardType ct=jugdeType(oppo);
		Model tipsModel=new Model();
		int tempOffset=0;
		boolean flag=true;
		switch(ct)
		{
			case c1:getOne(list, tipsModel);
				
				for(int i=tipsModel.a1.size()-1;i>=0;i--)
				{
					if(getValue(oppo.get(0))<getValue(tipsModel.a1.get(i)))
					{
						if(i-tipOffset<0)
						{
							tipOffset=0;
						}
						tipList.addAll(getCardsByName(list, tipsModel.a1.get(i-tipOffset)));
						tipOffset++;
						break;
					}
				}
				break;
			case c2: getTwo(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a2.size()-1;i>=0;i--)
					{
						if(getValue(oppo.get(0))<getValue(tipsModel.a2.get(i)))
						{
							flag=true;
							if(tempOffset-->0) continue;
							tipList.addAll(getCardsByName(list, tipsModel.a2.get(i)));
							tipOffset++;
							return tipList;
						}
					}
				}
				
				break;
			case c31:getThree(list, tipsModel);getOne(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a3.size()-1;i>=0;i--)
					{
						for(int j=tipsModel.a1.size()-1;j>=0;j--)
						{
							if(getValue(tipsModel.a3.get(i))!=getValue(tipsModel.a1.get(j))
									&&
									getValue(oppo2.get(0))<getValue(tipsModel.a3.get(i)))
							{
								flag=true;
								if(tempOffset-->0) continue;
								tipList.addAll(getCardsByName(list, tipsModel.a3.get(i)));
								tipList.addAll(getCardsByName(list, tipsModel.a1.get(j)));
								tipOffset++;
								return tipList;
							}
						}
					}
				}
				break;
			case c32:getThree(list, tipsModel);getTwo(list, tipsModel);
			
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a3.size()-1;i>=0;i--)
					{
						for(int j=tipsModel.a2.size()-1;j>=0;j--)
						{
							if(getValue(tipsModel.a3.get(i))!=getValue(tipsModel.a2.get(j))
									&&
									getValue(oppo2.get(0))<getValue(tipsModel.a3.get(i)))
							{
								flag=true;
								if(tempOffset-->0) continue;
								tipList.addAll(getCardsByName(list, tipsModel.a3.get(i)));
								tipList.addAll(getCardsByName(list, tipsModel.a2.get(j)));
								tipOffset++;
								return tipList;
							}
						}
					}
				}
				break;
			case c3: getThree(list, tipsModel);
				for(int i=tipsModel.a3.size()-1;i>=0;i--)
				{
					if(getValue(oppo.get(0))<getValue(tipsModel.a3.get(i)))
					{
						if(i-tipOffset<0)
						{
							tipOffset=0;
						}
						tipList.addAll(getCardsByName(list, tipsModel.a3.get(i-tipOffset)));
						tipOffset++;
						break;
					}
				}	
				break;
			case c411:getBoomb(list, tipsModel);getOne(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a4.size()-1;i>=0;i--)
					{
						for(int j=tipsModel.a1.size()-1;j-1>=0;j-=2)
						{
							if(getValue(tipsModel.a4.get(i))!=getValue(tipsModel.a1.get(j))
									&&
								getValue(tipsModel.a4.get(i))!=getValue(tipsModel.a1.get(j-1))
									&&
									getValue(oppo2.get(0))<getValue(tipsModel.a4.get(i)))
							{
								flag=true;
								if(tempOffset-->0) continue;
								tipList.addAll(getCardsByName(list, tipsModel.a4.get(i)));
								tipList.addAll(getCardsByName(list, tipsModel.a1.get(j)));
								tipList.addAll(getCardsByName(list, tipsModel.a1.get(j-1)));
								tipOffset++;
								return tipList;
							}
						}
					}
				}
				break;
			case c422:getBoomb(list, tipsModel);getTwo(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a4.size()-1;i>=0;i--)
					{
						for(int j=tipsModel.a2.size()-1;j-1>=0;j-=2)
						{
							if(getValue(tipsModel.a4.get(i))!=getValue(tipsModel.a2.get(j))
									&&
								getValue(tipsModel.a4.get(i))!=getValue(tipsModel.a2.get(j-1))
									&&
									getValue(oppo2.get(0))<getValue(tipsModel.a4.get(i)))
							{
								flag=true;
								if(tempOffset-->0) continue;
								tipList.addAll(getCardsByName(list, tipsModel.a4.get(i)));
								tipList.addAll(getCardsByName(list, tipsModel.a2.get(j)));
								tipList.addAll(getCardsByName(list, tipsModel.a2.get(j-1)));
								tipOffset++;
								return tipList;
							}
						}
					}
				}
				break;
			
			case c4: getBoomb(list, tipsModel);
				for(int i=tipsModel.a4.size()-1;i>=0;i--)
				{
					if(getValue(oppo.get(0))<getValue(tipsModel.a4.get(i)))
					{
						if(i-tipOffset<0)
						{
							tipOffset=0;
						}
						tipList.addAll(getCardsByName(list, tipsModel.a4.get(i-tipOffset)));
						tipOffset++;
						break;
					}
				}
				break;
			case c123: get123(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a123.size()-1;i>=0;i--)
					{
						String[] s = tipsModel.a123.get(i).split(",");
						if(s.length==oppo.size()&&
								getValue(oppo.get(0))<getValue(tipsModel.a123.get(i))
								)
						{
							flag=true;
							if(tempOffset-->0) continue;
							tipList.addAll(getCardsByName(list, tipsModel.a123.get(i)));
							tipOffset++;
							return tipList;
						}
					}
				}
				break;
			case c1122: getTwoTwo(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a112233.size()-1;i>=0;i--)
					{
						String[] s = tipsModel.a112233.get(i).split(",");
						if(s.length==oppo.size()&&
								getValue(oppo.get(0))<getValue(tipsModel.a112233.get(i))
								)
						{
							flag=true;
							if(tempOffset-->0) continue;
							tipList.addAll(getCardsByName(list, tipsModel.a112233.get(i)));
							tipOffset++;
							return tipList;
						}
					}
				}
				break;
			case c11122234:getPlane(list, tipsModel);getOne(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a111222.size()-1;i>=0;i--)
					{
						String[] s = tipsModel.a111222.get(i).split(",");
						if(s.length==oppo.size())
						{
							for(int j=tipsModel.a1.size()-1;j-s.length/3-1>=0;j-=s.length/3)
							{
								boolean l=true;
								for(int k=j;k>=j-s.length/3-1;k--)
								{
									if(     !(getValue(tipsModel.a111222.get(i))<getValue(tipsModel.a1.get(k))
											||
											getValue(tipsModel.a111222.get(i))-(s.length/3-1)>getValue(tipsModel.a1.get(k))
											)
										)
									l=false;
								}
								if(l&&getValue(oppo2.get(0))<getValue(tipsModel.a111222.get(i)))
								{
									flag=true;
									if(tempOffset-->0) continue;
									tipList.addAll(getCardsByName(list, tipsModel.a111222.get(i)));
									for(int k=j;k>=j-s.length/3-1;k--)
										tipList.addAll(getCardsByName(list, tipsModel.a1.get(k)));
									tipOffset++;
									return tipList;
								}
							}
						}
					}
				}
				break;
			case c1112223344:getPlane(list, tipsModel);getTwo(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a111222.size()-1;i>=0;i--)
					{
						String[] s = tipsModel.a111222.get(i).split(",");
						if(s.length==oppo.size())
						{
							for(int j=tipsModel.a2.size()-1;j-s.length/3-1>=0;j-=s.length/3)
							{
								boolean l=true;
								for(int k=j;k>=j-s.length/3-1;k--)
								{
									if(     !(getValue(tipsModel.a111222.get(i))<getValue(tipsModel.a2.get(k))
											||
											getValue(tipsModel.a111222.get(i))-(s.length/3-1)>getValue(tipsModel.a2.get(k))
											)
										)
									l=false;
								}
								if(l&&getValue(oppo2.get(0))<getValue(tipsModel.a111222.get(i)))
								{
									flag=true;
									if(tempOffset-->0) continue;
									tipList.addAll(getCardsByName(list, tipsModel.a111222.get(i)));
									for(int k=j;k>=j-s.length/3-1;k--)
										tipList.addAll(getCardsByName(list, tipsModel.a2.get(k)));
									tipOffset++;
									return tipList;
								}
							}
						}
					}
				}
				break;
			case c111222: getPlane(list, tipsModel);
				tempOffset=tipOffset;
				while(flag)
				{
					flag=false;
					for(int i=tipsModel.a111222.size()-1;i>=0;i--)
					{
						String[] s = tipsModel.a111222.get(i).split(",");
						if(s.length==oppo.size()&&
								getValue(oppo.get(0))<getValue(tipsModel.a111222.get(i))
								)
						{
							flag=true;
							if(tempOffset-->0) continue;
							tipList.addAll(getCardsByName(list, tipsModel.a111222.get(i)));
							tipOffset++;
							return tipList;
						}
					}
				}
				break;
				
		}
		if(tipList.size()>0)
			return tipList;
		else if(ct!=CardType.c4)
		{
			getBoomb(list, tipsModel);
			for(int i=tipsModel.a4.size()-1;i>=0;i--)
			{
				if(i-tipOffset<0)
					{
						tipOffset=0;
					}
					tipList.addAll(getCardsByName(list, tipsModel.a4.get(i-tipOffset)));
					tipOffset++;
					break;
			}
			if(tipList.size()>0)
				return tipList;
		}
		return null;
	}
	// �õ��������ѡ��
	public static List<Card> getBestAI(List<Card> curList, List<Card> oppo) {
		List<Card> list = new ArrayList<Card>(curList);
		Model model = new Model();
		Model modelSingle = new Model();

		// �ҳ����ж���,3����ը�����ɻ���˫˳����˳
		getTwo(list, model);
		getThree(list, model);
		get123(list, model);
		getBoomb(list, model);
		getTwoTwo(list, model);
		getPlane(list, model);
		getSingle(list, model);
		// ȥ��model�����������
		checkModel(list, model, modelSingle);
		// ���ڷֱ����ÿ�ֿ����Ե�Ȩֵ,��������ȡ�����Ǹ�(ע����Щ��������صģ��������Ͳ����������)
		// �������һ������ǰҪ�ж��������͵��ƻ��Ƿ����
		// �ȱȽ������ٱȽ�Ȩֵ
		Model bestModel = null, myModel = null;
		int value = 0;
		int time = 99;
		for (int i = 0, len1 = model.a4.size(); i <= len1; i++)
			for (int j = 0, len2 = model.a3.size(); j <= len2; j++)
				for (int k = 0, len3 = model.a2.size(); k <= len3; k++)
					for (int l = 0, len4 = model.a123.size(); l <= len4; l++)
						for (int m = 0, len5 = model.a112233.size(); m <= len5; m++)
							for (int n = 0, len6 = model.a111222.size(); n <= len6; n++) {
								List<Card> newlist = new ArrayList<Card>(list);
								// �ҳ������ѭ���е㳤..�������CPU
								bestModel = getBestModel(newlist, model,
										new int[] { i, j, k, l, m, n });
								// ���϶�������
								bestModel.a1.addAll(modelSingle.a1);
								bestModel.a2.addAll(modelSingle.a2);
								bestModel.a3.addAll(modelSingle.a3);
								bestModel.a4.addAll(modelSingle.a4);
								bestModel.a123.addAll(modelSingle.a123);
								bestModel.a112233.addAll(modelSingle.a112233);
								bestModel.a111222.addAll(modelSingle.a111222);
								// ���ϵ���
								for (Card singleCard : newlist) {
									bestModel.a1.add(Integer.toString(singleCard.imageId));
								}
								// ��������������Ȩֵ
								if (getTimes(bestModel) < time) {
									time = getTimes(bestModel);
									myModel = bestModel;
								} else if (getTimes(bestModel) == time
										&& getCountValues(bestModel) > value) {
									value = getCountValues(bestModel);
									myModel = bestModel;
								}

							}
		// ��ʼ����
		List<Card> showCardslList = new ArrayList<Card>();
		if (oppo == null) {
			HjLog(gInfo.CurrentPlayerId+":oppo==null");
			// ��������
			showCards(myModel, showCardslList, curList);
		} else {
			HjLog(gInfo.CurrentPlayerId+":oppo==yes");
			// ��������
			showCards2(myModel, showCardslList, curList, oppo);
		}

		if (showCardslList == null || showCardslList.size() == 0)
		{
			HjLog(gInfo.CurrentPlayerId+":û�г���");
			return null;
		}
		return showCardslList;
	}
	// ��
	public static void getOne(List<Card> list, Model model) {
		for (int i = 0, len = list.size(); i < len; i++) {
			model.a1.add(list.get(i).imageId + "");
		}
	}

	// �����
	public static void getTwo(List<Card> list, Model model) {
		// ����2����ͬ
		for (int i = 0, len = list.size(); i < len; i++) {
			if (i + 1 < len
					&& getValue(list.get(i)) == getValue(list.get(i + 1))) {
				String s = list.get(i).imageId + ",";
				s += list.get(i + 1).imageId;
				model.a2.add(s);
				i = i + 1;
			}
		}
	}

	// ��3��
	public static void getThree(List<Card> list, Model model) {
		// ����3����ͬ
		for (int i = 0, len = list.size(); i < len; i++) {
			if (i + 2 < len
					&& getValue(list.get(i)) == getValue(list.get(i + 2))) {
				String s = list.get(i).imageId + ",";
				s += list.get(i + 1).imageId + ",";
				s += list.get(i + 2).imageId;
				model.a3.add(s);
				i = i + 2;
			}
		}
	}

	// ��ը��
	public static void getBoomb(List<Card> list, Model model) {
		if (list.size() < 1)
			return;
		// ��ը
		if (list.size() >= 2 && getColor(list.get(0)) == 5
				&& getColor(list.get(1)) == 5) {
			model.a4.add(list.get(0).imageId + "," + list.get(1).imageId); // �����ּ���
		}
		// һ���ը��
		for (int i = 0, len = list.size(); i < len; i++) {
			if (i + 3 < len
					&& getValue(list.get(i)) == getValue(list.get(i + 3))) {
				String s = list.get(i).imageId + ",";
				s += list.get(i + 1).imageId + ",";
				s += list.get(i + 2).imageId + ",";
				s += list.get(i + 3).imageId;
				model.a4.add(s);
				i = i + 3;
			}
		}
	}

	// ��˫˳
	public static void getTwoTwo(List<Card> list, Model model) {
		// ��model����Ķ�����
		List<String> l = model.a2;
		if (l.size() < 3)
			return;
		Integer s[] = new Integer[l.size()];
		for (int i = 0, len = l.size(); i < len; i++) {
			String[] name = l.get(i).split(",");
			s[i] = getValue(name[0]);
		}
		// s0,1,2,3,4 13,9,8,7,6
		for (int i = 0, len = l.size(); i < len; i++) {
			int k = i;
			for (int j = i; j < len; j++) {
				if (s[i] - s[j] == j - i)
					k = j;
			}
			if (k - i >= 2)// k=4 i=1
			{// ˵����i��k������
				String ss = "";
				for (int j = i; j < k; j++) {
					ss += l.get(j) + ",";
					// del.add(l.get(j));
				}
				ss += l.get(k);
				model.a112233.add(ss);
				// del.add(l.get(k));
				i = k;
			}
		}
		// l.removeAll(del);
	}

	// ��ɻ�
	public static void getPlane(List<Card> list, Model model) {
		// List<String> del = new ArrayList<String>();// Ҫɾ����Cards
		// ��model�����3����
		List<String> l = model.a3;
		if (l.size() < 2)
			return;
		Integer s[] = new Integer[l.size()];
		for (int i = 0, len = l.size(); i < len; i++) {
			String[] name = l.get(i).split(",");
			s[i] = getValue(name[0]);
		}
		for (int i = 0, len = l.size(); i < len; i++) {
			int k = i;
			for (int j = i; j < len; j++) {
				if (s[i] - s[j] == j - i)
					k = j;
			}
			if (k != i) {// ˵����i��k�Ƿɻ�
				String ss = "";
				for (int j = i; j < k; j++) {
					ss += l.get(j) + ",";
					// del.add(l.get(j));
				}
				ss += l.get(k);
				model.a111222.add(ss);
				// del.add(l.get(k));
				i = k;
			}
		}
		// l.removeAll(del);
	}

	// ������
	public static void get123(List<Card> list, Model model) {
		// List<Card> del = new ArrayList<Card>();// Ҫɾ����Cards
		if (list.size() < 5)
			return;
		// ��Ҫ�����в��ظ����ƹ�Ϊһ�࣬��ֹ3��������Ӱ��
		List<Card> list2 = new ArrayList<Card>(list);
		List<Card> temp = new ArrayList<Card>();
		List<Integer> integers = new ArrayList<Integer>();
		for (Card card : list2) {
			if (integers.indexOf(getValue(card)) < 0 && getColor(card) != 5 && getValue(card)<15){
				integers.add(getValue(card));
				temp.add(card);
			}
		}
		setOrder(temp);
		for (int i = 0, len = temp.size(); i < len; i++) {
			int k = i;
			for (int j = i; j < len; j++) {
				if (getValue(temp.get(i)) - getValue(temp.get(j)) == j - i) {
					k = j;
				}
			}
			if (k - i >= 4) {
				String s = "";
				for (int j = i; j < k; j++) {
					s += temp.get(j).imageId + ",";
					// del.add(temp.get(j));
				}
				s += temp.get(k).imageId;
				// del.add(temp.get(k));
				model.a123.add(s);
				i = k;
			}
		}
		// list.removeAll(del);
	}

	// ����
	public static void getSingle(List<Card> list, Model model) {
		// List<Card> del = new ArrayList<Card>();// Ҫɾ����Cards
		// 1
		for (int i = 0, len = list.size(); i < len; i++) {
			model.a1.add(list.get(i).imageId + "");
			// del.add(list.get(i));
		}
		delSingle(model.a2, model);
		delSingle(model.a3, model);
		delSingle(model.a4, model);
		delSingle(model.a123, model);
		delSingle(model.a112233, model);
		delSingle(model.a111222, model);
		// list.removeAll(del);
	}

	// ȡ��
	public static void delSingle(List<String> list, Model model) {
		for (int i = 0, len = list.size(); i < len; i++) {
			String s[] = list.get(i).split(",");
			for (int j = 0; j < s.length; j++)
				model.a1.remove(s[j]);
		}
	}

	// ͳ�Ƹ�������Ȩֵ������
	public static Model getBestModel(List<Card> list2, Model oldModel, int[] n) {
		// a4 a3 a2 a123 a112233 a111222
		Model temp = new Model();
		// ����ը��
		for (int i = 0; i < n[0]; i++) {
			if (isExists(list2, oldModel.a4.get(i))) {
				temp.a4.add(oldModel.a4.get(i));
				list2.removeAll(getCardsByName(list2, oldModel.a4.get(i)));
			}
		}
		// 3��
		for (int i = 0; i < n[1]; i++) {
			if (isExists(list2, oldModel.a3.get(i))) {
				temp.a3.add(oldModel.a3.get(i));
				list2.removeAll(getCardsByName(list2, oldModel.a3.get(i)));
			}
		}
		// ����
		for (int i = 0; i < n[2]; i++) {
			if (isExists(list2, oldModel.a2.get(i))) {
				temp.a2.add(oldModel.a2.get(i));
				list2.removeAll(getCardsByName(list2, oldModel.a2.get(i)));
			}
		}
		// ˳��
		for (int i = 0; i < n[3]; i++) {
			if (isExists(list2, oldModel.a123.get(i))) {
				temp.a123.add(oldModel.a123.get(i));
				list2.removeAll(getCardsByName(list2,
						oldModel.a123.get(i)));
			}
		}
		// ˫˳
		for (int i = 0; i < n[4]; i++) {
			if (isExists(list2, oldModel.a112233.get(i))) {
				temp.a112233.add(oldModel.a112233.get(i));
				list2.removeAll(getCardsByName(list2,
						oldModel.a112233.get(i)));
			}
		}
		// �ɻ�
		for (int i = 0; i < n[5]; i++) {
			if (isExists(list2, oldModel.a111222.get(i))) {
				temp.a111222.add(oldModel.a111222.get(i));
				list2.removeAll(getCardsByName(list2,
						oldModel.a111222.get(i)));
			}
		}
		return temp;
	}

	// ȥ����������
	public static void checkModel(List<Card> list, Model model1,
			Model modelSingle) {

		List<String> group[] = new List[6];
		group[0] = model1.a2;
		group[1] = model1.a3;
		group[2] = model1.a4;
		group[3] = model1.a111222;
		group[4] = model1.a112233;
		group[5] = model1.a123;
		List<String> groupS[] = new List[6];
		groupS[0] = modelSingle.a2;
		groupS[1] = modelSingle.a3;
		groupS[2] = modelSingle.a4;
		groupS[3] = modelSingle.a111222;
		groupS[4] = modelSingle.a112233;
		groupS[5] = modelSingle.a123;
		// �ҳ�����������ص�����
		for (int g = 0; g < 6; g++) {
			for (int i = 0, len = group[g].size(); i < len; i++) {
				int flag = 0;
				// Log.i("mylog","..."+ model1.a2.get(i));
				String s[] = group[g].get(i).split(",");
				for (int k = 0; k < 6; k++) {
					if (k != g) {
						flag += checkModel_1(group[k], s);
					}
				}
				if (flag == 0) {
					groupS[g].add(group[g].get(i));
					list.removeAll(getCardsByName(list, group[g].get(i)));
				}
			}
			group[g].removeAll(groupS[g]);
		}
	}

	public static int checkModel_1(List<String> list, String[] s) {
		for (int j = 0, len2 = list.size(); j < len2; j++) {
			String ss[] = list.get(j).split(",");
			for (int k = 0; k < ss.length; k++) {
				for (int m = 0; m < s.length; m++) {
					if (s[m].equals(ss[k])) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	// ��������
	public static int getTimes(Model model) {
		int count = 0;
		count += model.a4.size() + model.a3.size() + model.a2.size();
		count += model.a111222.size() + model.a112233.size()
				+ model.a123.size();
		int temp = 0;
		temp = model.a1.size() - model.a3.size() * 2 - model.a4.size() * 3
				- model.a111222.size() * 3;
		count += temp;
		return count;
	}

	// ����Ȩֵ ��1 ����2 ��3 ը��10 �ɻ�7 ˫˳5 ˳��4
	public static int getCountValues(Model model) {
		int count = 0;
		count += model.a1.size() + model.a2.size() * 2 + model.a3.size() * 3;
		count += model.a4.size() * 10 + model.a111222.size() * 7
				+ model.a112233.size() * 5 + model.a123.size() * 4;
		return count;
	}

	// ��������
	public static void showCards2(Model model, List<Card> to, List<Card> from,
			List<Card> oppo) {
		// oppo�Ƕ��ֳ�����,from���Լ����е���,to��ҩ�߳�����
		List<String> list = new ArrayList<String>();// װҪ�߳����Ƶ�name
		CardType cType = jugdeType(oppo);
		// ���ظ�������,����ֻ��Ƚϵ�һ����
		oppo = getOrder2(oppo);
		switch (cType) {
		case c1:
			// ������ѳ��ƱȽϴ��ҾͲ��ӣ�����Ͳ�ע����
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 13))
				break;
			for (int len = model.a1.size(), i = len - 1; i >= 0; i--) {
				if ((!isLessFive())
						&& Comm.getValue(model.a1.get(i)) >= 16)
					break;
				if (Comm.getValue(model.a1.get(i)) > Comm
						.getValue(oppo.get(0))) {
					list.add(model.a1.get(i));
					break;
				}
			}
			if (Comm.isLessFive()) {
				for (int i = 0, leni = from.size(); i < leni; i++) {
					if (Comm.getValue(from.get(i)) > Comm.getValue(oppo
							.get(0))) {
						to.add(from.get(i));
						break;
					}
				}
				return;
			}
			break;
		case c2:
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 12))
				break;
			for (int len = model.a2.size(), i = len - 1; i >= 0; i--) {
				if ((!isLessFive())
						&& Comm.getValue(model.a2.get(i)) >= 15)
					break;
				if (Comm.getValue(model.a2.get(i)) > Comm
						.getValue(oppo.get(0))) {
					list.add(model.a2.get(i));
					break;
				}
			}
			if (Comm.isLessFive() && model.a3.size() > 0) {
				// ��3����
				for (int len = model.a3.size(), i = len - 1; i >= 0; i--) {

					if (Comm.getValue(model.a3.get(i)) > Comm
							.getValue(oppo.get(0))) {
						List<Card> t = Comm.getCardsByName(from,
								model.a3.get(i));
						to.add(t.get(0));
						to.add(t.get(1));
						return;
					}
				}
			}
			break;
		case c3:
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 13))
				break;
			for (int len = model.a3.size(), i = len - 1; i >= 0; i--) {
				if ((!isLessFive())
						&& Comm.getValue(model.a3.get(i)) >= 15)
					break;
				if (Comm.getValue(model.a3.get(i)) > Comm
						.getValue(oppo.get(0))) {
					list.add(model.a3.get(i));
					break;
				}
			}
			break;
		case c31:
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 9))
				break;
			int len1 = model.a3.size();
			int len2 = model.a1.size();
			if (!(len1 < 1 || len2 < 1)) {
				for (int len = len1, i = len - 1; i >= 0; i--) {
					if ((!isLessFive())
							&& Comm.getValue(model.a3.get(i)) >= 15)
						break;
					if (Comm.getValue(model.a3.get(i)) > Comm
							.getValue(oppo.get(0))) {
						list.add(model.a3.get(i));
						break;
					}
				}
				if (list.size() > 0)
					list.add(model.a1.get(len2 - 1));
			}
			break;
		case c32:
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 9))
				break;
			len1 = model.a3.size();
			len2 = model.a2.size();
			if (!(len1 < 1 || len2 < 1)) {
				for (int len = len1, i = len - 1; i >= 0; i--) {
					if ((!isLessFive())
							&& Comm.getValue(model.a3.get(i)) >= 15)
						break;
					if (Comm.getValue(model.a3.get(i)) > Comm
							.getValue(oppo.get(0))) {
						list.add(model.a3.get(i));
						break;
					}
				}
				if (list.size() > 0)
					list.add(model.a2.get(len2 - 1));
			}
			break;
		case c411:
			if (Comm.isFriend())
				break;
			len1 = model.a4.size();
			len2 = model.a1.size();
			if (!(len1 < 1 || len2 < 2)) {
				for (int len = len1, i = len - 1; i >= 0; i--) {
					if (Comm.getValue(model.a4.get(i)) > Comm
							.getValue(oppo.get(0))) {
						list.add(model.a4.get(i));
						break;
					}
				}
				if (list.size() > 0) {
					list.add(model.a1.get(len2 - 1));
					list.add(model.a1.get(len2 - 2));
				}
			}
			break;
		case c422:
			if (Comm.isFriend())
				break;
			len1 = model.a4.size();
			len2 = model.a2.size();
			if (!(len1 < 1 || len2 < 2)) {
				for (int len = len1, i = len - 1; i >= 0; i--) {
					if (Comm.getValue(model.a4.get(i)) > Comm
							.getValue(oppo.get(0))) {
						list.add(model.a4.get(i));
						break;
					}
				}
				if (list.size() > 0) {
					list.add(model.a2.get(len2 - 1));
					list.add(model.a2.get(len2 - 2));
				}
			}
			break;
		case c123:
			if (Comm.isFriend() && (Comm.getValue(oppo.get(0)) >= 9))
				break;
			for (int len = model.a123.size(), i = len - 1; i >= 0; i--) {
				String[] s = model.a123.get(i).split(",");
				if (s.length == oppo.size()
						&& Comm.getValue(model.a123.get(i)) > Comm
								.getValue(oppo.get(0))) {
					list.add(model.a123.get(i));
					break;
				}
			}
			break;
		case c1122:
			if (Comm.isFriend())
				break;
			for (int len = model.a112233.size(), i = len - 1; i >= 0; i--) {
				String[] s = model.a112233.get(i).split(",");
				if (s.length == oppo.size()
						&& Comm.getValue(model.a112233.get(i)) > Comm
								.getValue(oppo.get(0))) {
					list.add(model.a112233.get(i));
					break;
				}
			}
			break;
		case c11122234:
			if (Comm.isFriend())
				break;
			len1 = model.a111222.size();
			len2 = model.a1.size();

			if (!(len1 < 1 || len2 < 2)) {
				for (int i = len1 - 1; i >= 0; i--) {
					String[] s = model.a111222.get(i).split(",");
					if ((s.length / 3 <= len2)
							&& (s.length * 4 == oppo.size())
							&& Comm.getValue(model.a111222.get(i)) > Comm
									.getValue(oppo.get(0))) {
						list.add(model.a111222.get(i));
						for (int j = 1; j <= s.length / 3; j++)
							list.add(model.a1.get(len2 - j));
					}
				}
			}

			break;
		case c1112223344:
			if (Comm.isFriend())
				break;
			len1 = model.a111222.size();
			len2 = model.a2.size();

			if (!(len1 < 1 || len2 < 2)) {
				for (int i = len1 - 1; i >= 0; i--) {
					String[] s = model.a111222.get(i).split(",");
					if ((s.length / 3 <= len2)
							&& (s.length * 4 == oppo.size())
							&& Comm.getValue(model.a111222.get(i)) > Comm
									.getValue(oppo.get(0))) {
						list.add(model.a111222.get(i));
						for (int j = 1; j <= s.length / 3; j++)
							list.add(model.a2.get(len2 - j));
					}
				}
			}
			break;
		case c4:
			for (int len = model.a4.size(), i = len - 1; i >= 0; i--) {
				if (Comm.getValue(model.a4.get(i)) > Comm
						.getValue(oppo.get(0))) {
					list.add(model.a4.get(i));
					break;
				}
			}
		default:
			break;
		}
		if (list.size() == 0) {
			if (Comm.isLessFive())// �����ͬ���ƳԲ��𣬶�������5�ţ�����ը��
			{
				if (model.a4.size() > 0 &&(cType!=CardType.c4)) {
					list.add(model.a4.get(model.a4.size() - 1));
					for (String s : list) {
						to.addAll(Comm.getCardsByName(from, s));
					}
					return;
				}
			}
			to = null;
		} else {
			for (String s : list) {
				to.addAll(Comm.getCardsByName(from, s));
			}
		}
	}

	// ��������
	public static void showCards(Model model, List<Card> to, List<Card> from) {

		List<String> list = new ArrayList<String>();
		if (model.a123.size() > 0) {
			list.add(model.a123.get(model.a123.size() - 1));
		}
		// �е����� (����3�����ɻ��ܴ��ĵ���)
		else if ((!isSingleOpper())
				&& model.a1.size() > (model.a111222.size() * 2 + model.a3
						.size())
				&& Comm.getValue(model.a1.get(model.a1.size() - 1)) < 15) {
			list.add(model.a1.get(model.a1.size() - 1));
		} else if (Comm.isSingleOpper()
				&& model.a1.size() > (model.a111222.size() * 2 + model.a3
						.size())) {
			list.add(model.a1.get(0));
		}
		// �ж��ӳ����� (����3�����ɻ�)
		else if (model.a2.size() > (model.a111222.size() * 2 + model.a3.size())
				&& Comm.getValue(model.a2.get(model.a2.size() - 1)) < 15) {
			list.add(model.a2.get(model.a2.size() - 1));
		}
		// ��3���ͳ�3����û�оͳ���3
		else if (model.a3.size() > 0
				&& Comm.getValue(model.a3.get(model.a3.size() - 1)) < 15) {
			// 3����,�ҷǹؼ�ʱ�̲��ܴ�����2
			if (model.a1.size() > 0) {
				list.add(model.a1.get(model.a1.size() - 1));
			}// 3����
			else if (model.a2.size() > 0) {
				list.add(model.a2.get(model.a2.size() - 1));
			}
			list.add(model.a3.get(model.a3.size() - 1));
		}// ��˫˳��˫˳
		else if (model.a112233.size() > 0) {
			list.add(model.a112233.get(model.a112233.size() - 1));
		}// �зɻ����ɻ�
		else if (model.a111222.size() > 0) {
			String name[] = model.a111222.get(0).split(",");
			// ����
			if (name.length / 3 <= model.a1.size()) {
				list.add(model.a111222.get(model.a111222.size() - 1));
				for (int i = 0; i < name.length / 3; i++)
					list.add(model.a1.get(i));
			} else if (name.length / 3 <= model.a2.size())// ��˫
			{
				list.add(model.a111222.get(model.a111222.size() - 1));
				for (int i = 0; i < name.length / 3; i++)
					list.add(model.a2.get(i));
			}

		} else if ((!Comm.isSingleOpper())
				&& model.a1.size() > (model.a111222.size() * 2 + model.a3
						.size())) {
			list.add(model.a1.get(model.a1.size() - 1));
		} else if (model.a2.size() > (model.a111222.size() * 2 + model.a3
				.size())) {
			list.add(model.a2.get(model.a2.size() - 1));
		} else if (Comm.getValue(model.a3.get(0)) < 15
				&& model.a3.size() > 0) {
			// 3����,�ҷǹؼ�ʱ�̲��ܴ�����2
			if (model.a1.size() > 0) {
				list.add(model.a1.get(model.a1.size() - 1));
			}// 3����
			else if (model.a2.size() > 0) {
				list.add(model.a2.get(model.a2.size() - 1));
			}
			list.add(model.a3.get(model.a3.size() - 1));
		}
		// ��ը����ը��
		else if (model.a4.size() > 0) {
			// 4��2,1
			int sizea1 = model.a1.size();
			int sizea2 = model.a2.size();
			if (sizea1 >= 2) {
				list.add(model.a1.get(sizea1 - 1));
				list.add(model.a1.get(sizea1 - 2));
				list.add(model.a4.get(0));

			} else if (sizea2 >= 2) {
				list.add(model.a2.get(sizea1 - 1));
				list.add(model.a2.get(sizea1 - 2));
				list.add(model.a4.get(0));

			} else {// ֱ��ը
				list.add(model.a4.get(0));

			}
		}
		if(list.size()==0)
		{
			if(model.a1.size()>0)
				list.add(model.a1.get(model.a1.size() - 1));
		}
		for (String s : list) {
			to.addAll(Comm.getCardsByName(from, s));
		}

	}

	// ����ֵ
	public static int getValue(Card card) {
		return card.value;
	}

	// ����ֵ
	public static int getValue(String s) {
		String[] name = s.split(",");
		int imageId = Integer.parseInt(name[0]), value;
		value = (imageId - 1) % 13 + 1;
		if (value <= 2)
			value += 13;
		if (imageId == 53 || imageId == 54) {
			value = 16+imageId-53;
		}
		return value;
	}

	// ���ػ�ɫ
	public static int getColor(Card card) {
		return card.color;
	}

	// ͨ��name�õ�card
	public static List<Card> getCardsByName(List<Card> list, String s) {
		String[] name = s.split(",");
		List<Card> temp = new ArrayList<Card>();
		int c = 0;
		for (int i = 0, len = list.size(); i < len; i++) {
			if (Integer.toString(list.get(i).imageId).equals(name[c])) {
				temp.add(list.get(i));
				if (c == name.length - 1)
					return temp;
				c++;
				i = 0;
			}
		}
		return temp;
	}
	//�ж�ĳ���ͻ�����list��
	public static Boolean isExists(List<Card> list,String s){
		String name[]=s.split(",");
		int c=0;
		for(int i=0,len=list.size();i<len;i++){
			if(Integer.toString(list.get(i).imageId).equals(name[c]))
			{
				if(c==name.length-1)
					return true;
				c++;
				i=0;
			}	
		}
		return false;
	}
	//�ж��Լ��ǲ��ǵ���
	public static Boolean isDizhu(){
		if(gInfo.CurrentPlayerId==gInfo.LandlordId)
			return true;
		else {
			return false;
		}
	}
	//�ж϶������ǲ����Լ��Ķ���
	public static Boolean isFriend(){
		if(isDizhu())
			return false;
		else if(oppoerFlag!=gInfo.LandlordId)
		{
			return true;
		}
		else {
			return false;
		}
	}
	//������˻����Լ�ֻ��5����һ�£���������
	public static Boolean isLessFive(){
		if((!isFriend())&&((gInfo.playerList[oppoerFlag].size()<=5)||(gInfo.playerList[gInfo.CurrentPlayerId].size()<5)))
		{
			return true;
		}
		return false;
	}
	//�������ֻ��һ���ƣ�����������������Ҳ�����ĳ�
	public static Boolean isSingleOpper(){
		if(!isFriend()&&gInfo.playerList[oppoerFlag].size()==1)
			return true;
		else
			return false;
	}
}
