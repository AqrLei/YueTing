package com.example.testapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RemoteViews;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    //private PageFactory pageFactory;
    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";

    private RemoteViews remoteView;
    private Notification notification;
    private NotificationManager nm;
    private PDFView pdfView;

    private void test(String... name) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookpage);
        File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/test.pdf";
        pdfView = findViewById(R.id.pdfView);

        pdfView.fromFile(new File(path))
                //  .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(true)
                .enableDoubletap(true)
                .defaultPage(0)
                // allows to draw something on the current page, usually visible in the middle of the screen
                // .onDraw(onDrawListener)
                // allows to draw something on all pages, separately for every page. Called only for visible pages
                // .onDrawAll(onDrawListener)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Log.d("test", "loadComplete");

                    }
                }) // called after document is loaded and starts to be rendered
                .onPageChange(new OnPageChangeListener() {
                    @Override
                    public void onPageChanged(int page, int pageCount) {
                        Log.d("test", "onPageChanged");

                    }
                })
                .onPageScroll(new OnPageScrollListener() {
                    @Override
                    public void onPageScrolled(int page, float positionOffset) {
                        Log.d("test", "onPageScrolled");

                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Log.d("test", "onError");

                    }
                })
                .onPageError(new OnPageErrorListener() {
                    @Override
                    public void onPageError(int page, Throwable t) {
                        Log.d("test", "onPageError");

                    }
                })
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages) {
                        Log.d("test", "onInitiallyRendered:\t" + nbPages);

                    }
                }) // called after document is rendered for the first time
                // called on single tap, return true if handled, false to toggle scroll handle visibility
                .onTap(new OnTapListener() {
                    @Override
                    public boolean onTap(MotionEvent e) {
                        Log.d("test", "OnTap");
                        return false;
                    }
                })
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                //.linkHandler(new DefaultLinkHandler(this.pdfView))
                .pageFitPolicy(FitPolicy.BOTH)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        Log.d("test",
                                pdfView.getDocumentMeta().getAuthor() +
                                        pdfView.getDocumentMeta().getCreationDate() +
                                        pdfView.getDocumentMeta().getCreator() +
                                        pdfView.getDocumentMeta().getProducer() +
                                        pdfView.getDocumentMeta().getTitle() +
                                        pdfView.getDocumentMeta().getSubject() +
                                        pdfView.getDocumentMeta().getKeywords() +
                                        pdfView.getDocumentMeta().getModDate() + ":\t" + nbPages
                        );
                    }
                })
                .load();

      /*  remoteView = new RemoteViews(this.getPackageName(), R.layout.notification_foreground);
        notification = new NotificationCompat.Builder(this)
                //.setContent(remoteView)


                // .setContentTitle("标题")
                // .setContentText("新消息来了")
                .setSmallIcon(R.mipmap.ic_launcher_round)//必须设置，不然无法显示自定义的View
                .setTicker("新消息来了")
                .setOngoing(true)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setDefaults(Notification.DEFAULT_ALL)
                //.setCustomBigContentView(remoteView)
                .setContent(remoteView)
                // .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .build();
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        findViewById(R.id.tvShowNotification).setOnClickListener(this);
*/
       /* BaseDragZoomImageView iv = findViewById(R.id.biv_test);
        Drawable dr = getResources().getDrawable(R.mipmap.ic_launcher, null);
        int w = dr.getIntrinsicWidth();
        int h = dr.getIntrinsicHeight();
        //iv.setImageDrawable(dr);
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        dr.setBounds(0, 0, w, h);
        dr.draw(canvas);
        iv.setImageBitmap(bitmap);*/

        // BookMessage bookMessage = new BookMessage();
     /*   File sd = Environment.getExternalStorageDirectory();
        String path = sd.getPath() + "/Android.pdf";

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, new PdfRendererFragment(),
                            FRAGMENT_PDF_RENDERER_BASIC)
                    .commit();
        }*/


        // String path = sd.getPath() + "/太古神王.txt";
      /*  File file = new File(path);
        bookMessage.setName(file.getName());
        bookMessage.setPath(file.getPath());
        bookMessage.setEncoding("GBK");

        PageView mView = (PageView) findViewById(R.id.pv_page);
        pageFactory = new PageFactory(mView, bookMessage);
        pageFactory.nextPage();
        mView.setOnScrollListener(this);
        *//*Test*//*
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (inputStream != null) {
            String str = getString(inputStream);
            TextView tv = (TextView) findViewById(R.id.tv_test);
            tv.setMovementMethod(ScrollingMovementMethod.getInstance());
            tv.setText(str);

        }
*/
    }

  /*  *//*Test*//*
    private String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder sb = new StringBuilder("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public void onLeftScroll() {
        pageFactory.nextPage();

    }

    @Override
    public void onRightScroll() {
        pageFactory.prePage();
    }*/
   /* public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }*/
}
