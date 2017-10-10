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
import com.stedi.randomimagegenerator.app.di.qualifiers.RootSavePath;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.model.data.Preset;
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams;
import com.stedi.randomimagegenerator.app.other.Utils;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresetsAdapter extends RecyclerView.Adapter<PresetsAdapter.ViewHolder> {
    private final List<Preset> presetsList = new ArrayList<>();
    private final GeneratorTypeImageLoader imageLoader;
    private final String rootSavePath;
    private final ClickListener listener;

    private Preset pendingPreset;

    public interface ClickListener {
        void onCardClick(@NonNull Preset preset);

        void onDeleteClick(@NonNull Preset preset);

        void onGenerateClick(@NonNull Preset preset);

        void onSaveClick(@NonNull Preset preset);
    }

    public PresetsAdapter(@NonNull GeneratorTypeImageLoader imageLoader, @RootSavePath @NonNull String rootSavePath, @NonNull ClickListener listener) {
        setHasStableIds(true);
        this.imageLoader = imageLoader;
        this.rootSavePath = rootSavePath;
        this.listener = listener;
    }

    public void set(@NonNull List<Preset> presets, @Nullable Preset pendingPreset) {
        this.pendingPreset = pendingPreset;
        presetsList.clear();
        if (pendingPreset != null)
            presetsList.add(pendingPreset);
        presetsList.addAll(presets);
        notifyDataSetChanged();
    }

    public void remove(@NonNull Preset preset) {
        int listIndex = presetsList.indexOf(preset);
        if (listIndex >= 0) {
            presetsList.remove(listIndex);
            notifyItemRemoved(listIndex);
        }
    }

    @Override
    public long getItemId(int position) {
        return presetsList.get(position).getId();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Preset preset = presetsList.get(position);
        holder.preset = preset;

        GeneratorType mainType = preset.getGeneratorParams().getType();
        GeneratorType secondType = null;
        if (preset.getGeneratorParams() instanceof EffectGeneratorParams)
            secondType = ((EffectGeneratorParams) preset.getGeneratorParams()).getTarget().getType();

        holder.tvName.setText(preset.getName());
        holder.tvFolder.setText(Utils.formatSavePath(rootSavePath, preset.getPathToSave()));
        holder.tvCreated.setText(Utils.formatTime(preset.getTimestamp()));
        holder.btnAction.setText(preset == pendingPreset ? R.string.save : R.string.generate);
        holder.imageView.setImageResource(R.drawable.ic_texture_adapter_rig_image_size);

        holder.itemView.setOnClickListener(holder);
        holder.btnAction.setOnClickListener(holder);
        holder.btnDelete.setOnClickListener(holder);

        imageLoader.load(mainType, secondType, (params, bitmap) -> {
            if (mainType == holder.preset.getGeneratorParams().getType()) {
                holder.imageView.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presetsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.preset_item_tv_name) TextView tvName;
        @BindView(R.id.preset_item_tv_folder) TextView tvFolder;
        @BindView(R.id.preset_item_tv_created) TextView tvCreated;
        @BindView(R.id.preset_item_image) ImageView imageView;
        @BindView(R.id.preset_item_btn_action) Button btnAction;
        @BindView(R.id.preset_item_btn_delete) View btnDelete;

        Preset preset;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                listener.onCardClick(preset);
            } else if (v == btnAction) {
                if (preset == pendingPreset) {
                    listener.onSaveClick(preset);
                } else {
                    listener.onGenerateClick(preset);
                }
            } else if (v == btnDelete) {
                listener.onDeleteClick(preset);
            }
        }
    }
}
