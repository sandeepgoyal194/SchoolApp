package frameworks.basemvp;

import android.content.Intent;


import frameworks.appsession.AppBaseApplication;
import frameworks.appsession.IProfileIntractor;
import frameworks.appsession.ProfileInteractorImpl;
import frameworks.appsession.UserInfo;
/**
 * Created by sandeep.g9 on 7/27/2017.
 */

public abstract class AppBasePresenter<T extends IView> implements IPresenter<T>, IProfileIntractor.onProfileDownloaded {

    @Override
    public void attachView(T view) {
    }

    public void onViewStarted() {

    }

    @Override
    public void onViewCreated() {

    }

    public void detachView() {

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }

    public void syncProfile() {
        IProfileIntractor intractor = new ProfileInteractorImpl();
        intractor.getProfile(this);
    }

    public void profileSyneced() {

    }

    @Override
    public void onProfileDownloadSuccessfully(UserInfo userInfo) {
        profileSyneced();
    }

    @Override
    public void onProfileDowloadFail() {

    }
}
