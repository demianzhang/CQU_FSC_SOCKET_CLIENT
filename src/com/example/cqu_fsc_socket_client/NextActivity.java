package com.example.cqu_fsc_socket_client;

//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
//import android.widget.Toast;
//import java.text.SimpleDateFormat;
//import java.util.Date;
import java.util.Timer;  
import java.util.TimerTask; 

public class NextActivity extends Activity {
	private TextView textReceive1 = null;
	private TextView textReceive2 = null;
	private TextView textReceive4 = null;
	private TextView textReceive5 = null;
	private TextView textReceive6 = null;
	private LinearLayout layout = null;
	private LinearLayout linearLayout = null;
	private static OutputStream outStream;
	private static final String ServerIP="10.110.114.119";
	private static final int ServerPort = 12345;
//	private static final String ServerIP="192.168.191.1";
//	private static final int ServerPort = 8899;
	private Socket socket = null;
	private boolean isConnect = false;
	private Handler myHandler1 = null;
	private static ReceiveThread receiveThread = null;
	private boolean isReceive = false;
	private int flag1=0;
	private int flag3=0;
	private int timeflag=0;
	private int flag5=0;
	private int oldmeter=0;
	private int meter=0;
	private int cspeed=0;
	private int ccspeed=0;
	private int espeed=0;
	private int wtemp=0;
	private String stalls="空";
	OilView view;
	private long startnumber = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.next);
        view =(OilView) findViewById(R.id.view1);
        layout = (LinearLayout)findViewById(R.id.lyaout);
        linearLayout = (LinearLayout)findViewById(R.id.linearlayout);
        textReceive1 = (TextView)findViewById(R.id.textViewReceive1);
        textReceive2 = (TextView)findViewById(R.id.textViewReceive2);
        textReceive4 = (TextView)findViewById(R.id.textViewReceive4);
        textReceive5 = (TextView)findViewById(R.id.textViewReceive5);
        textReceive6 = (TextView)findViewById(R.id.textViewReceive6);
        mtimer2.schedule(task2,1000,10);
//		mtimer.schedule(task, 2000, 2000); 
      
		if (!isConnect){
			new Thread(connectThread).start();
		}
		myHandler1 = new Handler(){
			@Override
			public void handleMessage(Message msg1){
				//textReceive.append((msg.obj).toString());
				switch (msg1.what) {
				case 1:
					textReceive2.setText(Integer.toString(msg1.arg1));//刷新车速
					textReceive1.setText(Integer.toString(meter/1000)+"."+Integer.toString(meter%1000));//刷新里程
					break;
				case 2:
					textReceive6.setText(Integer.toString(msg1.arg1));
					view.setRotate_degree((float)(msg1.arg1*263/18750+49));//刷新转速表
					view.invalidate();			
					break;
				case 3:
					textReceive4.setText(Integer.toString(msg1.arg1));//刷新水温
					if(wtemp>95)
						linearLayout.setBackgroundColor(0xffff0000);
					else
						linearLayout.setBackgroundColor(0xff000000);
					break;
				case 4:
					textReceive5.setText((msg1.obj).toString());//刷新档位
					break;
				default:
					break;
				}		
			}
		};	
    }
	Timer mtimer2 = new Timer();
	TimerTask task2 = new TimerTask() {
		@Override
		public void run() {
			timeflag++;
			if(timeflag==50){
				if(flag5==0){
					meter+= (ccspeed+oldmeter)*0.69444;
					startnumber+=1;
				}
				else {
					oldmeter=0;
				}
				oldmeter=cspeed;
				timeflag=0;
//				if(startnumber%4==0){
//					if(flag4==flag3){
//						isConnect = false;
//						Message msg2 = new Message();
//						Message msg3 = new Message();
//						Message msg4 = new Message();
//						Message msg5 = new Message();
//						msg2.obj = "0";
//						msg3.obj = "0";
//						msg4.obj = "0";
//						msg5.obj = "空";
//						cspeed=0;
//						espeed=0;
//						wtemp=0;
//						stalls="空";
//						myHandler2.sendMessage(msg2);
//						myHandler3.sendMessage(msg3);
//						myHandler4.sendMessage(msg4);
//						myHandler5.sendMessage(msg5);
//						flag1=0;
//						flag2=0;
//						flag3=0;
//						flag4=0;
//						flag5=1;
//
//	        		}
//	       		 	else { 
//	        			flag4=flag3;
//	        			flag5=0;
//	        		}	
//				}
			}
//			if(flag1==0)  new Thread(connectThread).start();
		}
	};
Runnable connectThread = new Runnable() {
		@Override 
		public void run() { 
			try { 
				socket = new Socket(ServerIP, ServerPort);
				isConnect = true;
				isReceive = true;
				receiveThread = new ReceiveThread(socket);
				receiveThread.start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.out.println("UnknownHostException-->" + e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IOException" + e.toString());
			}
		}
	};

	private class ReceiveThread extends Thread{
		private InputStream inStream = null;
		private byte[] buffer;
		private String str1 = null;
		private String str2 = null;
		private String str3 = null;
		private String str4 = null;
		private String str5 = null;
		ReceiveThread(Socket socket){
			try {
				inStream = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run(){
			flag1++;
			while(isReceive){
				flag3+=1;
				if(flag3>4)
					flag3=1;
				try {
					outStream = socket.getOutputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					outStream.write(flag3);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				buffer= new byte[2];
				Message msg1 = new Message();
				try {
					inStream.read(buffer);

				} catch (IOException e) {
					e.printStackTrace();
				}	
				switch (flag3) {
				case 1:
					cspeed=(int)(buffer[0]+(buffer[1]<<8));
					ccspeed=cspeed;
					msg1.arg1=cspeed;
					msg1.what=1;
					break;
				case 2:
					espeed=(int)(buffer[0]+(buffer[1]<<8));
					msg1.arg1=espeed;
					msg1.what=2;
					break;
				case 3:
					wtemp=(int)(buffer[0]);
					msg1.arg1=wtemp;
					msg1.what=3;
					break;
				case 4:
					switch (buffer[0]) {//档位	byte[6]
						case 1:
							stalls="1";
							break;
						case 2:
							stalls="2";
							break;
						case 3:
							stalls="3";
							break;
						case 4:
							stalls="4";
							break;
						case 5:
							stalls="5";
							break;
						case 6:
							stalls="6";
							break;
						default:
							stalls="空";
							break;
					}
					str1=stalls;
					msg1.obj=str1;
					msg1.what=4;
					break;
				default:
					break;
				}
				myHandler1.sendMessage(msg1);
				flag1--;
			}	
		}
	}	
	@Override    
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
		switch (keyCode) {
	    case KeyEvent.KEYCODE_HOME:
	        return true;
	    case KeyEvent.KEYCODE_BACK:
	        return true;
	    case KeyEvent.KEYCODE_CALL:
	        return true;
	    case KeyEvent.KEYCODE_SYM:
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_DOWN:
	        return true;
	    case KeyEvent.KEYCODE_VOLUME_UP:
	        return true;
	}
	return  super.onKeyDown(keyCode, event);     
	} 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(receiveThread != null){
			isReceive = false;
			receiveThread.interrupt();
		}
//		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
//		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
//		String nowtime = formatter.format(curDate);
//		try{
//			FileOutputStream store = openFileOutput("meter.txt",MODE_APPEND);
//			store.write((nowtime+Integer.toString(meter/10)+"\n").getBytes());
//			store.close();
//		} catch(FileNotFoundException e){
//		
//		} catch (IOException e) {
//			// TODO: handle exception
//		}
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.startActivity(intent);
		System.exit(0);
	}
}