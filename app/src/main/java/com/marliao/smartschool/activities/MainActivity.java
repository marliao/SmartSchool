package com.marliao.smartschool.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mLlLogin;
    private LinearLayout mLlNotice;
    private LinearLayout mLlNews;
    private LinearLayout mLlComplaint;
    private LinearLayout mLlGrade;
    private LinearLayout mLlrecipe;
    private LinearLayout mLlClubActivity;
    private LinearLayout mLlUsedMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        mLlLogin.setOnClickListener(this);
        mLlNotice.setOnClickListener(this);
        mLlNews.setOnClickListener(this);
        mLlComplaint.setOnClickListener(this);
        mLlGrade.setOnClickListener(this);
        mLlrecipe.setOnClickListener(this);
        mLlClubActivity.setOnClickListener(this);
        mLlUsedMarket.setOnClickListener(this);
    }

    private void initView() {
        mLlLogin = (LinearLayout) findViewById(R.id.ll_login);
        mLlNotice = (LinearLayout) findViewById(R.id.ll_notice);
        mLlNews = (LinearLayout) findViewById(R.id.ll_news);
        mLlComplaint = (LinearLayout) findViewById(R.id.ll_complaint);
        mLlGrade = (LinearLayout) findViewById(R.id.ll_grade);
        mLlrecipe = (LinearLayout) findViewById(R.id.ll__recipe);
        mLlClubActivity = (LinearLayout) findViewById(R.id.ll_club_activity);
        mLlUsedMarket = (LinearLayout) findViewById(R.id.ll_used_market);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SpUtils.put(AppClient.mContext, SpUtils.ISLOGIN, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_login://登录
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.ll_notice://公告
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, NoticeActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll_news://新闻
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, NewsActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll_complaint://投诉
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, ComplaintActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll_grade://成绩
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, GradeActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll__recipe://菜谱
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, RecipeActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll_club_activity://社团活动
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, ClubActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
            case R.id.ll_used_market://二手市场
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ISLOGIN, false)) {
                    startActivity(new Intent(this, UserdMarketActivity.class));
                }else {
                    T.showShort("请登录");
                }
                break;
        }
    }
}
