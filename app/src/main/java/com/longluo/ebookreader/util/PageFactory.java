package com.longluo.ebookreader.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.longluo.ebookreader.constant.Constants;
import com.longluo.ebookreader.manager.ReadSettingManager;
import com.longluo.ebookreader.R;
import com.longluo.ebookreader.db.BookContent;
import com.longluo.ebookreader.db.BookMeta;
import com.longluo.ebookreader.view.PageWidget;
import com.longluo.ebookreader.widget.page.PageStyle;

import org.litepal.LitePal;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PageFactory {
    private static final String TAG = "PageFactory";
    private static PageFactory pageFactory;

    private Context mContext;
    private ReadSettingManager readSettingManager;
    //当前的书本
//    private File book_file = null;
    // 默认背景颜色
    private int m_backColor = 0xffff9e85;
    //页面宽
    private int mWidth;
    //页面高
    private int mHeight;
    //文字字体大小
    private int mTextSize;
    //时间格式
    private SimpleDateFormat sdf;
    //时间
    private String date;
    //进度格式
    private DecimalFormat df;
    //电池边界宽度
    private float mBorderWidth;
    // 上下与边缘的距离
    private float marginHeight;
    // 左右与边缘的距离
    private float measureMarginWidth;
    // 左右与边缘的距离
    private float marginWidth;
    //状态栏距离底部高度
    private float statusMarginBottom;
    //行间距
    private float lineSpace;
    //段间距
    private float paragraphSpace;
    //字高度
    private float fontHeight;
    //字体
    private Typeface typeface;
    //文字画笔
    private Paint mPaint;
    //加载画笔
    private Paint waitPaint;
    //文字颜色
    private int mTextColor = Color.rgb(50, 65, 78);
    // 绘制内容的宽
    private float mVisibleHeight;
    // 绘制内容的宽
    private float mVisibleWidth;
    // 每页可以显示的行数
    private int mLineCount;
    //电池画笔
    private Paint mBatterryPaint;
    //电池字体大小
    private float mBatterryFontSize;
    //背景图片
    private Bitmap m_book_bg = null;
    //当前显示的文字
//    private StringBuilder word = new StringBuilder();
    //当前总共的行
//    private Vector<String> m_lines = new Vector<>();
//    // 当前页起始位置
//    private long m_mbBufBegin = 0;
//    // 当前页终点位置
//    private long m_mbBufEnd = 0;
//    // 之前页起始位置
//    private long m_preBegin = 0;
//    // 之前页终点位置
//    private long m_preEnd = 0;
    // 图书总长度
//    private long m_mbBufLen = 0;
    private Intent batteryInfoIntent;
    //电池电量百分比
    private float mBatteryPercentage;
    //电池外边框
    private RectF rect1 = new RectF();
    //电池内边框
    private RectF rect2 = new RectF();
    //文件编码
//    private String m_strCharsetName = "GBK";
    //当前是否为第一页
    private boolean m_isfirstPage;
    //当前是否为最后一页
    private boolean m_islastPage;
    //书本widget
    private PageWidget mBookPageWidget;
    //    //书本所有段
//    List<String> allParagraph;
//    //书本所有行
//    List<String> allLines = new ArrayList<>();
    //现在的进度
    private float currentProgress;
    //目录
//    private List<BookCatalogue> directoryList = new ArrayList<>();
    //书本路径
    private String bookPath = "";
    //书本名字
    private String bookName = "";
    private BookMeta bookMeta;
    //书本章节
    private int currentCharter = 0;
    //当前电量
    private int level = 0;
    private BookUtils mBookUtils;
    private PageEvent mPageEvent;
    private TRPage currentPage;
    private TRPage prePage;
    private TRPage cancelPage;
    private BookTask bookTask;
    ContentValues values = new ContentValues();

    private static Status mStatus = Status.OPENING;

    public enum Status {
        OPENING,
        FINISH,
        FAIL,
    }

    public static synchronized PageFactory getInstance() {
        return pageFactory;
    }

    public static synchronized PageFactory createPageFactory(Context context) {
        if (pageFactory == null) {
            pageFactory = new PageFactory(context);
        }
        return pageFactory;
    }

    private PageFactory(Context context) {
        mBookUtils = new BookUtils();
        mContext = context.getApplicationContext();
        readSettingManager = ReadSettingManager.getInstance();
        //获取屏幕宽高
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metric = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metric);
        mWidth = metric.widthPixels;
        mHeight = metric.heightPixels;

        sdf = new SimpleDateFormat("HH:mm");//HH:mm为24小时制,hh:mm为12小时制
        date = sdf.format(new java.util.Date());
        df = new DecimalFormat("#0.0");

        marginWidth = mContext.getResources().getDimension(R.dimen.readingMarginWidth);
        marginHeight = mContext.getResources().getDimension(R.dimen.readingMarginHeight);
        statusMarginBottom = mContext.getResources().getDimension(R.dimen.reading_status_margin_bottom);
        lineSpace = context.getResources().getDimension(R.dimen.reading_line_spacing);
        paragraphSpace = context.getResources().getDimension(R.dimen.reading_paragraph_spacing);
        mVisibleWidth = mWidth - marginWidth * 2;
        mVisibleHeight = mHeight - marginHeight * 2;

        typeface = readSettingManager.getTypeface();
        mTextSize = readSettingManager.getTextSize();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        mPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        mPaint.setTextSize(mTextSize);// 字体大小
        mPaint.setColor(mTextColor);// 字体颜色
        mPaint.setTypeface(typeface);
        mPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果

        waitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 画笔
        waitPaint.setTextAlign(Paint.Align.LEFT);// 左对齐
        waitPaint.setTextSize(mContext.getResources().getDimension(R.dimen.reading_max_text_size));// 字体大小
        waitPaint.setColor(mTextColor);// 字体颜色
        waitPaint.setTypeface(typeface);
        waitPaint.setSubpixelText(true);// 设置该项为true，将有助于文本在LCD屏幕上的显示效果
        calculateLineCount();

        mBorderWidth = mContext.getResources().getDimension(R.dimen.reading_board_battery_border_width);
        mBatterryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBatterryFontSize = CommonUtils.sp2px(context, 12);
        mBatterryPaint.setTextSize(mBatterryFontSize);
        mBatterryPaint.setTypeface(typeface);
        mBatterryPaint.setTextAlign(Paint.Align.LEFT);
        mBatterryPaint.setColor(mTextColor);
        batteryInfoIntent = context.getApplicationContext().registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));//注册广播,随时获取到电池电量信息

        initBg(readSettingManager.isNightMode());
        measureMarginWidth();
    }

    private void measureMarginWidth() {
        float wordWidth = mPaint.measureText("\u3000");
        float width = mVisibleWidth % wordWidth;
        measureMarginWidth = marginWidth + width / 2;

//        Rect rect = new Rect();
//        mPaint.getTextBounds("好", 0, 1, rect);
//        float wordHeight = rect.height();
//        float wordW = rect.width();
//        Paint.FontMetrics fm = mPaint.getFontMetrics();
//        float wrodH = (float) (Math.ceil(fm.top + fm.bottom + fm.leading));
//        String a = "";
    }

    //初始化背景
    private void initBg(Boolean isNight) {
        if (isNight) {
            //设置背景
            Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.BLACK);
            setBgBitmap(bitmap);
            //设置字体颜色
            setTextColor(Color.rgb(128, 128, 128));
            setBookPageBg(Color.BLACK);
        } else {
            setBookPageStyle(readSettingManager.getBookPageStyle());
        }
    }

    private void calculateLineCount() {
        mLineCount = (int) (mVisibleHeight / (mTextSize + lineSpace));// 可显示的行数
    }

    private void drawStatus(Bitmap bitmap) {
        String status = "";
        switch (mStatus) {
            case OPENING:
                status = "正在打开书本...";
                break;

            case FAIL:
                status = "打开书本失败！";
                break;
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
        waitPaint.setColor(getTextColor());
        waitPaint.setTextAlign(Paint.Align.CENTER);

        Rect targetRect = new Rect(0, 0, mWidth, mHeight);
//        c.drawRect(targetRect, waitPaint);
        Paint.FontMetricsInt fontMetrics = waitPaint.getFontMetricsInt();
        // 转载请注明出处：http://blog.csdn.net/hursing
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        // 下面这行是实现水平居中，drawText对应改为传入targetRect.centerX()
        waitPaint.setTextAlign(Paint.Align.CENTER);
        c.drawText(status, targetRect.centerX(), baseline, waitPaint);
//        c.drawText("正在打开书本...", mHeight / 2, 0, waitPaint);
        mBookPageWidget.postInvalidate();
    }

    public void onDraw(Bitmap bitmap, List<String> m_lines, Boolean updateCharter) {
        if (getDirectoryList().size() > 0 && updateCharter) {
            currentCharter = getCurrentCharter();
        }
        //更新数据库进度
        if (currentPage != null && bookMeta != null) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    values.put("begin", currentPage.getBegin());
                    LitePal.update(BookMeta.class, values, bookMeta.getId());
                }
            }.start();
        }

        Canvas c = new Canvas(bitmap);
        c.drawBitmap(getBgBitmap(), 0, 0, null);
