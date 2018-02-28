package com.example.app.ui.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.app.R;
import com.example.app.base.BaseFragment;
import com.example.app.bean.picture.Picture;
import com.example.app.presenter.HomePresenter;
import com.example.app.presenter.SearchPresenter;
import com.example.app.ui.ia.HomeViewIA;
import com.example.app.ui.ia.SearchViewIA;
import com.panxw.android.imageindicator.AutoPlayManager;
import com.panxw.android.imageindicator.ImageIndicatorView;
import com.panxw.android.imageindicator.NetworkImageIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.example.app.module.main.view
 * Class: ChartFragment
 * Description: 首页界面视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/22 9:32
 */
public class HomeFragment extends BaseFragment implements SearchViewIA, HomeViewIA {

    private AutoCompleteTextView autoCompleteTextView;
    private NetworkImageIndicatorView imageIndicatorView;
    private AutoPlayManager autoBrocastManager;

    private List<String> mDatas = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private SearchPresenter searchPresenter;
    private HomePresenter homePresenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    public View onCreateViewImpl(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        imageIndicatorView = (NetworkImageIndicatorView) view.findViewById(R.id.imageIndicatorView);
        autoBrocastManager =  new AutoPlayManager(imageIndicatorView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mDatas);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                doRequest();
            }
        });
        imageIndicatorView.setIndicateStyle(ImageIndicatorView.INDICATE_ROUND_STYLE);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        imageIndicatorView.destroyDrawingCache();
        imageIndicatorView = null;
    }

    @Override
    protected void initPresenter() {
        searchPresenter = new SearchPresenter(this);
        homePresenter = new HomePresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        searchPresenter.onDestroy();
        homePresenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        searchPresenter.doRequest(autoCompleteTextView.getText().toString());
        homePresenter.loadData(0);
    }

    @Override
    public void showProgressDialog() {

    }
    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void notifySearch(List<String> list) {
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void notifyBanner(List<String> list) {
        final List<String> urlList= new ArrayList<String>();
        urlList.addAll(list);
        imageIndicatorView.setupLayoutByImageUrl(urlList);
        imageIndicatorView.show();
        autoBrocastManager.setBroadCastTimes(5);//loop times
        autoBrocastManager.setBroadcastTimeIntevel(3 * 1000, 3 * 1000);//set first play time and interval
        autoBrocastManager.stop();
        autoBrocastManager.setBroadcastEnable(true);
        autoBrocastManager.loop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            autoBrocastManager.stop();
        } else {
            autoBrocastManager.setBroadcastEnable(true);
            autoBrocastManager.loop();
        }
    }
}
