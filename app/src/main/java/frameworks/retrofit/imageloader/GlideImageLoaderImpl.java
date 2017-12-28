package frameworks.retrofit.imageloader;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
/**
 * Created by sandeep.g9 on 10/20/2016.
 */
public class GlideImageLoaderImpl implements ImageLoader {
    Context mContext;

    public GlideImageLoaderImpl(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        RequestOptions myOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        load(url,imageView,myOptions);

    }

    public void loadImage(String url, ImageView imageView, int placeHolderID) {
        RequestOptions myOptions = new RequestOptions().placeholder(placeHolderID)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        load(url,imageView,myOptions);
    }

    private void load(String url, ImageView imageView, RequestOptions requestOptions) {
        Glide.with(mContext)
                .load(url)
                .apply(requestOptions)
                .into(imageView);
    }
}
