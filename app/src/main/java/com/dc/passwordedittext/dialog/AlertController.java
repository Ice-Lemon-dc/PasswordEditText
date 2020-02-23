package com.dc.passwordedittext.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

class AlertController {

    private AlertDialog mDialog;
    private Window mWindow;

    private DialogViewHelper mViewHelper;

    AlertController(AlertDialog dialog, Window window) {
        this.mDialog = dialog;
        this.mWindow = window;
    }

    private void setViewHelper(DialogViewHelper viewHelper) {
        this.mViewHelper = viewHelper;
    }

    /**
     * 设置文本
     *
     * @param viewId int
     * @param text CharSequence
     */
    void setText(int viewId, CharSequence text) {
        mViewHelper.setText(viewId, text);
    }

    <T extends View> T getView(int viewId) {
        return mViewHelper.getView(viewId);
    }

    /**
     * 设置点击事件
     *
     * @param viewId int
     * @param listener OnClickListener
     */
    void setOnclickListener(int viewId, View.OnClickListener listener) {
        mViewHelper.setOnclickListener(viewId, listener);
    }

    /**
     * 获取Dialog
     *
     * @return
     */
    private AlertDialog getDialog() {
        return mDialog;
    }

    /**
     * 获取Dialog的Window
     *
     * @return
     */
    private Window getWindow() {
        return mWindow;
    }

    static class AlertParams {
        Context mContext;
        int mThemeResId;
        /**
         * 点击空白是否能够取消  默认点击阴影可以取消
         */
        boolean mCancelable = true;
        /**
         * dialog Cancel监听
         */
        DialogInterface.OnCancelListener mOnCancelListener;
        /**
         * dialog Dismiss监听
         */
        DialogInterface.OnDismissListener mOnDismissListener;
        /**
         * dialog Key监听
         */
        DialogInterface.OnKeyListener mOnKeyListener;
        View mView;
        /**
         * 布局layout id
         */
        int mViewLayoutResId;
        /**
         * 存放字体的修改
         */
        SparseArray<CharSequence> mTextArray = new SparseArray<>();
        /**
         * 存放点击事件
         */
        SparseArray<View.OnClickListener> mClickArray = new SparseArray<>();
        /**
         * 宽度
         */
        int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * 动画
         */
        int mAnimations = 0;
        /**
         * 位置
         */
        int mGravity = Gravity.CENTER;
        /**
         * 高度
         */
        int mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;

        AlertParams(Context context, int themeResId) {
            this.mContext = context;
            this.mThemeResId = themeResId;
        }

        /**
         * 绑定和设置参数
         *
         * @param mAlert AlertController
         */
        void apply(AlertController mAlert) {
            // 完善这个地方 设置参数

            // 1. 设置Dialog布局  DialogViewHelper
            DialogViewHelper viewHelper = null;
            if (mViewLayoutResId != 0) {
                viewHelper = new DialogViewHelper(mContext, mViewLayoutResId);
            }

            if (mView != null) {
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if (viewHelper == null) {
                throw new IllegalArgumentException("请设置布局setContentView()");
            }

            // 给Dialog 设置布局
            mAlert.getDialog().setContentView(viewHelper.getContentView());

            // 设置 Controller的辅助类
            mAlert.setViewHelper(viewHelper);

            // 2.设置文本
            int textArraySize = mTextArray.size();
            for (int i = 0; i < textArraySize; i++) {
                mAlert.setText(mTextArray.keyAt(i), mTextArray.valueAt(i));
            }


            // 3.设置点击
            int clickArraySize = mClickArray.size();
            for (int i = 0; i < clickArraySize; i++) {
                mAlert.setOnclickListener(mClickArray.keyAt(i), mClickArray.valueAt(i));
            }

            // 4.配置自定义的效果  全屏  从底部弹出    默认动画
            Window window = mAlert.getWindow();
            // 设置位置
            window.setGravity(mGravity);

            // 设置动画
            if (mAnimations != 0) {
                window.setWindowAnimations(mAnimations);
            }

            // 设置宽高
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
