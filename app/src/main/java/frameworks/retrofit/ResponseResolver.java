package frameworks.retrofit;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import frameworks.appsession.AppBaseApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public abstract class ResponseResolver<T> implements Callback<T> {

    String MSG_DEFAULT_ERROR = "An error occurred.Please try again";
    String NO_INTERNET_MESSAGE = "Please check internet connectivity status";
    String REMOTE_SERVER_FAILED_MESSAGE = "Remote server failed to respond";
    String CONNECTION_TIME_OUT_MESSAGE = "Connection timed out";
    String UNEXPECTED_ERROR_OCCURRED = "Internet connectivity issue";
    String SSL_HANDSHAKE_FAILED = "SSL handshake failed";
    String CONNECTION_REFUSED = "Connection was refused from server";

    private boolean showFailureDialog = true;
    private boolean sessionExpired = false;
    private Context ctx;
    private AlertDialog alertDialog;

    public ResponseResolver() {
        this(null, false);
    }

    @Deprecated
    public ResponseResolver(Context ctx) {
        this(ctx, false);
    }

    public ResponseResolver(Context ctx, boolean showFailureDialog) {
        this.ctx = ctx;
        this.showFailureDialog = showFailureDialog;

    }

    public abstract void onSuccess(T t, Response response);

    public abstract void onFailure(RestError error, String msg);

    private void displayErrorDialog(String errorMessage) {
//        if (sessionExpired) {
//            alertDialog = Utils.showDialog(ctx, "Error", errorMessage, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }, null, false, "OK");
//        } else {
//            alertDialog = Utils.showDialog(ctx, errorMessage);
//        }
    }

    public void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.dismiss();
    }

    private RestError resolveNetworkError(Throwable cause) {
        RestError error = new RestError();
        if (cause instanceof UnknownHostException) {
            error.setHttpCode(1000);
            error.setMessage(NO_INTERNET_MESSAGE);
        } else if (cause instanceof SocketTimeoutException) {
            error.setHttpCode(1001);
            error.setMessage(REMOTE_SERVER_FAILED_MESSAGE);
        } else if (cause instanceof ConnectTimeoutException) {
            error.setHttpCode(1002);
            error.setMessage(CONNECTION_TIME_OUT_MESSAGE);
        } else if (cause instanceof SSLHandshakeException) {
            error.setHttpCode(1003);
            error.setMessage(SSL_HANDSHAKE_FAILED);
        } else if (cause instanceof ConnectException) {
            error.setHttpCode(1004);
            error.setMessage(CONNECTION_REFUSED);
        } else {
            error.setHttpCode(1005);
            error.setMessage(cause.getMessage());
        }
        return error;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            T blossomResponse = response.body();
            onSuccess(blossomResponse, response);
        } else {
            RestError body = new RestError();
            body.setHttpCode(response.code());

            String errorMessage = MSG_DEFAULT_ERROR;
            try {
                switch (response.code()) {
                    case 403:
                        sessionExpired = true;
                        break;
                    case 404:
                        errorMessage = "404 NOT FOUND";
                        break;
                    case 401:
                        AppBaseApplication.getApplication().onLogout();
                        break;
                }

                String json = new String(response.errorBody().bytes());
                Log.v("", "error json " + json);
                body = new Gson().fromJson(json, RestError.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (showFailureDialog) {
                displayErrorDialog(errorMessage);
            }
            onFailure(body, body.getMessage());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        RestError body = resolveNetworkError(t);

        onFailure(body, body.getMessage());
    }
}
