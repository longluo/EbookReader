package com.longluo.ebookreader.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.longluo.ebookreader.bean.Cache;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.libs.LibAntiword;
import com.longluo.ebookreader.libs.LibMobi;
import com.longluo.ebookreader.ui.activity.ReadActivity;
import com.longluo.viewer.DocumentActivity;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class BookUtils {
    private static final String LOG_TAG = "BookUtils";

    private static final String cachedPath = Environment.getExternalStorageDirectory() + "/ebookreader/";

    //存储的字符数
    public static final int cachedSize = 30000;
//    protected final ArrayList<WeakReference<char[]>> myArray = new ArrayList<>();

    protected final ArrayList<Cache> myArray = new ArrayList<>();
    //目录
    private List<BookContent> directoryList = new ArrayList<>();

    private String strCharsetName;
    private String bookName;
    private String bookPath;
    private long bookLen;
    private long position;
    private BookMeta bookMeta;

    public BookUtils() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    public static boolean isBookFormatSupport(String fileName) {
        if (fileName.endsWith(".txt") || fileName.endsWith(".epub") || fileName.endsWith(".mobi")
                || fileName.endsWith(".azw") || fileName.endsWith(".azw3") || fileName.endsWith(".pdf")
                || fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return true;
        }

        return false;
    }

    public synchronized void openBook(BookMeta bookMeta) throws IOException {
        this.bookMeta = bookMeta;
        //如果当前缓存不是要打开的书本就缓存书本同时删除缓存

        if (bookPath == null || !bookPath.equals(bookMeta.getBookPath())) {
            cleanCacheFile();
            this.bookPath = bookMeta.getBookPath();
            bookName = FileUtils.getFileName(bookPath);
            cacheBook();
        }
    }

    public static void openBook(Activity activity, BookMeta bookMeta) {
        String filePath = bookMeta.getBookPath();
        File file = new File(filePath);
        String suffix = FileUtils.getSuffix(filePath);

        Timber.d("openBook: filePath=" + filePath + ", suffix=" + suffix);

        if (suffix.equals("txt")) {
            ReadActivity.openBook(activity, bookMeta);
        } else if (suffix.equals("epub") || suffix.equals("pdf")) {
            openEpubPdfBook(activity, file);
        } else if (suffix.equals("mobi") || suffix.equals("azw") || suffix.equals("azw3") || suffix.equals("azw4")) {
            openMobiAzwBook(activity, file);
        }
    }

    public static void openEpubPdfBook(Activity activity, File file) {
        Intent intent = new Intent(activity, DocumentActivity.class);
        // API>=21: intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT); /* launch as a new document */
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT); /* launch as a new document */
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        activity.startActivity(intent);
    }

    public static void openMobiAzwBook(Activity activity, File file) {
        String path = file.getAbsolutePath();
        String folderPath = path.substring(0, path.lastIndexOf("/"));

        String hashCodeStr = path.hashCode() + "";
        String convertFilePath = folderPath + File.separator + hashCodeStr + ".epub";
        Timber.d("openMobiAzwBook: file=" + path + ", folder=" + folderPath
                + ",convertFilePath=" + convertFilePath);
        File convertFile = new File(convertFilePath);
        if (!convertFile.exists()) {
            LibMobi.convertToEpub(path, new File(folderPath, hashCodeStr).getPath());
        }
        File firstConvertFile = new File(folderPath + File.separator + hashCodeStr + hashCodeStr + ".epub");
        if (firstConvertFile.exists()) {
            firstConvertFile.renameTo(new File(convertFilePath));
        }
        openEpubPdfBook(activity, convertFile);
    }

    public static void openDocFile(Activity activity, File file) {
        String path = file.getAbsolutePath();
        String folderPath = path.substring(0, path.lastIndexOf("/"));

        String hashCodeStr = path.hashCode() + "";
        String convertFilePath = folderPath + File.separator + hashCodeStr + ".epub";
        Timber.d("openMobiAzwBook: file=" + path + ", folder=" + folderPath
                + ",convertFilePath=" + convertFilePath);
        File convertFile = new File(convertFilePath);
        if (!convertFile.exists()) {
            LibAntiword.convertDocToHtml(path, new File(folderPath, hashCodeStr).getPath());
        }
        File firstConvertFile = new File(folderPath + File.separator + hashCodeStr + hashCodeStr + ".epub");
        if (firstConvertFile.exists()) {
            firstConvertFile.renameTo(new File(convertFilePath));
        }
        openEpubPdfBook(activity, convertFile);
    }

    private void cleanCacheFile() {
        File file = new File(cachedPath);
        if (!file.exists()) {
            file.mkdir();
        } else {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
        }
    }

    public int next(boolean back) {
        position += 1;
        if (position > bookLen) {
            position = bookLen;
            return -1;
        }
        char result = current();
        if (back) {
            position -= 1;
        }
        return result;
    }

    public char[] nextLine() {
        if (position >= bookLen) {
            return null;
        }
        String line = "";
        while (position < bookLen) {
            int word = next(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\r") && (((char) next(true)) + "").equals("\n")) {
                next(false);
                break;
            }
            line += wordChar;
        }
        return line.toCharArray();
    }

    public char[] preLine() {
        if (position <= 0) {
            return null;
        }
        String line = "";
        while (position >= 0) {
            int word = pre(false);
            if (word == -1) {
                break;
            }
            char wordChar = (char) word;
            if ((wordChar + "").equals("\n") && (((char) pre(true)) + "").equals("\r")) {
                pre(false);
//                line = "\r\n" + line;
                break;
            }
            line = wordChar + line;
        }
        return line.toCharArray();
    }

    public char current() {
        int cachePos = 0;
        int pos = 0;
        int len = 0;
        for (int i = 0; i < myArray.size(); i++) {
            long size = myArray.get(i).getSize();
            if (size + len - 1 >= position) {
                cachePos = i;
                pos = (int) (position - len);
                break;
            }
            len += size;
        }

        char[] charArray = block(cachePos);
        return charArray[pos];
    }

    public int pre(boolean back) {
        position -= 1;
        if (position < 0) {
            position = 0;
            return -1;
        }
        char result = current();
        if (back) {
            position += 1;
        }
        return result;
    }

    public long getPosition() {
        return position;
    }

    public void setPostition(long position) {
        this.position = position;
    }

    //缓存书本
    private void cacheBook() throws IOException {
        if (TextUtils.isEmpty(bookMeta.getCharset())) {
            strCharsetName = FileUtils.getCharset(bookPath);
            if (strCharsetName == null) {
                strCharsetName = "utf-8";
            }
            ContentValues values = new ContentValues();
            values.put("charset", strCharsetName);
            LitePal.update(BookMeta.class, values, bookMeta.getId());
        } else {
            strCharsetName = bookMeta.getCharset();
        }

        File file = new File(bookPath);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), strCharsetName);
        int index = 0;
        bookLen = 0;
        directoryList.clear();
        myArray.clear();
        while (true) {
            char[] buf = new char[cachedSize];
            int result = reader.read(buf);
            if (result == -1) {
                reader.close();
                break;
            }

            String bufStr = new String(buf);
            bufStr = bufStr.replaceAll("\r\n+\\s*", "\r\n\u3000\u3000");
            bufStr = bufStr.replaceAll("\u0000", "");
            buf = bufStr.toCharArray();
            bookLen += buf.length;

            Cache cache = new Cache();
            cache.setSize(buf.length);
            cache.setData(new WeakReference<char[]>(buf));

            myArray.add(cache);
            try {
                File cacheBook = new File(fileName(index));
                if (!cacheBook.exists()) {
                    cacheBook.createNewFile();
                }
                final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(fileName(index)), "UTF-16LE");
                writer.write(buf);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException("Error during writing " + fileName(index));
            }
            index++;
        }

        new Thread() {
            @Override
            public void run() {
                getChapter();
            }
        }.start();
    }

    //获取章节
    public synchronized void getChapter() {
        try {
            long size = 0;
            for (int i = 0; i < myArray.size(); i++) {
                char[] buf = block(i);
                String bufStr = new String(buf);
                String[] paragraphs = bufStr.split("\r\n");
                for (String str : paragraphs) {
                    if (str.length() <= 30 && (str.matches(".*第.{1,8}章.*") || str.matches(".*第.{1,8}节.*"))) {
                        BookContent bookContent = new BookContent();
                        bookContent.setBookContentStartPos(size);
                        bookContent.setBookContent(str);
                        bookContent.setBookPath(bookPath);
                        directoryList.add(bookContent);
                    }
                    if (str.contains("\u3000\u3000")) {
                        size += str.length() + 2;
                    } else if (str.contains("\u3000")) {
                        size += str.length() + 1;
                    } else {
                        size += str.length();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<BookContent> getDirectoryList() {
        return directoryList;
    }

    public long getBookLen() {
        return bookLen;
    }

    protected String fileName(int index) {
        return cachedPath + bookName + index;
    }

    //获取书本缓存
    public char[] block(int index) {
        if (myArray.size() == 0) {
            return new char[1];
        }
        char[] block = myArray.get(index).getData().get();
        if (block == null) {
            try {
                File file = new File(fileName(index));
                int size = (int) file.length();
                if (size < 0) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                block = new char[size / 2];
                InputStreamReader reader =
                        new InputStreamReader(
                                new FileInputStream(file),
                                "UTF-16LE"
                        );
                if (reader.read(block) != block.length) {
                    throw new RuntimeException("Error during reading " + fileName(index));
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException("Error during reading " + fileName(index));
            }
            Cache cache = myArray.get(index);
            cache.setData(new WeakReference<char[]>(block));
        }

        return block;
    }
}