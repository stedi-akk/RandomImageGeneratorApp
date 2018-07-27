package com.stedi.randomimagegenerator.app.view.adapters

import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.squareup.picasso.Picasso
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.di.ActivityContext
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.other.formatTime
import com.stedi.randomimagegenerator.app.view.components.RigRequestHandler
import java.util.*

class PresetsAdapter(
        @ActivityContext private val context: Context,
        private val listener: ClickListener) : RecyclerView.Adapter<PresetsAdapter.ViewHolder>() {

    private val storageDir = Environment.getExternalStorageDirectory()

    private val presetsList = ArrayList<Preset>()
    private var pendingPreset: Preset? = null

    private val imageSize = context.dim2px(R.dimen.adapter_rig_image_size)

    interface ClickListener {
        fun onCardClick(preset: Preset)

        fun onDeleteClick(preset: Preset)

        fun onGenerateClick(preset: Preset)

        fun onSaveClick(preset: Preset)
    }

    init {
        setHasStableIds(true)
    }

    fun set(presets: List<Preset>, pendingPreset: Preset?) {
        this.pendingPreset = pendingPreset
        presetsList.clear()
        if (pendingPreset != null) {
            presetsList.add(pendingPreset)
        }
        presetsList.addAll(presets)
        notifyDataSetChanged()
    }

    fun remove(preset: Preset) {
        val listIndex = presetsList.indexOf(preset)
        if (listIndex >= 0) {
            presetsList.removeAt(listIndex)
            notifyItemRemoved(listIndex)
        }
    }

    override fun getItemCount() = presetsList.size

    override fun getItemId(position: Int) = presetsList[position].id.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.preset_item, parent, false) as CardView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preset = presetsList[position]
        val quality = preset.getQuality()
        holder.preset = preset

        holder.cardView.setCardBackgroundColor(if (preset === pendingPreset) context.resources.getColor(R.color.pending_preset) else Color.WHITE)
        holder.tvName.text = preset.name
        holder.tvFolder.text = context.getString(R.string.storage_s, preset.pathToSave.removePrefix(storageDir.absolutePath))
        holder.tvCreated.text = formatTime(preset.timestamp)
        holder.tvQuality.text = context.getString(R.string.quality_s_percent_short, quality.format.name, quality.qualityValue.toString())
        holder.tvCount.text = context.getString(R.string.count_s_short, preset.getRealCount().toString())
        holder.btnAction.setText(if (preset === pendingPreset) R.string.save else R.string.generate)

        Picasso.get().load(RigRequestHandler.makeFromPreset(preset, imageSize, imageSize, false, true))
                .placeholder(context.resources.getDrawable(R.drawable.ic_texture_rig))
                .into(holder.imageView)

        holder.cardView.setOnClickListener(holder)
        holder.btnAction.setOnClickListener(holder)
        holder.btnDelete.setOnClickListener(holder)
    }

    inner class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView), View.OnClickListener {
        @BindView(R.id.preset_item_tv_name) lateinit var tvName: TextView
        @BindView(R.id.preset_item_tv_folder) lateinit var tvFolder: TextView
        @BindView(R.id.preset_item_tv_created) lateinit var tvCreated: TextView
        @BindView(R.id.preset_item_tv_quality) lateinit var tvQuality: TextView
        @BindView(R.id.preset_item_tv_count) lateinit var tvCount: TextView
        @BindView(R.id.preset_item_image) lateinit var imageView: ImageView
        @BindView(R.id.preset_item_btn_action) lateinit var btnAction: Button
        @BindView(R.id.preset_item_btn_delete) lateinit var btnDelete: View

        var preset: Preset? = null

        init {
            ButterKnife.bind(this, cardView)
        }

        override fun onClick(v: View) {
            val preset = preset ?: return
            if (v === cardView) {
                listener.onCardClick(preset)
            } else if (v === btnAction) {
                if (preset === pendingPreset) {
                    listener.onSaveClick(preset)
                } else {
                    listener.onGenerateClick(preset)
                }
            } else if (v === btnDelete) {
                listener.onDeleteClick(preset)
            }
        }
    }
}