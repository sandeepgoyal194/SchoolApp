package frameworks.customlayout;

import android.os.Bundle;
import android.support.annotation.Nullable;


import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.AppBaseFragment;
import frameworks.basemvp.IPresenter;
import transport.school.com.schoolapp.R;
public abstract class ActivitySingleFragment<T extends AppBaseFragment> extends AppBaseActivity {
    String TAG = "SingleFragment";
    @Override
    public int getViewToCreate() {
        return R.layout.activity_single_fragment;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addFrgment(getFragment(),TAG,R.id.container,true);
    }
    @Override
    public IPresenter getPresenter() {
        return null;
    }
    protected abstract T getFragment();
}
