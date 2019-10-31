package exe.weazy.cvchecker.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import exe.weazy.cvchecker.arch.MainContract
import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.entity.Rank
import exe.weazy.cvchecker.presenter.MainPresenter
import java.util.*

class MainModel(private val presenter : MainPresenter) : MainContract.Model {

    private val firestore = FirebaseFirestore.getInstance()

    override fun loadParticipants() {
        firestore.collection("viewers").orderBy("uid", Query.Direction.DESCENDING).get().addOnCompleteListener { snapshot ->
            val data = snapshot.result

            if (data != null) {

                if (!data.isEmpty) {
                    val participants = mutableListOf<Viewer>()

                    data.forEach {
                        val name = it["name"] as String
                        val surname = it["surname"] as String
                        val patronymic = it["patronymic"] as String
                        val rank = Rank.valueOf(it["rank"] as String)
                        val uid = it["uid"] as String
                        val email = it["email"] as String

                        participants.add(Viewer(name, surname, patronymic, uid, rank, email))
                    }

                    presenter.onParticipantsLoadSuccess(participants)
                } else {
                    presenter.onParticipantsLoadFailure(Throwable("Data snapshot is empty"))
                }
            } else {
                presenter.onParticipantsLoadFailure(Throwable("Data snapshot is null"))
            }
        }
    }

    override fun loadScanStats() {

    }

    override fun uploadViewer(viewer: Viewer) {
        firestore.document("viewers/${viewer.uid}").set(viewer)
    }

    override fun uploadScanStats(viewer: Viewer, date: Date) {

    }
}