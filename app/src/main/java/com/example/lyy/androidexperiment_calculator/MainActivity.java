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
    private String nowNumber;
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
    private Stack<Character> opers = new Stack<>();
    private char[] operator = {'+', '-', '*', '/', '(', ')', '#'};
    private int[][] priority = {{0, 0, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {1, 1, 1 ,1, 1, -1, -2},
            {0, 0, 0, 0, -2, 0, 0},
            {1, 1, 1, 1, 1, -2, -3}};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.resultView = (TextView) this.findViewById(R.id.resultView);
        clearData();
    }
    //设置横竖屏
    @Override
    public void onConfigurationChanged (Configuration newConfig){

        super.onConfigurationChanged(newConfig);

        int mCurrentOrientation = getResources().getConfiguration().orientation;
        //竖屏
        if ( mCurrentOrientation == Configuration.ORIENTATION_PORTRAIT ) {
            System.out.println("the result is"+resultView.getText().toString());
            String str = resultView.getText().toString();
            setContentView(R.layout.activity_main);
            this.resultView = (TextView) this.findViewById(R.id.resultView);
            refreshResultView(str);
        } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {//横屏
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
        //如果点击了数字按钮
        if(text.equals("0")||text.equals("1")||text.equals("2")||text.equals("3")||text.equals("4")||text.equals("5")||
                text.equals("6")||text.equals("7")||text.equals("8")||text.equals("9")||text.equals(".")){
            //如果数字中已经存在小数点，又重复点击小数点，不处理
            if(isDot&&text.equals(".")){
                return;
            }else {//否则，将点击的数字存起来
                refreshResultView(text);
                expressionString+=text;
                System.out.println(expressionString);
            }
        }else {
            //如果点击"+"、"-"、"*"、"/"、"%"
            if(text.equals("+")||text.equals("-")||text.equals("*")||text.equals("/")||text.equals("(")||text.equals(")")){
                //如果是重复点击这些运算符，不处理
                if(text.equals("(")){
                    
                }
                if(expressionString.endsWith("+")||expressionString.endsWith("-")||expressionString.endsWith("*")||
                        expressionString.endsWith("/")){
                    return;
                }else {//否则，运算符入栈
                    double number = Double.parseDouble(resultView.getText().toString());
                    numbers.push(number);
                    System.out.println("push:"+numbers.peek());
                    System.out.println("now text is "+ text);
//                    isNeedRefresh=true;
//                    refreshResultView("");
                    if(canCal(text.charAt(0))){
                        isNeedRefresh=true;
                        compute(numbers.pop(),numbers.pop(),opers.pop());
                        refreshResultView(resNum);
                    }
                    expressionString+=text;
                    System.out.println(expressionString);
                }
            }else if(text.equals("AC")){
                clearData();
                isNeedRefresh=true;
                refreshResultView("0");
            }
            isNeedRefresh=true;
        }
    }
    public boolean canCal(char op){
        int col = 0;
        int row = 0;
        for(int i = 0;i < operator.length;i++) {
            if (operator[i] == op) {
                col = i;
                break;
            }
        }
        for(int i = 0;i < operator.length;i++) {
            if (operator[i] == opers.peek()){
                row = i;
                break;
            }
        }
        if(priority[row][col] == 1) {
            opers.push(op);
        } else if(priority[row][col] == 0) {
            return true;
        } else if(priority[row][col] == -1) {
            opers.pop();
        }
        return false;
    }

    public void clearData(){
        nowNumber = "";
        resNum = "";
        isNeedResult = false;
        isFinish = false;
        isDot = false;
        isNeedRefresh=false;
        numbers.clear();
        opers.clear();
        opers.push('#');
        expressionString="";
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
        resultView.setText(str);
    }
    public String compute(double nNumber, double pNumber, char operator) {
        double result = 0;
        switch(operator) {
            case '+':
                result = pNumber+nNumber;
                numbers.push(result);
                break;
            case '-':
                result = pNumber-nNumber;
                numbers.push(result);
                break;
            case '*':
                result = pNumber*nNumber;
                numbers.push(result);
                break;
            case '/':
                if(nNumber == 0) {
                    resNum = "Error";
                    return resNum;
                }
                result = pNumber / nNumber;
                numbers.push(result);
                break;
        }
        System.out.println("res:"+result);
        resNum = result+"";
        return resNum;
    }
}
