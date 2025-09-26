package co.netguru.baby.monitor.client.feature.client.home.log

import android.view.LayoutInflater
import android.view.ViewGroup
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.DateProvider
import co.netguru.baby.monitor.client.common.view.BaseViewHolder
import co.netguru.baby.monitor.client.data.client.home.log.LogData
import co.netguru.baby.monitor.client.databinding.ItemLogActivityHeaderBinding
import co.netguru.baby.monitor.client.databinding.ItemLogActivityRecordBinding
import org.threeten.bp.LocalDateTime
import org.threeten.bp.temporal.ChronoUnit

abstract class LogsViewHolder(
    val parent: ViewGroup,
    viewType: Int
) : BaseViewHolder<LogData>(
    LayoutInflater.from(parent.context).inflate(viewType, parent, false)
) {

    class DataLogsViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : LogsViewHolder(parent, viewType) {

        private val binding = ItemLogActivityRecordBinding.bind(itemView)

        override fun bindView(item: LogData) {
            val hourBefore = LocalDateTime.now().minusHours(1)
            if (item is LogData.Data) {
                binding.itemActivityLogActionTv.text = item.action
                val minutesAgo =
                    (HOUR_IN_MINUTES - hourBefore.until(item.timeStamp, ChronoUnit.MINUTES)).toInt()
                binding.itemActivityLogActionTimestampTv.text = if (item.timeStamp.isAfter(hourBefore)) {
                    itemView.context.resources.getQuantityString(
                        R.plurals.minutes_ago, minutesAgo, minutesAgo
                    )
                } else {
                    item.timeStamp.format(DateProvider.timeStampFormatter)
                }
            }
        }
    }

    class HeaderViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : LogsViewHolder(parent, viewType) {

        private val binding = ItemLogActivityHeaderBinding.bind(itemView)

        override fun bindView(item: LogData) {
            val today = DateProvider.midnight
            val yesterday = DateProvider.yesterdaysMidnight
            val baseText = item.timeStamp.format(DateProvider.headerFormatter)

            binding.itemActivityLogHeaderTv.text = when {
                item.timeStamp.isAfter(today) -> itemView.context.getString(
                    R.string.date_today,
                    baseText
                )
                item.timeStamp.isAfter(yesterday) -> itemView.context.getString(
                    R.string.date_yesterday,
                    baseText
                )
                else -> baseText
            }
        }
    }

    class EndTextHolder(
        parent: ViewGroup,
        viewType: Int
    ) : LogsViewHolder(parent, viewType) {
        override fun bindView(item: LogData) = Unit
    }

    companion object {
        const val HOUR_IN_MINUTES = 60
    }
}
