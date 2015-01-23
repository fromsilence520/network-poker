package com.hj.player;

import com.hj.tool.Card;
import com.hj.tool.GameInfo;

public class Human implements IPlayer{

	GameInfo gInfo;
	public Human(GameInfo ginfo)
	{
		this.gInfo=ginfo;
		
	}
	//����
	@Override
	public void sendCard() {
		//����֮ǰ���˵ĺ���
		//gInfo.showCancelLabel[0].setVisible(false);
		//gInfo.showCancelLabel[2].setVisible(false);
		//����֮ǰ������
		if(gInfo.playerOutList[1]!=null)
		{
			for(Card c:gInfo.playerOutList[1])
			{
				c.setVisible(false);
			}
			gInfo.playerOutList[1].clear();
		}
		gInfo.showCancelLabel[1].setVisible(false);
		//��ʾ��ť
		for(int i=4;i<8;i++)
		{
			gInfo.button[i].setVisible(true);
		}
	}

}
