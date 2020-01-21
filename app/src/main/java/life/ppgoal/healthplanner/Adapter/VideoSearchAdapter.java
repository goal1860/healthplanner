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

public class VideoSearchAdapter extends RecyclerView.Adapter<VideoSearchAdapter.ViewHolder> {

    private Method method;
    private AdShow adShow;
    private Activity activity;
    private int columnWidth;
    private List<VideoList> videoLists;

    public VideoSearchAdapter(Activity activity, List<VideoList> videoLists, AdShow adShow) {
        this.activity = activity;
        this.adShow = adShow;
        this.videoLists = videoLists;
        method = new Method(activity, adShow);
        columnWidth = (method.getScreenWidth());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fitness_video_adapter, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));

        holder.textView.setText(videoLists.get(position).getVideo_title());
        holder.textView_time.setText(videoLists.get(position).getVideo_duration());

        if (videoLists.get(position).getVideo_type().equals("youtube")) {
            Picasso.get().load(Constant_Api.YOUTUBE_IMAGE_FRONT
                    + videoLists.get(position).getVideo_id()
                    + Constant_Api.YOUTUBE_SMALL_IMAGE_BACK).into(holder.imageView);
        } else {
            Picasso.get().load(videoLists.get(position).getVideo_thumbnail_b()).into(holder.imageView);
        }

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoLists.get(position).getVideo_type().equals("youtube")) {
                    adShow.position(position, "", "", "", "", "");
                } else {
                    method.interstitialAdShow(position, "", "", "", "", "");
                }
            }
        });

        holder.imageView_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoLists.get(position).getVideo_type().equals("youtube")) {
                    adShow.position(position, "", "", "", "", "");
                } else {
                    method.interstitialAdShow(position, "", "", "", "", "");
                }
            }
        });

        holder.imageView_share.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public int getItemCount() {
        return videoLists.size();
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
}
