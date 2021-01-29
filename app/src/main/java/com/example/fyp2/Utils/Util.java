package com.example.fyp2.Utils;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.xutils.common.util.DensityUtil.getDensity;

/**
 * Created by Kai on 2017/1/17.
 */

public class Util
{
    public static ArrayList<String> strToList(String str)
    {
        if (str == null || str.length() <= 0)
        {
            return new ArrayList<String>();
        }
        String[] strArr = str.split(",");
        ArrayList<String> result = new ArrayList<String>();
        for (String item : strArr)
        {
            result.add(item.trim());
        }
        return result;
    }

    public static Map<String, Object> gsonToMap(String gsonStr)
    {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>()
        {
        }.getType();
        Map<String, Object> map = gson.fromJson(gsonStr, type);
        return map;
    }

    public static List<Map<String, Object>> gsonToList(String gsonStr)
    {
        gsonStr = gsonStr.trim();
        if (gsonStr == null || gsonStr.equals(""))
        {
            return new ArrayList<>();
        }
        if (!gsonStr.substring(0, 1).equals("["))
        {
            gsonStr = "[" + gsonStr + "]";
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String, Object>>>()
        {
        }.getType();
        List<Map<String, Object>> list = gson.fromJson(gsonStr, type);
        return list;
    }

//    public static String mapToJson(Map<String, Object> map)
//    {
//        int tab = 0;
//        String tab_str = "";
//        for (int m = 0; m < tab; m++)
//        {
//            tab_str += " ";
//        }
//        String json = "\n" + tab_str + "{" + "\n";
//        int i = 0;
//        for (String key : map.keySet())
//        {
//            if (map.get("product_picture") != null)
//            {
//                continue;
//            }
//            if (i >= map.size()) break;
//            String content = "";
//            try
//            {
//                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(key);
//                content += "[";
//                int j = 0;
//                for (Map<String, Object> map2 : list)
//                {
//                    if (j == list.size()) break;
//                    content += Util.mapToJson(map2, tab + 8) + (j++ == list.size() - 1 ? "" : ",");
//                }
//                content += "\n" + tab_str + "    " + "]" + (i == (map.size() - 1) ? "" : ",\n");
//            } catch (Exception e)
//            {
//                content = "\"" + map.get(key).toString() + "\"";
//            }
//            json += tab_str + "    " + "\"" + key + "\":" + content + (i++ == map.size() - 1 ? "" : ",\n");
//        }
//        json += "\n" + tab_str + "}";
//        return json;
//    }

    public static String mapToJson(Map<String, Object> map)
    {
        String tab_str = "";
        String json = tab_str + "{";
        int i = 0;
        for (String key : map.keySet())
        {
            if (i >= map.size())
                break;
            String content = "";
            try
            {
                List<Map<String, Object>> list = (List<Map<String, Object>>) map.get(key);
                content += "[";
                int j = 0;
                for (Map<String, Object> map2 : list) {
                    if (j == list.size())
                        break;
                    content += mapToJson(map2) + (j++ == list.size() - 1 ? "" : ",");
                }
                content += tab_str + "]"/* + (i == (map.size() - 1) ? "" : ",")*/;
            }
            catch (Exception e)
            {
                content = "\"" + map.get(key).toString() + "\"";
            }
            json += tab_str + "\"" + key + "\":" + content + (i++ == map.size() - 1 ? "" : ",");
        }
        json += tab_str + "}";
        return json;
    }

    public static int dip2px(float dpValue) {
        return (int) (dpValue * getDensity());
    }

    public static void setProductImg(ImageView img, String url)
    {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(dip2px(120), dip2px(120))//图片大小
//                .setRadius(DensityUtil.dip2px(0))//ImageView圆角半径
                .setImageScaleType(ImageView.ScaleType.FIT_XY)//缩放
                .setLoadingDrawableId(R.drawable.ic_launcher_background)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.drawable.ic_launcher_background)//加载失败后默认显示图片
                .build();
        x.image().bind(img, url, imageOptions);
    }

    public static void setImg(ImageView img, String url)
    {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))//图片大小
//                .setRadius(DensityUtil.dip2px(0))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)//缩放
                .setLoadingDrawableId(R.drawable.ic_launcher_background)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.drawable.ic_launcher_background)//加载失败后默认显示图片
                .build();
        x.image().bind(img, url, imageOptions);
    }


    public static void setBannerImg(ImageView img, String url)
    {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(640), DensityUtil.dip2px(240))//图片大小
//                .setRadius(DensityUtil.dip2px(0))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)//缩放
                .setLoadingDrawableId(R.drawable.ic_launcher_background)//加载中默认显示图片
                .setUseMemCache(true)//设置使用缓存
                .setFailureDrawableId(R.drawable.ic_launcher_background)//加载失败后默认显示图片
                .build();
        x.image().bind(img, url, imageOptions);
    }

    public static void showToast(final Activity activity, final String word, final long time)
    {
        activity.runOnUiThread(new Runnable()
        {
            public void run()
            {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    public void run()
                    {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }


    /**
     * 判断文本框是否为空,
     *
     * @param tv
     * @return 都不为空返回假，否则返回真
     */
    public static boolean textIsEmpty(TextView... tv)
    {
        for (TextView item : tv)
        {
            if (TextUtils.isEmpty(item.getText()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断编辑框是否为空,
     *
     * @param editTexts
     * @return 都不为空返回假，否则返回真
     */
    public static boolean editIsEmpty(EditText... editTexts)
    {
        for (EditText item : editTexts)
        {
            if (TextUtils.isEmpty(item.getText()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * List 转换 String
     *
     * @param list
     * @return
     */
    public static String listToStr(List<String> list)
    {
        if (list == null || list.size() <= 0)
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String str : list)
        {
            builder.append(str).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();

    }

    /**
     * get Window Width
     *
     * @param activity
     * @return
     */

    public static int getWindowWidth(Activity activity)
    {
        return activity.getWindowManager().getDefaultDisplay().getWidth();
    }

    /**
     * get Window Height
     *
     * @param activity
     * @return
     */
    public static int getWindowHeight(Activity activity)
    {
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }
}
