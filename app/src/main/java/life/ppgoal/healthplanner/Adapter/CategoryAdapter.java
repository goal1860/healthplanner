package life.ppgoal.healthplanner.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.CategoryList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Method;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Activity activity;
    private Method method;
    private int columnWidth;
    private List<CategoryList> categoryLists;

    public CategoryAdapter(Activity activity, List<CategoryList> categoryLists, AdShow adShow) {
        this.activity = activity;
        this.categoryLists = categoryLists;
        method = new Method(activity, adShow);
        columnWidth = (method.getScreenWidth());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.category_adapter, parent, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth / 2, columnWidth / 4));
        holder.view.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth / 2, columnWidth / 4));

        Picasso.get().load(categoryLists.get(position).getCategory_image())
                .into(holder.imageView);

        holder.textView.setTypeface(Method.scriptable);
        holder.textView.setText(categoryLists.get(position).getCategory_name());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                method.interstitialAdShow(0, categoryLists.get(position).getCategory_name()
                        , "", "", categoryLists.get(position).getCid(), categoryLists.get(position).getCategory_image());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private View view;
        private RoundedImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView_category_adapter);
            view = itemView.findViewById(R.id.view_category_adapter);
            imageView = itemView.findViewById(R.id.imageView_category_adapter);

        }

    }

}
