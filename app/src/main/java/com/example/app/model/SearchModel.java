package com.example.app.model;

import com.example.app.model.ia.SearchIA;

import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Package: com.example.app.module.user.model
 * Class: SearchModel
 * Description: 搜索业务实现类
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class SearchModel implements SearchIA {
    public Observable<List<String>> onSearch(String text) {
        return Observable.create(new ObservableOnSubscribe<List<String>>() {
            @Override
            public void subscribe(ObservableEmitter<List<String>> emitter) {
                String[] arrayList = {"beijing","beijing2","beijing3","shanghai1","shanghai2","shanghai3"};
                emitter.onNext(Arrays.asList(arrayList));
                emitter.onComplete();
            }
        });
    }
}
