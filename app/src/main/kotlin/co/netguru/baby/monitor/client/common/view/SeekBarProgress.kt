package co.netguru.baby.monitor.client.common.view

import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.feature.settings.ChangeState
import co.netguru.baby.monitor.client.databinding.SeekBarProgressBinding

class SeekBarProgress : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, attributeSetId: Int) : super(
        context,
        attrs,
        attributeSetId
    )

    private val binding = SeekBarProgressBinding.inflate(LayoutInflater.from(context), this)

    fun setState(valueState: Pair<ChangeState?, Int?>) {
        val (changeState, value) = valueState
        resolveVisibility(changeState)
        when (changeState) {
            ChangeState.Completed -> setAnimatedDrawable(true)
            ChangeState.Failed -> setAnimatedDrawable(false)
            ChangeState.InProgress -> Unit
            null -> {
                value?.let {
                    binding.progressText.text = it.toString()
                }
            }
        }
    }

    private fun resolveVisibility(changeState: ChangeState?) {
        binding.progressText.isVisible = changeState == null
        binding.progress.isVisible = changeState == ChangeState.InProgress
        binding.progressIcon.isVisible =
            changeState == ChangeState.Completed || changeState == ChangeState.Failed
    }

    private fun setAnimatedDrawable(success: Boolean) {
        val animatedVectorDrawable = AppCompatResources.getDrawable(
            context,
            if (success) R.drawable.animated_done else R.drawable.animated_fail
        ) as? AnimatedVectorDrawable
        animatedVectorDrawable?.let {
            binding.progressIcon.setImageDrawable(it)
            it.start()
        }
    }
}
