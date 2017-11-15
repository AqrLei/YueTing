package com.example.testapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * Author : AqrLei
 * Name : MyLearning
 * Description :
 * Date : 2017/11/9.
 */

public class PageFactory {
    private int screenHeight, screenWidth;
    private int pageHeight, pageWidth;
    private int lineNumber;
    private Paint mPaint;
    private Context mContext;
    private int margin;
    private int lineSpace;
    private PageView mView;
    private Canvas mCanvas;
    private String encoding;
    private int fileLength;
    private int end;
    private int begin;
    private int fontSize = 45;
    private MappedByteBuffer mappedFile;
    private RandomAccessFile randomFile;

    private ArrayList<String> content = new ArrayList<>();

    public PageFactory(PageView pageView, Book book) {

        DisplayMetrics metrics = new DisplayMetrics();
        mContext = pageView.getContext();
        margin = Util.getPXWithDP(mContext, 5);
        lineSpace = Util.getPXWithDP(mContext, 5);
        mView = pageView;
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenHeight = metrics.heightPixels;
        screenWidth = metrics.widthPixels;
        pageHeight = screenHeight - 2 * margin;
        pageWidth = screenWidth - 2 * margin;
        lineNumber = pageHeight / (fontSize + lineSpace) - 1;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(fontSize);
        mPaint.setColor(Color.BLACK);

        Bitmap bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mView.setBitmap(bitmap);
        mCanvas = new Canvas(bitmap);
        mCanvas.drawColor(Color.YELLOW);
        content.add(book.getName());
        content.add(book.getPath());
        openBook(book);


    }

    private void openBook(Book book) {
        encoding = book.getEncoding();
        File file = new File(book.getPath());
        begin = 0;
        end = 0;
        fileLength = (int) file.length();
        try {
            randomFile = new RandomAccessFile(file, "r");
            mappedFile = randomFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, fileLength);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("testApp", "打开失败");
        }
        // Log.d("testApp", book.getName());
    }

    public void nextPage() {
        if (end >= fileLength) {
            return;
        } else {
            content.clear();
            pageDown();
        }
        printPage();

    }

    private void pageDown() {
        String strParagraph = "";
        int i = 0;
        while ((content.size() < lineNumber) && (end < fileLength)) {
            byte[] byteTemp = readParagraphForward(end);
            end += byteTemp.length;
            try {
                strParagraph = new String(byteTemp, encoding);
            } catch (Exception e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ");
            strParagraph = strParagraph.replaceAll("\n", " ");
            while (strParagraph.length() > 0) {
                i++;
                int size = mPaint.breakText(strParagraph, true, pageWidth,
                        null);
                content.add(strParagraph.substring(0, size));
                strParagraph = strParagraph.substring(size);
                if (content.size() >= lineNumber) {
                    break;
                }
            }
            if (strParagraph.length() > 0) {
                try {
                    end -= (strParagraph).getBytes(encoding).length;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
       /* Log.d("testHeight", "Height: " + pageHeight + "I: " + i + "LineNumber: " + lineNumber +
                "Height: " + (lineNumber * (fontSize + lineSpace)) +
                "Margin: " + margin+"Size:" +content.size());*/

    }

    private byte[] readParagraphForward(int end) {
        byte b0;
        int before = 0;
        int i = end;
        while (i < fileLength) {
            b0 = mappedFile.get(i);
            if (encoding.equals("UTF-16LE")) {
                if (b0 == 0 && before == 10) {
                    break;
                }
            } else {
                if (b0 == 10) {
                    break;
                }
            }
            before = b0;
            i++;
        }
        i = Math.min(fileLength - 1, i);
        int nParaSize = i - end + 1;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mappedFile.get(end + i);
        }
        return buf;
    }

    public void prePage() {
        if (begin <= 0) {
            return;
        } else {
            content.clear();
            pageUp();
            end = begin;
            pageDown();
        }
        printPage();

    }

    private void pageUp() {
        String strParagraph = "";
        List<String> tempList = new ArrayList<>();
        while (tempList.size() < lineNumber && begin > 0) {
            byte[] byteTemp = readParagraphBack(begin);
            begin -= byteTemp.length;
            try {
                strParagraph = new String(byteTemp, encoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "  ");
            strParagraph = strParagraph.replaceAll("\n", "  ");
            while (strParagraph.length() > 0) {
                int size = mPaint.breakText(strParagraph, true, pageWidth, null);
                tempList.add(strParagraph.substring(0, size));
                strParagraph = strParagraph.substring(size);
                if (tempList.size() >= lineNumber) {
                    break;
                }
            }
            if (strParagraph.length() > 0) {
                try {
                    begin += strParagraph.getBytes(encoding).length;
                } catch (UnsupportedEncodingException u) {
                    u.printStackTrace();
                }
            }
        }

    }

    private byte[] readParagraphBack(int begin) {
        byte b0;
        byte before = 1;
        int i = begin - 1;
        while (i > 0) {
            b0 = mappedFile.get(i);
            if (encoding.equals("UTF-16LE")) {
                if (b0 == 10 && before == 0 && i != begin - 2) {
                    i += 2;
                    break;
                }
            } else {
                if (b0 == 0X0a && i != begin - 1) {
                    i++;
                    break;
                }
            }
            i--;
            before = b0;
        }
        int nParaSize = begin - i;
        byte[] buf = new byte[nParaSize];
        for (int j = 0; j < nParaSize; j++) {
            buf[j] = mappedFile.get(i + j);
        }
        return buf;
    }

    private void printPage() {
        int y = margin;
        mCanvas.drawColor(Color.YELLOW);
        for (String line : content) {
            y += fontSize + lineSpace;
            mCanvas.drawText(line, margin, y, mPaint);
            // Log.d("text",line);
        }
        mView.invalidate();
    }


}
