package frameworks.mvpbaseadapter.view;

import android.content.Intent;

import frameworks.basemvp.IBaseView;
/**
 * Created by sandeep on 31/7/17.
 */

public interface IBaseAdapterView extends IBaseView {
    public void startActivity(Intent intent);
    public void notifyChange();
}
