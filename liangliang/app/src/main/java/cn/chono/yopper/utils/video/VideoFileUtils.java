package cn.chono.yopper.utils.video;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.chono.yopper.utils.CheckUtil;

public class VideoFileUtils {

    /**
     * 保存路径的文件夹名称
     */
    public static String DIR_VIDEO = "video";
    public static String DIR_MP4 = "mp4";
    public static String DIR_MUSIC = "music";
    public static String DIR_MP4_IMG="image";

    public static String MY_DIR_VIDEO = "myvideo";


    // /**
    // * 给指定的文件名按照时间命名
    // */
    // private static SimpleDateFormat OUTGOING_DATE_FORMAT =
    // new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
    /**
     * 给指定的文件名按照时间命名
     */
    private static long OUTGOING_DATE_FORMAT = System.currentTimeMillis();

    // new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");

    /**
     * 得到指定的Video保存路径
     *
     * @return
     */
    public static File getDoneVideoPath(Context context) {
        File dir=null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            dir = new File(Environment.getExternalStorageDirectory() + File.separator + DIR_VIDEO);

            if (!dir.exists()) {
                dir.mkdirs();
            }
        }else{
            dir = new File(context.getFilesDir()+ File.separator + DIR_VIDEO);

            if (!dir.exists()) {
                dir.mkdirs();
            }
        }


        return dir;
    }

    public static File getDoneVideoPath() {
        File dir=null;
        dir = new File(Environment.getExternalStorageDirectory() + File.separator + DIR_VIDEO);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }



    /**
     * 得到输出的Video--mp4保存路径
     *
     * @return
     */
    public static String newOutgoingFilePath() {


        File dir = new File(getDoneVideoPath() + File.separator+DIR_MP4);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName =dir+File.separator+ OUTGOING_DATE_FORMAT + ".mp4";
        return fileName;
    }

    public static String  newOUtgoingMp4ImageFilePath(){
        File dir = new File(getDoneVideoPath() + File.separator+DIR_MP4_IMG);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName =dir+File.separator;

        return fileName;
    };


    public static File getVideoMuiscRoot(){
        return new File(getDoneVideoPath() + File.separator+DIR_MUSIC);
    }


    public static void saveImgFile(Bitmap bitmap,String fileName,int id) {
        try {

            File dir=new File(getVideoMuiscRoot()+File.separator+id+File.separator+fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File nameFile = new File(dir+File.separator+"icon_without_name.png");
            if (!nameFile.exists()) {
                nameFile.createNewFile();


                if (null != bitmap) {
                    LogUtils.e("开始文件");
                    FileOutputStream fOut = null;

                    fOut = new FileOutputStream(nameFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fOut);
                    Log.e("TAG", "保存成功，你爽不爽");
                    fOut.flush();
                    fOut.close();

                    // 回收内存空间
                    bitmap.recycle();
                    System.gc();

                }
            }

        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.e("IOException"+e.getMessage());
            e.printStackTrace();
        }

    }

    public static File  getVideoMuiacFilePath(String fileName,int id){
        File dir=new File(getVideoMuiscRoot()+File.separator+id+File.separator+fileName);
        return  dir;
    }

    public static boolean isVideoMuiacFile(String fileName,int id){
        File dir=new File(getVideoMuiacFilePath(fileName, id)+File.separator+"audio.mp3");
        if (!dir.exists()) {
            return false;
        }
        return true;
    }

    public static void saveVideoMuiacFile(byte [] muiscByte,String fileName,int id){
        try{
            File dir=new File(getVideoMuiscRoot()+File.separator+id+File.separator+fileName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File nameFile = new File(dir+File.separator+"audio.mp3");
            if (!nameFile.exists()) {

                FileOutputStream fos = new FileOutputStream(nameFile);


                fos.write(muiscByte);
                fos.close();
            }

        } catch (FileNotFoundException e) {
            LogUtils.e("FileNotFoundException"+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.e("IOException"+e.getMessage());
            e.printStackTrace();
        }
    }

    public static  List<VideoFileUtils.VideoMuiscData> saveMuiscFile( File[] files){

        List<VideoFileUtils.VideoMuiscData> videoMuiscDataList=new ArrayList<>();


        try {
            if (files==null)
                return videoMuiscDataList ;



            for (File f :files){

                if (f.isDirectory()){//目录存在

                    if (isNumeric(f.getName())){
                        if (f.listFiles() !=null ){
                            for (File f2 :f.listFiles()){
                                if (f2.isDirectory()){
                                    if (f2.listFiles() !=null){

                                        for (File f3 :f2.listFiles()){


                                            LogUtils.e("FileNotFoundException"+f.getName());
                                            LogUtils.e("FileNotFoundException"+f2.getName());
                                            LogUtils.e("FileNotFoundException"+f3.getName());
                                            LogUtils.e("------------------------------------------------------");
                                            if (!f3.isDirectory()){
                                                String fileName = f3.getName();
                                                if (TextUtils.equals(fileName,"audio.mp3")){

                                                    VideoFileUtils.VideoMuiscData videoMuiscData =new VideoFileUtils.VideoMuiscData();
                                                    videoMuiscData.id=Integer.valueOf(f.getName());//
                                                    videoMuiscData.name=f2.getName();//
                                                    videoMuiscData.muiscPath=f2.getPath();
                                                    videoMuiscDataList.add(videoMuiscData);

                                                }
                                            }
                                        }
                                    }

                                }
                            }
                        }
                    }



                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  videoMuiscDataList;
    }

    public static boolean isNumeric(String str){
        Pattern pattern=Pattern.compile("[0-9]");
        Matcher isNum=pattern.matcher(str);
        if (!isNum.matches()){
            return  false;
        }
        return true;
    }


    public static class  VideoMuiscData{

        public int id;
        public  String  name;
        public  String muiscPath;
    }


    public static boolean saveMyVideoFile(Context context,byte [] videoByte,String fileName){
        boolean issuccess=false;
        File dir=null;
        try{
            dir=new File(getDoneVideoPath(context) + File.separator+MY_DIR_VIDEO);

            long size=getFileSize(dir);

            if(size/(1024*1024)>=200){
                dir.delete();
            }

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File nameFile = new File(dir+File.separator+fileName+".mp4");
            if (!nameFile.exists()) {

                FileOutputStream fos = new FileOutputStream(nameFile);

                fos.write(videoByte);
                fos.close();
                issuccess= true;
            }

        } catch (Exception e) {

            if(dir!=null && dir.exists()){
                dir.delete();
            }
            issuccess= false;
            LogUtils.e("Exception" + e.getMessage());
            e.printStackTrace();
        }
        return issuccess;
    }

    public static boolean isMyVideoFileExists(Context context,String fileName){
        if(CheckUtil.isEmpty(fileName)){
            return false;
        }
        File dir=new File(getDoneVideoPath(context) + File.separator+MY_DIR_VIDEO+File.separator+fileName+".mp4");
        if (!dir.exists()) {
            return false;
        }
        return true;
    }

    //取得文件夹大小
    public static long getFileSize(File dir){
        long size = 0;
        try{
            if(dir!=null && dir.exists()){

                File flist[] = dir.listFiles();
                for (int i = 0; i < flist.length; i++)
                {
                    if (flist[i].isDirectory())
                    {
                        size = size + getFileSize(flist[i]);
                    } else
                    {
                        size = size + flist[i].length();
                    }
                }

            }
        }catch (Exception e){
            size=0;
        }
        return size;
    }

}
