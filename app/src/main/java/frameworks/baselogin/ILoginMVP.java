package frameworks.baselogin;




import frameworks.appsession.UserInfo;
import frameworks.basemvp.IPresenter;
import frameworks.basemvp.IView;
import transport.school.com.schoolapp.bean.LoginResponse;
/**
 * Created by naveen.g9 on 7/27/2017.
 * This is Base Login MVP
 * Every type{Email, Social Login} should extend this for common login methods
 */

public interface ILoginMVP {
    public interface ILoginView extends IView {
        public void sendLoginSuccessToActivity(UserInfo user);
        public void sendLoginFailToActivity();
    }

    public interface ILoginPresenter<T extends ILoginView> extends IPresenter<T> {
        @Override
        void attachView(T view);
        void onSignIn(UserInfo user);
        void onSignFail();
    }
    public interface ILoginListener{
        public void onLoginSuccessful(LoginResponse loginResponse);
        public void onLoginFail();
    }
    public interface ILoginServerIntractor {

    }

}
