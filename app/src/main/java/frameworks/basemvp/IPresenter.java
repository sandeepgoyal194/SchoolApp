package frameworks.basemvp;

import android.content.Intent;
/**
 * Created by sandeep.g9 on 3/7/2017.
 */

public interface IPresenter<T extends IView> {
    public void onViewStarted();

    public void attachView(T view);

    public void onViewCreated();
    public boolean onActivityResult(int requestCode, int resultCode, Intent data);

    public void detachView();
}
