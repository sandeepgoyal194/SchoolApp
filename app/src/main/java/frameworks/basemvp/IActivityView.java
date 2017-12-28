package frameworks.basemvp;

import android.content.Intent;
/**
 * Created by sandeep.g9 on 7/28/2017.
 */

public interface IActivityView extends IView {
    Intent getIntent();
    void setTitle(CharSequence title);
}
