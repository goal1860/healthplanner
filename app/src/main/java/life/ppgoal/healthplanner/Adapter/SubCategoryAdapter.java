package life.ppgoal.healthplanner.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.SubCategoryList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Method;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    private Activity activity;
    private Method method;
    private int columnWidth;
    String strings;
    StringBuilder sb;
    private List<SubCategoryList> subCategoryLists;

    public SubCategoryAdapter(Activity activity, List<SubCategoryList> subCategoryLists, AdShow adShow) {
        this.activity = activity;
        this.subCategoryLists = subCategoryLists;
        method = new Method(activity, adShow);
        columnWidth = (method.getScreenWidth());
        sb = new StringBuilder(subCategoryLists.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.sub_category_adapter, parent, false);

        return new SubCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 6));
        holder.view.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 6));
        holder.textView.setTypeface(Method.scriptable);
        holder.textView.setText(subCategoryLists.get(position).getDiet_title());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < subCategoryLists.size(); i++) {
                    sb.append(subCategoryLists.get(i).getDiet_title()).append(",,/");
                }
                strings = sb.toString();
                method.interstitialAdShow(position, "", String.valueOf(subCategoryLists.size()), strings, "","");
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private View view;
        private RoundedImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView_sub_category_adapter);
            view = itemView.findViewById(R.id.view_sub_category_adapter);
            imageView = itemView.findViewById(R.id.imageView_sub_category_adapter);
        }
    }
}
