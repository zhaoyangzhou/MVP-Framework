package com.example.app.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.app.R;
import com.example.app.base.OnFragmentInteractionListener;
import com.example.app.base.gesture.ItemTouchHelperAdapter;
import com.example.app.base.gesture.ItemTouchHelperViewHolder;
import com.example.app.bean.StockLive;

import java.util.Collections;
import java.util.List;

/**
 * Package: com.example.app.module.main.adapter
 * Class: StockLiveAdapter
 * Description: 列表适配器
 * Author: zhaoyangzhou
 * Email: zhaoyangzhou@126.com
 * Created on: 2017/12/8 8:54
 */
public class StockLiveAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemTouchHelperAdapter {
    private final List<StockLive> mValues;
    private final OnFragmentInteractionListener mListener;

    //数据项类型
    private static final int TYPE_ITEM0 = 0;
    private static final int TYPE_ITEM1 = 1;

    public StockLiveAdapter(List<StockLive> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM0) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_stock_live, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_ITEM1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.empty, null);
            return new EmptyViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.mItem = mValues.get(position);
            itemViewHolder.tvCode.setText(mValues.get(position).getCode());
            itemViewHolder.tvName.setText(mValues.get(position).getName());
            itemViewHolder.tvYesterdayEnd.setText(mValues.get(position).getYesterdayEnd());
            itemViewHolder.tvColor.setText(mValues.get(position).getColor());
            itemViewHolder.tvStart.setText(mValues.get(position).getStart());
            itemViewHolder.tvHightest.setText(mValues.get(position).getHightest());
            if(position % 2 == 0) {
                itemViewHolder.tvStart.setTextColor(Color.RED);
                itemViewHolder.tvHightest.setTextColor(Color.RED);
            } else {
                itemViewHolder.tvStart.setTextColor(Color.GREEN);
                itemViewHolder.tvHightest.setTextColor(Color.GREEN);
            }

            itemViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onFragmentInteraction(itemViewHolder.mItem);
                    }
                }
            });
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mValues, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mValues.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position >= 0) {
            return TYPE_ITEM0;
        } else {
            return TYPE_ITEM1;
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        public final View mView;
        public final TextView tvCode;
        public final TextView tvName;
        public final TextView tvYesterdayEnd;
        public final TextView tvColor;
        public final TextView tvStart;
        public final TextView tvHightest;
        public StockLive mItem;

        public ItemViewHolder(View view) {
            super(view);
            mView = view;
            tvCode = (TextView) view.findViewById(R.id.tvCode);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvYesterdayEnd = (TextView) view.findViewById(R.id.tvYesterdayEnd);
            tvColor = (TextView) view.findViewById(R.id.tvColor);
            tvStart = (TextView) view.findViewById(R.id.tvStart);
            tvHightest = (TextView) view.findViewById(R.id.tvHightest);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + tvName.getText() + "'";
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public EmptyViewHolder(View view) {
            super(view);
            mView = view;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
