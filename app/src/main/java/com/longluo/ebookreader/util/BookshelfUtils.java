package com.longluo.ebookreader.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.longluo.ebookreader.constant.AppConstants;
import com.longluo.ebookreader.db.BookMeta;

import org.litepal.LitePal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.longluo.util.StringUtils;

/**
 * 添加删除Book
 */
public class BookshelfUtils {
    private static final Pattern chapterNamePattern = Pattern.compile("^(.*?第([\\d零〇一二两三四五六七八九十百千万壹贰叁肆伍陆柒捌玖拾佰仟０-９\\s]+)[章节篇回集])[、，。　：:.\\s]*");

    public static String getCachePathName(String bookName, String tag) {
        return formatFolderName(bookName + "-" + tag);
    }

    @SuppressLint("DefaultLocale")
    public static String getCacheFileName(int chapterIndex, String chapterName) {
        return String.format("%05d-%s", chapterIndex, formatFolderName(chapterName));
    }

    public static void clearCaches(boolean clearChapterList) {
        FileUtils.deleteFile(AppConstants.BOOK_CACHE_PATH);
        FileUtils.getFolder(AppConstants.BOOK_CACHE_PATH);
        if (clearChapterList) {
//            DbHelper.getDaoSession().getBookChapterBeanDao().deleteAll();
        }
    }

    /**
     * 删除章节文件
     */
    public static void delChapter(String folderName, int index, String fileName) {
        FileUtils.deleteFile(AppConstants.BOOK_CACHE_PATH + folderName
                + File.separator + getCacheFileName(index, fileName) + FileUtils.SUFFIX_NB);
    }

