/*
 * Copyright (C) 2018 Jenly Yu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.eohi.zxinglibrary;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.eohi.zxinglibrary.camera.CameraManager;
import com.eohi.zxinglibrary.util.BitMapUtil;
import com.eohi.zxinglibrary.util.CodeUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.util.EnumMap;
import java.util.Map;

public class CaptureActivity extends AppCompatActivity implements OnCaptureCallback {

    public static final String KEY_RESULT = Intents.Scan.RESULT;

    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private View ivTorch;
    private ImageView ivBack;

    private CaptureHelper mCaptureHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layoutId = getLayoutId();
        if (isContentView(layoutId)) {
            setContentView(layoutId);
        }
        initUI();
        mCaptureHelper.onCreate();
    }

    /**
     * ?????????
     */
    public void initUI() {
        ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1001);
            }
        });
        surfaceView = findViewById(getSurfaceViewId());
        int viewfinderViewId = getViewfinderViewId();
        if (viewfinderViewId != 0) {
            viewfinderView = findViewById(viewfinderViewId);
        }
        int ivTorchId = getIvTorchId();
        if (ivTorchId != 0) {
            ivTorch = findViewById(ivTorchId);
            ivTorch.setVisibility(View.INVISIBLE);
        }
        initCaptureHelper();
    }

    public void initCaptureHelper() {
        mCaptureHelper = new CaptureHelper(this, surfaceView, viewfinderView, ivTorch);
        mCaptureHelper.setOnCaptureCallback(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (data != null) {
                Uri uri = data.getData();
                String imagePath = BitMapUtil.getPicturePathFromUri(CaptureActivity.this, uri);
                //??????????????????????????????????????????
                Bitmap bitmap = BitMapUtil.compressPicture(imagePath);
                Result result = setZxingResult(bitmap);
                if (result == null) {
                    Toast.makeText(CaptureActivity.this, "?????????????????????", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent();
                    intent.putExtra(Intents.Scan.RESULT,result.getText());
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }

            }
        }
    }

    /**
     * ??????true?????????????????????{@link #setContentView(int)}????????????false????????????????????????{@link #setContentView(int)}
     *
     * @param layoutId
     * @return ????????????true
     */
    public boolean isContentView(@LayoutRes int layoutId) {
        return true;
    }

    /**
     * ??????id
     *
     * @return
     */
    public int getLayoutId() {
        return R.layout.zxl_capture;
    }

    /**
     * {@link #viewfinderView} ??? ID
     *
     * @return ????????????{@code R.id.viewfinderView}, ????????????????????????????????????0
     */
    public int getViewfinderViewId() {
        return R.id.viewfinderView;
    }


    /**
     * ????????????{@link #surfaceView} ???ID
     *
     * @return
     */
    public int getSurfaceViewId() {
        return R.id.surfaceView;
    }

    /**
     * ?????? {@link #ivTorch} ???ID
     *
     * @return ????????????{@code R.id.ivTorch}, ??????????????????????????????????????????0
     */
    public int getIvTorchId() {
        return R.id.ivTorch;
    }

    /**
     * Get {@link CaptureHelper}
     *
     * @return {@link #mCaptureHelper}
     */
    public CaptureHelper getCaptureHelper() {
        return mCaptureHelper;
    }

    /**
     * Get {@link CameraManager} use {@link #getCaptureHelper()#getCameraManager()}
     *
     * @return {@link #mCaptureHelper#getCameraManager()}
     */
    @Deprecated
    public CameraManager getCameraManager() {
        return mCaptureHelper.getCameraManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCaptureHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mCaptureHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCaptureHelper.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCaptureHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * ????????????????????????
     *
     * @param result ????????????
     * @return ??????true???????????????????????????????????????????????????false?????????????????????????????????
     */
    @Override
    public boolean onResultCallback(String result) {
        return false;
    }


    private static Result setZxingResult(Bitmap bitmap) {
        if (bitmap == null) return null;
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int[] pix = new int[picWidth * picHeight];
        //Log.e(TAG, "decodeFromPicture:??????????????? " + bitmap.getByteCount() / 1024 / 1024 + "M");
        bitmap.getPixels(pix, 0, picWidth, 0, 0, picWidth, picHeight);
        //??????LuminanceSource??????
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(picWidth
                , picHeight, pix);
        BinaryBitmap bb = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //?????????????????????????????????????????????????????????QRCodeReader????????????
        QRCodeReader qrCodeReader = new QRCodeReader();
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        hints.put(DecodeHintType.TRY_HARDER, true);
        Result result;
        try {
            result = qrCodeReader.decode(bb, hints);
            return result;
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
            return null;
        }
    }

}