package transport.school.com.schoolapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import frameworks.appsession.AppBaseApplication;
import frameworks.basemvp.AppBaseActivity;
import frameworks.basemvp.IPresenter;
import frameworks.retrofit.ResponseResolver;
import frameworks.retrofit.RestError;
import frameworks.retrofit.WebServicesWrapper;
import retrofit2.Response;
import transport.school.com.schoolapp.bean.NotificationBean;

public class SendMessage extends AppBaseActivity {


    @BindView(R.id.edt_msg)
    EditText edtMsg;

    @Override
    public int getViewToCreate() {
        return R.layout.activity_send_message;
    }


    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmToolbar(true);
    }

    @OnClick(R.id.btn_sendmsg)
    public void onViewClicked() {
        String message = edtMsg.getText().toString();
        if(message == null || message.trim().isEmpty()) {
            edtMsg.setError("Please fill this");
            edtMsg.requestFocus();
            return;
        }
        NotificationBean notificationBean = new NotificationBean();
        notificationBean.setMessage(message);
        notificationBean.setRouteid(AppBaseApplication.getApplication().getRoute().getRouteid());
        WebServicesWrapper.getInstance().pushNotificatione(notificationBean, new ResponseResolver<String>() {
            @Override
            public void onSuccess(String s, Response response) {
                finish();
            }

            @Override
            public void onFailure(RestError error, String msg) {

            }
        });
    }
}
