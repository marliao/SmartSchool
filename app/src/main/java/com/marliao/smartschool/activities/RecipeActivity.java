package com.marliao.smartschool.activities;

import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.R;
import com.marliao.smartschool.bean.Recipe;
import com.marliao.smartschool.utils.MyHelper;
import com.marliao.smartschool.utils.SpUtils;
import com.marliao.smartschool.utils.T;

import java.util.List;

public class RecipeActivity extends AppCompatActivity {

    private ImageView mIvBack;
    private ListView mLvRecipe;
    private Dao<Recipe, ?> dao;
    private List<Recipe> recipeList;
    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        initView();
        initData();
        initlistener();
    }

    private void initlistener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLvRecipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if ((boolean) SpUtils.get(AppClient.mContext, SpUtils.ADMIN, false)){
                    Recipe recipe = new Recipe();
                    for (Recipe item : recipeList) {
                        if (item.getWeek() == position) {
                            recipe = item;
                        }
                    }
                    recipe.setWeek(position);
                    String week = getWeek(position);
                    showUpdateRecipeDialog(recipe, week);
                }
                return true;
            }
        });
    }

    private void showUpdateRecipeDialog(final Recipe recipe, String week) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_update_recipe, null);
        TextView mTvWeek = view.findViewById(R.id.tv_week);
        mTvWeek.setText(week + "菜谱");
        final EditText mEtBreakfast = view.findViewById(R.id.et_breakfast);
        final EditText mEtLaunch = view.findViewById(R.id.et_launch);
        final EditText mEtDinner = view.findViewById(R.id.et_dinner);
        Button mBtnCancel = view.findViewById(R.id.btn_cancel);
        Button mBtnUpdate = view.findViewById(R.id.btn_update);
        i = 0;
        if (recipe.getBreakfast() != null) {
            mEtBreakfast.setText(recipe.getBreakfast());
        } else {
            i++;
        }
        if (recipe.getLaunch() != null) {
            mEtLaunch.setText(recipe.getLaunch());
        } else {
            i++;
        }
        if (recipe.getDinner() != null) {
            mEtDinner.setText(recipe.getDinner());
        } else {
            i++;
        }
        dialog.setView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String breakfast = mEtBreakfast.getText().toString().trim();
                    String launch = mEtLaunch.getText().toString().trim();
                    String dinner = mEtDinner.getText().toString().trim();
                    boolean b = checkValues(breakfast, launch, dinner);
                    if (b) {
                        recipe.setBreakfast(breakfast);
                        recipe.setLaunch(launch);
                        recipe.setDinner(dinner);
                        if (i == 3) {
                            dao.create(recipe);
                        } else {
                            dao.update(recipe);
                        }
                        dialog.dismiss();
                        initData();
                        T.showShort("修改成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean checkValues(String breakfast, String launch, String dinner) {
        if (TextUtils.isEmpty(breakfast)) {
            T.showShort("请输入早餐");
            return false;
        }
        if (TextUtils.isEmpty(launch)) {
            T.showShort("请输入午餐");
            return false;
        }
        if (TextUtils.isEmpty(dinner)) {
            T.showShort("请输入晚餐");
            return false;
        }
        return true;
    }

    private void initData() {
        try {
            dao = MyHelper.getInstance().getDao(Recipe.class);
            recipeList = dao.queryForAll();
            initAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAdapter() {
        MyAdapter myAdapter = new MyAdapter();
        mLvRecipe.setAdapter(myAdapter);
    }

    public class MyAdapter extends BaseAdapter {

        private ViewHolder viewHolder;

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Recipe getItem(int position) {
            return recipeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(RecipeActivity.this, R.layout.item_recipe, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (position % 2 == 0) {
                viewHolder.ll_recipe.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                viewHolder.ll_recipe.setBackgroundColor(Color.WHITE);
            }
            String week = getWeek(position);
            viewHolder.tv_week_day.setText(week);
            viewHolder.tv_breakfast.setText("——");
            viewHolder.tv_launch.setText("——");
            viewHolder.tv_dinner.setText("——");
            for (Recipe recipe : recipeList) {
                if (recipe.getWeek() == position) {
                    if (recipe.getBreakfast() != null) {
                        viewHolder.tv_breakfast.setText(recipe.getBreakfast());
                    } else {
                        viewHolder.tv_breakfast.setText("——");
                    }
                    if (recipe.getLaunch() != null) {
                        viewHolder.tv_launch.setText(recipe.getLaunch());
                    } else {
                        viewHolder.tv_launch.setText("——");
                    }
                    if (recipe.getDinner() != null) {
                        viewHolder.tv_dinner.setText(recipe.getDinner());
                    } else {
                        viewHolder.tv_dinner.setText("——");
                    }
                    break;
                }
            }
            return convertView;
        }

        public class ViewHolder {
            public View rootView;
            public TextView tv_week_day;
            public TextView tv_breakfast;
            public TextView tv_launch;
            public TextView tv_dinner;
            public LinearLayout ll_recipe;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.tv_week_day = (TextView) rootView.findViewById(R.id.tv_week_day);
                this.tv_breakfast = (TextView) rootView.findViewById(R.id.tv_breakfast);
                this.tv_launch = (TextView) rootView.findViewById(R.id.tv_launch);
                this.tv_dinner = (TextView) rootView.findViewById(R.id.tv_dinner);
                this.ll_recipe = (LinearLayout) rootView.findViewById(R.id.ll_recipe);
            }

        }
    }

    private String getWeek(int position) {
        switch (position) {
            case 0:
                return "周一";
            case 1:
                return "周二";
            case 2:
                return "周三";
            case 3:
                return "周四";
            case 4:
                return "周五";
            case 5:
                return "周六";
            case 6:
                return "周日";
            default:
                return "周一";
        }
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mLvRecipe = (ListView) findViewById(R.id.lv_recipe);
    }
}
