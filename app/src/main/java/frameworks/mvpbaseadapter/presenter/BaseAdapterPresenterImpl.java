package frameworks.mvpbaseadapter.presenter;


import frameworks.mvpbaseadapter.view.IBaseAdapterView;
import frameworks.mvpbaseadapter.view.IBaseViewHolderView;
/**
 * Created by sandeep on 3/8/17.
 */

public abstract class BaseAdapterPresenterImpl <T extends IBaseViewHolderView,E extends IBaseAdapterView> implements IBaseAdapterPresenter<T,E> {

    @Override
    public void bindView(T view, int position) {
        view.linkTo(position);
    }
}
