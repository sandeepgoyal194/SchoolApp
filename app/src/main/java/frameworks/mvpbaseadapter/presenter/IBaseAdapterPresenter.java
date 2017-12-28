package frameworks.mvpbaseadapter.presenter;


import frameworks.mvpbaseadapter.view.IBaseAdapterView;
import frameworks.mvpbaseadapter.view.IBaseViewHolderView;
/**
 * Created by sandeep on 1/8/17.
 */

public interface IBaseAdapterPresenter<T extends IBaseViewHolderView,E extends IBaseAdapterView> {
    public void attachView(E view);
    public void bindView(T view, int position);
    public int getSize();
}
