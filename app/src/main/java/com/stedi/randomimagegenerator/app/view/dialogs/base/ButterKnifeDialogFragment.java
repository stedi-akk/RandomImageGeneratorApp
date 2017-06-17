package com.stedi.randomimagegenerator.app.view.dialogs.base;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ButterKnifeDialogFragment extends BaseDialogFragment {
    private Unbinder unbinder;

    protected View inflateAndBind(@LayoutRes int layoutResId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutResId, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}
