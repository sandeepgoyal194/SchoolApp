package frameworks.retrofit.imageloader;

import android.widget.ImageView;
/**
 * Created by naveen.g9 on 10/20/2016.
 */

public interface ImageLoader {
    void loadImage(String url, ImageView imageView);

    void loadImage(String url, ImageView imageView, int placeHolderID);
}
