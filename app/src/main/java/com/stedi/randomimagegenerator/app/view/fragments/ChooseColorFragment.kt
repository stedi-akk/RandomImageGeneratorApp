package com.stedi.randomimagegenerator.app.view.fragments

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import butterknife.BindView
import com.edmodo.rangebar.RangeBar
import com.stedi.randomimagegenerator.app.R
import com.stedi.randomimagegenerator.app.presenter.interfaces.ChooseColorPresenter
import com.stedi.randomimagegenerator.app.view.components.BaseViewModel
import com.stedi.randomimagegenerator.app.view.components.HSVRangeBar
import com.stedi.randomimagegenerator.app.view.fragments.base.GenerationFragment
import timber.log.Timber
import javax.inject.Inject

class ChooseColorFragmentModel : BaseViewModel<ChooseColorFragment>() {
    @Inject lateinit var presenter: ChooseColorPresenter

    override fun onCreate(view: ChooseColorFragment) {
        view.generationComponent.inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        presenter.onDestroy()
    }
}

class ChooseColorFragment : GenerationFragment(),
        ChooseColorPresenter.UIImpl,
        RangeBar.OnRangeBarChangeListener,
        CompoundButton.OnCheckedChangeListener {

    private lateinit var viewModel: ChooseColorFragmentModel

    @BindView(R.id.choose_color_fragment_range_bar) lateinit var rangeBar: HSVRangeBar
    @BindView(R.id.choose_color_fragment_cb_use_light) lateinit var cbUseLight: CheckBox
    @BindView(R.id.choose_color_fragment_cb_use_dark) lateinit var cbUseDark: CheckBox
    @BindView(R.id.choose_color_fragment_cb_grayscale) lateinit var cbGrayscale: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChooseColorFragmentModel::class.java)
        viewModel.init(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.choose_color_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rangeBar.setOnRangeBarChangeListener(this)
        setCheckBoxListener(this)
        viewModel.presenter.onAttach(this)
    }

    override fun onIndexChangeListener(rangeBar: RangeBar?, left: Int, right: Int) {
        Timber.d("OnRangeBarChangeListener left=$left right=$right")
        viewModel.presenter.setColorRange(left, right)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Timber.d("onCheckedChanged cbUseLight=${cbUseLight.isChecked} cbUseDark=${cbUseDark.isChecked} cbGrayscale=${cbGrayscale.isChecked}")
        viewModel.presenter.setColorExtras(cbUseLight.isChecked, cbUseDark.isChecked, cbGrayscale.isChecked)
    }

    override fun showColorRange(colorFrom: Int, colorTo: Int) {
        rangeBar.setOnRangeBarChangeListener(null)
        rangeBar.setThumbIndices(colorFrom, colorTo)
        rangeBar.setOnRangeBarChangeListener(this)
    }

    override fun showColorExtras(useLightColor: Boolean, useDarkColor: Boolean, isGrayscale: Boolean) {
        setCheckBoxListener(null)
        cbUseLight.isChecked = useLightColor
        cbUseDark.isChecked = useDarkColor
        cbGrayscale.isChecked = isGrayscale
        cbUseLight.jumpDrawablesToCurrentState()
        cbUseDark.jumpDrawablesToCurrentState()
        cbGrayscale.jumpDrawablesToCurrentState()
        setCheckBoxListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.presenter.onDetach()
    }

    private fun setCheckBoxListener(listener: CompoundButton.OnCheckedChangeListener?) {
        cbUseLight.setOnCheckedChangeListener(listener)
        cbUseDark.setOnCheckedChangeListener(listener)
        cbGrayscale.setOnCheckedChangeListener(listener)
    }
}