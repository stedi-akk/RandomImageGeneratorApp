package com.stedi.randomimagegenerator.app.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        VERTICAL(ColoredNoiseGenerator.Orientation.VERTICAL, R.string.vertical),
        HORIZONTAL(ColoredNoiseGenerator.Orientation.HORIZONTAL, R.string.horizontal),
        RANDOM(ColoredNoiseGenerator.Orientation.RANDOM, R.string.random);

        private final ColoredNoiseGenerator.Orientation orientation;
        private final int nameRes;

        MapedOrientation(ColoredNoiseGenerator.Orientation orientation, int nameRes) {
            this.orientation = orientation;
            this.nameRes = nameRes;
        }

        private String getName(Context context) {
            return context.getString(nameRes);
        }

        private static String[] toArray(Context context) {
            String[] array = new String[MapedOrientation.values().length];
            for (int i = 0; i < array.length; i++) {
                array[i] = MapedOrientation.values()[i].getName(context);
            }
            return array;
        }
    }

    private enum MapedType {
        TYPE_1(ColoredNoiseGenerator.Type.TYPE_1, R.string.type_s, "1"),
        TYPE_2(ColoredNoiseGenerator.Type.TYPE_2, R.string.type_s, "2"),
        TYPE_3(ColoredNoiseGenerator.Type.TYPE_3, R.string.type_s, "3"),
        TYPE_4(ColoredNoiseGenerator.Type.TYPE_4, R.string.type_s, "4"),
        TYPE_5(ColoredNoiseGenerator.Type.TYPE_5, R.string.type_s, "5"),
        TYPE_6(ColoredNoiseGenerator.Type.TYPE_6, R.string.type_s, "6"),
        RANDOM(ColoredNoiseGenerator.Type.RANDOM, R.string.random, null);

        private final ColoredNoiseGenerator.Type type;
        private final int nameRes;
        private final String formatArg;

        MapedType(ColoredNoiseGenerator.Type type, int nameRes, String formatArg) {
            this.type = type;
            this.nameRes = nameRes;
            this.formatArg = formatArg;
        }

        private String getName(Context context) {
            return formatArg != null ? context.getString(nameRes, formatArg) : context.getString(nameRes);
        }

        private static String[] toArray(Context context) {
            String[] array = new String[MapedType.values().length];
            for (int i = 0; i < array.length; i++) {
                array[i] = MapedType.values()[i].getName(context);
            }
            return array;
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
        spOrientation.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, MapedOrientation.toArray(getContext())));
        spType.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, MapedType.toArray(getContext())));
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
