package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

import javax.inject.Inject;

import butterknife.BindView;

public class ColoredNoiseParamsDialog extends ButterKnifeDialogFragment implements ColoredNoiseParamsPresenter.UIImpl {
    @Inject ColoredNoiseParamsPresenter presenter;

    @BindView(R.id.colored_noise_params_dialog_sp_orientation) Spinner spOrientation;
    @BindView(R.id.colored_noise_params_dialog_sp_type) Spinner spType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Components.getGenerationComponent(this).inject(this);
        presenter.onAttach(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", (dialog, which) -> {
            presenter.setOrientation(ColoredNoiseGenerator.Orientation.values()[spOrientation.getSelectedItemPosition()]);
            presenter.setType(ColoredNoiseGenerator.Type.values()[spType.getSelectedItemPosition()]);
        });
        builder.setTitle("Edit colored noise params");
        builder.setView(inflateAndBind(R.layout.colored_noise_params_dialog));
        spOrientation.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ColoredNoiseGenerator.Orientation.values()));
        spType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, ColoredNoiseGenerator.Type.values()));
        if (savedInstanceState == null)
            presenter.getValues();
        return builder.create();
    }

    @Override
    public void showOrientation(@NonNull ColoredNoiseGenerator.Orientation orientation) {
        spOrientation.setSelection(orientation.ordinal());
    }

    @Override
    public void showType(@NonNull ColoredNoiseGenerator.Type type) {
        spType.setSelection(type.ordinal());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}
