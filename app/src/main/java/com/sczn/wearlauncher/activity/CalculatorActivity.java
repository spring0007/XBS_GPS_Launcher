package com.sczn.wearlauncher.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sczn.wearlauncher.R;
import com.sczn.wearlauncher.app.MxyLog;
import com.sczn.wearlauncher.base.AbsActivity;
import com.sczn.wearlauncher.util.DoubleUtil;

import java.util.regex.Pattern;

/**
 * Description:计算器
 * Created by Bingo on 2019/3/4.
 */
public class CalculatorActivity extends AbsActivity {
    private final String TAG = "CalculatorActivity";
    private TextView display;
    private Operation operation;
    private double firstNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_v2);
        display = findViewById(R.id.display);
        operation = Operation.STARTOVER;
    }

    private enum Operation {
        ADD,
        SUBTRACT,
        MULTIPLY,
        DIVIDE,
        STARTOVER,
        NONE
    }

    /**
     * 加减乘除的操作按钮
     *
     * @param view
     */
    public void operationButtonClick(View view) {
        String input = display.getText().toString();
        MxyLog.d(TAG, "加减乘除的操作按钮:" + input);
        if (verifyNumber(input)) {
            return;
        }
        firstNumber = Double.parseDouble(input);
        display.setText("");
        switch (view.getId()) {
            case R.id.add:
                operation = Operation.ADD;
                break;
            case R.id.multiply:
                operation = Operation.MULTIPLY;
                break;
            case R.id.subtract:
                operation = Operation.SUBTRACT;
                break;
            case R.id.divide:
                operation = Operation.DIVIDE;
                break;
        }
    }

    /**
     * 数字按钮
     *
     * @param view
     */
    public void numberButtonClick(View view) {
        TextView button = (TextView) view;
        if (operation == Operation.STARTOVER) {
            display.setText("");
            operation = Operation.NONE;
        }
        //防止.1小数点开头
        if (button.getText().toString().equals(".") && display.getText().toString().equals("")) {
            return;
        }
        //防止出现多个小数点
        if (button.getText().toString().equals(".") && display.getText().toString().contains(".")) {
            return;
        }
        display.append(button.getText().toString());
    }

    /**
     * 等于=的按钮操作
     *
     * @param view
     */
    public void equalButtonClick(View view) {
        String input = display.getText().toString().trim();
        MxyLog.d(TAG, "等于=的按钮操作:" + input);
        if (verifyNumber(input)) {
            return;
        }
        double secondNumber = Double.parseDouble(input);
        switch (operation) {
            case ADD:
                firstNumber = DoubleUtil.add(firstNumber, secondNumber);
                break;
            case SUBTRACT:
                firstNumber = DoubleUtil.sub(firstNumber, secondNumber);
                break;
            case MULTIPLY:
                firstNumber = DoubleUtil.mul(firstNumber, secondNumber);
                break;
            case DIVIDE:
                firstNumber = DoubleUtil.divide(firstNumber, secondNumber, 8);
                break;
            default:
                firstNumber = secondNumber;
        }
        display.setText(getPrettyNumber(firstNumber));
        operation = Operation.STARTOVER;
    }

    /**
     * 清空
     *
     * @param view
     */
    public void clear(View view) {
        display.setText("");
        operation = Operation.STARTOVER;
    }

    /**
     * 回退一个字母
     *
     * @param view
     */
    public void back(View view) {
        String input = display.getText().toString().trim();
        if (!input.isEmpty()) {
            input = input.substring(0, input.length() - 1);
            display.setText(input);
        }
    }

    /**
     * 检查是否为数字
     *
     * @param input
     * @return
     */
    private boolean verifyNumber(String input) {
        Pattern pattern = Pattern.compile("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
        return input.equals("") || !pattern.matcher(input).matches();
    }

    /**
     * 去掉多余的0
     *
     * @param number
     * @return
     */
    private String getPrettyNumber(double number) {
        //return BigDecimal.valueOf(number)
        //        .stripTrailingZeros().toPlainString();
        MxyLog.i(TAG, "" + number);
        return subZeroAndDot(String.valueOf(number));
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
