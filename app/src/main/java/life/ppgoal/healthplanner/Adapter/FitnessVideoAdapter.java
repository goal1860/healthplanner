package life.ppgoal.healthplanner.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.VideoList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FitnessVideoAdapter extends RecyclerView.Adapter {

    private Method method;
    private AdShow adShow;
    private Activity activity;
    private int columnWidth;
    private List<VideoList> videoLists;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public FitnessVideoAdapter(Activity activity, List<VideoList> videoLists, AdShow adShow) {
        this.activity = activity;
        this.videoLists = videoLists;
        this.adShow = adShow;
        method = new Method(activity, adShow);
        columnWidth = (method.getScreenWidth());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.fitness_video_adapter, parent, false);
            return new ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View v = LayoutInflater.from(activity).inflate(R.layout.layout_loading_item, parent, false);
            return new ProgressViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        if (holder instanceof ViewHolder) {

            final ViewHolder viewHolder = (ViewHolder) holder;

            viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));

            viewHolder.textView.setText(videoLists.get(position).getVideo_title());
            viewHolder.textView_time.setText(videoLists.get(position).getVideo_duration());

            if (videoLists.get(position).getVideo_type().equals("youtube")) {
                Picasso.get().load(Constant_Api.YOUTUBE_IMAGE_FRONT + videoLists.get(position).getVideo_id() + Constant_Api.YOUTUBE_SMALL_IMAGE_BACK).into(viewHolder.imageView);
            } else {
                Picasso.get().load(videoLists.get(position).getVideo_thumbnail_b()).into(viewHolder.imageView);
            }

            viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoLists.get(position).getVideo_type().equals("youtube")) {
                        adShow.position(position, "", "", "", "", "");
                    } else {
                        method.interstitialAdShow(position, "", "", "", "", "");
                    }
                }
            });

            viewHolder.imageView_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoLists.get(position).getVideo_type().equals("youtube")) {
                        adShow.position(position, "", "", "", "", "");
                    } else {
                        method.interstitialAdShow(position, "", "", "", "", "");
                    }
                }
            });

            viewHolder.imageView_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String sAux = "\n" + activity.getResources().getString(R.string.Let_me_recommend_you_this_application) + "\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + activity.getApplication().getPackageName();
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    activity.startActivity(Intent.createChooser(i, "choose one"));
                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return videoLists.size() + 1;
    }

    private boolean isHeader(int position) {
        return position == videoLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public void hideHeader() {
        ProgressViewHolder.progressBar.setVisibility(View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView imageView;
        private TextView textView, textView_time;
        private ImageView imageView_share, imageView_play;
        private RelativeLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView_video_adapter);
            textView = itemView.findViewById(R.id.textView_video_adapter);
            textView_time = itemView.findViewById(R.id.textView_time_video_adapter);
            imageView_play = itemView.findViewById(R.id.imageView_play_video_adapter);
            imageView_share = itemView.findViewById(R.id.imageView_share_video_adapter);
            relativeLayout = itemView.findViewById(R.id.relativelayout_video_adapter);

        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @SuppressLint("StaticFieldLeak")
        public static ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }

}
