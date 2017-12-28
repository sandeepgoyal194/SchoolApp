package frameworks.appsession;



import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;

/**
 * Created by sandeep on 24/8/17.
 */

public class ProfileInteractorImpl implements IProfileIntractor {
    @Override
    public void getProfile(final onProfileDownloaded listener) {
        WebServicesWrapper.getInstance().getMyProfile(new ResponseResolver<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo, Response response) {
                listener.onProfileDownloadSuccessfully(userInfo);
            }

            @Override
            public void onFailure(RestError error, String msg) {
                listener.onProfileDowloadFail();
            }
        });
    }
}
