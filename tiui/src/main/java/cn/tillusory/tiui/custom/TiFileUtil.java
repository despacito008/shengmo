package cn.tillusory.tiui.custom;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cn.tillusory.sdk.TiSDK;

public class TiFileUtil {

    public static void resetFile(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String newSticker = getJsonString(context, "sticker/stickers.json");
                String modifiedSticker = modifyJsonByExistsFile(context, newSticker, "stickers");
                String stickerPath = TiSDK.getStickerPath(context) + File.separator + "stickers.json";
//                modifyFile(newSticker, stickerPath);
                modifyFile(modifiedSticker, stickerPath);

                String newInteraction = getJsonString(context, "interaction/interactions.json");
                String modifiedInteraction = modifyJsonByExistsFile(context, newInteraction, "interactions");
                String interactionPath = TiSDK.getInteractionPath(context) + File.separator + "interactions.json";
//                modifyFile(newInteraction, interactionPath);
                modifyFile(modifiedInteraction, interactionPath);

                String newMask = getJsonString(context, "mask/masks.json");
                String modifiedMask = modifyJsonByExistsFile(context, newMask, "masks");
                String maskPath = TiSDK.getMaskPath(context) + File.separator + "masks.json";
//                modifyFile(newMask, maskPath);
                modifyFile(modifiedMask, maskPath);

                String newGift = getJsonString(context, "gift/gifts.json");
                String modifiedGift = modifyJsonByExistsFile(context, newGift, "gifts");
                String giftPath = TiSDK.getGiftPath(context) + File.separator + "gifts.json";
//                modifyFile(newGift, giftPath);
                modifyFile(modifiedGift, giftPath);

                String newWatermark = getJsonString(context, "watermark/watermarks.json");
                String watermarkPath = TiSDK.getWatermarkPath(context) + File.separator + "watermarks.json";
                modifyFile(newWatermark, watermarkPath);

                String newGreenScreen = getJsonString(context, "greenscreen/greenscreens.json");
                String greenScreenPath = TiSDK.getGreenScreenPath(context) + File.separator + "greenscreens.json";
                modifyFile(newGreenScreen, greenScreenPath);

                String newPortrait = getJsonString(context, "portrait/portraits.json");
                String modifiedPortrait = modifyJsonByExistsFile(context, newPortrait, "portraits");
                String portraitPath = TiSDK.getPortraitPath(context) + File.separator + "portraits.json";
//                modifyFile(newPortrait, portraitPath);
                modifyFile(modifiedPortrait, portraitPath);

                String newMakeup = getJsonString(context, "makeup/makeups.json");
                String modifiedMakeup = modifyJsonByExistsFile(context, newMakeup, "makeups");
                String makeupPath = TiSDK.getMakeupPath(context) + File.separator + "makeups.json";
//                modifyFile(newMakeup, makeupPath);
                modifyFile(modifiedMakeup, makeupPath);
            }
        }).start();
    }

    public static String modifyJsonByExistsFile(Context context, String srcJson, String objectName) {
        int count = 0;
        JsonObject jsonObject = new Gson().fromJson(srcJson, JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray(objectName);
        for (JsonElement jsonElement : jsonArray) {
            JsonObject item = jsonElement.getAsJsonObject();
            String itemName = "";
            String itemType = "";
            if (item.has("name")) {
                itemName = item.get("name").getAsString();
            }
            if (item.has("type")) {
                itemType = item.get("type").getAsString();
            }

            String path = getResourcePath(context, objectName, itemName, itemType);
            if (isFileExists(path)) {
                item.addProperty("downloaded", true);
                count++;
            }
        }

        JsonObject result = new JsonObject();
        result.add(objectName, jsonArray);
//        Log.e("mylog", "downloaded " + count + ", " + objectName + ".json-->" +  result.toString());
        return result.toString();
    }

    private static String getResourcePath(Context context, String objectName, String itemName, String itemType) {
        String path = "";
        switch (objectName) {
            case "stickers":
                path = TiSDK.getStickerPath(context) + File.separator + itemName;
                break;
            case "interactions":
                path = TiSDK.getInteractionPath(context) + File.separator + itemName;
                break;
            case "masks":
                path = TiSDK.getMaskPath(context) + File.separator + itemName;
                break;
            case "gifts":
                path = TiSDK.getGiftPath(context) + File.separator + itemName;
                break;
            case "portraits":
                path = TiSDK.getPortraitPath(context) + File.separator + itemName;
                break;
            case "makeups":
                path = TiSDK.getMakeupPath(context) + File.separator + itemType + File.separator + itemName;
                break;
        }
        return path;
    }

    private static boolean isFileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static String getJsonString(Context context, String fileName) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            AssetManager manager = context.getAssets();
            br = new BufferedReader(new InputStreamReader(manager.open(fileName)));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    private static void modifyFile(String content, String filePath) {
        try {
            FileWriter fileWriter = new FileWriter(filePath, false);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制assets目录下所有文件及文件夹到指定路径
     *
     * @param context 上下文
     * @param mAssetsPath Assets目录的相对路径
     * @param mSavePath 复制文件的保存路径
     * @return void
     */
    public static void copyAssetsFiles(Context context, String mAssetsPath, String mSavePath) {
        try {
            // 获取assets目录下的所有文件及目录名
            String[] fileNames = context.getAssets().list(mAssetsPath);
            if (fileNames.length > 0) {
                // 若是目录
                for (String fileName : fileNames) {
                    //排除系统自动写在assets中的文件
//                    if (fileName.endsWith(".xml") || fileName.equals("feature") || fileName.equals("images") || fileName.equals("pfw") || fileName.equals("webkit")) {
//                        continue;
//                    }

                    String newAssetsPath;
                    // 确保Assets路径后面没有斜杠分隔符，否则将获取不到值
                    if ((mAssetsPath == null) || "".equals(mAssetsPath) || "/".equals(mAssetsPath)) {
                        newAssetsPath = fileName;
                    } else {
                        if (mAssetsPath.endsWith("/")) {
                            newAssetsPath = mAssetsPath + fileName;
                        } else {
                            newAssetsPath = mAssetsPath + "/" + fileName;
                        }
                    }
                    // 递归调用
                    copyAssetsFiles(context, newAssetsPath, mSavePath + "/" + fileName);
                }
            } else {
                // 若是文件
                File file = new File(mSavePath);
                // 若文件夹不存在，则递归创建父目录
                file.getParentFile().mkdirs();
                InputStream is = context.getAssets().open(mAssetsPath);
                FileOutputStream fos = new FileOutputStream(new File(mSavePath));
                byte[] buffer = new byte[1024];
                int byteCount;
                // 循环从输入流读取字节
                while ((byteCount = is.read(buffer)) != -1) {
                    // 将读取的输入流写入到输出流
                    fos.write(buffer, 0, byteCount);
                }
                // 刷新缓冲区
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


