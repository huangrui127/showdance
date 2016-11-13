package com.android.app.showdance.custom;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 功能：文字上下滚动显示数字用TextView
 */
public class RandomTextView extends TextView {

    public static final int FIRSTF_FIRST = 0;// 高位快
    public static final int FIRSTF_LAST = 1;// 高位慢
    public static final int ALL = 2;// 速度相同
    public static final int USER = 3;// 用户自定义速度
    @SuppressWarnings("unused")
    private int pianyiliangTpye;// 偏移速度类型
    private int maxLine = 10;// 滚动总行数 可设置
    private int numLength = 0;// 当前字符串长度
    private String text;// 当前text
    private int[] pianyilianglist;// 滚动速度数组
    private int[] pianyiliangSum;// 总滚动距离数组
    private int[] overLine;// 滚动完成判断
    private Paint p;
    private boolean firstIn = true;// 第一次绘制
    private boolean auto = true;// 滚动中
    private ArrayList<Integer> arrayListText;// text int值列表
    private float f0; // 字体宽度
    private int baseline;// 基准线

    public RandomTextView(Context context) {
        super(context);
    }

    public RandomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RandomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // 按系统提供的类型滚动
    public void setPianyilian(int pianyiliangTpye) {
        this.text = getText().toString();
        pianyiliangSum = new int[text.length()];
        overLine = new int[text.length()];
        pianyilianglist = new int[text.length()];
        switch (pianyiliangTpye) {
        case FIRSTF_FIRST:
            for (int i = 0; i < text.length(); i++) {
                pianyilianglist[i] = 40 - i;
            }
            break;
        case FIRSTF_LAST:
            for (int i = 0; i < text.length(); i++) {
                pianyilianglist[i] = 30 + i;
            }
            break;
        case ALL:
            for (int i = 0; i < text.length(); i++) {
                pianyilianglist[i] = 15;
            }
            break;
        }
    }

    // 自定义滚动速度数组
    public void setPianyilian(int[] list) {
        this.text = getText().toString();
        pianyiliangSum = new int[list.length];
        overLine = new int[list.length];
        pianyilianglist = list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (firstIn) {
            firstIn = false;
            super.onDraw(canvas);
            p = getPaint();
            Paint.FontMetricsInt fontMetrics = p.getFontMetricsInt();
            baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
            float[] widths = new float[4];
            p.getTextWidths("9999", widths);
            f0 = widths[0];
            invalidate();
        }
        drawNumber(canvas);
    }

    // 绘制
    private void drawNumber(Canvas canvas) {
        for (int j = 0; j < numLength; j++) {
            for (int i = 1; i < maxLine; i++) {
                if (i == maxLine - 1 && i * baseline + pianyiliangSum[j] <= baseline) {
                    pianyilianglist[j] = 0;
                    overLine[j] = 1;
                    int auto = 0;
                    for (int k = 0; k < numLength; k++) {
                        auto += overLine[k];
                    }
                    if (auto == numLength * 2 - 1) {
                        this.auto = false;
                        handler.removeCallbacks(task);
                        invalidate();
                    }
                }
                if (overLine[j] == 0)
                    canvas.drawText(setBack(arrayListText.get(j), maxLine - i - 1) + "", 0 + f0 * j,
                            i * baseline + pianyiliangSum[j], p);
                else {
                    // 定位后画一次就好啦
                    if (overLine[j] == 1) {
                        overLine[j]++;
                        canvas.drawText(arrayListText.get(j) + "", 0 + f0 * j, baseline, p);
                    }
                    // break;
                }
            }
        }
    }

    // 设置上方数字0-9递减
    private int setBack(int c, int back) {
        if (back == 0)
            return c;
        back = back % 10;
        int re = c - back;
        if (re < 0)
            re = re + 10;
        return re;
    }

    // 开始滚动
    public void start() {
        this.text = getText().toString();
        numLength = text.length();
        arrayListText = getList(text);
        handler.postDelayed(task, 17);
        auto = true;
    }

    public void setMaxLine(int l) {
        this.maxLine = l;
    }

    private ArrayList<Integer> getList(String s) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for (int i = 0; i < s.length(); i++) {
            String ss = s.substring(i, i + 1);
            int a = Integer.parseInt(ss);
            arrayList.add(a);
        }
        return arrayList;
    }

    private static final Handler handler = new Handler();

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if (auto) {
                handler.postDelayed(this, 100);
                for (int j = 0; j < numLength; j++) {
                    pianyiliangSum[j] -= pianyilianglist[j];
                }
                invalidate();
            }
        }
    };

}
