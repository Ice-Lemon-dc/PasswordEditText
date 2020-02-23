package com.dc.passwordedittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * 自定义输入密码框
 */
public class PasswordEditText extends AppCompatEditText {

    /**
     * 密码的个数默认为6位数
     */
    private int mPasswordNumber = 6;

    /**
     * 密码圆点的半径大小
     */
    private int mPasswordRadius = 4;

    /**
     * 密码圆点的颜色
     */
    private int mPasswordColor = Color.parseColor("#d1d2d6");

    /**
     * 分割线的颜色
     */
    private int mDivisionLineColor = Color.parseColor("#d1d2d6");


    /**
     * 分割线的大小
     */
    private int mDivisionLineSize = 1;

    /**
     * 背景边框颜色
     */
    private int mBgColor = Color.parseColor("#d1d2d6");

    /**
     * 背景边框大小
     */
    private int mBgSize = 1;

    /**
     * 背景边框圆角大小
     */
    private int mBgCorner = 0;


    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 一个密码所占的宽度
     */
    private int mPasswordItemWidth;

    /**
     * 设置当前密码是否已满的接口回掉
     */
    private PasswordFullListener mListener;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initAttribute(context, attrs);

        initPaint();

        // 限制只能输入数字和字母
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
    }

    private void initAttribute(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordEditText);

        mPasswordNumber = array.getInt(R.styleable.PasswordEditText_passwordNumber, mPasswordNumber);
        mPasswordRadius = (int) array.getDimension(R.styleable.PasswordEditText_passwordRadius, dip2px(mPasswordRadius));
        mPasswordColor = array.getColor(R.styleable.PasswordEditText_passwordColor, mDivisionLineColor);
        mDivisionLineColor = array.getColor(R.styleable.PasswordEditText_divisionLineColor, mDivisionLineColor);
        mDivisionLineSize = (int) array.getDimension(R.styleable.PasswordEditText_divisionLineSize, dip2px(mDivisionLineSize));
        mBgColor = array.getColor(R.styleable.PasswordEditText_bgColor, mBgColor);
        mBgSize = (int) array.getDimension(R.styleable.PasswordEditText_bgSize, dip2px(mBgSize));
        mBgCorner = (int) array.getDimension(R.styleable.PasswordEditText_bgCorner, 0);

        array.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);

        // 去除背景横线
        setBackground(null);
    }


    /**
     * dip 转 px
     */
    private float dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 一个密码的宽度 --> (控件宽度 - (密码框边框宽度 * 2) -  (密码个数 - 1) * 分割线宽度) / 密码个数
        mPasswordItemWidth = (getWidth() - 2 * mBgSize - (mPasswordNumber - 1) * mDivisionLineSize) / mPasswordNumber;

        drawBg(canvas);
        drawDivisionLine(canvas);
        drawPassword(canvas);

        if (mListener != null) {
            String password = getText().toString().trim();
            if (password.length() >= mPasswordNumber) {
                mListener.passwordFull(password);
            }
        }
    }

    /**
     * 绘制背景
     *
     * @param canvas Canvas
     */
    private void drawBg(Canvas canvas) {
        RectF rect = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        mPaint.setStrokeWidth(mBgSize);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.STROKE);
        if (mBgCorner == 0) {
            canvas.drawRect(rect, mPaint);
        } else {
            canvas.drawRoundRect(rect, mBgCorner, mBgCorner, mPaint);
        }
    }


    /**
     * 绘制分割线
     *
     * @param canvas Canvas
     */
    private void drawDivisionLine(Canvas canvas) {
        mPaint.setStrokeWidth(mDivisionLineSize);
        mPaint.setColor(mDivisionLineColor);
        for (int i = 0; i < mPasswordNumber - 1; i++) {
            int startX = mBgSize + (i + 1) * mPasswordItemWidth + i * mDivisionLineSize;
            int endX = startX;
            int startY = mBgSize;
            int endY = getHeight() - mBgSize;

            canvas.drawLine(startX, startY, endX, endY, mPaint);
        }
    }

    /**
     * 绘制密码
     *
     * @param canvas Canvas
     */
    private void drawPassword(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPasswordColor);
        String text = getText().toString().trim();
        int passwordLength = text.length();
        for (int i = 0; i < passwordLength; i++) {
            int cx = mBgSize + mPasswordItemWidth * i + mDivisionLineSize * i + mPasswordItemWidth / 2;
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, mPasswordRadius, mPaint);
        }
    }

    /**
     * 添加一个密码
     *
     * @param number String
     */
    public void addPassword(String number) {

        String password = getText().toString().trim();

        if (password.length() >= mPasswordNumber) {
            return;
        }

        password += number;

        // 调用setText后会调用onDraw方法
        setText(password);
    }

    /**
     * 删除最后一位密码
     */
    public void deleteLastPassword() {
        String password = getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            return;
        }
        password = password.substring(0, password.length() - 1);
        setText(password);
    }


    public void setOnPasswordFullListener(PasswordFullListener listener){
        this.mListener = listener;
    }

    /**
     * 密码已经全部填满
     */
    public interface PasswordFullListener {

        void passwordFull(String password);
    }
}
