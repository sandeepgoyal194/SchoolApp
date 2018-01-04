package frameworks.basemvp;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationRequest;

import butterknife.ButterKnife;
import frameworks.apppermission.PermissionActivity;
import frameworks.customlayout.EmptyLayout;
import frameworks.locationmanager.LocationManagerService;
import frameworks.retrofit.imageloader.GlideImageLoaderImpl;
import transport.school.com.schoolapp.R;
/**
 * Created by sandeep.g9 on 3/7/2017.
 */
public abstract class AppBaseActivity<T extends IPresenter> extends PermissionActivity implements IActivityView {
    public static final String TAG = "Villeger";
    protected android.support.v4.app.Fragment mAddedFragment;
    protected ActionBarDrawerToggle mActionBarDrawerToggle;
    public Toolbar mToolbar;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    IPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewToCreate());
        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    public abstract int getViewToCreate();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        //Activity don't want to setContent View;
        if (layoutResID == -1) {
            return;
        }
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setmToolbar();
        if (mPresenter != null) {
            mPresenter.onViewCreated();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.onViewStarted();
        }
    }

    protected void onStop() {
        super.onStop();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    @Override
    public void showProgressBar() {
    }

    @Override
    public void showProgressBar(String message) {
    }

    @Override
    public void hideProgressBar() {
    }

    @Override
    public void showSnackBar(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public abstract T getPresenter();

    public void setmToolbar() {
        setmToolbar(false);
    }

    public void setmToolbar(boolean needBackButton) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            if (needBackButton) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow, null));
                } else {
                    mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow));
                }
                mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    protected void addFrgment(android.support.v4.app.Fragment fragment, String fragmentTag, int containerView, boolean addToBackStack) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction mOwnerTransaction = mFragmentManager.beginTransaction();
        android.support.v4.app.Fragment fragment1 = mFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment1 != null) {
            fragment = fragment1;
        }
        if (!mFragmentManager.popBackStackImmediate(fragment.getClass().getName(), 0)) {
            mOwnerTransaction.replace(containerView, fragment, fragmentTag);
            if (addToBackStack) {
                if (mFragmentManager.getFragments() != null && mFragmentManager.getFragments().size() > 0)
                    mOwnerTransaction.addToBackStack(null);
            }
            mAddedFragment = fragment;
            mOwnerTransaction.commit();
        }
    }

    protected void popFragment(android.support.v4.app.Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(fragment);
        trans.commit();
        manager.popBackStack();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText(title);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void setPermission(int requestCode, boolean isGranted) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getPresenter() != null) {
            getPresenter().onActivityResult(requestCode, resultCode, data);
        }
    }

    public void setLeftIcon(int leftIcon) {
        ImageView leftIconView = (ImageView) findViewById(R.id.left_icon);
        View left_menu = findViewById(R.id.left_menu);
        if (leftIconView != null) {
            leftIconView.setBackgroundResource(leftIcon);
            leftIconView.setVisibility(View.VISIBLE);
            left_menu.setVisibility(View.VISIBLE);
            left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onLeftMenuClick();
                }
            });
        }
    }

    public void setRightIcon(String url) {
        ImageView rightIconView = (ImageView) findViewById(R.id.right_icon);
        View right_menu = findViewById(R.id.right_menu);
        if (rightIconView != null) {
            new GlideImageLoaderImpl(getContext()).loadImage(url, rightIconView);
            rightIconView.setVisibility(View.VISIBLE);
            right_menu.setVisibility(View.VISIBLE);
            right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRightMenuCLick();
                }
            });
        }
    }

    public void onLeftMenuClick() {
    }

    public void onRightMenuCLick() {
    }

    public void addEmptyLayout() {
        setContentView(getEmptyLayout());
    }

    public EmptyLayout getEmptyLayout() {
        return null;
    }

    @Override
    public void onLocationPermissionGranted() {
    }

    @Override
    public void onLocationPermissionDenied() {
        showToast("Results may not be accurate as your location cannot be checked");
        finish();
    }

    @Override
    public void onLocationSettingEnabled() {
        mLocationManager.setLocationPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onLocationSettingEnableDenied() {
        mLocationManager.setLocationPriority(LocationRequest.PRIORITY_NO_POWER);
    }

    @Override
    public void onLocationChanged(Location location) {
        LocationManagerService.getInstance().setCurrentLocation(location);
    }
}
