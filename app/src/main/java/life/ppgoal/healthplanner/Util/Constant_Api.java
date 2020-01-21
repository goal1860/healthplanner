package life.ppgoal.healthplanner.Util;

import life.ppgoal.healthplanner.BuildConfig;
import life.ppgoal.healthplanner.Item.AboutUsList;
import life.ppgoal.healthplanner.Item.SubCategoryList;

import java.util.ArrayList;
import java.util.List;

public class Constant_Api {

    //main server api url
    public static String url = BuildConfig.My_api;

    //Array Tag
    public static String tag = "DIET_APP";

    //youtube api key
    public static String YOUR_DEVELOPER_KEY = BuildConfig.My_youtube_api_key;

    //Image url
    public static String image = url + "images/";

    //App info url
    public static String app_info = url + "api.php?app_details";

    //Category
    public static String category = url + "api.php?cat_list";

    //Sub Category
    public static String sub_category = url + "api.php?cat_id=";

    //Diet Search
    public static String search_diet = url + "api.php?diet_search_text=";

    //Video Search
    public static String search_video = url + "api.php?video_search_text=";

    //Video
    public static String video = url + "api.php?video_list&page=";

    //Sub Category
    public static String notification = url + "api.php?diet_id=";

    public static final String YOUTUBE_IMAGE_FRONT = "http://img.youtube.com/vi/";
    public static final String YOUTUBE_SMALL_IMAGE_BACK = "/hqdefault.jpg";

    public static int AD_COUNT = 0;
    public static int AD_COUNT_SHOW = 0;

    public static AboutUsList aboutUsList;
    public static List<SubCategoryList> subCategoryLists;
    public static List<SubCategoryList> searchList;
    public static List<SubCategoryList> notificationSCL = new ArrayList<>();

}
