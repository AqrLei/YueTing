package com.example.testapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //private PageFactory pageFactory;
    public static final String FRAGMENT_PDF_RENDERER_BASIC = "pdf_renderer_basic";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookpage);

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
