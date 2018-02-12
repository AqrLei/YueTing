package com.example.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2018/2/12.
 */

public class PdfRendererFragment extends Fragment implements View.OnTouchListener, View.OnClickListener {


    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    private static final String FILENAME = "sample.pdf";

    private ParcelFileDescriptor mFileDescriptor;

    private PdfRenderer mPdfRenderer;

    private PdfRenderer.Page mCurrentPage;

    private ImageView mImageView;
    private TextView mTvTop;
    private TextView mTvBottom;

    private int mPageIndex;
    private int touchSlop;
    private int width;
    private int height;

    private GestureDetector gestureDetector;

    public PdfRendererFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pdf_renderer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mImageView = view.findViewById(R.id.iv_pdf_read);
        mTvBottom = view.findViewById(R.id.tv_pdf_bottom);
        mTvTop = view.findViewById(R.id.tv_pdf_top);
        WindowManager wm = this.getActivity().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;

        mImageView.setOnTouchListener(this);
        mImageView.setOnClickListener(this);
        touchSlop = ViewConfiguration.get(this.getActivity()).getScaledTouchSlop();
        gestureDetector = new GestureDetector(this.getActivity(), new MoveGestureListener());
        mPageIndex = 0;
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            openRenderer(getActivity());
            showPage(mPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage.getIndex());
        }
    }


    private void openRenderer(Context context) throws IOException {

        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {

            InputStream asset = context.getAssets().open(FILENAME);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }


    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mPdfRenderer.close();
        mFileDescriptor.close();
    }


    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index || index < 0) {
            return;
        }

        if (null != mCurrentPage) {
            mCurrentPage.close();
        }

        mCurrentPage = mPdfRenderer.openPage(index);

        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(),
                Bitmap.Config.ARGB_8888);
        bitmap = bitmapZoom(bitmap,
                (float) width,
                (float) height);

        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        mImageView.setImageBitmap(bitmap);

        updateUi();
    }


    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();

    }

    private Bitmap bitmapZoom(Bitmap bitmap, float newWidth, float newHeight) {
        int oldWidth = bitmap.getWidth();
        int oldHeight = bitmap.getHeight();
        float scaleWidth = newWidth / oldWidth;
        float scaleHeight = newHeight / oldHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, oldWidth, oldHeight, matrix, true);
    }


    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {

        mTvTop.setVisibility(mTvTop.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        mTvTop.bringToFront();
        mTvBottom.setVisibility(mTvBottom.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        mTvBottom.bringToFront();
    }

    private class MoveGestureListener extends GestureDetector.SimpleOnGestureListener {
        private float moveX;

        @Override
        public boolean onDown(MotionEvent e) {
            //mImageView.performClick();
            moveX = e.getX();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (Math.abs(e.getX() - moveX) <= touchSlop) {
                mImageView.performClick();
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (e2.getX() - e1.getX() > 0 && Math.abs(e2.getX() - e1.getX()) > touchSlop) {
                showPage(mCurrentPage.getIndex() - 1);
                return true;
            }
            if (e2.getX() - e1.getX() < 0 && Math.abs(e2.getX() - e1.getX()) > touchSlop) {
                showPage(mCurrentPage.getIndex() + 1);

                return true;

            }
            return false;
        }
    }
}
