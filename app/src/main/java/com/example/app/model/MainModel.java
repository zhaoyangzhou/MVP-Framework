package com.example.app.model;

import com.example.app.base.BaseModel;
import com.example.app.base.netty.IMUtil;
import com.example.app.model.ia.MainIA;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Package: com.example.app.module.main.model
 * Class: MainModel
 * Description: 主界面业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class MainModel extends BaseModel implements MainIA {

    public MainModel() {
        IMUtil.startPush();
    }

    @Override
    public void getCodelist() {
        final ArrayList pack = new ArrayList();
        pack.add((short)36);
        pack.add((short)0);
        pack.add(30);
        pack.add(5);
        pack.add(32);
        pack.add((short)0);
        pack.add(new byte[]{36, 30, 30, 36, 31, 38, 0, 0, 0, 0, 0, 0});
        pack.add(0);
        pack.add(0);

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                IMUtil.sendMsg(pack);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Object data) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        disposable.dispose();
                    }
                });
    }
}
