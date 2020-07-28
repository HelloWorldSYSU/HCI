package com.sysumach.voiceindentify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import top.androidman.SuperButton;

import static android.speech.SpeechRecognizer.isRecognitionAvailable;

public class MainActivity extends Activity implements View.OnClickListener{
    private SuperButton speechBut;
    private TextView result;
    private SpeechRecognizer mSpeechRecognizer;
    private String string = "what's your name";


    class mHandler extends Handler{

        private WeakReference<MainActivity> wr;

        public mHandler(MainActivity ma){
            wr = new WeakReference<>(ma);
        }

        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            if(msg.arg1 != 6){
                if(msg.arg1 % 2 == 1){
                    Toast.makeText(wr.get(), 5 - msg.arg1 + "秒后退出APP", Toast.LENGTH_SHORT).show();
                }
            }else{
                wr.get().finish();
            }
        }
    }

    private TextToSpeech textToSpeech;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speechBut:
                doSpeechRecognition(v);
                break;
        }
    }

    private List<Message> msgs = new ArrayList<>();
    private MessageAdapter adapter;
    private ListView listView;
    private mHandler handler = new mHandler(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        final OpenJurisdiction openJurisdiction = new OpenJurisdiction();
        openJurisdiction.open(MainActivity.this);

        this.speechBut= findViewById(R.id.speechBut);
        this.speechBut.setOnClickListener(this);

        adapter = new MessageAdapter(MainActivity.this, R.layout.msg_item, msgs);
        listView = findViewById(R.id.chat);

        listView.setAdapter(adapter);
        initTTS();

    }

    private final int RESULT_SPEECH = 100;

    public void doSpeechRecognition(View view){
        if(isRecognitionAvailable(MainActivity.this)){
            Intent recognitionIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS,true);
            recognitionIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"zh-CN");
            startActivityForResult(recognitionIntent, RESULT_SPEECH);
        }else{
            Toast.makeText(MainActivity.this,"没有语音识别服务",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String myVoice = text.get(0);
                    if (myVoice.equals("关机")){

                        for(int i = 0; i <= 5; i ++){
                            android.os.Message message = android.os.Message.obtain();
                            message.arg1 = i;
                            handler.sendMessageAtTime(message,
                                    SystemClock.uptimeMillis() + 1000 * i);
                        }
                        android.os.Message message = android.os.Message.obtain();
                        message.arg1 = 6;
                        handler.sendMessageAtTime(message,
                                SystemClock.uptimeMillis() + 1000 * 6);

                    }

                    msgs.add(new Message(2, text.get(0)));
                    msgs.add(new Message(1, GetAnswer.get(text.get(0))));
                    adapter.notifyDataSetChanged();
                    listView.setSelection(msgs.size());
                    startAuto(GetAnswer.get(text.get(0)));
                }
                break;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    private void startAuto(String data) {
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setPitch(1.0f);
        // 设置语速
        textToSpeech.setSpeechRate(0.3f);
        textToSpeech.speak(data,//输入中文，若不支持的设备则不会读出来
                TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    protected void onStop() {
        super.onStop();
        textToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        textToSpeech.shutdown(); // 关闭，释放资源
    }

    private void initTTS() {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == textToSpeech.SUCCESS) {
                    // Toast.makeText(MainActivity.this,"成功输出语音",
                    // Toast.LENGTH_SHORT).show();
                    // Locale loc1=new Locale("us");
                    // Locale loc2=new Locale("china");

                    textToSpeech.setPitch(1.0f);//方法用来控制音调
                    textToSpeech.setSpeechRate(1.0f);//用来控制语速

                    //判断是否支持下面两种语言
                    int result1 = textToSpeech.setLanguage(Locale.US);
                    int result2 = textToSpeech.setLanguage(Locale.
                            SIMPLIFIED_CHINESE);
                    boolean a = (result1 == TextToSpeech.LANG_MISSING_DATA || result1 == TextToSpeech.LANG_NOT_SUPPORTED);
                    boolean b = (result2 == TextToSpeech.LANG_MISSING_DATA || result2 == TextToSpeech.LANG_NOT_SUPPORTED);

                    Log.i("zhh_tts", "US支持否？--》" + a +
                            "\nzh-CN支持否》--》" + b);

                } else {
                    Toast.makeText(MainActivity.this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
