package com.hj.singlejoker;


import com.badlogic.gdx.backends.android.AndroidApplication;
import com.hj.net.Constant;
import com.hj.net.Server;
import com.hj.screen.LoadScreen;
import com.hj.screen.MainScreen;
import com.hj.screen.NetScreen;
import com.hj.screen.RommScreen;
import com.hj.screen.SettingScreen;
import com.hj.screen.UiScreen;
import com.hj.tool.Comm; 
import com.umeng.analytics.game.UMGameAgent;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.loader.Fetcher;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AndroidApplication {

	//��ʼ��
	public UiScreen us; //��ʼ�� �� 
	public MainScreen ms; //��Ϸ����
	public RommScreen rs;//�������
	public NetScreen ns;//������Ϸ����
	public SettingScreen ss;//��������
	public LoadScreen ls;//����
	public HjGame hg;
	public Constant con=Constant.getCons();

	public static  Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ��������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        UMGameAgent.setDebugMode(true);//�����������ʱ��־
        UMGameAgent.init( this );
        //ScoreManager
        ScoreManager.init(this);
		//AD
		try {
	      Ads.init(this, AD.ADS_APP_ID, AD.ADS_SECRET_KEY);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
		Ads.preLoad(this, Fetcher.AdFormat.interstitial, AD.TAG_INTERSTITIAL_FULLSCREEN);
		Ads.preLoad(this, Fetcher.AdFormat.appwall, AD.TAG_INTERSTITIAL_List);
		
		mHandler = new Handler();   
	   
        us=new UiScreen(this);
        ms=new MainScreen(this);
        rs=new RommScreen(this);
        ns=new NetScreen(this);
        ss=new SettingScreen(this);
        ls=new LoadScreen(this);
        hg=new HjGame(ls);
        initialize(hg,true);   
        Comm.HjLog("oncreate");
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Comm.HjLog("ondestroy");
		//һ��Ҫ�ͷ���Դ
		HjGame.unload(HjGame.getManager());
		Server.getServer().stop=true;
		super.onDestroy();
	}
	@Override
	protected void onPause() {
		UMGameAgent.onPause(this);
		SoundManager.CloseBackMusic();
		super.onPause();
	}
	@Override
	protected void onResume() {
		 UMGameAgent.onResume(this);
		if(SoundManager.backgroundMusic!=null)
			SoundManager.StartBackMusic();
		super.onResume();
	}
}