//        word.setLength(0);
        mPaint.setTextSize(getFontSize());
        mPaint.setColor(getTextColor());
        mBatterryPaint.setColor(getTextColor());
        if (m_lines.size() == 0) {
            return;
        }

        if (m_lines.size() > 0) {
            float y = marginHeight;
            for (String strLine : m_lines) {
                y += mTextSize + lineSpace;
                c.drawText(strLine, measureMarginWidth, y, mPaint);
//                word.append(strLine);
            }
        }

        //画进度及时间
        int dateWith = (int) (mBatterryPaint.measureText(date) + mBorderWidth);//时间宽度
        float fPercent = (float) (currentPage.getBegin() * 1.0 / mBookUtils.getBookLen());//进度
        currentProgress = fPercent;
        if (mPageEvent != null) {
            mPageEvent.changeProgress(fPercent);
        }
        String strPercent = df.format(fPercent * 100) + "%";//进度文字
        int nPercentWidth = (int) mBatterryPaint.measureText("999.9%") + 1;  //Paint.measureText直接返回參數字串所佔用的寬度
        c.drawText(strPercent, mWidth - nPercentWidth, mHeight - statusMarginBottom, mBatterryPaint);//x y为坐标值
        c.drawText(date, marginWidth, mHeight - statusMarginBottom, mBatterryPaint);
        // 画电池
        level = batteryInfoIntent.getIntExtra("level", 0);
        int scale = batteryInfoIntent.getIntExtra("scale", 100);
        mBatteryPercentage = (float) level / scale;
        float rect1Left = marginWidth + dateWith + statusMarginBottom;//电池外框left位置
        //画电池外框
        float width = CommonUtils.convertDpToPixel(mContext, 20) - mBorderWidth;
        float height = CommonUtils.convertDpToPixel(mContext, 10);
        rect1.set(rect1Left, mHeight - height - statusMarginBottom, rect1Left + width, mHeight - statusMarginBottom);
        rect2.set(rect1Left + mBorderWidth, mHeight - height + mBorderWidth - statusMarginBottom, rect1Left + width - mBorderWidth, mHeight - mBorderWidth - statusMarginBottom);
        c.save();
        c.clipRect(rect2, Region.Op.DIFFERENCE);
        c.drawRect(rect1, mBatterryPaint);
        c.restore();
        //画电量部分
        rect2.left += mBorderWidth;
        rect2.right -= mBorderWidth;
        rect2.right = rect2.left + rect2.width() * mBatteryPercentage;
        rect2.top += mBorderWidth;
        rect2.bottom -= mBorderWidth;
        c.drawRect(rect2, mBatterryPaint);
        //画电池头
        int poleHeight = (int) CommonUtils.convertDpToPixel(mContext, 10) / 2;
        rect2.left = rect1.right;
        rect2.top = rect2.top + poleHeight / 4;
        rect2.right = rect1.right + mBorderWidth;
        rect2.bottom = rect2.bottom - poleHeight / 4;
        c.drawRect(rect2, mBatterryPaint);
        //画书名
        c.drawText(CommonUtils.subString(bookName, 12), marginWidth, statusMarginBottom + mBatterryFontSize, mBatterryPaint);
        //画章
        if (getDirectoryList().size() > 0) {
            String charterName = CommonUtils.subString(getDirectoryList().get(currentCharter).getBookContent(), 12);
            int nChaterWidth = (int) mBatterryPaint.measureText(charterName) + 1;
            c.drawText(charterName, mWidth - marginWidth - nChaterWidth, statusMarginBottom + mBatterryFontSize, mBatterryPaint);
        }

        mBookPageWidget.postInvalidate();
    }

    //向前翻页
    public void prePage() {
        if (currentPage.getBegin() <= 0) {
            Log.e(TAG, "当前是第一页");
            if (!m_isfirstPage) {
                Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show();
            }
            m_isfirstPage = true;
            return;
        } else {
            m_isfirstPage = false;
        }

        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
        currentPage = getPrePage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
    }

    //向后翻页
    public void nextPage() {
        if (currentPage.getEnd() >= mBookUtils.getBookLen()) {
            Log.e(TAG, "已经是最后一页了");
            if (!m_islastPage) {
                Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
            }
            m_islastPage = true;
            return;
        } else {
            m_islastPage = false;
        }

        cancelPage = currentPage;
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), true);
        prePage = currentPage;
        currentPage = getNextPage();
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), true);
        Log.e("nextPage", "nextPagenext");
    }

    //取消翻页
    public void cancelPage() {
        currentPage = cancelPage;
    }

    /**
     * 打开书本
     *
     * @throws IOException
     */
    public void openBook(BookMeta bookMeta) throws IOException {
        //清空数据
        currentCharter = 0;
//        m_mbBufLen = 0;
        initBg(readSettingManager.isNightMode());

        this.bookMeta = bookMeta;
        bookPath = bookMeta.getBookPath();
        bookName = FileUtils.getFileName(bookPath);

        mStatus = Status.OPENING;
        drawStatus(mBookPageWidget.getCurPage());
        drawStatus(mBookPageWidget.getNextPage());
        if (bookTask != null && bookTask.getStatus() != AsyncTask.Status.FINISHED) {
            bookTask.cancel(true);
        }
        bookTask = new BookTask();
        bookTask.execute(bookMeta.getBegin());
    }

    private class BookTask extends AsyncTask<Long, Void, Boolean> {
        private long begin = 0;

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            Log.e("onPostExecute", isCancelled() + "");
            if (isCancelled()) {
                return;
            }
            if (result) {
                PageFactory.mStatus = PageFactory.Status.FINISH;
//                m_mbBufLen = mBookUtil.getBookLen();
                currentPage = getPageForBegin(begin);
                if (mBookPageWidget != null) {
                    currentPage(true);
                }
            } else {
                PageFactory.mStatus = PageFactory.Status.FAIL;
                drawStatus(mBookPageWidget.getCurPage());
                drawStatus(mBookPageWidget.getNextPage());
                Toast.makeText(mContext, "打开书本失败！", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Long... params) {
            begin = params[0];
            try {
                mBookUtils.openBook(bookMeta);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }

    public TRPage getNextPage() {
        mBookUtils.setPostition(currentPage.getEnd());

        TRPage trPage = new TRPage();
        trPage.setBegin(currentPage.getEnd() + 1);
        Log.e("begin", currentPage.getEnd() + 1 + "");
        trPage.setLines(getNextLines());
        trPage.setWholePageStr();
        Log.e("end", mBookUtils.getPosition() + "");
        trPage.setEnd(mBookUtils.getPosition());
        return trPage;
    }

    public TRPage getPrePage() {
        mBookUtils.setPostition(currentPage.getBegin());

        TRPage trPage = new TRPage();
        trPage.setEnd(mBookUtils.getPosition() - 1);
        Log.e("end", mBookUtils.getPosition() - 1 + "");
        trPage.setLines(getPreLines());
        trPage.setWholePageStr();
        Log.e("begin", mBookUtils.getPosition() + "");
        trPage.setBegin(mBookUtils.getPosition());
        return trPage;
    }

    public TRPage getPageForBegin(long begin) {
        TRPage trPage = new TRPage();
        trPage.setBegin(begin);

        mBookUtils.setPostition(begin - 1);
        trPage.setLines(getNextLines());
        trPage.setWholePageStr();
        trPage.setEnd(mBookUtils.getPosition());
        return trPage;
    }

    public List<String> getNextLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        float height = 0;
        String line = "";
        while (mBookUtils.next(true) != -1) {
            char word = (char) mBookUtils.next(false);
            //判断是否换行
            if ((word + "").equals("\r") && (((char) mBookUtils.next(true)) + "").equals("\n")) {
                mBookUtils.next(false);
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = "";
                    width = 0;
//                    height +=  paragraphSpace;
                    if (lines.size() == mLineCount) {
                        break;
                    }
                }
            } else {
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    lines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }

            if (lines.size() == mLineCount) {
                if (!line.isEmpty()) {
                    mBookUtils.setPostition(mBookUtils.getPosition() - 1);
                }
                break;
            }
        }

        if (!line.isEmpty() && lines.size() < mLineCount) {
            lines.add(line);
        }
        for (String str : lines) {
            Log.e(TAG, str + "   ");
        }
        return lines;
    }

    public List<String> getPreLines() {
        List<String> lines = new ArrayList<>();
        float width = 0;
        String line = "";

        char[] par = mBookUtils.preLine();
        while (par != null) {
            List<String> preLines = new ArrayList<>();
            for (int i = 0; i < par.length; i++) {
                char word = par[i];
                float widthChar = mPaint.measureText(word + "");
                width += widthChar;
                if (width > mVisibleWidth) {
                    width = widthChar;
                    preLines.add(line);
                    line = word + "";
                } else {
                    line += word;
                }
            }
            if (!line.isEmpty()) {
                preLines.add(line);
            }

            lines.addAll(0, preLines);

            if (lines.size() >= mLineCount) {
                break;
            }
            width = 0;
            line = "";
            par = mBookUtils.preLine();
        }

        List<String> reLines = new ArrayList<>();
        int num = 0;
        for (int i = lines.size() - 1; i >= 0; i--) {
            if (reLines.size() < mLineCount) {
                reLines.add(0, lines.get(i));
            } else {
                num = num + lines.get(i).length();
            }
            Log.e(TAG, lines.get(i) + "   ");
        }

        if (num > 0) {
            if (mBookUtils.getPosition() > 0) {
                mBookUtils.setPostition(mBookUtils.getPosition() + num + 2);
            } else {
                mBookUtils.setPostition(mBookUtils.getPosition() + num);
            }
        }

        return reLines;
    }

    //上一章
    public void preChapter() {
        if (mBookUtils.getDirectoryList().size() > 0) {
            int num = currentCharter;
            if (num == 0) {
                num = getCurrentCharter();
            }
            num--;
            if (num >= 0) {
                long begin = mBookUtils.getDirectoryList().get(num).getBookContentStartPos();
                currentPage = getPageForBegin(begin);
                currentPage(true);
                currentCharter = num;
            }
        }
    }

    //下一章
    public void nextChapter() {
        int num = currentCharter;
        if (num == 0) {
            num = getCurrentCharter();
        }
        num++;
        if (num < getDirectoryList().size()) {
            long begin = getDirectoryList().get(num).getBookContentStartPos();
            currentPage = getPageForBegin(begin);
            currentPage(true);
            currentCharter = num;
        }
    }

    //获取现在的章
    public int getCurrentCharter() {
        int num = 0;
        for (int i = 0; getDirectoryList().size() > i; i++) {
            BookContent bookContent = getDirectoryList().get(i);
            if (currentPage.getEnd() >= bookContent.getBookContentStartPos()) {
                num = i;
            } else {
                break;
            }
        }
        return num;
    }

    //绘制当前页面
    public void currentPage(Boolean updateChapter) {
        onDraw(mBookPageWidget.getCurPage(), currentPage.getLines(), updateChapter);
        onDraw(mBookPageWidget.getNextPage(), currentPage.getLines(), updateChapter);
    }

    //更新电量
    public void updateBattery(int mLevel) {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            if (level != mLevel) {
                level = mLevel;
                currentPage(false);
            }
        }
    }

    public void updateTime() {
        if (currentPage != null && mBookPageWidget != null && !mBookPageWidget.isRunning()) {
            String mDate = sdf.format(new java.util.Date());
            if (date != mDate) {
                date = mDate;
                currentPage(false);
            }
        }
    }

    //改变进度
    public void changeProgress(float progress) {
        long begin = (long) (mBookUtils.getBookLen() * progress);
        currentPage = getPageForBegin(begin);
        currentPage(true);
    }

    //改变进度
    public void changeChapter(long begin) {
        currentPage = getPageForBegin(begin);
        currentPage(true);
    }

    //改变字体大小
    public void changeFontSize(int fontSize) {
        this.mTextSize = fontSize;
        mPaint.setTextSize(mTextSize);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);
    }

    //改变字体
    public void changeTypeface(Typeface typeface) {
        this.typeface = typeface;
        mPaint.setTypeface(typeface);
        mBatterryPaint.setTypeface(typeface);
        calculateLineCount();
        measureMarginWidth();
        currentPage = getPageForBegin(currentPage.getBegin());
        currentPage(true);
    }

    //改变背景
    public void changeBookBg(int type) {
        setBookBg(type);
        currentPage(false);
    }

    public void changeBookPageStyle(PageStyle pageStyle) {
        setBookPageStyle(pageStyle);
        currentPage(false);
    }

    public void setBookBg(int type) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (type) {
            case Constants.READ_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }
                bitmap = BitmapUtils.decodeSampledBitmapFromResource(
                        mContext.getResources(), R.drawable.paper, mWidth, mHeight);
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_default));
                break;

            case Constants.READ_BG_1:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_1));
                break;

            case Constants.READ_BG_2:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_2));
                break;

            case Constants.READ_BG_3:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(R.color.read_bg_3));
                }
                break;

            case Constants.READ_BG_4:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_4));
                break;

            default:
                break;
        }

        setBgBitmap(bitmap);
        //设置字体颜色
        setTextColor(color);
    }

    public void setBookPageBg(int color) {
        if (mBookPageWidget != null) {
            mBookPageWidget.setBgColor(color);
        }
    }

    public void setBookPageStyle(PageStyle pageStyle) {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        int color = 0;
        switch (pageStyle.ordinal()) {
            case Constants.READ_BG_DEFAULT:
                canvas = null;
                bitmap.recycle();
                if (getBgBitmap() != null) {
                    getBgBitmap().recycle();
                }
                bitmap = BitmapUtils.decodeSampledBitmapFromResource(
                        mContext.getResources(), R.drawable.paper, mWidth, mHeight);
                color = mContext.getResources().getColor(R.color.read_font_default);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_default));
                break;

            case Constants.READ_BG_1:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_1));
                color = mContext.getResources().getColor(R.color.read_font_1);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_1));
                break;

            case Constants.READ_BG_2:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_2));
                color = mContext.getResources().getColor(R.color.read_font_2);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_2));
                break;

            case Constants.READ_BG_3:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_3));
                color = mContext.getResources().getColor(R.color.read_font_3);
                if (mBookPageWidget != null) {
                    mBookPageWidget.setBgColor(mContext.getResources().getColor(R.color.read_bg_3));
                }
                break;

            case Constants.READ_BG_4:
                canvas.drawColor(mContext.getResources().getColor(R.color.read_bg_4));
                color = mContext.getResources().getColor(R.color.read_font_4);
                setBookPageBg(mContext.getResources().getColor(R.color.read_bg_4));
                break;
        }

        setBgBitmap(bitmap);
        //设置字体颜色
        setTextColor(color);
    }

    //设置日间或者夜间模式
    public void setDayOrNight(Boolean isNgiht) {
        initBg(isNgiht);
        currentPage(false);
    }

    public void clear() {
        currentCharter = 0;
        bookPath = "";
        bookName = "";
        bookMeta = null;
        mBookPageWidget = null;
        mPageEvent = null;
        cancelPage = null;
        prePage = null;
        currentPage = null;
    }

    public static Status getStatus() {
        return mStatus;
    }

    public long getBookLen() {
        return mBookUtils.getBookLen();
    }

    public TRPage getCurrentPage() {
        return currentPage;
    }

    //获取书本的章
    public List<BookContent> getDirectoryList() {
        return mBookUtils.getDirectoryList();
    }

    public String getBookPath() {
        return bookPath;
    }

    //是否是第一页
    public boolean isfirstPage() {
        return m_isfirstPage;
    }

    //是否是最后一页
    public boolean islastPage() {
        return m_islastPage;
    }

    //设置页面背景
    public void setBgBitmap(Bitmap BG) {
        m_book_bg = BG;
    }

    //设置页面背景
    public Bitmap getBgBitmap() {
        return m_book_bg;
    }

    //设置文字颜色
    public void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    //获取文字颜色
    public int getTextColor() {
        return this.mTextColor;
    }

    //获取文字大小
    public float getFontSize() {
        return this.mTextSize;
    }

    public void setPageWidget(PageWidget mBookPageWidget) {
        this.mBookPageWidget = mBookPageWidget;
    }

    public void setPageEvent(PageEvent pageEvent) {
        this.mPageEvent = pageEvent;
    }

    public interface PageEvent {
        void changeProgress(float progress);
    }
}
