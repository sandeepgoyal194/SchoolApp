package transport.school.com.schoolapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Naveen.Goyal on 11/29/2017.
 */

public class CustomCheckBox extends LinearLayout {

    @BindView(R.id.checkbox)
    CheckBox checkbox;


    public CustomCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        int layout = R.layout.custom_checkbox;
        inflate(getContext(), layout, this);
        ButterKnife.bind(this);
    }

    public void setChecked(boolean status) {
        checkbox.setChecked(status);
    }

    public boolean isChecked() {
        return checkbox.isChecked();
    }

}
