package exe.weazy.cvchecker.arch

import exe.weazy.cvchecker.entity.Viewer

interface MainContract {
    interface View {
        fun showLoading()
        fun showError(msg: String?)
        fun showContent(data: List<Viewer>)
    }

    interface Presenter {
        fun attachView(view : View)
        fun loadViewers(isUpdate: Boolean = false)
        fun findViewer(uid: String) : Viewer?
        fun search(query: String)
        fun getCurrentViewer(position : Int) : Viewer
    }

    interface Model {
        fun loadParticipants()
    }

    interface LoadingListener {
        fun onParticipantsLoadSuccess(data: List<Viewer>)
        fun onParticipantsLoadFailure(t: Throwable)
    }
}