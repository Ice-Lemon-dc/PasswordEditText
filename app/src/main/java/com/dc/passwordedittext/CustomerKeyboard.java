package com.dc.passwordedittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class CustomerKeyboard extends LinearLayout implements View.OnClickListener {

    /**
     * 设置点击回掉监听
     */
    private CustomerKeyboardClickListener mListener;

    public CustomerKeyboard(Context context) {
        this(context, null);
    }

    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomerKeyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_customer_keyboard, this);

        setItemClickListener(this);
    }

    /**
     * 设置子View的点击事件
     *
     * @param view View
     */
    private void setItemClickListener(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = viewGroup.getChildAt(i);
                setItemClickListener(childView);
            }
        } else {
            view.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof TextView) {
            String number = ((TextView)v).getText().toString().trim();
            if (mListener != null) {
                mListener.click(number);
            }
        }
        if (v instanceof ImageView) {
            if (mListener != null) {
                mListener.delete();
            }
        }
    }

    public void setOnCustomerKeyboardClickListener(CustomerKeyboardClickListener listener){
        this.mListener = listener;
    }


    /**
     * 点击键盘的回调监听
     */
    public interface CustomerKeyboardClickListener {
        void click(String number);
        void delete();
    }
}
