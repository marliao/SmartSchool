package com.marliao.smartschool.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.marliao.smartschool.bean.Grade;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GradeActivity extends AppCompatActivity {

    private MyHelper myHelper;
    private List<Grade> gradeList;
    private ImageView mIvBack;
    private ImageView mIvAdd;
    private ListView mLvGrade;
    private TextView mTvNoGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);
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
        //发布成绩
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
                    showAddNewsDialog();
                }
            }
        });
        //点击子条目查看成绩内容
        mLvGrade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grade grade = gradeList.get(position);
                showNoticeDetailDialog(grade, position + 1);
            }
        });
        //长按子条目删除
        mLvGrade.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
                    showDeleteDialog(gradeList.get(position));
                }
                return true;
            }
        });
    }

    private void showDeleteDialog(final Grade grade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("提示").setMessage("你确定要删除这条成绩？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Dao<Grade, ?> dao = myHelper.getDao(Grade.class);
                            dao.delete(grade);
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

    private void showNoticeDetailDialog(Grade grade, int ranking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_grade_detail, null);
        TextView mTvStudentId = view.findViewById(R.id.tv_student_id);
        TextView mTvStudentName = view.findViewById(R.id.tv_student_name);
        TextView mTvRanking = view.findViewById(R.id.tv__ranking);
        TextView mTvChinese = view.findViewById(R.id.tv_chinese);
        TextView mTvMath = view.findViewById(R.id.tv_math);
        TextView mTvEnglish = view.findViewById(R.id.tv_english);
        TextView mTvAllGrade = view.findViewById(R.id.tv_all_grade);
        Button mBtnDetamin = view.findViewById(R.id.btn_detamin);
        mTvStudentId.setText(grade.getStudent_id() + "");
        mTvStudentName.setText(grade.getStudent_name());
        mTvRanking.setText(ranking + "");
        mTvAllGrade.setText(grade.getAll_grade() + "");
        mTvChinese.setText(grade.getChinese() + "");
        mTvMath.setText(grade.getMath() + "");
        mTvEnglish.setText(grade.getEnglish() + "");
        if (grade.getChinese() < 60) {
            mTvChinese.setTextColor(Color.RED);
        } else {
            mTvChinese.setTextColor(Color.BLACK);
        }
        if (grade.getMath() < 60) {
            mTvMath.setTextColor(Color.RED);
        } else {
            mTvMath.setTextColor(Color.BLACK);
        }
        if (grade.getEnglish() < 60) {
            mTvEnglish.setTextColor(Color.RED);
        } else {
            mTvEnglish.setTextColor(Color.BLACK);
        }
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
        View view = View.inflate(this, R.layout.dialog_add_grade, null);
        TextView mTvGradeTitle = view.findViewById(R.id.tv_grade_title);
        final EditText mEtStudentId = view.findViewById(R.id.et_student_id);
        final EditText mEtStudentName = view.findViewById(R.id.et_student_name);
        final EditText mEtChinese = view.findViewById(R.id.et_chinese);
        final EditText mEtMath = view.findViewById(R.id.et_math);
        final EditText mEtEnglish = view.findViewById(R.id.et_english);
        final Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnSend = view.findViewById(R.id.btn_send);
        mTvGradeTitle.setText("上传成绩");
        mBtnSend.setText("上传");
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
                try {
                    String student_id = mEtStudentId.getText().toString().trim();
                    String student_name = mEtStudentName.getText().toString().trim();
                    String chinese = mEtChinese.getText().toString().trim();
                    String english = mEtEnglish.getText().toString().trim();
                    String math = mEtMath.getText().toString().trim();
                    boolean b = checkValues(student_id, student_name, chinese, english, math);
                    if (b) {
                        Grade grade = new Grade();
                        grade.setStudent_id(student_id);
                        grade.setStudent_name(student_name);
                        grade.setChinese(Integer.parseInt(chinese));
                        grade.setEnglish(Integer.parseInt(english));
                        grade.setMath(Integer.parseInt(math));
                        Dao<Grade, ?> dao = myHelper.getDao(Grade.class);
                        dao.create(grade);
                        initData();
                        dialog.dismiss();
                        T.showShort("上传成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkValues(String student_id, String student_name, String chinese, String english, String math) {
        if (TextUtils.isEmpty(student_id)) {
            T.showShort("学号不能为空");
            return false;
        }
        if (TextUtils.isEmpty(student_name)) {
            T.showShort("姓名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(chinese)) {
            T.showShort("语文成绩不能为空");
            return false;
        }
        if (TextUtils.isEmpty(english)) {
            T.showShort("英语成绩不能为空");
            return false;
        }
        if (TextUtils.isEmpty(math)) {
            T.showShort("数学成绩不能为空");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            Dao<Grade, ?> dao = myHelper.getDao(Grade.class);
            gradeList = dao.queryForAll();
            for (Grade grade : gradeList) {
                int all_grade = grade.getMath() + grade.getEnglish() + grade.getChinese();
                grade.setAll_grade(all_grade);
            }
            Collections.sort(gradeList, new Comparator<Grade>() {
                @Override
                public int compare(Grade o1, Grade o2) {
                    return o2.getAll_grade() - o1.getAll_grade();
                }
            });
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        if (gradeList.size() == 0) {
            mLvGrade.setVisibility(View.GONE);
            mTvNoGrade.setVisibility(View.VISIBLE);
        } else {
            mLvGrade.setVisibility(View.VISIBLE);
            mTvNoGrade.setVisibility(View.GONE);
        }
        MyAdapter myAdapter = new MyAdapter();
        mLvGrade.setAdapter(myAdapter);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);
        mLvGrade = (ListView) findViewById(R.id.lv_grade);
        mTvNoGrade = (TextView) findViewById(R.id.tv_no_grade);
        if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
            mIvAdd.setVisibility(View.VISIBLE);
        }else {
            mIvAdd.setVisibility(View.GONE);
        }
    }


    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return gradeList.size();
        }

        @Override
        public Grade getItem(int position) {
            return gradeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(GradeActivity.this, R.layout.item_grade, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position < 3) {
                viewHolder.tv_id.setTypeface(Typeface.DEFAULT_BOLD);
                viewHolder.tv_student_id.setTypeface(Typeface.DEFAULT_BOLD);
                viewHolder.tv_student_name.setTypeface(Typeface.DEFAULT_BOLD);
                viewHolder.tv_grade.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                viewHolder.tv_id.setTypeface(Typeface.DEFAULT);
                viewHolder.tv_student_id.setTypeface(Typeface.DEFAULT);
                viewHolder.tv_student_name.setTypeface(Typeface.DEFAULT);
                viewHolder.tv_grade.setTypeface(Typeface.DEFAULT);
            }
            final Grade item = getItem(position);
            viewHolder.tv_id.setText(position + 1 + "");
            viewHolder.tv_student_id.setText(item.getStudent_id() + "");
            viewHolder.tv_student_name.setText(item.getStudent_name());
            viewHolder.tv_grade.setText(item.getAll_grade() + "");
            if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
                viewHolder.iv_update.setVisibility(View.VISIBLE);
            }else {
                viewHolder.iv_update.setVisibility(View.GONE);
            }
            viewHolder.iv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)) {
                        showUpdateGradeDialog(item);
                    }
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_id;
            public TextView tv_student_id;
            public TextView tv_student_name;
            public TextView tv_grade;
            public ImageView iv_update;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_id = (TextView) rootView.findViewById(R.id.tv_id);
                this.tv_student_id = (TextView) rootView.findViewById(R.id.tv_student_id);
                this.tv_student_name = (TextView) rootView.findViewById(R.id.tv_student_name);
                this.tv_grade = (TextView) rootView.findViewById(R.id.tv_grade);
                this.iv_update = (ImageView) rootView.findViewById(R.id.iv_update);
            }

        }
    }

    private void showUpdateGradeDialog(final Grade grade) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_add_grade, null);
        TextView mTvGradeTitle = view.findViewById(R.id.tv_grade_title);
        final EditText mEtStudentId = view.findViewById(R.id.et_student_id);
        final EditText mEtStudentName = view.findViewById(R.id.et_student_name);
        final EditText mEtChinese = view.findViewById(R.id.et_chinese);
        final EditText mEtMath = view.findViewById(R.id.et_math);
        final EditText mEtEnglish = view.findViewById(R.id.et_english);
        final Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnSend = view.findViewById(R.id.btn_send);
        mTvGradeTitle.setText("修改成绩");
        mBtnSend.setText("修改");
        mEtStudentId.setText(grade.getStudent_id() + "");
        mEtStudentName.setText(grade.getStudent_name());
        mEtChinese.setText(grade.getChinese() + "");
        mEtMath.setText(grade.getMath() + "");
        mEtEnglish.setText(grade.getEnglish() + "");
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
                try {
                    String student_id = mEtStudentId.getText().toString().trim();
                    String student_name = mEtStudentName.getText().toString().trim();
                    String chinese = mEtChinese.getText().toString().trim();
                    String english = mEtEnglish.getText().toString().trim();
                    String math = mEtMath.getText().toString().trim();
                    boolean b = checkValues(student_id, student_name, chinese, english, math);
                    if (b) {
                        grade.setStudent_id(student_id);
                        grade.setStudent_name(student_name);
                        grade.setChinese(Integer.parseInt(chinese));
                        grade.setEnglish(Integer.parseInt(english));
                        grade.setMath(Integer.parseInt(math));
                        Dao<Grade, ?> dao = myHelper.getDao(Grade.class);
                        dao.update(grade);
                        initData();
                        dialog.dismiss();
                        T.showShort("修改成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