    /**
     * 存储章节
     */
    public static synchronized boolean saveChapterInfo(String folderName, int index, String fileName, String content) {
        if (content == null) {
            return false;
        }
        File file = getBookFile(folderName, index, fileName);
        //获取流并存储
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(fileName + "\n\n");
            writer.write(content);
            writer.write("\n\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建或获取存储文件
     */
    public static File getBookFile(String folderName, int index, String fileName) {
        return FileUtils.createFileIfNotExist(AppConstants.BOOK_CACHE_PATH + formatFolderName(folderName)
                + File.separator + getCacheFileName(index, fileName) + FileUtils.SUFFIX_NB);
    }

    private static String formatFolderName(String folderName) {
        return folderName.replaceAll("[\\\\/:*?\"<>|.]", "");
    }

    private static int getChapterNum(String chapterName) {
        if (chapterName != null) {
            Matcher matcher = chapterNamePattern.matcher(chapterName);
            if (matcher.find()) {
                return StringUtils.stringToInt(matcher.group(2));
            }
        }
        return -1;
    }

    private static String getPureChapterName(String chapterName) {
        return chapterName == null ? ""
                : StringUtils.fullToHalf(chapterName).replaceAll("\\s", "")
                .replaceAll("^第.*?章|[(\\[][^()\\[\\]]{2,}[)\\]]$", "")
                .replaceAll("[^\\w\\u4E00-\\u9FEF〇\\u3400-\\u4DBF\\u20000-\\u2A6DF\\u2A700-\\u2EBEF]", "");
        // 所有非字母数字中日韩文字 CJK区+扩展A-F区
    }

    /**
     * 获取所有书籍
     */
    public static List<BookMeta> getAllBook() {
/*        List<BookMeta> bookShelfList = DbHelper.getDaoSession().getBookShelfBeanDao().queryBuilder()
                .orderDesc(BookShelfBeanDao.Properties.FinalDate).list();
        for (int i = 0; i < bookShelfList.size(); i++) {
            BookInfoBean bookInfoBean = DbHelper.getDaoSession().getBookInfoBeanDao().queryBuilder()
                    .where(BookInfoBeanDao.Properties.NoteUrl.eq(bookShelfList.get(i).getNoteUrl())).limit(1).build().unique();
            if (bookInfoBean != null) {
                bookShelfList.get(i).setBookInfoBean(bookInfoBean);
            } else {
                bookShelfList.remove(i);
                i--;
            }
        }*/

        List<BookMeta> bookShelfList = LitePal.findAll(BookMeta.class);

        return bookShelfList;
    }

    /**
     * 获取书籍按bookUrl
     */
    public static BookMeta getBook(String bookUrl) {
//        BookMeta bookShelfBean = DbHelper.getDaoSession().getBookShelfBeanDao().load(bookUrl);

        List<BookMeta> bookMetaList = LitePal.where("bookPath = ?", bookUrl).find(BookMeta.class);

//        if (bookShelfBean != null) {
//            BookInfoBean bookInfoBean = DbHelper.getDaoSession().getBookInfoBeanDao().load(bookUrl);
//            if (bookInfoBean != null) {
//                bookShelfBean.setBookInfoBean(bookInfoBean);
//                return bookShelfBean;
//            }
//        }

        return bookMetaList.get(0);
    }

    /**
     * 移除书籍
     */
    public static void removeFromBookShelf(BookMeta bookShelfBean, boolean keepCaches) {
//        DbHelper.getDaoSession().getBookShelfBeanDao().deleteByKey(bookShelfBean.getNoteUrl());
//        DbHelper.getDaoSession().getBookInfoBeanDao().deleteByKey(bookShelfBean.getBookInfoBean().getNoteUrl());
//        delChapterList(bookShelfBean.getNoteUrl());
//        if (!keepCaches) {
//            String bookName = bookShelfBean.getBookInfoBean().getName();
//            // 如果书架上有其他同名书籍，只删除本书源的缓存
//            long bookNum = DbHelper.getDaoSession().getBookInfoBeanDao().queryBuilder()
//                    .where(BookInfoBeanDao.Properties.Name.eq(bookName)).count();
//            if (bookNum > 0) {
//                FileUtils.deleteFile(AppConstants.BOOK_CACHE_PATH + getCachePathName(bookShelfBean.getBookInfoBean().getName(), bookShelfBean.getTag()));
//                return;
//            }
//            // 没有同名书籍，删除本书所有的缓存
//            try {
//                File file = FileUtils.getFolder(AppConstants.BOOK_CACHE_PATH);
//                String[] bookCaches = file.list((dir, name) -> new File(dir, name).isDirectory() && name.startsWith(bookName + "-"));
//                for (String bookPath : bookCaches) {
//                    FileUtils.deleteFile(AppConstants.BOOK_CACHE_PATH + bookPath);
//                }
//            } catch (Exception ignored) {
//            }
//        }
    }

    /**
     * 移除书籍
     */
    public static void removeFromBookShelf(BookMeta bookShelfBean) {
        removeFromBookShelf(bookShelfBean, false);
    }

    /**
     * 保存书籍
     */
    public static void saveBookToShelf(BookMeta bookShelfBean) {
//        if (bookShelfBean.getErrorMsg() == null) {
//            DbHelper.getDaoSession().getBookInfoBeanDao().insertOrReplace(bookShelfBean.getBookInfoBean());
//            DbHelper.getDaoSession().getBookShelfBeanDao().insertOrReplace(bookShelfBean);
//        }
    }

    public static String getReadProgress(int durChapterIndex, int chapterAll, int durPageIndex, int durPageAll) {
        DecimalFormat df = new DecimalFormat("0.0%");
        if (chapterAll == 0 || (durPageAll == 0 && durChapterIndex == 0)) {
            return "0.0%";
        } else if (durPageAll == 0) {
            return df.format((durChapterIndex + 1.0f) / chapterAll);
        }
        String percent = df.format(durChapterIndex * 1.0f / chapterAll + 1.0f / chapterAll * (durPageIndex + 1) / durPageAll);
        if (percent.equals("100.0%") && (durChapterIndex + 1 != chapterAll || durPageIndex + 1 != durPageAll)) {
            percent = "99.9%";
        }
        return percent;
    }

    public static String formatAuthor(String author) {
        if (author == null) {
            return "";
        }
        return author.replaceAll("作\\s*者[\\s:：]*", "").replaceAll("\\s+", " ").trim();
    }

    public static int guessChapterNum(String name) {
        if (TextUtils.isEmpty(name) || name.matches("第.*?卷.*?第.*[章节回]"))
            return -1;
        Matcher matcher = chapterNamePattern.matcher(name);
        if (matcher.find()) {
            return StringUtils.stringToInt(matcher.group(2));
        }
        return -1;
    }

    /**
     * 排序
     */
    public static void order(List<BookMeta> books, String bookshelfOrder) {
        if (books == null || books.size() == 0) {
            return;
        }
        switch (bookshelfOrder) {
            case "0":
//                Collections.sort(books, (o1, o2) -> Long.compare(o2.getFinalDate(), o1.getFinalDate()));
                break;

            case "1":
//                Collections.sort(books, (o1, o2) -> Long.compare(o2.getFinalRefreshData(), o1.getFinalRefreshData()));
                break;

            case "2":
//                Collections.sort(books, (o1, o2) -> Integer.compare(o1.getSerialNumber(), o2.getSerialNumber()));
                break;
        }
    }

    /**
     * 清除书架
     */
    public static void clearBookshelf() {
        // TODO: 2022/1/1

    }
}
