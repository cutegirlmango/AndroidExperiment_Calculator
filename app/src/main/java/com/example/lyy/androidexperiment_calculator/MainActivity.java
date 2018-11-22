package com.example.lyy.androidexperiment_calculator;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView resultView;
    private String expressionString="";
    // 第一个操作数
    private String firstNum;
    // 第二个操作数
    private String secNum;
    // 操作符
    private String opt;
    // 结果
    private String resNum;
    // 下一步是否需要计算结果
    private boolean isNeedResult;
    // 是否已经完成
    private boolean isFinish;
    // 是否已经输入小数点
    private boolean isDot;
    //是否需要刷新textView
    private boolean isNeedRefresh;
    private Stack<Double> numbers = new Stack<>();
    private Stack<String> opers = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.resultView = (TextView) this.findViewById(R.id.resultView);
    }
    //设置横竖屏
    @Override
    public void onConfigurationChanged (Configuration newConfig){

        super.onConfigurationChanged(newConfig);

        int mCurrentOrientation = getResources().getConfiguration().orientation;

        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
            System.out.println("the result is"+resultView.getText().toString());
            String str = resultView.getText().toString();
            setContentView(R.layout.activity_main);
            this.resultView = (TextView) this.findViewById(R.id.resultView);
            refreshResultView(str);
        } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {
            System.out.println("the result is"+resultView.getText().toString());
            String str = resultView.getText().toString();
            setContentView(R.layout.activity_main_land);
            this.resultView = (TextView) this.findViewById(R.id.resultViewLand);
            refreshResultView(str);
        }

    }

    public void onBtnClick(View view){
        TextView textView = (TextView)view;
        String text = textView.getText().toString();
        if(text.equals("0")||text.equals("1")||text.equals("2")||text.equals("3")||text.equals("4")||text.equals("5")||
                text.equals("6")||text.equals("7")||text.equals("8")||text.equals("9")||text.equals(".")){
            if(isDot&&text.equals(".")){

            }else {
                refreshResultView(text);
            }
        }else {
            isNeedRefresh=true;
        }
        Log.d(TAG, text);
        expressionString+=text;
        System.out.println(expressionString);
    }
    public void clearData(){
        firstNum = "";
        secNum = "";
        opt = null;
        resNum = "";
        isNeedResult = false;
        isFinish = false;
        isDot = false;
        isNeedRefresh=true;
    }
    // 刷新TextView：输完运算符
    public void refreshResultView(String addStr){
        String str = resultView.getText().toString();
        if (str.equals("0")||isNeedRefresh){
            isDot=false;
            isNeedRefresh=false;
            str = "";
        }
        if(addStr.equals(".")&&!isDot){
            isDot=true;
        }
        str += addStr;
        System.out.println("refresh:"+str);
        double number = Double.parseDouble(str);
        System.out.println(str);

        resultView.setText(str);
    }

}
