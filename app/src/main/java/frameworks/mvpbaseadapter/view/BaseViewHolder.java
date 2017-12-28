package frameworks.mvpbaseadapter.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;
/**
 * Created by sandeep on 31/7/17.
 */

public abstract class BaseViewHolder<E extends IBaseAdapterView> extends RecyclerView.ViewHolder implements IBaseViewHolderView {

    protected int position;

    public BaseViewHolder(View itemView, E context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void linkTo(int position) {
        this.position = position;
    }

    @Override
    public int getLinkedPosition() {
        return position;
    }

}
