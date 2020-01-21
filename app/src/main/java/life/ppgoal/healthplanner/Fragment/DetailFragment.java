package life.ppgoal.healthplanner.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.detail_fragment, container, false);

        Method method = new Method(getActivity());
        assert getArguments() != null;
        int position = getArguments().getInt("position");
        String type = getArguments().getString("type");

        int columnWidth = method.getScreenWidth();

        TextView textView_title = view.findViewById(R.id.textView_title_detail_fragment);
        WebView webView = view.findViewById(R.id.webView_detail_fragment);
        RoundedImageView imageView = view.findViewById(R.id.imageView_detail_fragment);
        View view_layout = view.findViewById(R.id.view_detail_fragment);

        textView_title.setTypeface(Method.scriptable);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        String mimeType = "text/html";
        String encoding = "utf-8";

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));
        view_layout.setLayoutParams(new RelativeLayout.LayoutParams(columnWidth, columnWidth / 2));

        assert type != null;
        switch (type) {
            case "notification":
                if (Constant_Api.notificationSCL != null) {
                    Picasso.get().load(Constant_Api.notificationSCL.get(position).getDiet_image()).into(imageView);
                    textView_title.setText(Html.fromHtml(Constant_Api.notificationSCL.get(position).getDiet_title()));

                    String htmlText = Constant_Api.notificationSCL.get(position).getDiet_info();

                    String text = "<html><head>"
                            + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/latoregular.ttf\")}"
                            + "</style></head>"
                            + "<body>"
                            + htmlText
                            + "</body></html>";

                    webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                }

                break;
            case "search": {
                Picasso.get().load(Constant_Api.searchList.get(position).getDiet_image()).into(imageView);
                textView_title.setText(Html.fromHtml(Constant_Api.searchList.get(position).getDiet_title()));

                String htmlText = Constant_Api.searchList.get(position).getDiet_info();

                String text = "<html><head>"
                        + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/latoregular.ttf\")}"
                        + "</style></head>"
                        + "<body>"
                        + htmlText
                        + "</body></html>";

                webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);

                break;
            }
            default: {
                Picasso.get().load(Constant_Api.subCategoryLists.get(position).getDiet_image()).into(imageView);
                textView_title.setText(Html.fromHtml(Constant_Api.subCategoryLists.get(position).getDiet_title()));

                String htmlText = Constant_Api.subCategoryLists.get(position).getDiet_info();

                String text = "<html><head>"
                        + "<style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/latoregular.ttf\")}"
                        + "</style></head>"
                        + "<body>"
                        + htmlText
                        + "</body></html>";

                webView.loadDataWithBaseURL(null, text, mimeType, encoding, null);
                break;
            }
        }

        return view;

    }

}
