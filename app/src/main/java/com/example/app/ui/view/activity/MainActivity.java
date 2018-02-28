package com.example.app.ui.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.app.R;
import com.example.app.base.BaseActivity;
import com.example.app.presenter.MainPresenter;
import com.example.app.ui.ia.MainViewIA;
import com.example.app.ui.view.fragment.ChartFragment;
import com.example.app.ui.view.fragment.HomeFragment;
import com.example.app.ui.view.fragment.RecyclerGridFragment;
import com.example.app.ui.view.fragment.WaterfallFragment;
import com.example.app.ui.view.fragment.WeatherListFragment;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Package: com.example.app.module.main.view
 * Class: MainActivity
 * Description: 主界面视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class MainActivity extends BaseActivity implements MainViewIA {
    private Context context = this;
    //点击返回按钮计数器，连续点击两次则退出应用
    private long mExitTime;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private FrameLayout container;
    private MainPresenter presenter;

    @Override
    protected void onCreateImpl(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        initActionBar(getString(R.string.app_name), false);
        container = (FrameLayout) findViewById(R.id.container);
        initBottomNavigationBar();
        /*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);*/
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initPresenter() {
        presenter = new MainPresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        presenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        presenter.getCodelist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //初始化搜索框
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = searchView.getQuery().toString();
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // search goes here !!
                // listAdapter.getFilter().filter(query);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initBottomNavigationBar() {
        final String[] menuNames = new String[]{getString(R.string.menu0), getString(R.string.menu1), getString(R.string.menu2), getString(R.string.menu3)};

        BadgeItem badgeItem = new BadgeItem();
        badgeItem.setHideOnSelect(false)
                .setText("10")
                .setBackgroundColorResource(R.color.badge_background)
                .setBorderWidth(0);

        BottomNavigationBar mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_launcher, menuNames[0]).setInactiveIconResource(R.drawable.ic_empty).setActiveColorResource(R.color.menu_background).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher, menuNames[1]).setInactiveIconResource(R.drawable.ic_empty).setActiveColorResource(R.color.menu_background))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher, menuNames[2]).setInactiveIconResource(R.drawable.ic_empty).setActiveColorResource(R.color.menu_background))
                .addItem(new BottomNavigationItem(R.drawable.ic_launcher, menuNames[3]).setInactiveIconResource(R.drawable.ic_empty).setActiveColorResource(R.color.menu_background))//依次添加item,分别icon和名称
                .setFirstSelectedPosition(0)//设置默认选择item
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE)
                .initialise();//初始化
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                if (position == 0) {
                    hideActionBar();
                } else {
                    showActionBar(menuNames[position]);
                }
                String tag = String.valueOf(position);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment newFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (newFragment == null) {
                    switch (position) {
                        case 0:
                            newFragment = HomeFragment.newInstance();
                            break;
                        case 1:
                            newFragment = ChartFragment.newInstance();
                            break;
                        case 2:
                            newFragment = WaterfallFragment.newInstance();
                            break;
                        case 3:
                            newFragment = WeatherListFragment.newInstance();
                            break;
                    }
                    ft.add(R.id.container, newFragment, tag);
                } else {
                    ft.show(newFragment);
                }
                ft.commitAllowingStateLoss();
                ft = null;
            }

            @Override
            public void onTabUnselected(int position) {
                String tag = String.valueOf(position);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment newFragment = (Fragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (newFragment != null) {
                    ft.hide(newFragment);
                }
                ft.commitAllowingStateLoss();
                ft = null;
            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        mBottomNavigationBar.selectTab(0);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getCodelistEventBus(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
