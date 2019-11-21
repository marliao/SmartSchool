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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.bean.Club;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ClubActivity extends AppCompatActivity {

    private MyHelper myHelper;
    private List<Club> clubList;
    private ImageView mIvBack;
    private ImageView mIvAdd;
    private ListView mLvClub;
    private TextView mTvNoClub;
    private long datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);
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
        //发布活动
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewsDialog();
            }
        });
        //点击子条目查看活动内容
        mLvClub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Club club = clubList.get(position);
                showNoticeDetailDialog(club);
            }
        });
        //长按子条目删除
        mLvClub.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(clubList.get(position));
                return true;
            }
        });
    }

    private void showDeleteDialog(final Club club) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("提示").setMessage("你确定要删除这条活动？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Dao<Club, ?> dao = myHelper.getDao(Club.class);
                            dao.delete(club);
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

    private void showNoticeDetailDialog(Club club) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_club_detail, null);
        TextView mTvClubTitle = view.findViewById(R.id.tv_club_title);
        TextView mTvSendTime = view.findViewById(R.id.tv_send_time);
        TextView mTvDeadline = view.findViewById(R.id.tv__deadline);
        TextView mTvSendAccount = view.findViewById(R.id.tv_send_account);
        TextView mTvClubContent = view.findViewById(R.id.tv_club_content);
        Button mBtnDetamin = view.findViewById(R.id.btn_detamin);
        mTvClubTitle.setText(club.getTitle());
        mTvSendAccount.setText("发布者：" + club.getSend_user());
        String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(club.getTime()));
        String deadline = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(club.getDeadline()));
        mTvDeadline.setText("截止时间：" + deadline);
        mTvSendTime.setText("发布时间：" + date);
        mTvClubContent.setText("\t\t\t" + club.getContent());
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
        View view = View.inflate(this, R.layout.dialog_add_club, null);
        final EditText mEtTitle = view.findViewById(R.id.et_title);
        final EditText mEtContent = view.findViewById(R.id.et_content);
        final TextView mTvDeadline = view.findViewById(R.id.tv__deadline);
        Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnSend = view.findViewById(R.id.btn_send);
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mTvDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatepickerDialog(mTvDeadline);
            }
        });
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
                if (datetime > System.currentTimeMillis()) {
                    if (b) {
                        try {
                            Club club = new Club();
                            club.setContent(content);
                            club.setTitle(title);
                            club.setDeadline(datetime);
                            club.setSend_user((String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin"));
                            club.setTime(System.currentTimeMillis());
                            Dao<Club, ?> dao = myHelper.getDao(Club.class);
                            dao.create(club);
                            dialog.dismiss();
                            T.showShort("发布成功");
                            initData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    T.showShort("截止日期必须大于当前时间");
                }
            }
        });

    }

    private void showDatepickerDialog(final TextView mTvDeadline) {
        datetime = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_date_picker, null);
        final DatePicker mDatepicker = view.findViewById(R.id.datepicker);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int year = mDatepicker.getYear();
                    int month = mDatepicker.getMonth() + 1;
                    int day = mDatepicker.getDayOfMonth();
                    String sMonth, sDay;
                    if (month < 10) {
                        sMonth = "0" + month;
                    } else {
                        sMonth = "" + month;
                    }
                    if (day < 10) {
                        sDay = "0" + day;
                    } else {
                        sDay = "" + day;
                    }
                    String date = year + "." + sMonth + "." + sDay + " 00:00:00";
                    mTvDeadline.setText(date);
                    datetime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").parse(date).getTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private boolean checkValue(String title, String content) {
        if (TextUtils.isEmpty(title)) {
            T.showShort("活动标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            T.showShort("活动内容不能为空");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            Dao<Club, ?> dao = myHelper.getDao(Club.class);
            clubList = dao.queryForAll();
            Collections.reverse(clubList);
            long cuttentTime = System.currentTimeMillis();
            for (Club club : clubList) {
                if (club.getDeadline() < cuttentTime) {
                    dao.delete(club);
                }
            }
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (clubList.size() == 0) {
            mLvClub.setVisibility(View.GONE);
            mTvNoClub.setVisibility(View.VISIBLE);
        } else {
            mLvClub.setVisibility(View.VISIBLE);
            mTvNoClub.setVisibility(View.GONE);
        }
        MyAdapter myAdapter = new MyAdapter();
        mLvClub.setAdapter(myAdapter);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mLvClub = (ListView) findViewById(R.id.lv_club);
        mTvNoClub = (TextView) findViewById(R.id.tv_no_club);
    }


    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return clubList.size();
        }

        @Override
        public Club getItem(int position) {
            return clubList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(ClubActivity.this, R.layout.item_notice, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Club item = getItem(position);
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
