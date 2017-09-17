package com.stedi.randomimagegenerator.app.view.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresetsAdapter extends RecyclerView.Adapter<PresetsAdapter.ViewHolder> implements View.OnClickListener {
    private final List<Preset> presetsList = new ArrayList<>();
    private final GeneratorTypeImageLoader imageLoader;
    private final ClickListener listener;

    private Preset pendingPreset;

    public interface ClickListener {
        void onCardClick(@NonNull Preset preset);

        void onDeleteClick(@NonNull Preset preset);

        void onGenerateClick(@NonNull Preset preset);

        void onSaveClick(@NonNull Preset preset);
    }

    public PresetsAdapter(@NonNull GeneratorTypeImageLoader imageLoader, @NonNull ClickListener listener) {
        this.imageLoader = imageLoader;
        this.listener = listener;
    }

    public void set(@NonNull List<Preset> presets, @Nullable Preset pendingPreset) {
        presetsList.clear();
        presetsList.addAll(presets);
        this.pendingPreset = pendingPreset;
        notifyDataSetChanged();
    }

    public void remove(@NonNull Preset preset) {
        if (pendingPreset == preset) {
            pendingPreset = null;
            notifyItemRemoved(0);
        } else {
            int listIndex = presetsList.indexOf(preset);
            if (listIndex >= 0) {
                presetsList.remove(listIndex);
                notifyItemRemoved(pendingPreset == null ? listIndex : listIndex + 1);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Preset preset;

        if (pendingPreset != null) {
            preset = position == 0 ? pendingPreset : presetsList.get(position - 1);
        } else {
            preset = presetsList.get(position);
        }

        GeneratorType mainType = preset.getGeneratorParams().getType();
        GeneratorType secondType = null;
        if (preset.getGeneratorParams() instanceof EffectGeneratorParams)
            secondType = ((EffectGeneratorParams) preset.getGeneratorParams()).getTarget().getType();

        holder.itemView.setTag(mainType);
        holder.tvName.setText(preset.getName());
        holder.tvFolder.setText(preset.getPathToSave());
        holder.tvCreated.setText(Utils.formatTime(preset.getTimestamp()));
        holder.btnAction.setText(preset == pendingPreset ? R.string.save : R.string.generate);

        setPresetBoundedClickListener(holder.itemView, preset);
        setPresetBoundedClickListener(holder.btnAction, preset);
        setPresetBoundedClickListener(holder.btnDelete, preset);

        imageLoader.load(mainType, secondType, (params, bitmap) -> {
            GeneratorType holderType = (GeneratorType) holder.itemView.getTag();
            if (mainType == holderType) {
                holder.imageView.setImageBitmap(bitmap);
            }
        });
    }

    private void setPresetBoundedClickListener(View view, Preset preset) {
        view.setTag(R.id.tag_preset, preset);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Preset preset = (Preset) v.getTag(R.id.tag_preset);
        switch ((String) v.getTag(R.id.tag_key)) {
            case ViewHolder.ITEM_VIEW_TAG:
                listener.onCardClick(preset);
                break;
            case ViewHolder.BTN_ACTION_TAG:
                if (preset == pendingPreset) {
                    listener.onSaveClick(preset);
                } else {
                    listener.onGenerateClick(preset);
                }
                break;
            case ViewHolder.BTN_DELETE_TAG:
                listener.onDeleteClick(preset);
                break;
            default:
                throw new IllegalStateException("unreachable code");
        }
    }

    @Override
    public int getItemCount() {
        return pendingPreset == null ? presetsList.size() : presetsList.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private static final String ITEM_VIEW_TAG = "ITEM_VIEW_TAG";
        private static final String BTN_ACTION_TAG = "BTN_ACTION_TAG";
        private static final String BTN_DELETE_TAG = "BTN_DELETE_TAG";

        @BindView(R.id.preset_item_tv_name) TextView tvName;
        @BindView(R.id.preset_item_tv_folder) TextView tvFolder;
        @BindView(R.id.preset_item_tv_created) TextView tvCreated;
        @BindView(R.id.preset_item_image) ImageView imageView;
        @BindView(R.id.preset_item_btn_action) Button btnAction;
        @BindView(R.id.preset_item_btn_delete) View btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(R.id.tag_key, ITEM_VIEW_TAG);
            btnAction.setTag(R.id.tag_key, BTN_ACTION_TAG);
            btnDelete.setTag(R.id.tag_key, BTN_DELETE_TAG);
        }
    }
}
