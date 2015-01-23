package com.hj.tool;

import java.util.List;
import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import com.hj.singlejoker.AD;
import com.hj.singlejoker.ScoreManager;
import com.hj.singlejoker.SoundManager;
 

public class GameInfo {
	public int Score[]=new int[3];//�з�
	public int LandlordId=-1; //������־
	public int FirstLandLordId=-1;//��һ������������
	Card LandLordCard[]=new Card[3];//������
	int GameState=0;//δ��ʼ  1:���ֿ�ʼ  2��
	public int CurrentPlayerId=-1;//��ǰ������
	public List<Card> playerList[]=new ArrayList[4];//�ƶ���
	public List<Card> playerOutList[]=new ArrayList[3];//���ƶ���
	public boolean PlayerActive[]=new boolean[3];//�Ƿ��Ѿ�������
	public Button button[]=new Button[8];//0,1,2,3Ϊ���ְ�ť   4,5,6Ϊ���ư�ť
	public Image showCancelLabel[]=new Image[3];//��ʾ��Ҫ��ť
	public Image winLabel[]=new Image[2];
	public Image winpage;
	public Button winBtn[]=new Button[2];
	public Label winScore[]=new Label[2];
	public Image labelpage[]=new Image[2];
	public float delay=0;
	public Card cards[] = new Card[56];// 1-54 55�Ǳ���
	public Image showScoreLabel[]=new Image[3];//��ʾ���ַ���
	public Label bscores,btimes;//�׷ֱ���
	public Label name[]=new Label[3];//����
	public Image readyLabelFinished[]=new Image[3];
	public int getCurrentPlayerId() {
		return CurrentPlayerId;
	}
	public void setCurrentPlayerId(int currentPlayerId) {
		CurrentPlayerId = currentPlayerId;
	}
	public Stage stage;
	public GameInfo(Stage s)
	{
		//��ʼ��
		this.stage=s;
		for(int i=0;i<3;i++)
		{
			playerList[i]=new ArrayList();
			playerOutList[i]=new ArrayList();
		}
		playerList[3]=new ArrayList();
	}
	public int getGameState() {
		return GameState;
	}
	public void setGameState(int gameState) {
		GameState = gameState;
	}
	public GameInfo()
	{
	}
	public int getScore(int i) {
		return Score[i];
	}
	public void setScore(int i,int score) {
		SoundManager.PlayBid(score);
		Score[i]=score;
	}
	public int getLandlordId() {
		return LandlordId;
	}
	public void setLandlordId(int landlordId) {
		LandlordId = landlordId;
	}
	public void checkAllActive()
	{
		int i;
		for(i=0;i<3;i++)
		{
			if(PlayerActive[i]==false)
				break;
		}
		if(i==3)
			setCurrentPlayerId(3);
		
	}
	public void printPlayer()
	{
		int i;
		for(i=0;i<playerList[1].size();i++)
		{
			Comm.HjLog(playerList[1].get(i).value+" ");
		}
	}
	public int prevPlayer()
	{
		return (CurrentPlayerId+2)%3;
	}
	public int nextPlayer()
	{
		return (CurrentPlayerId+1)%3;
	}
	public void isWin()
	{
		for(int i=0;i<3;i++)
		{
			if(playerList[i].size()==0)
			{
				
				String flag[]=new String[2];
				int score=ScoreManager.baseScore*ScoreManager.baseTimes*ScoreManager.base;
				if(i==LandlordId)
				{
					winLabel[0].setVisible(true);
					flag[0]="-";
					flag[1]="+";
					Comm.HjLog("����ʤ��");
					this.setGameState(3);
				}else
				{
					winLabel[1].setVisible(true);
					flag[0]="+";
					flag[1]="-";
					Comm.HjLog("ũ��ʤ��");
					this.setGameState(3);
				}
				if(i==ScoreManager.ma.con.me.getDeskId())
					SoundManager.PlayWin(true);
				else
					SoundManager.PlayWin(false);
				showWinPage();
				winScore[0].setText(flag[0]+score);
				winScore[1].setText(flag[1]+2*score);
				if(ScoreManager.ma.con.me.getDeskId()==getLandlordId())
				{
					if(flag[1]=="+")
						ScoreManager.setScore(ScoreManager.getScore()+2*score);
					else
						ScoreManager.setScore(ScoreManager.getScore()-2*score);
				}else
				{
					if(flag[0]=="+")
						ScoreManager.setScore(ScoreManager.getScore()+score);
					else
						ScoreManager.setScore(ScoreManager.getScore()-score);
				}
				Comm.HjLog("���ڵ÷�:"+ScoreManager.getScore());
				AD.showWandoujia();
				break;
			}
		}
	}
	public List<Card> getOppo()
	{
		int id=getCurrentPlayerId();
		//��ȥ֮ǰ����
		showCancelLabel[id].setVisible(false);
		/*if(playerOutList[id]!=null)
		{
			for(Card c:playerOutList[id])
			{
				c.setVisible(false);
			}
		}*/
		
		List<Card> oppo=null;
		if(playerOutList[nextPlayer()]!=null&&playerOutList[nextPlayer()].size()>0)
		{
			if(playerOutList[prevPlayer()]!=null&&playerOutList[prevPlayer()].size()>0)
			{
				oppo=playerOutList[prevPlayer()];
				Comm.oppoerFlag=prevPlayer();
			}else
			{
				oppo=playerOutList[nextPlayer()];
				Comm.oppoerFlag=nextPlayer();
			}
		}else{
			if(playerOutList[prevPlayer()]!=null&&playerOutList[prevPlayer()].size()>0)
			{
				oppo=playerOutList[prevPlayer()];
				Comm.oppoerFlag=prevPlayer();
			}else
				oppo=null;
		}
		return oppo;
	}
	public void showWinPage()
	{
		for(Card cards:playerList[3])
			cards.setVisible(false);
		for(int i=0;i<8;i++)
		{
			button[i].setVisible(false);
			button[i].setDisabled(true);
		}
		winpage.setVisible(true);
		winScore[0].setVisible(true);
		winScore[1].setVisible(true);
		winBtn[0].setVisible(true);
		winBtn[1].setVisible(true);
		labelpage[0].setVisible(true);
		labelpage[1].setVisible(true);
	}
}
