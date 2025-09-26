package co.netguru.baby.monitor.client.feature.client.home.log

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import co.netguru.baby.monitor.client.R
import co.netguru.baby.monitor.client.common.base.BaseFragment
import co.netguru.baby.monitor.client.common.extensions.observeNonNull
import co.netguru.baby.monitor.client.common.extensions.setVisible
import co.netguru.baby.monitor.client.common.view.StickyHeaderDecorator
import co.netguru.baby.monitor.client.data.client.home.ToolbarState
import co.netguru.baby.monitor.client.databinding.FragmentClientActivityLogBinding
import co.netguru.baby.monitor.client.feature.analytics.Screen
import co.netguru.baby.monitor.client.feature.client.home.ClientHomeViewModel
import javax.inject.Inject

class ClientActivityLogFragment : BaseFragment() {
    override val layoutResource = R.layout.fragment_client_activity_log
    override val screen: Screen = Screen.CLIENT_ACTIVITY_LOG

    @Inject
    internal lateinit var factory: ViewModelProvider.Factory

    private val logAdapter by lazy { ActivityLogAdapter() }
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), factory)[ClientHomeViewModel::class.java]
    }
    private var _binding: FragmentClientActivityLogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.toolbarState.postValue(ToolbarState.HISTORY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentClientActivityLogBinding.bind(view)
        setupRecyclerView()

        viewModel.logData.observeNonNull(viewLifecycleOwner, { activities ->
            if (activities.isNotEmpty()) {
                logAdapter.setupList(activities)
            }
            binding.clientActivityLogRv.setVisible(activities.isNotEmpty())
            binding.clientActivityLogEndTv.setVisible(activities.isEmpty())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.toolbarState.postValue(ToolbarState.DEFAULT)
        binding.clientActivityLogRv.adapter = null
        _binding = null
    }

    private fun setupRecyclerView() {
        with(binding.clientActivityLogRv) {
            adapter = logAdapter
            addItemDecoration(StickyHeaderDecorator(logAdapter))
        }
    }
}
