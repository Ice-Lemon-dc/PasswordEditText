package com.dc.passwordedittext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dc.passwordedittext.dialog.AlertDialog;

public class MainActivity extends AppCompatActivity implements CustomerKeyboard.CustomerKeyboardClickListener, PasswordEditText.PasswordFullListener, View.OnClickListener {

    private ImageView mImageView;

    private CustomerKeyboard mCustomerKeyboard;

    private PasswordEditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.image);
        mImageView.setOnClickListener(this);

    }

    @Override
    public void click(String number) {
        mPasswordEditText.addPassword(number);
    }

    @Override
    public void delete() {
        mPasswordEditText.deleteLastPassword();
    }

    @Override
    public void passwordFull(String password) {
        // 去后台校验密码
        Toast.makeText(this, password, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setContentView(R.layout.dialog_customer_keyboard).fromBottom(true).fullWidth().show();
        mPasswordEditText =  dialog.getView(R.id.password_edit_text);
        mCustomerKeyboard = dialog.getView(R.id.custom_key_board);
        mCustomerKeyboard.setOnCustomerKeyboardClickListener(this);

        // 设置不可dia点击禁用系统键
        mPasswordEditText.setEnabled(false);
        mPasswordEditText.setOnPasswordFullListener(this);
    }
}
