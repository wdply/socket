package com.example.ply.myclientsocket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_ip, et_sendMsg;
    private Button bt_connect, bt_send;
    private TextView tv_msg;
    private String ip;

    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_sendMsg = (EditText) findViewById(R.id.et_sendMsg);
        bt_connect = (Button) findViewById(R.id.bt_connect);
        bt_send = (Button) findViewById(R.id.bt_send);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        bt_connect.setOnClickListener(this);
        bt_send.setOnClickListener(this);

        ip = et_ip.getText().toString().trim();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_connect:
                connect();
                break;
            case R.id.bt_send:
                send();
                break;
        }
    }

    /**
     * 链接serverSocket服务器
     */
    public void connect() {

        new AsyncTask<Void, String, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Socket socket = new Socket(ip, 13345);//服务器的IP和接口
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));
                    publishProgress("@sucess");

                    //读取serverSocket服务器发来的消息
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        showToast(line);
                        publishProgress(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("连接失败");
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(String... values) {
                if (values[0].equals("@sucess")) {
                    tv_msg.setText("已连接到服务器");
                } else {
                    tv_msg.append("别人说：" + values[0] + "\n");
                }
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    /**
     * 发送消息
     */
    public void send() {
        try {
            tv_msg.append("我说：" + et_sendMsg.getText().toString() + "\n");
            et_sendMsg.setText("");
            bufferedWriter.write(et_sendMsg.getText().toString() + "\n");
            bufferedWriter.flush();//强制将消息刷进去（ps: 如果不写，服务器收不到客户端消息）
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showToast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
