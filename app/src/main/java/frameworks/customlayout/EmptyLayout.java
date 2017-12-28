package frameworks.customlayout;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import transport.school.com.schoolapp.R;
/**
 * Created by sandeep on 4/11/17.
 */

public abstract class EmptyLayout extends RelativeLayout {
    public EmptyLayout(Context context) {
        super(context);
        init(context);
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EmptyLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    private void init(Context context) {
        LayoutParams layoutParams =  new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        setLayoutParams(layoutParams);
        View v = LayoutInflater.from(context)
                .inflate(R.layout.empty_layout, this, true);
        ImageView imageView = (ImageView) v.findViewById(R.id.empty_image);
        imageView.setImageResource(getEmptyDrawable());
        TextView textView = (TextView) v.findViewById(R.id.empty_text);
        textView.setText(getEmptyText());
    }

    public abstract int getEmptyDrawable();

    public abstract String getEmptyText();
}
