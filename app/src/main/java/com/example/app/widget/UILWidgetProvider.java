package com.example.app.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.example.app.AppApplication;
import com.example.app.R;
import com.example.app.base.BaseViewIA;
import com.example.app.widget.presenter.WidgetPresenter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Package: com.example.app.widget
 * Class: UILWidgetProvider
 * Description: 桌面插件界面
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class UILWidgetProvider extends AppWidgetProvider implements BaseViewIA {

    private static DisplayImageOptions displayOptions;
    private WidgetPresenter presenter;
    private UpdateWidgetListener listener;
    private UIHandler uiHandler = new UIHandler(this);
    private AppWidgetManager appWidgetManager;

    static {
        displayOptions = DisplayImageOptions.createSimple();
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        presenter = new WidgetPresenter(this);
        presenter.loadData();

        listener = new UpdateWidgetListener() {
            @Override
            public void refresh(ArrayList<String> list) {
                UILWidgetProvider.this.appWidgetManager = appWidgetManager;
                final int widgetCount = appWidgetIds.length;
                ArrayList tempList = new ArrayList(3);
                for (int i = 0; i < 3; i++) {
                    tempList.add(list.get(i));
                }
                for (int i = 0; i < widgetCount; i++) {
                    int appWidgetId = appWidgetIds[i];
                    updateAppWidget(appWidgetId, tempList, 0);
                }
            }
        };
    }

    private void updateAppWidget(int appWidgetId, ArrayList<String> list, int index) {
        if (index >= list.size()) index = 0;
        Message message = uiHandler.obtainMessage();
        Bundle bundle = message.getData();
        bundle.putInt("index", index);
        bundle.putInt("appWidgetId", appWidgetId);
        bundle.putSerializable("list", list);
        message.setData(bundle);
        if (index == 0) {
            uiHandler.sendMessage(message);
        } else {
            uiHandler.sendMessageDelayed(message, 2000);
        }
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showError(String msg) {

    }

    static class UIHandler extends Handler {
        private WeakReference<UILWidgetProvider> mContext;
        public UIHandler(UILWidgetProvider context) {
            this.mContext = new WeakReference<UILWidgetProvider>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            int index = bundle.getInt("index");
            final int appWidgetId = bundle.getInt("appWidgetId");
            final ArrayList<String> list = (ArrayList<String>) bundle.getSerializable("list");
            final RemoteViews views = new RemoteViews(AppApplication.getInstance().getPackageName(), R.layout.widget);
            ImageSize minImageSize = new ImageSize(70, 70); // 70 - approximate size of ImageView in widget
            String uri = list.get(index);
            ImageLoader.getInstance()
                    .loadImage(uri, minImageSize, displayOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            views.setImageViewBitmap(R.id.image_left, loadedImage);
                            mContext.get().appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    });
            ImageLoader.getInstance()
                    .loadImage(uri, minImageSize, displayOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            views.setImageViewBitmap(R.id.image_right, loadedImage);
                            mContext.get().appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    });
            index += 1;
            //mContext.get().updateAppWidget(appWidgetId, list, index);//轮循播放
        }
    }

    public void getData(ArrayList<String> data) {
        listener.refresh(data);
    }

    interface UpdateWidgetListener {
        void refresh(ArrayList<String> data);
    }
}
