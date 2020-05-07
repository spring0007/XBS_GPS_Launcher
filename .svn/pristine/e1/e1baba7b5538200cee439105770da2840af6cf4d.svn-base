package com.sczn.wearlauncher.contact.view;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

import com.sczn.wearlauncher.R;


/**
 * Created by Administrator on 2017/11/27 0027.
 */

public class ContextWrapperEdgeEffect extends ContextWrapper {
    private static ResourcesEdgeEffect RES_EDGE_EFFECT;

    private int mColor;
    private Drawable mEdgeDrawable;
    private Drawable mGlowDrawable;

    public ContextWrapperEdgeEffect(Context base) {
        super(base);
        Resources resources = base.getResources();

        if (RES_EDGE_EFFECT == null)

            RES_EDGE_EFFECT = new ResourcesEdgeEffect(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());

    }

    public void setEdgeEffectColor(int color) {
        mColor = color;
        if (mEdgeDrawable != null) mEdgeDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        if (mGlowDrawable != null) mGlowDrawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
    //返回自定义的Resources

    public Resources getResources() {

        return RES_EDGE_EFFECT;

    }

    public class ResourcesEdgeEffect extends Resources {

        private int
                overscroll_edge = getPlatformDrawableId("overscroll_edge");

        private int
                overscroll_glow = getPlatformDrawableId("overscroll_glow");

        /**
         * Create a new Resources object on top of an existing set of assets in an
         * AssetManager.
         *
         * @param assets  Previously created AssetManager.
         * @param metrics Current display metrics to consider when
         *                selecting/computing resource values.
         * @param config  Desired device configuration to consider when
         * @deprecated Resources should not be constructed by apps.
         * See {@link Context#createConfigurationContext(Configuration)}.
         */
        public ResourcesEdgeEffect(AssetManager assets, DisplayMetrics metrics, Configuration config) {

            super(assets, metrics, config);
        }

        private int getPlatformDrawableId(String name) {

            try {

                int i = ((Integer) Class.forName("com.android.internal.R$drawable").getField(name).get(null)).intValue();

                return i;

            } catch (ClassNotFoundException e) {

                Log.e("getPlatformDrawableId()", "Cannot find internal resource class");

                return 0;

            } catch (NoSuchFieldException e1) {

                Log.e("getPlatformDrawableId()", "Internal resource id does not exist: " + name);

                return 0;

            } catch (IllegalArgumentException e2) {

                Log.e("getPlatformDrawableId()", "Cannot access internal resource id: " + name);

                return 0;

            } catch (IllegalAccessException e3) {

                Log.e("getPlatformDrawableId()", "Cannot access internal resource id: " + name);

            }

            return 0;

        }


        public Drawable getDrawable(int resId) throws NotFoundException {

            Drawable ret = null;
            if (resId == this.overscroll_edge) {
                mEdgeDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.overscroll_edge);
                ret = mEdgeDrawable;
            } else if (resId == this.overscroll_glow) {
                mGlowDrawable = ContextWrapperEdgeEffect.this.getBaseContext().getResources().getDrawable(R.drawable.overscroll_glow);
                ret = mGlowDrawable;
            } else return super.getDrawable(resId);

            if (ret != null) {
                ret.setColorFilter(mColor, PorterDuff.Mode.MULTIPLY);
            }

            return ret;

        }


    }
}
