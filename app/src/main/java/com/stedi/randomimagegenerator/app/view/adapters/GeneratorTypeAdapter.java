package com.stedi.randomimagegenerator.app.view.adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneratorTypeAdapter extends RecyclerView.Adapter<GeneratorTypeAdapter.ViewHolder> implements View.OnClickListener {
    private final GeneratorTypeImageLoader imageLoader;
    private final GeneratorType[] generatorType;
    private final ClickListener listener;
    private final boolean isDeselectAllowed;

    private GeneratorType selectedType;
    private GeneratorType targetType;

    public interface ClickListener {
        void onSelected(@NonNull GeneratorType type);

        void onDeselected();

        void onEditSelected();
    }

    public GeneratorTypeAdapter(
            @NonNull GeneratorTypeImageLoader imageLoader,
            @NonNull GeneratorType[] generatorType, @Nullable GeneratorType selectedType, @Nullable GeneratorType targetType,
            @NonNull ClickListener listener, boolean isDeselectAllowed) {
        setHasStableIds(true);
        this.imageLoader = imageLoader;
        this.generatorType = generatorType;
        this.selectedType = selectedType;
        this.targetType = targetType;
        this.listener = listener;
        this.isDeselectAllowed = isDeselectAllowed;
    }

    @Override
    public GeneratorTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.generator_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GeneratorTypeAdapter.ViewHolder holder, int position) {
        GeneratorType type = generatorType[position];

        holder.card.setTag(type);
        holder.card.setOnClickListener(this);
        holder.text.setText(type.getStringRes());
        holder.btnEdit.setOnClickListener(this);
        holder.isSelected.setVisibility(type == selectedType ? View.VISIBLE : View.INVISIBLE);
        holder.btnEdit.setVisibility(View.INVISIBLE);
        holder.image.setImageResource(R.drawable.ic_texture_gray_medium_semi_24dp);

        imageLoader.load(type, targetType, (params, bitmap) -> {
            GeneratorType holderType = (GeneratorType) holder.card.getTag();
            if (type == holderType) {
                holder.btnEdit.setVisibility(params.isEditable() && holderType == selectedType ? View.VISIBLE : View.INVISIBLE);
                holder.image.setImageBitmap(bitmap);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.generator_type_item_btn_edit) {
            listener.onEditSelected();
        } else if (v.getId() == R.id.generator_type_item_card) {
            GeneratorType clickedType = (GeneratorType) v.getTag();
            if (selectedType != clickedType) {
                selectedType = clickedType;
                listener.onSelected(selectedType);
                notifyDataSetChanged();
            } else if (isDeselectAllowed) {
                selectedType = null;
                listener.onDeselected();
                notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return generatorType.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.generator_type_item_card) View card;
        @BindView(R.id.generator_type_item_text) TextView text;
        @BindView(R.id.generator_type_item_btn_edit) View btnEdit;
        @BindView(R.id.generator_type_item_selected) View isSelected;
        @BindView(R.id.generator_type_item_image) ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
