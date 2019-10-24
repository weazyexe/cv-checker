package exe.weazy.cvchecker.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.cvchecker.R
import exe.weazy.cvchecker.adapter.ViewersAdapter
import exe.weazy.cvchecker.adapter.ViewersDiffUtilCallback
import exe.weazy.cvchecker.arch.MainContract
import exe.weazy.cvchecker.entity.Rank
import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.util.buildViewerDialog
import exe.weazy.cvchecker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_viewers.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.text.isEmpty as isEmpty1

class ViewersFragment : Fragment(), MainContract.View {

    private lateinit var viewModel: MainViewModel
    private lateinit var presenter: MainContract.Presenter

    private lateinit var adapter: ViewersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_viewers, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel::class.java)

        val liveData = viewModel.getPresenter()
        liveData.observe(this, Observer {
            presenter = it

            presenter.attachView(this)
            presenter.loadViewers()
        })

/*val firestore = FirebaseFirestore.getInstance()
    fireste.collection("teams").get().addOnCompleteListener { snapshot ->
    val result = snapshot.result

    if (result != null && !result.isEmpty) {
        val docs = result.documents

        val viewers = LinkedList<Viewer>()

        var timestamp = Date().time / 1000

        docs.forEach { doc ->
            timestamp += 1000

            val status = doc["status"] as String

            if (status == "CONFIRMED") {
                val email = (doc["contacts"] as Map<String, String>)["email"] as String
                val players = doc["players"] as ArrayList<String>?
                val standins = doc["standins"] as ArrayList<String>?

                players?.forEach {
                    val splitted = it.split(Regex(" '|' "))

                    viewers.add(Viewer(splitted[0], splitted[2], "", timestamp.toString(16).toUpperCase(Locale.getDefault()), Rank.PLAYER, email))

                    timestamp += 10
                }

                standins?.forEach {
                    val splitted = it.split(Regex(" '|' "))

                    viewers.add(Viewer(splitted[0], splitted[2], "", timestamp.toString(16).toUpperCase(Locale.getDefault()), Rank.STANDIN, email))

                    timestamp += 10
                }
            }
        }

        viewers.forEach {
            firestore.document("viewers/${it.uid}").set(it)
        }
    }
}*/
    }


    override fun onStart() {
        super.onStart()

        text_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (text_search.text.toString().isBlank()) {
                    presenter.loadViewers()
                } else {
                    presenter.search(p0.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }



    override fun showLoading() {
        layout_error.visibility = View.GONE
        layout_loading.visibility = View.VISIBLE
        layout_content.visibility = View.GONE
    }

    override fun showError(msg: String?) {
        layout_error.visibility = View.VISIBLE
        layout_loading.visibility = View.GONE
        layout_content.visibility = View.GONE

        if (msg != null) {
            Snackbar.make(layout_main, msg, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun showContent(data: List<Viewer>) {

        if (!::adapter.isInitialized) {

            val onItemClickListener = View.OnClickListener {
                val position = rv_viewers.getChildAdapterPosition(it)
                val viewer = presenter.getCurrentViewer(position)

                val dialog = buildViewerDialog(viewer, requireActivity())
                dialog.show()
            }

            adapter = ViewersAdapter(data.toMutableList(), onItemClickListener)

            rv_viewers.adapter = adapter
            rv_viewers.layoutManager = LinearLayoutManager(requireContext())
        } else {

            val diffUtilCallback = ViewersDiffUtilCallback(adapter.viewers, data)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback, false)

            adapter.setViewers(data)
            diffUtilResult.dispatchUpdatesTo(adapter)
        }

        layout_error.visibility = View.GONE
        layout_loading.visibility = View.GONE
        layout_content.visibility = View.VISIBLE
    }
}