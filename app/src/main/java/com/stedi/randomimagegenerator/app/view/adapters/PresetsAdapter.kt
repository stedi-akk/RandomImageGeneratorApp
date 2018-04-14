package com.stedi.randomimagegenerator.app.view.adapters

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
import com.stedi.randomimagegenerator.app.model.data.GeneratorType
import com.stedi.randomimagegenerator.app.model.data.Preset
import com.stedi.randomimagegenerator.app.model.data.generatorparams.base.EffectGeneratorParams
import com.stedi.randomimagegenerator.app.other.dim2px
import com.stedi.randomimagegenerator.app.other.formatSavePath
import com.stedi.randomimagegenerator.app.other.formatTime
import com.stedi.randomimagegenerator.app.view.components.RigRequestHandler
import java.util.*

class PresetsAdapter(
        private val rootSavePath: String,
        private val listener: ClickListener) : RecyclerView.Adapter<PresetsAdapter.ViewHolder>() {

    private val presetsList = ArrayList<Preset>()

    private var pendingPreset: Preset? = null

    interface ClickListener {
        fun onCardClick(preset: Preset)

        fun onDeleteClick(preset: Preset)

        fun onGenerateClick(preset: Preset)

        fun onSaveClick(preset: Preset)
    }

    init {
        setHasStableIds(true)
    }

    operator fun set(presets: List<Preset>, pendingPreset: Preset?) {
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
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preset_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preset = presetsList[position]
        holder.preset = preset

        val generatorParams = preset.getGeneratorParams()
        val mainType = generatorParams.getType()
        var secondType: GeneratorType? = null
        if (generatorParams is EffectGeneratorParams) {
            secondType = generatorParams.target.getType()
        }

        holder.tvName.text = preset.name
        holder.tvFolder.text = formatSavePath(rootSavePath, preset.pathToSave)
        holder.tvCreated.text = formatTime(preset.timestamp)
        holder.btnAction.setText(if (preset === pendingPreset) R.string.save else R.string.generate)

        val size = holder.itemView.context.dim2px(R.dimen.adapter_rig_image_size)
        Picasso.get().load(RigRequestHandler.makeUri(mainType, secondType, size, size, preset.getQuality().format, preset.getQuality().qualityValue))
                .placeholder(R.drawable.ic_texture_adapter_rig_image_size)
                .into(holder.imageView)

        holder.itemView.setOnClickListener(holder)
        holder.btnAction.setOnClickListener(holder)
        holder.btnDelete.setOnClickListener(holder)
    }

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item), View.OnClickListener {
        @BindView(R.id.preset_item_tv_name) lateinit var tvName: TextView
        @BindView(R.id.preset_item_tv_folder) lateinit var tvFolder: TextView
        @BindView(R.id.preset_item_tv_created) lateinit var tvCreated: TextView
        @BindView(R.id.preset_item_image) lateinit var imageView: ImageView
        @BindView(R.id.preset_item_btn_action) lateinit var btnAction: Button
        @BindView(R.id.preset_item_btn_delete) lateinit var btnDelete: View

        var preset: Preset? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun onClick(v: View) {
            val preset = preset ?: return
            if (v === itemView) {
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