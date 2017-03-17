package com.dicot.jitprint.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dicot.jitprint.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;

    public RecyclerAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_recycler, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mTxt.setText(mDatas.get(i));

        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickLitener.onItemLongClick(viewHolder.itemView, i);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTxt;

        public ViewHolder(View view) {
            super(view);
            mTxt = (TextView) view.findViewById(R.id.recycler_item_name);
        }
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    public interface OnItemLongClickLitener {
        void onItemLongClick(View view, int position);
    }

    private OnItemLongClickLitener mOnItemLongClickLitener;

    public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
        this.mOnItemLongClickLitener = mOnItemLongClickLitener;
    }

}
