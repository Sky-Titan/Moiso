package com.jun.moiso.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jun.moiso.R;
import com.jun.moiso.activity.ControlActivity;
import com.jun.moiso.interfaces.SocketCallback;
import com.jun.moiso.socket.SocketLibrary;

import static android.content.Context.MODE_PRIVATE;

public class ConnectFragment extends Fragment {

    private EditText ip_edittext, port_edittext;
    private String group_name,user_name;
    private SocketLibrary socketLibrary;

    //자동 저장
    private SharedPreferences sf;
    private SharedPreferences.Editor editor;

    private static View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_connect, container, false);

        socketLibrary = SocketLibrary.getInstance();

        //저장된 IP 정보 가져오기
        sf = getActivity().getSharedPreferences("IP_INFO", MODE_PRIVATE);
        editor = sf.edit();

        Button connect = v.findViewById(R.id.connect_btn);
        connect.setOnClickListener( view -> {
                //PC 앱과 소켓 연결 작업 후 ControlActivity로 이동
                SocketCallback connectCallBack = result ->  {
                        getActivity().runOnUiThread(() ->  {
                            if(result)
                            {
                                Toast.makeText(getContext(), "연결 완료", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), ControlActivity.class);
                                startActivity(intent);
                            }
                            else
                                Toast.makeText(getContext(), "연결 실패", Toast.LENGTH_SHORT).show();
                        });
                };

                //연결 시작
                socketLibrary.connect(ip_edittext.getText().toString(), Integer.parseInt(port_edittext.getText().toString()), group_name, user_name, connectCallBack);
        });

        //ip주소 입력
        ip_edittext = v.findViewById(R.id.ip_edittext);
        ip_edittext.setText(sf.getString("IP",""));
        ip_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //저장을 하기위해 editor를 이용하여 값을 저장시켜준다.
                editor.putString("IP", ip_edittext.getText().toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //포트 입력
        port_edittext = v.findViewById(R.id.port_edittext);
        port_edittext.setText(sf.getString("PORT","5001"));
        port_edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editor.putString("PORT", port_edittext.getText().toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }


}