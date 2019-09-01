package louiza.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
EditText e1,e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e1=findViewById(R.id.editText1);
        e2=findViewById(R.id.editText2);
Thread thread=new Thread(new myServer());
thread.start();
    }
    class myServer implements Runnable{
ServerSocket serverSocket;
Socket socket;
DataInputStream dataInputStream;
String message;
Handler handler=new Handler();
        @Override
public void run(){
            try {
                serverSocket=new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                   Toast.makeText(getApplicationContext(),"waiting for client",Toast.LENGTH_SHORT).show();
                    }
                });
                while (true){
                    socket= serverSocket.accept();
                    dataInputStream=new DataInputStream(socket.getInputStream());
message=dataInputStream.readUTF();
handler.post(new Runnable() {
    @Override
    public void run() {
        LinearLayout ll;
        ll=findViewById(R.id.ll);
        TextView edit=new TextView(MainActivity.this);
        edit.setText(message.getBytes().toString());
        edit.setGravity(Gravity.LEFT);
ll.addView(edit);

    }
});
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void buttonClick(View v){


        BackgroundTask b=new BackgroundTask();
b.execute(e1.getText().toString(),e2.getText().toString());
    }
    class BackgroundTask extends AsyncTask<String,Void,String>{
        Socket s;
        DataOutputStream dos;
        String ip,message;
        @Override
protected String doInBackground(String...params){
            ip=params[0];
            message=params[1];
            try {
                s=new Socket(ip,9700);
                dos=new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);
                dos.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
