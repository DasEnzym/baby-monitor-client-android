package co.netguru.baby.monitor.client.feature.communication.nsd

import android.net.nsd.NsdServiceInfo
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.databinding.FoundServiceItemBinding

class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = FoundServiceItemBinding.bind(view)

    fun bind(nsdServiceInfo: NsdServiceInfo) {
        val deviceName = nsdServiceInfo.serviceName.removeSuffix(NsdServiceManager.SERVICE_NAME)
        binding.deviceName.text =
            if (deviceName.isNotEmpty()) deviceName else itemView.resources.getString(
                R.string.unknown_device
            )
    }
}
