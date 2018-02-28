package com.example.app.base;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * Package: com.example.app.base
 * Class: BasePresenter
 * Description: Presenter基类（控制层）
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public abstract class BasePresenter {
    private BaseViewIA baseViewIA;
    public BasePresenter(BaseViewIA baseViewIA) {
        this.baseViewIA = baseViewIA;
    }

    public void doRequest(Observable observable) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    private Disposable disposable;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        baseViewIA.showProgressDialog();
                    }

                    @Override
                    public void onNext(Object data) {
                        onNextImpl(data);
                    }

                    @Override
                    public void onError(Throwable e) {
                        baseViewIA.showError(e.getMessage());
                        baseViewIA.hideProgressDialog();
                        onErrorImpl(e);
                        disposable.dispose();
                    }

                    @Override
                    public void onComplete() {
                        baseViewIA.hideProgressDialog();
                        onCompletedImpl();
                        disposable.dispose();
                    }
                });
    }

    public void onDestroy() {
        baseViewIA = null;
    }

    protected abstract void onNextImpl(Object data);
    protected abstract void onErrorImpl(Throwable e);
    protected abstract void onCompletedImpl();
}
