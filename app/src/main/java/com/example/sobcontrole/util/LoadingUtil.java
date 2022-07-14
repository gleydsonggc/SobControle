package com.example.sobcontrole.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ProgressBar;

public class LoadingUtil {

    private static ProgressDialog progressDialog;

    public static void mostrar(Context context) {
        progressDialog = ProgressDialog.show(context, null, null);
        progressDialog.setContentView(new ProgressBar(context));
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public static void esconder() {
        progressDialog.dismiss();
    }
}
