package com.example.app.ui.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
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
import com.example.app.base.BaseActivity;
import com.example.app.base.BaseFragment;
import com.example.app.base.BaseListViewIA;
import com.example.app.base.OnFragmentInteractionListener;
import com.example.app.base.gesture.OnStartDragListener;
import com.example.app.base.gesture.SimpleItemTouchHelperCallback;
import com.example.app.bean.weather.WeatherData;
import com.example.app.presenter.ChartPresenter;
import com.example.app.presenter.WeatherListPresenter;
import com.example.app.ui.ia.ChartViewIA;

import java.util.ArrayList;
import java.util.List;

import cn.easydone.swiperefreshendless.EndlessRecyclerOnScrollListener;
import cn.limc.androidcharts.axis.Axis;
import cn.limc.androidcharts.entity.DateValueEntity;
import cn.limc.androidcharts.entity.IStickEntity;
import cn.limc.androidcharts.entity.LineEntity;
import cn.limc.androidcharts.entity.OHLCEntity;
import cn.limc.androidcharts.view.LineChart;
import cn.limc.androidcharts.view.SlipLineChart;

/**
 * Package: com.example.app.module.main.view
 * Class: ChartFragment
 * Description: 图表界面视图
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/22 9:32
 */
public class ChartFragment extends BaseFragment implements ChartViewIA {

    private SlipLineChart linechart;
    private ChartPresenter presenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChartFragment() {
    }

    public static ChartFragment newInstance() {
        ChartFragment fragment = new ChartFragment();
        return fragment;
    }

    public View onCreateViewImpl(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chart, container, false);
        linechart = (SlipLineChart) view.findViewById(R.id.linechart);
        linechart.setAxisXColor(Color.LTGRAY);
        linechart.setAxisYColor(Color.LTGRAY);
        linechart.setBorderColor(Color.LTGRAY);
        linechart.setLongitudeFontSize(20);
        linechart.setLatitudeFontSize(20);
        linechart.setLongitudeFontColor(Color.GRAY);
        linechart.setLatitudeColor(Color.GRAY);
        linechart.setLatitudeFontColor(Color.GRAY);
        linechart.setLongitudeColor(Color.GRAY);
        linechart.setMaxValue(280);
        linechart.setMinValue(240);
        linechart.setDisplayLongitudeTitle(true);
        linechart.setDisplayLatitudeTitle(true);
        linechart.setDisplayLatitude(true);
        linechart.setDisplayLongitude(true);
        linechart.setLatitudeNum(5);
        linechart.setLongitudeNum(6);
        linechart.setDataQuadrantPaddingTop(5);
        linechart.setDataQuadrantPaddingBottom(5);
        linechart.setDataQuadrantPaddingLeft(5);
        linechart.setDataQuadrantPaddingRight(5);
        // linechart.setAxisYTitleQuadrantWidth(50);
        //linechart.setAxisXTitleQuadrantHeight(20);
        linechart.setAxisXPosition(Axis.AXIS_X_POSITION_BOTTOM);
        linechart.setAxisYPosition(Axis.AXIS_Y_POSITION_RIGHT);
        return view;
    }

    @Override
    protected void initPresenter() {
        presenter = new ChartPresenter(this);
    }

    @Override
    protected void destroyPresenter() {
        presenter.onDestroy();
    }

    @Override
    protected void doRequest() {
        presenter.getData();
    }

    @Override
    public void showProgressDialog() {

    }
    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void updateView(List<DateValueEntity> list) {
        List<LineEntity<DateValueEntity>> lines = new ArrayList<LineEntity<DateValueEntity>>();

        // 计算5日均线
        LineEntity<DateValueEntity> MA5 = new LineEntity<DateValueEntity>();
        MA5.setTitle("MA5");
        MA5.setLineColor(Color.BLUE);
        MA5.setLineData(list);
        lines.add(MA5);

        // 为chart1增加均线
        linechart.setLinesData(lines);
        linechart.invalidate();
    }

    private void initLineChart(List<DateValueEntity> list) {

    }
}
