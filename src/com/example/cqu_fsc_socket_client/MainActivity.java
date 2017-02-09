package com.example.cqu_fsc_socket_client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private int flag=0;
	private int send=0;
	private static OutputStream outStream;
	private static String strMessage="f";
	private Button button1;
	private Button button2;
	private static String ServerIP = "192.168.191.1";
	private static final int ServerPort = 8899;
	private Socket socket = null;
	private boolean isConnect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = (Button)this.findViewById(R.id.button1);
        button2 = (Button)this.findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			Intent intent=new Intent(MainActivity.this,NextActivity.class);	
			startActivity(intent);
			}
		});
        button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isConnect){
					new Thread(connectThread).start();
				}
			}
		});
    }
	Runnable connectThread = new Runnable() {	
		@Override
		public void run() {
			flag++;
			// TODO Auto-generated method stub
			try {
				socket = new Socket(ServerIP, ServerPort);//设置socket的IP和端口号
				isConnect = true;
				//
				new Thread(sendThread).start();

					// TODO Auto-generated method stub
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("UnknownHostException-->" + e.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("IOException" + e.toString());
			}

		}
	};
	Runnable sendThread = new Runnable() {		
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			byte[] sendBuffer = null;//创建一个发送字符
//			try {
				if(flag%2!=0){
					send=1;
				}
				else {
					send=0;
				}
//				sendBuffer = strMessage.getBytes("UTF-8");
//
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			try {
				outStream = socket.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				outStream.write(send);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			isConnect = false;
		}
	};
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(sendThread != null){
			isConnect = false;
		}	
	}
}
