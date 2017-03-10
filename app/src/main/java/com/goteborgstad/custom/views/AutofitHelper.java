package com.goteborgstad.custom.views;

import android.widget.TextView;


public class AutofitHelper {

	private static final String TAG = "AutoFitTextHelper";
	private static final boolean SPEW = false;
	// Minimum size of the text in pixels
	private static final int DEFAULT_MIN_TEXT_SIZE = 8; // sp
	// How precise we want to be when reaching the target textWidth size
	private static final float DEFAULT_PRECISION = 0.3f;

	/**
	 * Creates a new instance of {@link AutofitHelper} that wraps a
	 * {@link TextView} and enables automatically sizing the text to fit.
	 */
	public static com.goteborgstad.custom.views.AutofitHelper create(android.widget.TextView view) {
		return create(view, null, 0);
	}

	/**
	 * Creates a new instance of {@link AutofitHelper} that wraps a
	 * {@link TextView} and enables automatically sizing the text to fit.
	 */
	public static com.goteborgstad.custom.views.AutofitHelper create(android.widget.TextView view, android.util.AttributeSet attrs) {
		return create(view, attrs, 0);
	}

	/**
	 * Creates a new instance of {@link AutofitHelper} that wraps a
	 * {@link TextView} and enables automatically sizing the text to fit.
	 */
	public static com.goteborgstad.custom.views.AutofitHelper create(android.widget.TextView view, android.util.AttributeSet attrs,
																	 int defStyle) {
		com.goteborgstad.custom.views.AutofitHelper helper = new com.goteborgstad.custom.views.AutofitHelper(view);
		boolean sizeToFit = true;
		if (attrs != null) {
			android.content.Context context = view.getContext();
			int minTextSize = (int) helper.getMinTextSize();
			float precision = helper.getPrecision();
			android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs,
					com.goteborgstad.R.styleable.AutofitTextView, defStyle, 0);
			sizeToFit = ta.getBoolean(com.goteborgstad.R.styleable.AutofitTextView_sizeToFit,
					sizeToFit);
			minTextSize = ta.getDimensionPixelSize(
					com.goteborgstad.R.styleable.AutofitTextView_minTextSize, minTextSize);
			precision = ta.getFloat(com.goteborgstad.R.styleable.AutofitTextView_precision,
					precision);
			ta.recycle();
			helper.setMinTextSize(minTextSize).setPrecision(precision);
		}
		helper.setEnabled(sizeToFit);
		return helper;
	}

	/**
	 * Re-sizes the textSize of the TextView so that the text fits within the
	 * bounds of the View.
	 */
	private static void autofit(android.widget.TextView view, android.text.TextPaint paint,
								float minTextSize, float maxTextSize, int maxLines, float precision) {
		if (maxLines <= 0 || maxLines == Integer.MAX_VALUE) {
			// Don't auto-size since there's no limit on lines.
			return;
		}
		int targetWidth = view.getWidth() - view.getPaddingLeft()
				- view.getPaddingRight();
		if (targetWidth <= 0) {
			return;
		}
		CharSequence text = view.getText();
		android.text.method.TransformationMethod method = view.getTransformationMethod();
		if (method != null) {
			text = method.getTransformation(text, view);
		}
		android.content.Context context = view.getContext();
		android.content.res.Resources r = android.content.res.Resources.getSystem();
		android.util.DisplayMetrics displayMetrics;
		float size = maxTextSize;
		float high = size;
		float low = 0;
		if (context != null) {
			r = context.getResources();
		}
		displayMetrics = r.getDisplayMetrics();
		paint.set(view.getPaint());
		paint.setTextSize(size);
		if ((maxLines == 1 && paint.measureText(text, 0, text.length()) > targetWidth)
				|| getLineCount(text, paint, size, targetWidth, displayMetrics) > maxLines) {
			size = getAutofitTextSize(text, paint, targetWidth, maxLines, low,
					high, precision, displayMetrics);
		}
		if (size < minTextSize) {
			size = minTextSize;
		}
		view.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, size);
	}

	/**
	 * Recursive binary search to find the best size for the text.
	 */
	private static float getAutofitTextSize(CharSequence text, android.text.TextPaint paint,
			float targetWidth, int maxLines, float low, float high,
			float precision, android.util.DisplayMetrics displayMetrics) {
		float mid = (low + high) / 2.0f;
		int lineCount = 1;
		android.text.StaticLayout layout = null;
		paint.setTextSize(android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_PX,
				mid, displayMetrics));
		if (maxLines != 1) {
			layout = new android.text.StaticLayout(text, paint, (int) targetWidth,
					android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
			lineCount = layout.getLineCount();
		}
		if (SPEW)
			com.goteborgstad.custom.Global.ShowLog(TAG, "low=" + low + " high=" + high + " mid=" + mid
					+ " target=" + targetWidth + " maxLines=" + maxLines
					+ " lineCount=" + lineCount);
		if (lineCount > maxLines) {
			return getAutofitTextSize(text, paint, targetWidth, maxLines, low,
					mid, precision, displayMetrics);
		} else if (lineCount < maxLines) {
			return getAutofitTextSize(text, paint, targetWidth, maxLines, mid,
					high, precision, displayMetrics);
		} else {
			float maxLineWidth = 0;
			if (maxLines == 1) {
				maxLineWidth = paint.measureText(text, 0, text.length());
			} else {
				for (int i = 0; i < lineCount; i++) {
					if (layout.getLineWidth(i) > maxLineWidth) {
						maxLineWidth = layout.getLineWidth(i);
					}
				}
			}
			if ((high - low) < precision) {
				return low;
			} else if (maxLineWidth > targetWidth) {
				return getAutofitTextSize(text, paint, targetWidth, maxLines,
						low, mid, precision, displayMetrics);
			} else if (maxLineWidth < targetWidth) {
				return getAutofitTextSize(text, paint, targetWidth, maxLines,
						mid, high, precision, displayMetrics);
			} else {
				return mid;
			}
		}
	}

	private static int getLineCount(CharSequence text, android.text.TextPaint paint,
			float size, float width, android.util.DisplayMetrics displayMetrics) {
		paint.setTextSize(android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_PX,
				size, displayMetrics));
		android.text.StaticLayout layout = new android.text.StaticLayout(text, paint, (int) width,
				android.text.Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
		return layout.getLineCount();
	}

	private static int getMaxLines(android.widget.TextView view) {
		int maxLines = -1; // No limit (Integer.MAX_VALUE also means no limit)
		android.text.method.TransformationMethod method = view.getTransformationMethod();
		if (method != null && method instanceof android.text.method.SingleLineTransformationMethod) {
			maxLines = 1;
		} else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			// setMaxLines() and getMaxLines() are only available on android-16+
			maxLines = view.getMaxLines();
		}
		return maxLines;
	}

	// Attributes
	private android.widget.TextView mTextView;
	private android.text.TextPaint mPaint;
	/**
	 * Original textSize of the TextView.
	 */
	private float mTextSize;
	private int mMaxLines;
	private float mMinTextSize;
	private float mMaxTextSize;
	private float mPrecision;
	private boolean mEnabled;
	private boolean mIsAutofitting;
	private java.util.ArrayList<com.goteborgstad.custom.views.AutofitHelper.OnTextSizeChangeListener> mListeners;
	private android.text.TextWatcher mTextWatcher = new com.goteborgstad.custom.views.AutofitHelper.AutofitTextWatcher();
	private android.view.View.OnLayoutChangeListener mOnLayoutChangeListener = new com.goteborgstad.custom.views.AutofitHelper.AutofitOnLayoutChangeListener();

	private AutofitHelper(android.widget.TextView view) {
		final android.content.Context context = view.getContext();
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		mTextView = view;
		mPaint = new android.text.TextPaint();
		setRawTextSize(view.getTextSize());
		mMaxLines = getMaxLines(view);
		mMinTextSize = scaledDensity * DEFAULT_MIN_TEXT_SIZE;
		mMaxTextSize = mTextSize;
		mPrecision = DEFAULT_PRECISION;
	}

	/**
	 * Adds a {@link OnTextSizeChangeListener} to the list of those whose
	 * methods are called whenever this {@link TextView}'s textSize changes.
	 */
	public com.goteborgstad.custom.views.AutofitHelper addOnTextSizeChangeListener(
			com.goteborgstad.custom.views.AutofitHelper.OnTextSizeChangeListener listener) {
		if (mListeners == null) {
			mListeners = new java.util.ArrayList<com.goteborgstad.custom.views.AutofitHelper.OnTextSizeChangeListener>();
		}
		mListeners.add(listener);
		return this;
	}

	/**
	 * Removes the specified {@link OnTextSizeChangeListener} from the list of
	 * those whose methods are called whenever this {@link TextView}'s textSize
	 * changes.
	 */
	public com.goteborgstad.custom.views.AutofitHelper removeOnTextSizeChangeListener(
			com.goteborgstad.custom.views.AutofitHelper.OnTextSizeChangeListener listener) {
		if (mListeners != null) {
			mListeners.remove(listener);
		}
		return this;
	}

	/**
	 * @return the amount of precision used to calculate the correct text size
	 *         to fit within its bounds.
	 */
	public float getPrecision() {
		return mPrecision;
	}

	/**
	 * Set the amount of precision used to calculate the correct text size to
	 * fit within its bounds. Lower precision is more precise and takes more
	 * time.
	 * 
	 * @param precision
	 *            The amount of precision.
	 */
	public com.goteborgstad.custom.views.AutofitHelper setPrecision(float precision) {
		if (mPrecision != precision) {
			mPrecision = precision;
			autofit();
		}
		return this;
	}

	/**
	 * @return the minimum size (in pixels) of the text.
	 */
	public float getMinTextSize() {
		return mMinTextSize;
	}

	/**
	 * Set the minimum text size to the given value, interpreted as
	 * "scaled pixel" units. This size is adjusted based on the current density
	 * and user font size preference.
	 * 
	 * @param size
	 *            The scaled pixel size.
	 * 
	 * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
	 */
	public com.goteborgstad.custom.views.AutofitHelper setMinTextSize(float size) {
		return setMinTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size);
	}

	/**
	 * Set the minimum text size to a given unit and value. See TypedValue for
	 * the possible dimension units.
	 * 
	 * @param unit
	 *            The desired dimension unit.
	 * @param size
	 *            The desired size in the given units.
	 * 
	 * @attr ref me.grantland.R.styleable#AutofitTextView_minTextSize
	 */
	public com.goteborgstad.custom.views.AutofitHelper setMinTextSize(int unit, float size) {
		android.content.Context context = mTextView.getContext();
		android.content.res.Resources r = android.content.res.Resources.getSystem();
		if (context != null) {
			r = context.getResources();
		}
		setRawMinTextSize(android.util.TypedValue.applyDimension(unit, size,
				r.getDisplayMetrics()));
		return this;
	}

	private void setRawMinTextSize(float size) {
		if (size != mMinTextSize) {
			mMinTextSize = size;
			autofit();
		}
	}

	/**
	 * @return the maximum size (in pixels) of the text.
	 */
	public float getMaxTextSize() {
		return mMaxTextSize;
	}

	/**
	 * Set the maximum text size to the given value, interpreted as
	 * "scaled pixel" units. This size is adjusted based on the current density
	 * and user font size preference.
	 * 
	 * @param size
	 *            The scaled pixel size.
	 * 
	 * @attr ref android.R.styleable#TextView_textSize
	 */
	public com.goteborgstad.custom.views.AutofitHelper setMaxTextSize(float size) {
		return setMaxTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size);
	}

	/**
	 * Set the maximum text size to a given unit and value. See TypedValue for
	 * the possible dimension units.
	 * 
	 * @param unit
	 *            The desired dimension unit.
	 * @param size
	 *            The desired size in the given units.
	 * 
	 * @attr ref android.R.styleable#TextView_textSize
	 */
	public com.goteborgstad.custom.views.AutofitHelper setMaxTextSize(int unit, float size) {
		android.content.Context context = mTextView.getContext();
		android.content.res.Resources r = android.content.res.Resources.getSystem();
		if (context != null) {
			r = context.getResources();
		}
		setRawMaxTextSize(android.util.TypedValue.applyDimension(unit, size,
				r.getDisplayMetrics()));
		return this;
	}

	private void setRawMaxTextSize(float size) {
		if (size != mMaxTextSize) {
			mMaxTextSize = size;
			autofit();
		}
	}

	/**
	 * @see TextView#getMaxLines()
	 */
	public int getMaxLines() {
		return mMaxLines;
	}

	/**
	 * @see TextView#setMaxLines(int)
	 */
	public com.goteborgstad.custom.views.AutofitHelper setMaxLines(int lines) {
		if (mMaxLines != lines) {
			mMaxLines = lines;
			autofit();
		}
		return this;
	}

	/**
	 * @return whether or not automatically resizing text is enabled.
	 */
	public boolean isEnabled() {
		return mEnabled;
	}

	/**
	 * Set the enabled state of automatically resizing text.
	 */
	public com.goteborgstad.custom.views.AutofitHelper setEnabled(boolean enabled) {
		if (mEnabled != enabled) {
			mEnabled = enabled;
			if (enabled) {
				mTextView.addTextChangedListener(mTextWatcher);
				mTextView.addOnLayoutChangeListener(mOnLayoutChangeListener);
				autofit();
			} else {
				mTextView.removeTextChangedListener(mTextWatcher);
				mTextView.removeOnLayoutChangeListener(mOnLayoutChangeListener);
				mTextView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mTextSize);
			}
		}
		return this;
	}

	/**
	 * @see TextView#getTextSize()
	 * @return the original text size of the View.
	 */
	public float getTextSize() {
		return mTextSize;
	}

	/**
	 * Set the original text size of the View.
	 * 
	 * @see TextView#setTextSize(float)
	 */
	public void setTextSize(float size) {
		setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size);
	}

	/**
	 * Set the original text size of the View.
	 * 
	 * @see TextView#setTextSize(int, float)
	 */
	public void setTextSize(int unit, float size) {
		if (mIsAutofitting) {
			// We don't want to update the TextView's actual textSize while
			// we're autofitting
			// since it'd get set to the autofitTextSize
			return;
		}
		android.content.Context context = mTextView.getContext();
		android.content.res.Resources r = android.content.res.Resources.getSystem();
		if (context != null) {
			r = context.getResources();
		}
		setRawTextSize(android.util.TypedValue.applyDimension(unit, size,
				r.getDisplayMetrics()));
	}

	private void setRawTextSize(float size) {
		if (mTextSize != size) {
			mTextSize = size;
		}
	}

	private void autofit() {
		float oldTextSize = mTextView.getTextSize();
		float textSize;
		mIsAutofitting = true;
		autofit(mTextView, mPaint, mMinTextSize, mMaxTextSize, mMaxLines,
				mPrecision);
		mIsAutofitting = false;
		textSize = mTextView.getTextSize();
		if (textSize != oldTextSize) {
			sendTextSizeChange(textSize, oldTextSize);
		}
	}

	private void sendTextSizeChange(float textSize, float oldTextSize) {
		if (mListeners == null) {
			return;
		}
		for (com.goteborgstad.custom.views.AutofitHelper.OnTextSizeChangeListener listener : mListeners) {
			listener.onTextSizeChange(textSize, oldTextSize);
		}
	}

	private class AutofitTextWatcher implements android.text.TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence charSequence, int start,
				int count, int after) {
			// do nothing
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int start,
				int before, int count) {
			autofit();
		}

		@Override
		public void afterTextChanged(android.text.Editable editable) {
			// do nothing
		}
	}

	private class AutofitOnLayoutChangeListener implements
			android.view.View.OnLayoutChangeListener {
		@Override
		public void onLayoutChange(android.view.View view, int left, int top, int right,
								   int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
			autofit();
		}
	}

	/**
	 * When an object of a type is attached to an AutofitHelper, its methods
	 * will be called when the textSize is changed.
	 */
	public interface OnTextSizeChangeListener {
		/**
		 * This method is called to notify you that the size of the text has
		 * changed to <code>textSize</code> from <code>oldTextSize</code>.
		 */
		public void onTextSizeChange(float textSize, float oldTextSize);
	}
}
