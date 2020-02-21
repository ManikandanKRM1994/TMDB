package com.krm.tmdb.draggablepanel.transformer;

import android.view.View;

public class TransformerFactory {

    public Transformer getTransformer(final boolean resize, final View view, final View parent) {
        Transformer transformer = null;
        if (resize) {
            transformer = new ResizeTransformer(view, parent);
        } else {
            transformer = new ScaleTransformer(view, parent);
        }
        return transformer;
    }
}
