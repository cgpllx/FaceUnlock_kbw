package com.kubeiwu.faceunlock.ui.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.kubeiwu.faceunlock.R;
import com.kubeiwu.faceunlock.core.OnSuccessListener;

public class Draw extends View implements AnimatorUpdateListener {

	private static final float[][] POINTS = { { 0.0F, -1.0F }, { 0.16F, -0.98F }, { 0.34F, -0.92F }, { 0.5F, -0.82F }, { 0.64F, -0.7F }, { 0.74F, -0.54F }, { 0.78F, -0.36F }, { 0.8F, -0.18F }, { 0.78F, 0.0F }, { 0.75F, 0.2F }, { 0.7F, 0.37F }, { 0.62F, 0.54F }, { 0.52F, 0.71F }, { 0.4F, 0.83F }, { 0.26F, 0.94F }, { 0.08F, 1.0F }, { -0.08F, 1.0F }, { -0.26F, 0.94F }, { -0.4F, 0.83F }, { -0.52F, 0.71F }, { -0.62F, 0.54F }, { -0.7F, 0.37F }, { -0.75F, 0.2F }, { -0.78F, 0.0F }, { -0.8F, -0.18F }, { -0.78F, -0.36F }, { -0.74F, -0.54F }, { -0.64F, -0.7F }, { -0.5F, -0.82F }, { -0.34F, -0.92F }, { -0.16F, -0.98F } };

	private Paint mPaint = new Paint();
	private static final int MAX_PROGRESS_COUNT = 1 + POINTS.length;
	private Bitmap mWhiteDot;
	// private ValueAnimator mCheckAlphaAnim;
	private Bitmap mCheckMark;
	private float mProgress = 0f;
	ValueAnimator mCheckAlphaAnim;
	private int mProgressCount = 0;
	// private ValueAnimator mPointAlphaAnim;
	private int mCheckAlpha;

	public Draw(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mPaint.setAntiAlias(true);
		this.mPaint.setColor(-1);
		this.mWhiteDot = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_facial_dot);
		this.mCheckMark = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_facial_dot_glow_correct);
		this.mCheckAlphaAnim = ValueAnimator.ofInt(new int[] { 0, 0 });
		// mCheckAlphaAnim.ofInt(values)
		this.mCheckAlphaAnim.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				// animation.g
				if (mProgressCount >= MAX_PROGRESS_COUNT) {
					if(onSuccessListener!=null){
						onSuccessListener.success();
					}
				}
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		this.mCheckAlphaAnim.addUpdateListener(this);
		this.mCheckAlphaAnim.setDuration(3000L);

	}

	public Draw(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	@Override
	protected void onAttachedToWindow() {
		// this.mPointAlphaAnim.start();
	}

	public Draw(Context context) {
		this(context, null, 0);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		for (int i = 0; i < POINTS.length; i++) {
			float f3 = 0.37F * (POINTS[i][0] * getWidth()) + getWidth() / 2;
			float f4 = 0.37F * (POINTS[i][1] * getWidth()) + getHeight() / 2;
			// f5 = f3 - this.mFinishGlow.getWidth() / 2;
			// f6 = f4 - this.mFinishGlow.getHeight() / 2;
			float f7 = f3 - this.mWhiteDot.getWidth() / 2;
			float f8 = f4 - this.mWhiteDot.getHeight() / 2;

			// float f1 = mProgress * MAX_PROGRESS_COUNT;

			if (i >= mCheckAlpha) {
				canvas.drawBitmap(this.mWhiteDot, f7, f8, this.mPaint);
			} else {
				canvas.drawBitmap(this.mCheckMark, f7, f8, this.mPaint);
			}

		}
	}

	public void setProgress(float progress) {
		mProgress = progress;
		// postInvalidate();
		
		beginGlow();
	}

	@Override
	public void onAnimationUpdate(ValueAnimator animation) {
		this.mCheckAlpha = ((Integer) animation.getAnimatedValue()).intValue();
		postInvalidate();
	}

	void beginGlow() {
		// setProgress(MAX_PROGRESS_COUNT);
		// this.mState = this.DRAW_CHECK;
		// this.mCheckAlphaAnim.
		mProgressCount = (int) (mProgress * MAX_PROGRESS_COUNT);
		mCheckAlphaAnim.setIntValues(mCheckAlpha, mProgressCount);
		System.out.println("mProgressCount=" + mProgressCount);
		System.out.println("mCheckAlpha=" + mCheckAlpha);
		this.mCheckAlphaAnim.start();
		// ValueAnimator.ofInt(new int[] { 0, 33 }).addUpdateListener(this);
	}
	OnSuccessListener onSuccessListener;

	public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
		this.onSuccessListener = onSuccessListener;
	}
	
}
