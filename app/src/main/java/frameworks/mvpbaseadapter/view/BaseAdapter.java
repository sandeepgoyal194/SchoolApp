package frameworks.mvpbaseadapter.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import frameworks.basemvp.AppBaseActivity;
import frameworks.mvpbaseadapter.presenter.IBaseAdapterPresenter;
/**
 * Created by sandeep on 31/7/17.
 */

public abstract class BaseAdapter<T extends BaseViewHolder,E extends IBaseAdapterPresenter> extends RecyclerView.Adapter<T> implements IBaseAdapterView {
    private IBaseAdapterPresenter mPresenter;
    AppBaseActivity mBaseActivity;

    public BaseAdapter(Context context) {
        mBaseActivity = (AppBaseActivity) context;
        mPresenter = createPresenter();
        getPresenter().attachView(this);
    }
   abstract public E getPresenter();

    public abstract E createPresenter();

    @Override
    public Context getContext() {
        return mBaseActivity;
    }

    @Override
    public void hideProgressBar() {
        mBaseActivity.hideProgressBar();
    }

    @Override
    public void showProgressBar() {
        mBaseActivity.showProgressBar();
    }

    @Override
    public void showProgressBar(String message) {
        mBaseActivity.showProgressBar(message);
    }

    @Override
    public void showToast(String message) {
        mBaseActivity.showToast(message);
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        mPresenter.bindView(holder, position);
        holder.linkTo(position);
    }

    @Override
    public void startActivity(Intent intent) {
        mBaseActivity.startActivity(intent);
    }

    @Override
    public void notifyChange() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return getPresenter().getSize();
    }
}
