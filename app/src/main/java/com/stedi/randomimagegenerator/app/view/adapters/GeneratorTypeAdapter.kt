package com.stedi.randomimagegenerator.app.view.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.view.components.RigRequestHandler

class GeneratorTypeAdapter(
        @ActivityContext private val context: Context,
        private val generatorType: Array<GeneratorType>,
        private var selectedType: GeneratorType?,
        private val targetType: GeneratorType?,
        private val listener: ClickListener,
        private val isDeselectAllowed: Boolean) : RecyclerView.Adapter<GeneratorTypeAdapter.ViewHolder>() {

    interface ClickListener {
        fun onSelected(type: GeneratorType)

        fun onDeselected()

        fun onEditSelected()
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemCount() = generatorType.size

    override fun getItemId(position: Int) = generatorType[position].ordinal.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneratorTypeAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.generator_type_item, parent, false))
    }

    override fun onBindViewHolder(holder: GeneratorTypeAdapter.ViewHolder, position: Int) {
        val type = generatorType[position]
        holder.generatorType = type

        holder.text.setText(type.nameRes)
        holder.btnEdit.visibility = if (type.isEditable && type === selectedType) View.VISIBLE else View.INVISIBLE
        holder.isSelected.visibility = if (type === selectedType) View.VISIBLE else View.INVISIBLE

        val size = context.dim2px(R.dimen.adapter_rig_image_size)
        Picasso.get().load(RigRequestHandler.makeUri(type, targetType, size, size))
                .placeholder(R.drawable.ic_texture_adapter_rig_image_size)
                .into(holder.image)

        holder.card.setOnClickListener(holder)
        holder.btnEdit.setOnClickListener(holder)
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {
        @BindView(R.id.generator_type_item_card) lateinit var card: View
        @BindView(R.id.generator_type_item_text) lateinit var text: TextView
        @BindView(R.id.generator_type_item_btn_edit) lateinit var btnEdit: View
        @BindView(R.id.generator_type_item_selected) lateinit var isSelected: View
        @BindView(R.id.generator_type_item_image) lateinit var image: ImageView

        var generatorType: GeneratorType? = null

        init {
            ButterKnife.bind(this, item)
        }

        override fun onClick(v: View) {
            if (v === btnEdit) {
                listener.onEditSelected()
            } else if (v === card) {
                val generatorType = generatorType ?: return
                if (selectedType !== generatorType) {
                    selectedType = generatorType
                    listener.onSelected(generatorType)
                    runSelectionAnimation()
                    notifyDataSetChanged()
                } else if (isDeselectAllowed) {
                    selectedType = null
                    listener.onDeselected()
                    notifyDataSetChanged()
                }
            }
        }

        private fun runSelectionAnimation() {
            isSelected.scaleX = 0f
            isSelected.scaleY = 0f
            isSelected.alpha = 0f
            btnEdit.alpha = 0f
            isSelected.animate().scaleX(1f).scaleY(1f)
                    .setInterpolator(OvershootInterpolator(1.2f))
                    .alpha(1f).setDuration(200)
                    .withEndAction { btnEdit.animate().alpha(1f).duration = 200 }
        }
    }
}