package com.goteborgstad.custom.views;

public class AutofitLayout extends android.widget.FrameLayout {
	private boolean mEnabled;
	private float mMinTextSize;
	private float mPrecision;
	private java.util.WeakHashMap<android.view.View, AutofitHelper> mHelpers = new java.util.WeakHashMap<android.view.View, AutofitHelper>();

	public AutofitLayout(android.content.Context context) {
		super(context);
		init(context, null, 0);
	}

	public AutofitLayout(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public AutofitLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		boolean sizeToFit = true;
		int minTextSize = -1;
		float precision = -1;
		if (attrs != null) {
			android.content.res.TypedArray ta = context.obtainStyledAttributes(attrs,
					com.goteborgstad.R.styleable.AutofitTextView, defStyle, 0);
			sizeToFit = ta.getBoolean(com.goteborgstad.R.styleable.AutofitTextView_sizeToFit,
					sizeToFit);
			minTextSize = ta.getDimensionPixelSize(
					com.goteborgstad.R.styleable.AutofitTextView_minTextSize, minTextSize);
			precision = ta.getFloat(com.goteborgstad.R.styleable.AutofitTextView_precision,
					precision);
			ta.recycle();
		}
		mEnabled = sizeToFit;
		mMinTextSize = minTextSize;
		mPrecision = precision;
	}

	@Override
	public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
		android.widget.TextView textView = (android.widget.TextView) child;
		AutofitHelper helper = AutofitHelper.create(textView).setEnabled(
				mEnabled);
		if (mPrecision > 0) {
			helper.setPrecision(mPrecision);
		}
		if (mMinTextSize > 0) {
			helper.setMinTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mMinTextSize);
		}
		mHelpers.put(textView, helper);
	}

	/**
	 * @return the {@link AutofitHelper} for this child View.
	 */
	public AutofitHelper getAutofitHelper(android.widget.TextView textView) {
		return mHelpers.get(textView);
	}

	/**
	 * @return the {@link AutofitHelper} for this child View.
	 */
	public AutofitHelper getAutofitHelper(int index) {
		return mHelpers.get(getChildAt(index));
	}
}