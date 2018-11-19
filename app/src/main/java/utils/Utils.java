package utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.emsphere.commando.emspherevms.R;

import activitys.BadgeDrwable;


/**
 * Created by commando5 on 5/8/2017.
 */

public class Utils {

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrwable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrwable) {
            badge = (BadgeDrwable) reuse;
        } else {
            badge = new BadgeDrwable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
}
