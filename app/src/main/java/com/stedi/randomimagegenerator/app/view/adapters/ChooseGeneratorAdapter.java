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

public class ChooseGeneratorAdapter extends RecyclerView.Adapter<ChooseGeneratorAdapter.ViewHolder> implements View.OnClickListener {
    private final GeneratorType[] generatorType;

    private ClickListener listener;

    public interface ClickListener {
        void onSelected(@NonNull GeneratorType type);

        void onEditSelected();
    }

    public ChooseGeneratorAdapter(@NonNull GeneratorType[] generatorType, ClickListener listener) {
        this.generatorType = generatorType;
        this.listener = listener;
    }

    @Override
    public ChooseGeneratorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_generator_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ChooseGeneratorAdapter.ViewHolder holder, int position) {
        GeneratorType type = generatorType[position];
        holder.itemView.setTag(type);
        holder.itemView.setOnClickListener(this);
        holder.text.setText(type.name());
        holder.btnEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (listener == null)
            return;

        if (v.getId() == R.id.choose_generator_item_btn_edit) {
            listener.onEditSelected();
        } else {
            listener.onSelected((GeneratorType) v.getTag());
        }
    }

    @Override
    public int getItemCount() {
        return generatorType.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.choose_generator_item_text) TextView text;
        @BindView(R.id.choose_generator_item_btn_edit) View btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
