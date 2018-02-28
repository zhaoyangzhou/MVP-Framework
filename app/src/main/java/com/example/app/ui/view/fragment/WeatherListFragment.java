package com.example.app.ui.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.adapter.WeatherListAdapter;
import com.example.app.base.BaseFragment;
import com.example.app.base.BaseListViewIA;
import com.example.app.base.OnFragmentInteractionListener;
import com.example.app.base.gesture.OnStartDragListener;
import com.example.app.base.gesture.SimpleItemTouchHelperCallback;
import com.example.app.bean.weather.WeatherData;
import com.example.app.presenter.WeatherListPresenter;
import com.example.app.ui.view.activity.SearchActivity;

import java.util.ArrayList;
import java.util.List;

import cn.easydone.swiperefreshendless.EndlessRecyclerOnScrollListener;

/**
 * Package: com.example.app.module.main.view
 * Class: WeatherListFragment
 * Description: 气象界面视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class WeatherListFragment extends BaseFragment implements OnFragmentInteractionListener, BaseListViewIA,
        OnStartDragListener, OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    private List<WeatherData> dataList = new ArrayList<WeatherData>();
    private WeatherListAdapter adapter;
    private EndlessRecyclerOnScrollListener onScrollListener;

    private WeatherListPresenter presenter;

    //刷新广播监听
    public static final String REFRESH_ACTION = "REFRESH_ACTION";
    private RefreshActionReceiver refreshActionReceiver = new RefreshActionReceiver();

    //页面控件
    private SwipeRefreshLayout mSwipeRefreshWidget;

    //拖拽
    private ItemTouchHelper mItemTouchHelper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherListFragment() {
    }

    public static WeatherListFragment newInstance() {
        WeatherListFragment fragment = new WeatherListFragment();
        return fragment;
    }

    @Override
    protected View onCreateViewImpl(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        if (view instanceof SwipeRefreshLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new WeatherListAdapter(dataList, mListener);

            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            onScrollListener = new EndlessRecyclerOnScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int currentPage) {
                    presenter.loadData(++currentPage);
                }
            };
            recyclerView.addOnScrollListener(onScrollListener);

            mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
            mSwipeRefreshWidget.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                    android.R.color.holo_orange_light, android.R.color.holo_red_light);
            mSwipeRefreshWidget.setOnRefreshListener(this);

            // 这句话是为了，第一次进入页面的时候显示加载进度条
            mSwipeRefreshWidget.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                            .getDisplayMetrics()));

            //拖拽
            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    @Override
    protected void initPresenter() {
        presenter = new WeatherListPresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        presenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        presenter.loadData(0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (this instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) this;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
        IntentFilter filter = new IntentFilter(REFRESH_ACTION);
        getActivity().registerReceiver(refreshActionReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        onScrollListener = null;
        getActivity().unregisterReceiver(refreshActionReceiver);
    }

    @Override
    public void showProgressDialog() {
        mSwipeRefreshWidget.setRefreshing(true);
    }
    @Override
    public void hideProgressDialog() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void refresh(List data) {
        dataList.clear();
        dataList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadNews(List data) {
        dataList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onFragmentInteraction(Object object) {
        WeatherData data = (WeatherData) object;
        Toast.makeText(getActivity(), data.getName(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onRefresh() {
        onScrollListener.reset();
        presenter.loadData(0);
    }

    private class RefreshActionReceiver extends BroadcastReceiver {

        // 子类收到广播后的逻辑
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(REFRESH_ACTION)) {
                presenter.loadData(0);
            }
        }
    }
}
