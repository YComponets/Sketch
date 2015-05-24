package me.xiaopan.sketch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import me.xiaopan.sketch.util.SketchUtils;

public class FixedRecycleBitmapDrawable extends Drawable implements RecycleDrawableInterface{
    private static final int DEFAULT_PAINT_FLAGS = Paint.FILTER_BITMAP_FLAG | Paint.DITHER_FLAG;
    private int bitmapWidth;
    private int bitmapHeight;
    private Rect srcRect;
    private Rect destRect;
    private Paint paint;
    private FixedSize fixedSize;
    private Bitmap bitmap;
    private RecycleBitmapDrawable recycleBitmapDrawable;

    public FixedRecycleBitmapDrawable(RecycleBitmapDrawable recycleBitmapDrawable) {
        this.recycleBitmapDrawable = recycleBitmapDrawable;
        this.bitmap = recycleBitmapDrawable!=null?recycleBitmapDrawable.getBitmap():null;
        if(bitmap != null){
            this.bitmapWidth = bitmap.getWidth();
            this.bitmapHeight = bitmap.getHeight();
            this.paint = new Paint(DEFAULT_PAINT_FLAGS);
            this.srcRect = new Rect(0, 0, bitmapWidth, bitmapHeight);
            this.destRect = new Rect(0, 0, bitmapWidth, bitmapHeight);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null && !bitmap.isRecycled() && srcRect != null && destRect != null && paint != null) {
            canvas.drawBitmap(bitmap, srcRect, destRect, paint);
        }
    }

    @Override
    public int getIntrinsicWidth() {
        return srcRect!=null?srcRect.width():0;
    }

    @Override
    public int getIntrinsicHeight() {
        return srcRect!=null?srcRect.height():0;
    }

    @Override
    public int getAlpha() {
        return paint!=null?paint.getAlpha():super.getAlpha();
    }

    @Override
    public void setAlpha(int alpha) {
        if(paint != null){
            final int oldAlpha = paint.getAlpha();
            if (alpha != oldAlpha) {
                paint.setAlpha(alpha);
                invalidateSelf();
            }
        }
    }

    @Override
    public ColorFilter getColorFilter() {
        return paint!=null?paint.getColorFilter():null;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if(paint != null){
            paint.setColorFilter(cf);
            invalidateSelf();
        }
    }

    @Override
    public void setDither(boolean dither) {
        if(paint != null){
            paint.setDither(dither);
            invalidateSelf();
        }
    }

    @Override
    public void setFilterBitmap(boolean filter) {
        if(paint != null){
            paint.setFilterBitmap(filter);
            invalidateSelf();
        }
    }

    @Override
    public int getOpacity() {
        return (bitmap == null || paint == null || bitmap.hasAlpha() || paint.getAlpha() < 255) ? PixelFormat.TRANSLUCENT : PixelFormat.OPAQUE;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if(destRect != null){
            destRect.set(0, 0, bounds.width(), bounds.height());
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public FixedSize getFixedSize() {
        return fixedSize;
    }

    public void setFixedSize(FixedSize fixedSize) {
        if(srcRect != null){
            this.fixedSize = fixedSize;
            if(fixedSize == null){
                srcRect.set(0, 0, bitmapWidth, bitmapHeight);
                super.setBounds(0, 0, bitmapWidth, bitmapHeight);
            }else{
                onUpdateFixedSize(fixedSize.getWidth(), fixedSize.getHeight());
            }
            invalidateSelf();
        }
    }

    public void setFixedSize(int width, int height) {
        if(srcRect != null){
            if(this.fixedSize == null){
                this.fixedSize = new FixedSize(width, height);
            }else{
                this.fixedSize.set(width, height);
            }
            onUpdateFixedSize(width, height);
            invalidateSelf();
        }
    }

    protected void onUpdateFixedSize(int fixedWidth, int fixedHeight){
        if(bitmapWidth == 0 || bitmapHeight == 0){
            srcRect.set(0, 0, 0, 0);
        }else if((float)bitmapWidth/(float)bitmapHeight == (float)fixedWidth/(float)fixedHeight){
            srcRect.set(0, 0, bitmapWidth, bitmapHeight);
        }else{
            SketchUtils.mapping(bitmapWidth, bitmapHeight, fixedWidth, fixedHeight, srcRect);
        }
        setBounds(0, 0, srcRect.width(), srcRect.height());
    }

    @Override
    public void setIsDisplayed(String callingStation, boolean displayed) {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.setIsDisplayed(callingStation, displayed);
        }
    }

    @Override
    public void setIsCached(String callingStation, boolean cached) {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.setIsCached(callingStation, cached);
        }
    }

    @Override
    public void setIsWaitDisplay(String callingStation, boolean waitDisplay) {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.setIsWaitDisplay(callingStation, waitDisplay);
        }
    }

    @Override
    public int getByteCount() {
        return recycleBitmapDrawable!=null?recycleBitmapDrawable.getByteCount():0;
    }

    @Override
    public boolean isRecycled() {
        return recycleBitmapDrawable==null || recycleBitmapDrawable.isRecycled();
    }

    @Override
    public String getMimeType() {
        return recycleBitmapDrawable!=null?recycleBitmapDrawable.getMimeType():null;
    }

    @Override
    public void setMimeType(String mimeType) {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.setMimeType(mimeType);
        }
    }

    @Override
    public void recycle() {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.recycle();
        }
    }

    @Override
    public String getSize() {
        return recycleBitmapDrawable!=null?recycleBitmapDrawable.getSize():null;
    }

    @Override
    public String getConfig() {
        return recycleBitmapDrawable!=null?recycleBitmapDrawable.getConfig():null;
    }

    @Override
    public String getInfo() {
        return recycleBitmapDrawable!=null?recycleBitmapDrawable.getInfo():null;
    }

    @Override
    public boolean canRecycle() {
        return recycleBitmapDrawable!=null&&recycleBitmapDrawable.canRecycle();
    }

    @Override
    public void setAllowRecycle(boolean allowRecycle) {
        if(recycleBitmapDrawable != null){
            recycleBitmapDrawable.setAllowRecycle(allowRecycle);
        }
    }
}