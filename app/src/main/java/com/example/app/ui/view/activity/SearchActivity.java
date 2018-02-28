package com.example.app.ui.view.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.base.BaseActivity;
import com.example.app.presenter.SearchPresenter;
import com.example.app.ui.ia.SearchViewIA;

import java.util.List;

/**
 * Package: com.example.app.module.user.view
 * Class: SearchActivity
 * Description: 搜索界面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class SearchActivity extends BaseActivity implements SearchViewIA {
    private TextView mTvShow;
    private SearchPresenter presenter;

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user);
        initActionBar(getString(R.string.action_settings), true);
        mTvShow = (TextView) findViewById(R.id.tv_show);
        mTvShow.setText("test");
    }

    @Override
    protected void initPresenter() {
        presenter = new SearchPresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        presenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        presenter.doRequest("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_child, menu);
        //初始化搜索框
        MenuItem homeItem = menu.findItem(R.id.action_home);
        homeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                finish();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void notifySearch(List<String> list) {

    }
}
