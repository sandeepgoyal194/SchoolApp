package frameworks.baselogin;



import frameworks.appsession.AppBaseApplication;
import frameworks.appsession.UserInfo;
import frameworks.basemvp.AppBasePresenter;
import transport.school.com.schoolapp.bean.LoginResponse;
/**
 * Created by sandeep.g9 on 7/27/2017.
 */

public abstract class BaseLoginPresenter<T extends ILoginMVP.ILoginView> extends AppBasePresenter<T> implements ILoginMVP.ILoginPresenter<T>, ILoginMVP.ILoginListener {
    ILoginMVP.ILoginView loginView;
    UserInfo user;
    public void attachView(T view) {
        super.attachView(view);
        loginView = view;
    }

    @Override
    public void onSignIn(UserInfo user) {
        loginView.showProgressBar();
        this.user = user;
        signInCalled();
    }

    public void onSignFail() {
        loginView.showToast("Login Failed");
    }

    public abstract void signInCalled();

    @Override
    public void onLoginSuccessful(LoginResponse loginResponse) {
        loginView.hideProgressBar();
        AppBaseApplication.getApplication().setSession(loginResponse);
        loginView.sendLoginSuccessToActivity(user);
    }

    @Override
    public void onLoginFail() {
        loginView.hideProgressBar();
        loginView.showToast("Login Failed");
        loginView.sendLoginFailToActivity();
    }

    @Override
    public void detachView() {
        loginView.hideProgressBar();
    }
}
