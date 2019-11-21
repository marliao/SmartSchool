package com.marliao.smartschool.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.bean.User;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mIvBack;
    private EditText mEtAccount;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private TextView mTvRegister;
    private Dao<User, ?> dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }

    private void initData() {
        try {
            dao = MyHelper.getInstance().getDao(User.class);
            List<User> query = dao.queryBuilder().where().eq("account", "admin").query();
            if (query.size() == 0) {
                User user = new User();
                user.setAccount("admin");
                user.setPassword("admin");
                dao.create(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mEtAccount = (EditText) findViewById(R.id.et_account);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mTvRegister = (TextView) findViewById(R.id.tv_register);
        mTvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_login:
                submit();
                break;
            case R.id.tv_register:
                showRegisterDialog();
                break;
        }
    }

    private void showRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_register, null);
        final EditText mEtAccount = view.findViewById(R.id.et_account);
        final EditText mEtPassword = view.findViewById(R.id.et_password);
        final EditText mEtPasswordAgain = view.findViewById(R.id.et_password_again);
        Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnLogin = view.findViewById(R.id.btn_login);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String account = mEtAccount.getText().toString().trim();
                    String password = mEtPassword.getText().toString().trim();
                    String password_again = mEtPasswordAgain.getText().toString().trim();
                    boolean b = checkRegisterValues(account, password, password_again);
                    if (b) {
                        List<User> query = dao.queryBuilder().where().eq("account", account).query();
                        if (query.size() == 0) {
                            User user = new User();
                            user.setAccount(account);
                            user.setPassword(password);
                            dao.create(user);
                            dialog.dismiss();
                            T.showShort("注册成功");
                        } else {
                            T.showShort("该用户已存在");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkRegisterValues(String account, String password, String password_again) {
        if (TextUtils.isEmpty(account)) {
            T.showShort("账户不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            T.showShort("密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(password_again)) {
            T.showShort("密码不能为空");
            return false;
        }
        if (password.equals(password_again)) {
            return true;
        } else {
            T.showShort("密码不一致，请重新输入密码");
            return false;
        }
    }

    private void submit() {
        // validate
        try {
            String account = mEtAccount.getText().toString().trim();
            if (TextUtils.isEmpty(account)) {
                Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            String password = mEtPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            List<User> query = dao.queryBuilder().where().eq("account", account).query();
            User user = query.get(0);
            if (user.getAccount().equals(account)) {
                if (user.getPassword().equals(password)) {
                    if (account.equals("admin")) {
                        SpUtils.put(AppClient.mContext, SpUtils.ADMIN, true);
                    } else {
                        SpUtils.put(AppClient.mContext, SpUtils.ADMIN, false);
                    }
                    SpUtils.put(AppClient.mContext, SpUtils.ACCOUNT, account);
                    SpUtils.put(AppClient.mContext, SpUtils.ISLOGIN, true);
                    finish();
                    T.showShort("登录成功");
                } else {
                    SpUtils.put(AppClient.mContext, SpUtils.ISLOGIN, false);
                    T.showShort("密码错误");
                }
            } else {
                SpUtils.put(AppClient.mContext, SpUtils.ISLOGIN, false);
                T.showShort("账号错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
