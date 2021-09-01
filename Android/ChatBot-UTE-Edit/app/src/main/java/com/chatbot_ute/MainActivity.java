package com.chatbot_ute;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MessageClass> messageList = new ArrayList<>();
    FloatingActionButton btnSend;
    EditText edtMessage;
    RecyclerView rcvMessage;
    MessageAdapter adapter;
    SharedPreferences data;
    TextView tvLoguot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtMessage = findViewById(R.id.edt_message);
        rcvMessage = findViewById(R.id.message_list);
        tvLoguot = findViewById(R.id.tv_logout);
        btnSend = findViewById(R.id.btn_send);
        rcvMessage.setHasFixedSize(true);

        adapter = new MessageAdapter(this, messageList);
        adapter.setHasStableIds(true);

        data = getSharedPreferences("data",MODE_PRIVATE);
        String msv = data.getString("msv","");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        rcvMessage.setLayoutManager(layoutManager);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMessage.getText().toString();
                if (msg != ""){
                    sendMessage(msg,msv.toString());

                    Toast.makeText(MainActivity.this   , "Send", Toast.LENGTH_LONG);
                    edtMessage.setText("");

                }else{
                    Toast.makeText(MainActivity.this   , "Vui lòng nhập tin nhắn của bạn", Toast.LENGTH_LONG);
                }
            }
        });
        tvLoguot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = data.edit();
                editor.putString("msv","");
                editor.commit();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        rcvMessage.setAdapter(adapter);
    }

    private void sendMessage(String msg, String msv) {
        MessageClass userMessage = null;
        if (msg.isEmpty()){
            Toast.makeText(MainActivity.this   , "Vui lòng nhập tin nhắn của bạn", Toast.LENGTH_LONG);
        }else{
            userMessage = new MessageClass(msg,msv,false);
            messageList.add(userMessage);
            adapter.notifyDataSetChanged();
            rcvMessage.scrollToPosition(adapter.getItemCount() - 1);

        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://a33966a60746.ngrok.io/webhooks/rest/")
                .client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
        MessageSender sender = retrofit.create(MessageSender.class);
        Call response = sender.messageSender(userMessage);
        response.enqueue(new Callback<ArrayList<BotResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<BotResponse>> call, Response<ArrayList<BotResponse>> response) {
                if (response.body() != null ){
                    BotResponse message = response.body().get(0);
                    messageList.add(new MessageClass(message.text,msv,true));
                    adapter.notifyDataSetChanged();
                    rcvMessage.scrollToPosition(adapter.getItemCount() - 1);
                }

            }

            @Override
            public void onFailure(Call<ArrayList<BotResponse>> call, Throwable t) {
                String message = "Check your connection";
                messageList.add(new MessageClass(message,msv, true));
            }
        });

    }
}