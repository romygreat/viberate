package com.longchherviberate.luominming.viberate;
import android.app.Activity;
import android.app.Service;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    private TextView daoji;
    private  CounTimeUtils mCounTimeUtils;
    boolean istesting=false;
    boolean disTime=true;
    CountDownTimer timer;
    private Handler handler=new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    daoji.setText((String)msg.obj);
                break;
                case 1:
                    timer.cancel();
                    daoji.setText("");
                    break;
                case 2:
                    vibrateCancel();
                    daoji.setText("测试完成");
                break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        daoji=findViewById(R.id.textView2);
        mCounTimeUtils=new CounTimeUtils();
    }
    public class CounTimeUtils {
        private  String daoji;
        public  void  startCountDownTime(long time)
        {
          timer = new CountDownTimer(time * 1000, 1000) {
                /**
                 * 最简单的倒计时类，实现了官方的CountDownTimer类（没有特殊要求的话可以使用）
                 * 即使退出activity，倒计时还能进行，因为是创建了后台的线程。
                 * 有onTick，onFinsh、cancel和start方法
                 */
                @Override
                public void onTick(long millisUntilFinished) {
                    //每隔countDownInterval秒会回调一次onTick()方法
                    Log.d("test", "onTick  " + millisUntilFinished / 1000);
                   String formate= formatSecond(millisUntilFinished / 1000);
                  // if(disTime)
                   {
                   Message message=handler.obtainMessage();
                   message.what=0;
                   message.obj=formate;
                   handler.sendMessage(message);
                }
                }
                @Override
                public void onFinish() {
                    Message message=handler.obtainMessage();
                    message.what=2;
                    message.obj=123;
                    handler.sendMessage(message);

                    Log.d("test", "onFinish -- 倒计时结束");
                }

            };
            timer.start();// 开始计时

        }
        public long getDateSeconds(){
//            long dateSecond=24*60*60;
            long dateSecond=24*60*60*3;
            return dateSecond;
        }
        public String formatSecond(long second){
            String  html="0秒";
//        if(second!=null)
            {
                long s=second;
                String format;
                Object[] array;
                Integer hours =(int) (s/(60*60));
                Integer minutes = (int) (s/60-hours*60);
                Integer seconds = (int) (s-minutes*60-hours*60*60);
                if(hours>0){
                    format="%1$,d时%2$,d分%3$,d秒";
                    array=new Object[]{hours,minutes,seconds};
                }else if(minutes>0){
                    format="%1$,d分%2$,d秒";
                    array=new Object[]{minutes,seconds};
                }else{
                    format="%1$,d秒";
                    array=new Object[]{seconds};
                }
                html= String.format(format, array);
                daoji=html;
            }
            return html;
        }
    }
    public static void Vibrate(final Activity activity, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
    public void vibrateCancel(){
        Vibrator vib = (Vibrator)MainActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
        vib.cancel();
    }
    public void onClick(View view){
        switch (view.getId()){
        case R.id.begin:
            disTime=true;
           if(!istesting)
            {
           mCounTimeUtils.startCountDownTime(mCounTimeUtils.getDateSeconds());
               // mCounTimeUtils.startCountDownTime(20);
            Vibrate(MainActivity.this,new long[]{2000,1000,2000,1000,2000,1000,2000,1000},true);}
            istesting=true;
            break;
        case R.id.end:
            disTime=false;
            istesting=false;
            vibrateCancel();
            Message message=handler.obtainMessage();
            message.what=1;
            message.obj=123;
            handler.sendMessage(message);
            break;
        }
    }
}
