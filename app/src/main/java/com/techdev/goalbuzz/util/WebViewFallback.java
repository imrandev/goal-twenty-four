package com.techdev.goalbuzz.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebViewFallback implements CustomTabActivityHelper.CustomTabFallback {

    @Override
    public void openUri(Activity activity, Uri uri) {
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(launchBrowser);
    }
}
