package com.example.app.bean.weather;

import com.example.app.base.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Created by Hal on 15/4/26.
 */

@ModelContainer
@Table(database = AppDatabase.class)
public class WeatherData extends BaseModel {
	@PrimaryKey(autoincrement = false)
    private long id;
	@Column
    private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
