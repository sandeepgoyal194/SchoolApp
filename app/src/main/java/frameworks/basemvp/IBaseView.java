package frameworks.basemvp;

import android.content.Context;
/**
 * Created by sandeep on 7/9/17.
 */

public interface IBaseView {
    public void hideProgressBar();

    public void showProgressBar();

    public void showProgressBar(String message);

    public void showToast(String message);

    public Context getContext();
}
