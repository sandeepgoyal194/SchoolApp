package transport.school.com.schoolapp;
import android.content.Context;

import frameworks.customlayout.EmptyLayout;
/**
 * Created by sandeep on 14/1/18.
 */
public class NoStudentLayout extends EmptyLayout {
    public NoStudentLayout(Context context) {
        super(context);
    }

    @Override
    public int getEmptyDrawable() {
        return 0;
    }

    @Override
    public String getEmptyText() {
        return "No Student ";
    }
}
