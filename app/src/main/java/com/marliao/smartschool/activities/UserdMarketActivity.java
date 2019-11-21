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
import com.marliao.smartschool.bean.Club;
import com.marliao.smartschool.bean.Market;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class UserdMarketActivity extends AppCompatActivity {

    private MyHelper myHelper;
    private List<Market> marketList;
    private ImageView mIvBack;
    private ImageView mIvAdd;
    private ListView mLvUsedMarket;
    private TextView mTvNoUsedMarket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_market);
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
        //发布商品
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewsDialog();
            }
        });
        //点击子条目查看商品内容
        mLvUsedMarket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Market market = marketList.get(position);
                showNoticeDetailDialog(market);
            }
        });
        //长按子条目删除
        mLvUsedMarket.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(marketList.get(position));
                return true;
            }
        });
    }

    private void showDeleteDialog(final Market market) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("提示").setMessage("你确定要删除这条商品？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Dao<Market, ?> dao = myHelper.getDao(Market.class);
                            dao.delete(market);
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

    private void showNoticeDetailDialog(Market market) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_product_detail, null);
        TextView mTvSendTime = view.findViewById(R.id.tv_send_time);
        TextView mTvSendAccount = view.findViewById(R.id.tv_send_account);
        TextView mTvTitle = view.findViewById(R.id.tv_title);
        TextView mTvContent = view.findViewById(R.id.tv_content);
        TextView mTvPrice = view.findViewById(R.id.tv_price);
        TextView mTvPhoneNumber = view.findViewById(R.id.tv_phone_number);
        Button mBtnSend = view.findViewById(R.id.btn_send);
        mTvSendAccount.setText("发布者：" + market.getSend_user());
        String date = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date(market.getTime()));
        mTvSendTime.setText("发布时间：" + date);
        mTvContent.setText("\t\t\t" + market.getDiscription());
        mTvTitle.setText(market.getProduct_name());
        mTvPrice.setText(market.getPrice() + "￥");
        mTvPhoneNumber.setText(market.getPhone_number() + "");
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showAddNewsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_product, null);
        final EditText mEtTitle = view.findViewById(R.id.et_title);
        final EditText mEtContent = view.findViewById(R.id.et_content);
        final EditText mEtPrice = view.findViewById(R.id.et_price);
        final EditText mEtPhoneNumber = view.findViewById(R.id.et_phone_number);
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
                String price = mEtPrice.getText().toString().trim();
                String phoneNumber = mEtPhoneNumber.getText().toString().trim();
                boolean b = checkValue(title, content, price, phoneNumber);
                if (b) {
                    try {
                        Market market = new Market();
                        market.setPrice(Integer.parseInt(price));
                        market.setPhone_number(phoneNumber);
                        market.setDiscription(content);
                        market.setProduct_name(title);
                        market.setSend_user((String) SpUtils.get(AppClient.mContext, SpUtils.ACCOUNT, "admin"));
                        market.setTime(System.currentTimeMillis());
                        Dao<Market, ?> dao = myHelper.getDao(Market.class);
                        dao.create(market);
                        dialog.dismiss();
                        T.showShort("上传成功");
                        initData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private boolean checkValue(String title, String content, String price, String phoneNumber) {
        if (TextUtils.isEmpty(title)) {
            T.showShort("商品名称不能为空");
            return false;
        }
        if (TextUtils.isEmpty(content)) {
            T.showShort("商品描述不能为空");
            return false;
        }
        if (TextUtils.isEmpty(price)) {
            T.showShort("商品价格不能为空");
            return false;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            T.showShort("联系电话不能为空");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            Dao<Market, ?> dao = myHelper.getDao(Market.class);
            marketList = dao.queryForAll();
            Collections.reverse(marketList);
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (marketList.size() == 0) {
            mLvUsedMarket.setVisibility(View.GONE);
            mTvNoUsedMarket.setVisibility(View.VISIBLE);
        } else {
            mLvUsedMarket.setVisibility(View.VISIBLE);
            mTvNoUsedMarket.setVisibility(View.GONE);
        }
        MyAdapter myAdapter = new MyAdapter();
        mLvUsedMarket.setAdapter(myAdapter);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mLvUsedMarket = (ListView) findViewById(R.id.lv_used_market);
        mTvNoUsedMarket = (TextView) findViewById(R.id.tv_no_used_market);
    }


    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return marketList.size();
        }

        @Override
        public Market getItem(int position) {
            return marketList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(UserdMarketActivity.this, R.layout.item_notice, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Market item = getItem(position);
            viewHolder.tv_id.setText(position + 1 + "");
            viewHolder.tv_notice_title.setText(item.getProduct_name());
            viewHolder.tv_notice_content.setText(item.getDiscription());
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
