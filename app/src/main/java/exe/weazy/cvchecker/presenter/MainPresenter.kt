package exe.weazy.cvchecker.presenter

import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.cvchecker.arch.MainContract
import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.model.MainModel
import java.util.*

class MainPresenter : MainContract.Presenter, MainContract.LoadingListener {

    private var viewers = mutableListOf<Viewer>()
    private var currentViewers = mutableListOf<Viewer>()

    private val model = MainModel(this)
    private lateinit var view : MainContract.View



    override fun attachView(view: MainContract.View) {
        this.view = view
    }

    override fun loadViewers(isUpdate: Boolean) {
        if (isUpdate || viewers.isNullOrEmpty()) {
            view.showLoading()
            model.loadParticipants()
        } else {
            view.showContent(viewers)
        }
    }

    override fun uploadViewer(viewer: Viewer) {
        model.uploadViewer(viewer)
        viewers.add(0, viewer)
        currentViewers.add(0, viewer)

        view.showContent(viewers)
    }

    override fun search(query: String) {
        val result = LinkedList<Viewer>()

        val q = query.toLowerCase()

        viewers.forEach {
            if (it.name.toLowerCase().contains(q) || it.surname.toLowerCase().contains(q)
                || it.patronymic.toLowerCase().contains(q)) {
                result.add(it)
            }
        }

        currentViewers = result

        view.showContent(result)
    }

    override fun findViewer(uid: String) = viewers.firstOrNull { it.uid == uid }

    override fun onParticipantsLoadSuccess(data: List<Viewer>) {
        viewers.clear()
        viewers.addAll(data)
        currentViewers.clear()
        currentViewers.addAll(data)


        view.showContent(viewers)
    }

    override fun onParticipantsLoadFailure(t: Throwable) {
        view.showError(t.message)
    }

    override fun getCurrentViewer(position: Int) = currentViewers[position]
}