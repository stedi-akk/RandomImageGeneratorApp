package com.stedi.randomimagegenerator.app.view.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.model.data.Preset;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresetsAdapter extends RecyclerView.Adapter<PresetsAdapter.ViewHolder> implements View.OnClickListener {
    private final List<Preset> presetsList = new ArrayList<>();
    private final ClickListener listener;

    private Preset pendingPreset;

    public interface ClickListener {
        void onCardClick(@NonNull Preset preset);

        void onDeleteClick(@NonNull Preset preset);

        void onGenerateClick(@NonNull Preset preset);

        void onSaveClick(@NonNull Preset preset);
    }

    public PresetsAdapter(@NonNull ClickListener listener) {
        this.listener = listener;
    }

    public void set(@NonNull List<Preset> presets) {
        presetsList.clear();
        presetsList.addAll(presets);
    }

    @NonNull
    public List<Preset> get() {
        return presetsList;
    }

    public void setPendingPreset(@Nullable Preset pendingPreset) {
        this.pendingPreset = pendingPreset;
    }

    @Nullable
    public Preset getPendingPreset() {
        return pendingPreset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.preset_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Preset preset;

        if (pendingPreset != null) {
            if (position == 0)
                preset = pendingPreset;
            else
                preset = presetsList.get(position - 1);
        } else {
            preset = presetsList.get(position);
        }

        holder.tvName.setText(preset.getName());
        holder.tvFolder.setText(preset.getSaveFolder());
        holder.btnAction.setText(preset == pendingPreset ? "save" : "generate");
        setPresetBoundedClickListener(holder.itemView, preset);
        setPresetBoundedClickListener(holder.btnAction, preset);
        setPresetBoundedClickListener(holder.btnDelete, preset);
    }

    private void setPresetBoundedClickListener(View view, Preset preset) {
        view.setTag(R.id.tag_preset, preset);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Preset preset = (Preset) v.getTag(R.id.tag_preset);
        switch ((String) v.getTag()) {
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
        @BindView(R.id.preset_item_btn_action) Button btnAction;
        @BindView(R.id.preset_item_btn_delete) Button btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(ITEM_VIEW_TAG);
            btnAction.setTag(BTN_ACTION_TAG);
            btnDelete.setTag(BTN_DELETE_TAG);
        }
    }
}
