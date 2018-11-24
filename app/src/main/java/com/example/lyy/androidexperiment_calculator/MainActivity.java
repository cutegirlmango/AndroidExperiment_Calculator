package com.example.lyy.androidexperiment_calculator;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    //显示的结果栏
    private TextView resultView;
    //当前输入的表达式
    private String expressionString="";
    // 结果
    private String resNum;
    // 是否已经输入小数点
    private boolean isDot;
    //是否需要刷新textView
    private boolean isNeedRefresh;
    //数字栈
    private Stack<Double> numbers = new Stack<>();
    //操作符栈
    private Stack<Character> opers = new Stack<>();
    //当前的操作符
    private char[] operator = {'+', '-', '*', '/', '(', ')', '#'};
    //操作符优先级数组，与以前的操作符优先级不同
    private int[][] priority = {{0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 1, 0, 0},
            {1, 1, 1 ,1, 1, -1, -2},
            {0, 0, 0, 0, -2, 0, 0},
            {1, 1, 1, 1, 1, -2, -3}};
    /*/
    * 函数名：onCreate
    * 函数功能：创建窗口(重载的方法)
    * 函数参数：
    * 返回值：无
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.resultView = (TextView) this.findViewById(R.id.resultView);
        clearData();
    }
    /*/
     * 函数名：
     * 函数功能：设置横竖屏
     * 函数参数：
     * 返回值：无
     * */
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
            isNeedRefresh=true;
            refreshResultView(str);
        } else if ( mCurrentOrientation == Configuration.ORIENTATION_LANDSCAPE ) {//横屏
            System.out.println("the result is"+resultView.getText().toString());
            String str = resultView.getText().toString();
            setContentView(R.layout.activity_main_land);
            this.resultView = (TextView) this.findViewById(R.id.resultViewLand);
            isNeedRefresh=true;
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
            }else if(text.endsWith("π")){
                isNeedRefresh = true;
                refreshResultView("Error");
                clearData();
            } else {//否则，将点击的数字存起来
                refreshResultView(text);
                expressionString+=text;
                System.out.println(expressionString);
            }
        }else {
            //如果点击"+"、"-"、"*"、"/"、"%"
            if(text.equals("+")||text.equals("-")||text.equals("*")||text.equals("/")||text.equals("(")||text.equals(")")){

                if(text.equals("(")&&(expressionString.endsWith("0")||expressionString.endsWith("1")||expressionString.endsWith("2")
                        ||expressionString.endsWith("3")||expressionString.endsWith("4")||expressionString.endsWith("5")
                        ||expressionString.endsWith("6")||expressionString.endsWith("7")||expressionString.endsWith("8")
                        ||expressionString.endsWith("9"))){
                    return;
                }
                if(text.equals(")")&&(expressionString.endsWith("+")||expressionString.endsWith("-")||expressionString.endsWith("*")
                        ||expressionString.endsWith("/"))){
                    return;
                }
                if(text.equals(")")){
                    int rBracket = 0;
                    int lBracket = 0;
                    for(int i = 0; i < expressionString.length();i++){
                        if(expressionString.charAt(i)=='('){
                            rBracket++;
                        }
                        if(expressionString.charAt(i)==')'){
                            lBracket++;
                        }
                    }
                    if(rBracket<=lBracket){
                        return;
                    }
                }
                //如果是重复点击这些运算符，不处理
                if(!text.equals("(")&&(expressionString.endsWith("+")||expressionString.endsWith("-")||expressionString.endsWith("*")||
                        expressionString.endsWith("/"))){
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
                        refreshResultView(resNum);
                    }
                    expressionString+=text;
                    System.out.println(expressionString);
                }
            }else if(text.equals("AC")){
                clearData();
                isNeedRefresh=true;
                refreshResultView("0");
            } else if(text.equals("=")){
                double number = Double.parseDouble(resultView.getText().toString());
                numbers.push(number);
                System.out.println("push:"+numbers.peek());
                System.out.println("now text is "+ text);
                if(canCal('#')){
                    isNeedRefresh=true;
                    refreshResultView(resNum);
                }
                clearData();
            } else if(text.equals("lnX")||text.equals("sin")||text.equals("exp")||text.equals("cos")||text.equals("tan")
                    ||text.equals("sqrt")){
                if(expressionString.endsWith("+")||expressionString.endsWith("-")||expressionString.endsWith("*")||
                        expressionString.endsWith("/")){
                    return;
                }
                double number = Double.parseDouble(resultView.getText().toString());
                numbers.push(number);
                System.out.println("push:"+numbers.peek());
                System.out.println("now text is "+ text);
                double x = numbers.pop();
                switch (text){
                    case "lnX":
                        x = Math.log(x);
                        break;
                    case "exp":
                        x = Math.exp(x);
                        break;
                    case "sqrt":
                        x = Math.pow(x,0.5);
                        break;
                    case "sin":
                        x = Math.sin(x);
                        break;
                    case "cos":
                        x = Math.cos(x);
                        break;
                    case "tan":
                        x = Math.tan(x);
                        break;
                }
                numbers.push(x);
                isNeedRefresh =true;
                refreshResultView(x+"");
            }else if(text.equals("π")){
                if(expressionString.endsWith("0")||expressionString.endsWith("1")||expressionString.endsWith("2")
                        ||expressionString.endsWith("3")||expressionString.endsWith("4")||expressionString.endsWith("5")
                        ||expressionString.endsWith("6")||expressionString.endsWith("7")||expressionString.endsWith("8")
                        ||expressionString.endsWith("9")){
                    return;
                }
                numbers.push(3.14);
            }
            isNeedRefresh=true;
        }
    }
    public boolean isNum(String str){
        if(str.equals("0")||str.equals("1")||str.equals("2")||str.equals("3")||str.equals("4")||str.equals("5")
                ||str.equals("6")||str.equals("7")||str.equals("8")||str.equals("9")){
            return true;
        }
        return false;
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
        System.out.println("priority is "+priority[row][col]);
        if(priority[row][col] == 1) {
            opers.push(op);
        } else if(priority[row][col] == 0) {
            compute(numbers.pop(),numbers.pop(),opers.pop());
            if(op==')'){
                opers.pop();
                return true;
            }
            opers.push(op);
            return true;
        } else if(priority[row][col] == -1) {
            opers.pop();
        }
        return false;
    }

    public void clearData(){
        resNum = "";
        isDot = false;
        isNeedRefresh=true;
        numbers.clear();
        opers.clear();
        opers.push('#');
        expressionString="";
    }
    // 刷新TextView：输完运算符
    public void refreshResultView(String addStr){
        String str = resultView.getText().toString();
        if (isNeedRefresh||(str.equals("0")&&!addStr.equals("."))){
            isDot=false;
            isNeedRefresh=false;
            str = "";
            System.out.println("clear!");
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
                System.out.println("add result push:"+numbers.peek());
                break;
            case '-':
                result = pNumber-nNumber;
                numbers.push(result);
                System.out.println("sub result push:"+numbers.peek());
                break;
            case '*':
                result = pNumber*nNumber;
                numbers.push(result);
                System.out.println("mul result push:"+numbers.peek());
                break;
            case '/':
                if(nNumber == 0) {
                    resNum = "Error";
                    return resNum;
                }
                result = pNumber / nNumber;
                numbers.push(result);
                System.out.println("div result push:"+numbers.peek());
                break;
        }
        System.out.println("res:"+result);
        DecimalFormat df = new DecimalFormat("#.00");
        resNum=df.format(result);
        return resNum;
    }
}
