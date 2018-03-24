package frameworks.basemvp;

import android.content.Intent;
/**
 * Created by naveen.g9 on 3/7/2017.
 */

public interface IView extends IBaseView {

    public void showProgressBar();

    public void showProgressBar(String message);

    public void hideProgressBar();

    public void showToast(String message);

    public void showSnackBar(String message);

    public void startActivity(Intent intent);

    public void startActivityForResult(Intent intent, int requestcode);

    public void setResult(int result);

    public void setResult(int result, Intent data);


    void finish();

    void addEmptyLayout() ;

}
