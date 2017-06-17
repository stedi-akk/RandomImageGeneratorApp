package com.stedi.randomimagegenerator.app.view.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stedi.randomimagegenerator.app.R;
import com.stedi.randomimagegenerator.app.model.data.GeneratorType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneratorTypeAdapter extends RecyclerView.Adapter<GeneratorTypeAdapter.ViewHolder> implements View.OnClickListener {
    private final GeneratorType[] generatorType;
    private final ClickListener listener;

    private GeneratorType selectedType;

    public interface ClickListener {
        void onSelected(@NonNull GeneratorType type);

        void onEditSelected();
    }

    public GeneratorTypeAdapter(@NonNull GeneratorType[] generatorType, @NonNull GeneratorType selectedType, ClickListener listener) {
        this.generatorType = generatorType;
        this.selectedType = selectedType;
        this.listener = listener;
    }

    @Override
    public GeneratorTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.generator_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GeneratorTypeAdapter.ViewHolder holder, int position) {
        GeneratorType type = generatorType[position];
        holder.itemView.setTag(type);
        holder.itemView.setOnClickListener(this);
        holder.text.setText(type.name());
        holder.btnEdit.setOnClickListener(this);
        holder.isSelected.setVisibility(type == selectedType ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (listener == null)
            return;

        if (v.getId() == R.id.generator_type_item_btn_edit) {
            listener.onEditSelected();
        } else {
            selectedType = (GeneratorType) v.getTag();
            listener.onSelected(selectedType);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return generatorType.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.generator_type_item_text) TextView text;
        @BindView(R.id.generator_type_item_btn_edit) View btnEdit;
        @BindView(R.id.generator_type_item_selected) View isSelected;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
