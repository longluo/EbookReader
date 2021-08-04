package com.longluo.ebookreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;


public class EpubActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.image)
    ImageView mImage;

    @BindView(R.id.tv_text)
    TextView mTvText;

    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private Book book;
    private MyAdatper mAdatper;
    private List<EpubBean> indexTitleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epub);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mAdatper = new MyAdatper(indexTitleList, this);
        mRecycler.setAdapter(mAdatper);

        try {
            EpubReader reader = new EpubReader();
            InputStream in = getAssets().open("176116.epub");
            book = reader.readEpub(in);

            //获取封面图方法一：
           /* Bitmap coverImage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
            if (coverImage!=null) {
                mImageView.setImageBitmap(coverImage);
            }else {
                Log.i(TAG, "onCreate: mImageView is null");
            }*/
            //  获取封面图方法二：
            /*nl.siegmann.epublib.domain.Resources resources = book.getResources();
            Resource res = resources.getById("cover");
            byte[] data = res.getData();
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            mImage.setImageBitmap(bitmap);*/

            Metadata metadata = book.getMetadata();

            StringBuffer buffer = new StringBuffer();
            for (String s : metadata.getDescriptions()) {
                buffer.append(s + " ");
            }

            /*String bookInfo = "作者：" + metadata.getAuthors() +
                    "\n出版社：" + metadata.getPublishers() +
                    "\n出版时间：" + metadata.getDates() +
                    "\n书名：" + metadata.getTitles() +
                    "\n简介：" + metadata.getDescriptions() +
                    "\n语言：" + metadata.getLanguage() +
                    "\n\n封面图：";*/

            String bookInfo = "作者：" + metadata.getAuthors().get(0) +
                    "\n出版社：" + metadata.getPublishers().get(0) +
                    "\n出版时间：" + TimeUtils.getStringData(metadata.getDates().get(0).getValue()) +
                    "\n书名：" + metadata.getTitles().get(0) +
                    "\n简介：" + metadata.getDescriptions().get(0) +
                    "\n语言：" + metadata.getLanguage() +
                    "\n\n封面图：";

            mTvText.setText(bookInfo);

            Log.i(TAG, "onCreate: bookInfo=" + bookInfo);

            // 书籍的阅读顺序，是一个线性的顺序。通过Spine可以知道应该按照怎样的章节,顺序去阅读，
            // 并且通过Spine可以找到对应章节的内容。
            Spine spine = book.getSpine();

            List<SpineReference> spineReferences = spine.getSpineReferences();
            if (spineReferences != null && spineReferences.size() > 0) {
                Resource resource = spineReferences.get(1).getResource();//获取带章节信息的那个html页面

                Log.i(TAG, "initView: book=" + resource.getId() + "  " + resource.getTitle() + "  " + resource.getSize() + " ");

                byte[] data = resource.getData();//和 resource.getInputStream() 返回的都是html格式的文章内容，只不过读取方式不一样
                String strHtml = StringUtils.bytes2Hex(data);
                Log.i(TAG, "initView: strHtml= " + strHtml);

                parseHtmlData(strHtml);

              /*  InputStream inputStream = resource.getInputStream();
                String strHtml = StringUtils.convertStreamToString(inputStream);
                Log.i(TAG, "initView: strHtml=" + strHtml);*/

            } else {
                Log.i(TAG, "initView: spineReferences is null");
            }

            // 获取所有章节内容。测试发现和 spine.getSpineReferences() 效果差不多
           /* List<Resource> contents = book.getContents();
            if (contents != null && contents.size() > 0) {
                try {
                    Resource resource = contents.get(1);
                    //byte[] data = resource.getData();
                    InputStream inputStream = resource.getInputStream();
                    String dddd = StringUtils.convertStreamToString(inputStream);
                    Log.i(TAG, "onCreate: dddd=" + dddd);
                    //mTextView.setText(Html.fromHtml(dddd));
                    //  mWebView.loadDataWithBaseURL(null, dddd, "text/html", "utf-8", null);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.i(TAG, "onCreate: contents is null");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析html
     */
    private void parseHtmlData(String strHtml) throws IOException {
        Document doc = Jsoup.parse(strHtml);
        Log.i(TAG, "parseHtmlData:  doc.title();=" + doc.title());
        Elements eles = doc.getElementsByTag("a"); // a标签
        // 遍历Elements的每个Element
        EpubBean epubBean;
        for (Element link : eles) {
            String linkHref = link.attr("href"); // a标签的href属性
            String text = link.text();
            epubBean = new EpubBean();
            epubBean.href = linkHref;
            epubBean.tilte = text;
            indexTitleList.add(epubBean);
            Log.i(TAG, "parseHtmlData: linkHref=" + linkHref + " text=" + text);
        }
    }

    private class MyAdatper extends RecyclerView.Adapter<MyAdatper.ViewHolder> {
        private final LayoutInflater mInflater;
        private List<EpubBean> mStrings;

        public MyAdatper(List<EpubBean> mStrings, Context context) {
            this.mStrings = mStrings;
            mInflater = LayoutInflater.from(context);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.book_title);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.layout_item, null);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.mTextView.setText(mStrings.get(position).tilte);
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通过href获取
                    String href = mStrings.get(position).href;
                    Intent intent = new Intent(EpubActivity.this, ChapterDetailActivity.class);
                    intent.putExtra("href", href);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mStrings.size();
        }
    }
}