package com.wlp.myanjunote.iot.udp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.wlp.myanjunote.R;
import com.wlp.myanjunote.iot.udp.socket.UDPSocket;

/**
 * Created by melo on 2017/9/20.
 */
public class UDPSocketActivity extends AppCompatActivity {

    private UDPSocket socket;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_socket);

        final EditText etMessage = (EditText) findViewById(R.id.et_message);
        Button btSend = (Button) findViewById(R.id.bt_send);

        socket = new UDPSocket(this);
        socket.startUDPSocket();

        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket.sendMessage(etMessage.getText().toString());
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.stopUDPSocket();
    }
}
