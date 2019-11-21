package com.marliao.smartschool.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.bean.News;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private MyHelper myHelper;
    private List<News> newsList;
    private ImageView mIvBack;
    private ImageView mIvAdd;
    private ListView mLvNotice;
    private TextView mTvNoNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        myHelper = MyHelper.getInstance();
        initData();
        initListener();
    }

    private void initListener() {
        //返回主页面
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //发布新闻
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewsDialog();
            }
        });
        //点击子条目查看新闻内容
        mLvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News news = newsList.get(position);
                showNoticeDetailDialog(news);
            }
        });
        //长按子条目删除
        mLvNotice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(newsList.get(position));
                return true;
            }
        });
    }

    private void showDeleteDialog(final News news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("提示").setMessage("你确定要删除这条新闻？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //判断是否为管理员
                            if (((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false))) {
                                Dao<News, ?> dao = myHelper.getDao(News.class);
                                dao.delete(news);
                                T.showShort("删除成功");
                                dialog.dismiss();
                                initData();
                            } else {
                                String account = (String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin");
                                //判断新闻是否是登录用户发布的
                                if (account.equals(news.getSend_user())) {
                                    Dao<News, ?> dao = myHelper.getDao(News.class);
                                    dao.delete(news);
                                    T.showShort("删除成功");
                                    dialog.dismiss();
                                    initData();
                                } else {
                                    T.showShort("只能删除自己发布的新闻");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void showNoticeDetailDialog(News news) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_news_detail, null);
        TextView mTvNoticeTitle = view.findViewById(R.id.tv_notice_title);
        TextView mTvSendTime = view.findViewById(R.id.tv_send_time);
        TextView mTvSendAccount = view.findViewById(R.id.tv_send_account);
        TextView mTvNoticeContent = view.findViewById(R.id.tv_notice_content);
        Button mBtnDetamin = view.findViewById(R.id.btn_detamin);
        mTvNoticeTitle.setText(news.getTitle());
        mTvSendAccount.setText("发布者：" + news.getSend_user());
        String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(news.getTime()));
        mTvSendTime.setText("发布时间：" + date);
        mTvNoticeContent.setText("\t\t\t" + news.getContent());
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mBtnDetamin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showAddNewsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_news, null);
        final EditText mEtTitle = view.findViewById(R.id.et_title);
        final EditText mEtContent = view.findViewById(R.id.et_content);
        Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnSend = view.findViewById(R.id.btn_send);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mEtTitle.getText().toString().trim();
                String content = mEtContent.getText().toString().trim();
                boolean b = checkValue(title, content);
                if (b) {
                    try {
                        News news = new News();
                        news.setContent(content);
                        news.setTitle(title);
                        news.setSend_user((String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin"));
                        news.setTime(System.currentTimeMillis());
                        Dao<News, ?> dao = myHelper.getDao(News.class);
                        dao.create(news);
                        dialog.dismiss();
                        T.showShort("发布成功");
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private boolean checkValue(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            T.showShort("新闻标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            T.showShort("新闻内容不能为空");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            Dao<News, ?> dao = myHelper.getDao(News.class);
            newsList = dao.queryForAll();
            Collections.reverse(newsList);
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (newsList.size() == 0) {
            mLvNotice.setVisibility(View.GONE);
            mTvNoNew.setVisibility(View.VISIBLE);
        } else {
            mLvNotice.setVisibility(View.VISIBLE);
            mTvNoNew.setVisibility(View.GONE);
        }
        MyAdapter myAdapter = new MyAdapter();
        mLvNotice.setAdapter(myAdapter);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mLvNotice = (ListView) findViewById(R.id.lv_notice);
        mTvNoNew = (TextView) findViewById(R.id.tv_no_new);
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public News getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(NewsActivity.this, R.layout.item_notice, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            News item = getItem(position);
            viewHolder.tv_id.setText(position + 1 + "");
            viewHolder.tv_notice_title.setText(item.getTitle());
            viewHolder.tv_notice_content.setText(item.getContent());
            String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date(item.getTime()));
            viewHolder.tv_notice_time.setText(date + "");
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_id;
            public TextView tv_notice_title;
            public TextView tv_notice_content;
            public TextView tv_notice_time;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_id = (TextView) rootView.findViewById(R.id.tv_id);
                this.tv_notice_title = (TextView) rootView.findViewById(R.id.tv_notice_title);
                this.tv_notice_content = (TextView) rootView.findViewById(R.id.tv_notice_content);
                this.tv_notice_time = (TextView) rootView.findViewById(R.id.tv_notice_time);
            }

        }
    }

}
