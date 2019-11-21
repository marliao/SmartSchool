package com.marliao.smartschool.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.bean.Complaint;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ComplaintActivity extends AppCompatActivity {

    private MyHelper myHelper;
    private List<Complaint> complaintList;
    private ImageView mIvBack;
    private ImageView mIvAdd;
    private ListView mLvNotice;
    private TextView mTvNoComplaint;
    private List<Complaint> currentcomplaintlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
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
        //发布投诉
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewsDialog();
            }
        });
        //点击子条目查看投诉内容
        mLvNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Complaint news = complaintList.get(position);
                showNoticeDetailDialog(news);
            }
        });
        //长按子条目删除
        mLvNotice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
                    showDeleteDialog(complaintList.get(position));
                }
                return true;
            }
        });
    }

    private void showDeleteDialog(final Complaint complaint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("提示").setMessage("你确定要删除这条投诉？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Dao<Complaint, ?> dao = myHelper.getDao(Complaint.class);
                            dao.delete(complaint);
                            T.showShort("删除成功");
                            dialog.dismiss();
                            initData();
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

    private void showNoticeDetailDialog(Complaint complaint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_complaint_detail, null);
        TextView mTvNoticeTitle = view.findViewById(R.id.tv_notice_title);
        TextView mTvSendTime = view.findViewById(R.id.tv_send_time);
        TextView mTvSendAccount = view.findViewById(R.id.tv_send_account);
        TextView mTvNoticeContent = view.findViewById(R.id.tv_notice_content);
        Button mBtnDetamin = view.findViewById(R.id.btn_detamin);
        mTvNoticeTitle.setText(complaint.getTitle());
        mTvSendAccount.setText("发布者：" + complaint.getSend_user());
        String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(complaint.getTime()));
        mTvSendTime.setText("发布时间：" + date);
        mTvNoticeContent.setText("\t\t\t" + complaint.getContent());
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
        View view = View.inflate(this, R.layout.dialog_add_compalint, null);
        final Spinner spinner_type = view.findViewById(R.id.spinner_type);
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
                String title = (String) spinner_type.getSelectedItem();
                String content = mEtContent.getText().toString().trim();
                boolean b = checkValue(title, content);
                if (b) {
                    try {
                        Complaint complaint = new Complaint();
                        complaint.setContent(content);
                        complaint.setTitle(title);
                        complaint.setSend_user((String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin"));
                        complaint.setTime(System.currentTimeMillis());
                        Dao<Complaint, ?> dao = myHelper.getDao(Complaint.class);
                        dao.create(complaint);
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
            T.showShort("投诉类型不能为空");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            T.showShort("投诉内容不能为空");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            Dao<Complaint, ?> dao = myHelper.getDao(Complaint.class);
            complaintList = dao.queryForAll();
            Collections.reverse(complaintList);
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (complaintList.size() == 0) {
            mLvNotice.setVisibility(View.GONE);
            mTvNoComplaint.setVisibility(View.VISIBLE);
        } else {
            mLvNotice.setVisibility(View.VISIBLE);
            mTvNoComplaint.setVisibility(View.GONE);
        }
        currentcomplaintlist = new ArrayList<>();
        if (!(boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
            for (Complaint complaint : complaintList) {
                if (complaint.getSend_user().equals((String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin"))) {
                    currentcomplaintlist.add(complaint);
                }
            }
        } else {
            currentcomplaintlist = complaintList;
        }
        MyAdapter myAdapter = new MyAdapter();
        mLvNotice.setAdapter(myAdapter);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mLvNotice = (ListView) findViewById(R.id.lv_notice);
        mTvNoComplaint = (TextView) findViewById(R.id.tv_no_complaint);
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return currentcomplaintlist.size();
        }

        @Override
        public Complaint getItem(int position) {
            return currentcomplaintlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(ComplaintActivity.this, R.layout.item_notice, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Complaint item = getItem(position);
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
