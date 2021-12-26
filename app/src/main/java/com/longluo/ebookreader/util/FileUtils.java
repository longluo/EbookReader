package com.longluo.ebookreader.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.longluo.ebookreader.App;
import com.longluo.ebookreader.db.BookMeta;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;


public class FileUtils {
    public static String name;
    public static int folderNum = 0;

    /**
     * 计算目录大小
     *
     * @param dir
     * @return
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        // 不是目录
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;

        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                // 递归调用
                dirSize += getDirSize(file);
            }
        }

        return dirSize;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS) {
        if (fileS == 0) {
            return "0.00B";
        }

        DecimalFormat dFormat = new DecimalFormat("#.00");
        String fileSizeString = "";

        if (fileS < 1024) {
            fileSizeString = dFormat.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = dFormat.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = dFormat.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = dFormat.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 文件目录地址
     *
     * @return
     */
    public static String fileDirectory(String dirPath, String fileName) {
        String filePath = "";

        String storageState = Environment.getExternalStorageState();

        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + dirPath;
            File file = new File(filePath);
            if (!file.exists()) {
                // 建立一个新的目录
                file.mkdirs();
            }
            filePath = filePath + fileName;
        }

        return filePath;
    }

    /**
     * 获取文件目录
     *
     * @return
     */
    public static File getDirectoryFile(String dirPath) {
        String storageState = Environment.getExternalStorageState();
        File file = null;

        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            String filePath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + dirPath;
            file = new File(filePath);
            if (!file.exists()) {
                // 建立一个新的目录
                file.mkdirs();
            }
        }

        return file;
    }

    /**
     * 检查文件后缀
     *
     * @param checkItsEnd
     * @param fileEndings
     * @return
     */
    private static boolean checkEndsWithInStringArray(String checkItsEnd,
                                                      String[] fileEndings) {
        for (String aEnd : fileEndings) {
            if (checkItsEnd.endsWith(aEnd))
                return true;
        }

        return false;
    }

    /**
     * 根据不同的后缀打开不同的文件
     *
     * @param fileName
     */
    /**  public static void openFile(Context context, String fileName, File file) {
     Intent intent;
     if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingImage))) {
     intent = OpenFiles.getImageFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingWebText))) {
     intent = OpenFiles.getHtmlFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPackage))) {
     intent = OpenFiles.getApkFileIntent(file);
     context.startActivity(intent);

     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingAudio))) {
     intent = OpenFiles.getAudioFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingVideo))) {
     intent = OpenFiles.getVideoFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingText))) {
     intent = OpenFiles.getTextFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPdf))) {
     intent = OpenFiles.getPdfFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingWord))) {
     intent = OpenFiles.getWordFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingExcel))) {
     intent = OpenFiles.getExcelFileIntent(file);
     context.startActivity(intent);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPPT))) {
     intent = OpenFiles.getPPTFileIntent(file);
     context.startActivity(intent);
     } else {
     Toast.makeText(context, "打开文件错误", Toast.LENGTH_SHORT).show();
     }
     }     */

    /**
     * 根据不同的后缀imageView设置不同的值
     *
     * @param fileName
     */
    /**   public static void setImage(Context context, String fileName,
     ImageView imageView) {
     if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingImage))) {
     imageView.setImageResource(R.drawable.file_icon_picture);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingWebText))) {
     imageView.setImageResource(R.drawable.file_icon_txt);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPackage))) {
     imageView.setImageResource(R.drawable.file_icon_rar);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingAudio))) {
     imageView.setImageResource(R.drawable.file_icon_mp3);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingVideo))) {
     imageView.setImageResource(R.drawable.file_icon_video);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingText))) {
     imageView.setImageResource(R.drawable.file_icon_txt);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPdf))) {
     imageView.setImageResource(R.drawable.file_icon_pdf);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingWord))) {
     imageView.setImageResource(R.drawable.file_icon_office);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingExcel))) {
     imageView.setImageResource(R.drawable.file_icon_office);
     } else if (checkEndsWithInStringArray(fileName, context.getResources()
     .getStringArray(R.array.fileEndingPPT))) {
     imageView.setImageResource(R.drawable.file_icon_office);
     } else {
     imageView.setImageResource(R.drawable.file);
     }
     }    */

    /**
     * 返回本地文件列表
     *
     * @param //本地文件夹路径
     */
    public static List<File> getFileListByPath(String path) {
        BookMeta bookMeta = new BookMeta();
        int fileNum = 0;

        File dir = new File(path);
        List<File> folderList = new ArrayList<File>();
        List<File> fileList = new ArrayList<File>();

        // 获取指定盘符下的所有文件列表。（listFiles可以获得指定路径下的所有文件，以数组方式返回）
        File[] files = dir.listFiles();
        // 如果该目录下面为空，则该目录的此方法执行
        if (files == null) {
            return folderList;
        }

        // 通过循环将所遍历所有文件
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isHidden()) {
                if (files[i].isDirectory()) {
                    folderList.add(files[i]);
                    folderNum++;
                }
                if (files[i].isFile()) {
                    if (files[i].toString().contains(".txt")) {           //txt".equals(extName)
                        fileList.add(files[i]);
                        //   name = files[i].toString();
//                        FileActivity.paths.add(files[i].toString());
                    }
                }
            }

            Log.d("Fileutil", folderNum + "");
        }

        folderList.addAll(fileList);

        return folderList;
    }

    public static int getFileNum(List<File> list) {
        File file;
        int num = 0;
        for (int i = 0; i < list.size(); i++) {
            file = list.get(i);
            if (file.isFile()) {
                num++;
            }
        }

        return num;
    }

    /**
     * 复制一个目录及其子目录、文件到另外一个目录
     *
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void copyFolder(File src, File dest) {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                // 递归复制
                copyFolder(srcFile, destFile);
            }
        } else {
            InputStream in;
            OutputStream out;
            try {
                in = new FileInputStream(src);
                out = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param src  源文件路径
     * @param name 源文件名字
     * @param dest 目标目录
     */
    public static void copyFile(File src, String name, File dest) {
        File file = new File(dest, name);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }

            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录或文件
     */
    public static void createDirorFile(String path, String name, Context context, int check) {
        File file = new File(path + File.separator + name);

        if (check == 0) {// 如果为文件
            try {
                file.createNewFile();
                Toast.makeText(context, "创建文件成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "创建文件失败", Toast.LENGTH_SHORT).show();
            }
        } else if (check == 1) {
            // 创建目录
            if (file.mkdirs()) {
                Toast.makeText(context, "创建目录成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "创建目录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 删除一个目录
     */
    public static void deleteDir(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file); // 递规的方式删除文件夹
        }
        dir.delete();
    }

    /**
     * @param fromDir  这个为源目录
     * @param fromPath 这个为源目录的上一级路径
     * @param toName   要修改的名字
     */
    public static boolean renameFile(File fromDir, String fromPath,
                                     String toName) {
        File tempFile = new File(fromPath + File.separator + toName);
        if (tempFile.exists()) {
            return false;
        } else {
            return fromDir.renameTo(tempFile);
        }
    }

    /**
     * 去掉文件扩展名
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * 获取文件编码
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getCharset(String fileName) throws IOException {
        String charset;
        FileInputStream fis = new FileInputStream(fileName);
        byte[] buf = new byte[4096];
        // (1)
        UniversalDetector detector = new UniversalDetector(null);
        // (2)
        int nread;
        while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
            detector.handleData(buf, 0, nread);
        }
        // (3)
        detector.dataEnd();
        // (4)
        charset = detector.getDetectedCharset();
        // (5)
        detector.reset();
        return charset;
    }

    /**
     * 根据路径获取文件名
     *
     * @param pathandname
     * @return
     */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return "";
        }
    }

    public static final String getPrefix(@NonNull String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static final String getSuffix(@NonNull String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static List<File> getSuffixFile(String filePath, String suffix) {
        List<File> files = new ArrayList<>();
        File file = new File(filePath);
        return getSuffixFile(files, file, suffix);
    }

    /**
     * 读取sd卡上指定后缀的所有文件
     *
     * @param files   返回的所有文件
     * @param file     路径(可传入sd卡路径)
     * @param suffix 后缀名称 比如 .gif
     * @return
     */
    public static List<File> getSuffixFile(List<File> files, File file, final String suffix) {
        if (!file.exists()) {
            return null;
        }

        File[] subFiles = file.listFiles();
        for (File subFile : subFiles) {
            if (subFile.isHidden()) {
                continue;
            }
            if (subFile.isDirectory()) {
                getSuffixFile(files, subFile, suffix);
            } else if (subFile.getName().endsWith(suffix)) {
                files.add(subFile);
            } else {
                //非指定目录文件 不做处理
            }

//            Log.e("filename",subFile.getName());
        }

        return files;
    }

    //获取Cache文件夹
    public static String getCachePath(){
        if (isSdCardExist()){
            return App.getContext()
                    .getExternalCacheDir()
                    .getAbsolutePath();
        }
        else{
            return App.getContext()
                    .getCacheDir()
                    .getAbsolutePath();
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 本来是获取File的内容的。但是为了解决文本缩进、换行的问题
     * 这个方法就是专门用来获取书籍的...
     *
     * 应该放在BookRepository中。。。
     * @param file
     * @return
     */
    public static String getFileContent(File file){
        Reader reader = null;
        String str = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            while ((str = br.readLine()) != null){
                //过滤空语句
                if (!str.equals("")){
                    //由于sb会自动过滤\n,所以需要加上去
                    sb.append("    "+str+"\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(reader);
        }
        return sb.toString();
    }

    //判断是否挂载了SD卡
    public static boolean isSdCardExist(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    //递归删除文件夹下的数据
    public static synchronized void deleteFile(String filePath){
        File file = new File(filePath);
        if (!file.exists()) return;

        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File subFile : files){
                String path = subFile.getPath();
                deleteFile(path);
            }
        }
        //删除文件
        file.delete();
    }

    //由于递归的耗时问题，取巧只遍历内部三层

    //获取txt文件
    public static List<File> getTxtFiles(String filePath,int layer){
        List txtFiles = new ArrayList();
        File file = new File(filePath);

        //如果层级为 3，则直接返回
        if (layer == 3){
            return txtFiles;
        }

        //获取文件夹
        File[] dirs = file.listFiles(
                pathname -> {
                    if (pathname.isDirectory() && !pathname.getName().startsWith(".")) {
                        return true;
                    }
                    //获取txt文件
                    else if(pathname.getName().endsWith(".txt")){
                        txtFiles.add(pathname);
                        return false;
                    }
                    else{
                        return false;
                    }
                }
        );
        //遍历文件夹
        for (File dir : dirs){
            //递归遍历txt文件
            txtFiles.addAll(getTxtFiles(dir.getPath(),layer + 1));
        }
        return txtFiles;
    }

    //由于遍历比较耗时
    public static Single<List<File>> getSDTxtFile(){
        //外部存储卡路径
        String rootPath = Environment.getExternalStorageDirectory().getPath();
        return Single.create(new SingleOnSubscribe<List<File>>() {
            @Override
            public void subscribe(SingleEmitter<List<File>> e) throws Exception {
                List<File> files = getTxtFiles(rootPath,0);
                e.onSuccess(files);
            }
        });
    }

    //获取文件的编码格式
    public static Charset getFileCharset(String fileName) {
        BufferedInputStream bis = null;
        Charset charset = Charset.GBK;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(fileName));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = Charset.UTF8;
                checked = true;
            }
            /*
             * 不支持 UTF16LE 和 UTF16BE
            else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Charset.UTF16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = Charset.UTF16BE;
                checked = true;
            } else */

            bis.mark(0);
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = Charset.UTF8;
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(bis);
        }
        return charset;
    }
}
