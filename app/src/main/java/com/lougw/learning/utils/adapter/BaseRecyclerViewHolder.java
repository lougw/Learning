
package com.lougw.learning.utils.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;


public abstract class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    protected T mData;
    protected Context mContext;
    protected WeakReference<BaseRecyclerAdapter.OnItemClickListener> mListenerReference;

    public BaseRecyclerViewHolder(View view, Context mContext) {
        super(view);
        this.mContext = mContext;
    }

    public void bindData(T data, int position) {
        mData = data;
        refreshView(position);
    }

    public T getData() {
        return mData;
    }

    public abstract void refreshView(int position);

    public abstract void recycle();

    public void recycleImageView(ImageView view) {
        if (null != view) {
            view.setImageDrawable(null);
        }
    }

    public void setItemListener(BaseRecyclerAdapter.OnItemClickListener listener) {
        mListenerReference = new WeakReference<BaseRecyclerAdapter.OnItemClickListener>(listener);
    }

    @Override
    public int hashCode() {
        if (null != mData) {
            return mData.hashCode();
        }
        return super.hashCode();
    }
}
