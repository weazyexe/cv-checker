package exe.weazy.cvchecker.arch

import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.entity.Visit
import java.util.*

interface MainContract {
    interface View {
        fun showLoading()
        fun showError(msg: String?)
        fun showContent(data: List<Viewer>)
        fun showSnackbar(msg: String)
    }

    interface Presenter {
        fun attachView(view : View)
        fun loadViewers(isUpdate: Boolean = false)
        fun findViewer(uid: String) : Viewer?
        fun search(query: String)
        fun getCurrentViewer(position : Int) : Viewer
        fun uploadViewer(viewer : Viewer)
        fun addVisit(viewer: Viewer)
    }

    interface Model {
        fun loadParticipants()
        fun loadVisits()
        fun uploadViewer(viewer: Viewer)
        fun updateVisits(visits: List<Visit>)
    }

    interface LoadingListener {
        fun onParticipantsLoadSuccess(data: List<Viewer>)
        fun onParticipantsLoadFailure(t: Throwable)

        fun onVisitsLoadSuccess(data: List<Visit>)
        fun onVisitsLoadFailure(t: Throwable)
    }
}