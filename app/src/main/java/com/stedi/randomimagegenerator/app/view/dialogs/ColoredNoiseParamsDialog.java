package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.stedi.randomimagegenerator.app.App;
import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.di.Components;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.presenter.interfaces.ColoredNoiseParamsPresenter;
import com.stedi.randomimagegenerator.app.view.dialogs.base.ButterKnifeDialogFragment;
import com.stedi.randomimagegenerator.generators.ColoredNoiseGenerator;

import javax.inject.Inject;

import butterknife.BindView;

public class ColoredNoiseParamsDialog extends ButterKnifeDialogFragment implements ColoredNoiseParamsPresenter.UIImpl {
    private enum MapedOrientation {
        VERTICAL(ColoredNoiseGenerator.Orientation.VERTICAL, App.getInstance().getString(R.string.vertical)),
        HORIZONTAL(ColoredNoiseGenerator.Orientation.HORIZONTAL, App.getInstance().getString(R.string.horizontal)),
        RANDOM(ColoredNoiseGenerator.Orientation.RANDOM, App.getInstance().getString(R.string.random));

        private final ColoredNoiseGenerator.Orientation orientation;
        private final String name;

        MapedOrientation(ColoredNoiseGenerator.Orientation orientation, String name) {
            this.orientation = orientation;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private enum MapedType {
        TYPE_1(ColoredNoiseGenerator.Type.TYPE_1, App.getInstance().getString(R.string.type_s, "1")),
        TYPE_2(ColoredNoiseGenerator.Type.TYPE_2, App.getInstance().getString(R.string.type_s, "2")),
        TYPE_3(ColoredNoiseGenerator.Type.TYPE_3, App.getInstance().getString(R.string.type_s, "3")),
        TYPE_4(ColoredNoiseGenerator.Type.TYPE_4, App.getInstance().getString(R.string.type_s, "4")),
        TYPE_5(ColoredNoiseGenerator.Type.TYPE_5, App.getInstance().getString(R.string.type_s, "5")),
        TYPE_6(ColoredNoiseGenerator.Type.TYPE_6, App.getInstance().getString(R.string.type_s, "6")),
        RANDOM(ColoredNoiseGenerator.Type.RANDOM, App.getInstance().getString(R.string.random));

        private final ColoredNoiseGenerator.Type type;
        private final String name;

        MapedType(ColoredNoiseGenerator.Type type, String name) {
            this.type = type;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

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
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            presenter.setOrientation(MapedOrientation.values()[spOrientation.getSelectedItemPosition()].orientation);
            presenter.setType(MapedType.values()[spType.getSelectedItemPosition()].type);
        });
        builder.setTitle(getString(R.string.s_parameters, getString(GeneratorType.COLORED_NOISE.getStringRes())));
        builder.setView(inflateAndBind(R.layout.colored_noise_params_dialog));
        spOrientation.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, MapedOrientation.values()));
        spType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, MapedType.values()));
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
