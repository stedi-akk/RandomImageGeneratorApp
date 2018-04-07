package com.stedi.randomimagegenerator.app.view.adapters

import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.GeneratorParams
import com.stedi.randomimagegenerator.app.view.components.GeneratorTypeImageLoader

class GeneratorTypeAdapter(
        private val imageLoader: GeneratorTypeImageLoader,
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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.generator_type_item, parent, false))
    }

    override fun onBindViewHolder(holder: GeneratorTypeAdapter.ViewHolder, position: Int) {
        val type = generatorType[position]
        holder.generatorType = type

        holder.card.setOnClickListener(holder)
        holder.text.setText(type.nameRes)
        holder.btnEdit.visibility = View.INVISIBLE
        holder.btnEdit.setOnClickListener(holder)
        holder.isSelected.visibility = if (type === selectedType) View.VISIBLE else View.INVISIBLE
        holder.image.setImageResource(R.drawable.ic_texture_adapter_rig_image_size)

        imageLoader.load(type, targetType, object : GeneratorTypeImageLoader.Callback {
            override fun onLoaded(params: GeneratorParams, bitmap: Bitmap) {
                if (type === holder.generatorType) {
                    holder.btnEdit.visibility = if (params.isEditable() && type === selectedType) View.VISIBLE else View.INVISIBLE
                    holder.image.setImageBitmap(bitmap)
                }
            }
        })
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {
        @BindView(R.id.generator_type_item_card)
        lateinit var card: View
        @BindView(R.id.generator_type_item_text)
        lateinit var text: TextView
        @BindView(R.id.generator_type_item_btn_edit)
        lateinit var btnEdit: View
        @BindView(R.id.generator_type_item_selected)
        lateinit var isSelected: View
        @BindView(R.id.generator_type_item_image)
        lateinit var image: ImageView

        var generatorType: GeneratorType? = null

        init {
            ButterKnife.bind(this, item)
        }

        override fun onClick(v: View) {
            if (v === btnEdit) {
                listener.onEditSelected()
            } else if (v === card) {
                if (selectedType !== generatorType) {
                    selectedType = generatorType
                    listener.onSelected(selectedType!!)
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