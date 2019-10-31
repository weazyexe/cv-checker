package exe.weazy.cvchecker.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import exe.weazy.cvchecker.arch.MainContract
import exe.weazy.cvchecker.entity.Viewer
import exe.weazy.cvchecker.entity.Rank
import exe.weazy.cvchecker.entity.Visit
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

    override fun loadVisits() {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        firestore.document("visits/$day.$month").get().addOnCompleteListener { snapshot ->
            if (snapshot.isSuccessful && snapshot.result != null) {
                val data = snapshot.result!!

                val rawVisitsMap = data["visits"] as ArrayList<Map<String, Any>>?

                if (rawVisitsMap != null) {
                    val rawVisits = mutableListOf<FirestoreVisit>()

                    rawVisitsMap.forEach {
                        rawVisits.add(FirestoreVisit(it["viewer"] as DocumentReference, it["timestamp"] as Timestamp))
                    }

                    val visits = mutableListOf<Visit>()

                    rawVisits.forEach {
                        it.viewer.get().addOnCompleteListener { viewerSnapshot ->
                            if (viewerSnapshot.isSuccessful && viewerSnapshot.result != null) {
                                // viewer snapshot data
                                val vsd = viewerSnapshot.result!!

                                val name = vsd["name"] as String
                                val surname = vsd["surname"] as String
                                val patronymic = vsd["patronymic"] as String
                                val rank = Rank.valueOf(vsd["rank"] as String)
                                val uid = vsd["uid"] as String
                                val email = vsd["email"] as String

                                val viewer = Viewer(name, surname, patronymic, uid, rank, email)

                                visits.add(Visit(viewer, it.timestamp))

                                presenter.onVisitsLoadSuccess(visits)
                            } else {
                                presenter.onVisitsLoadFailure(Throwable("Can't get viewer data: ${it.viewer.id}"))
                            }
                        }
                    }
                } else {
                    presenter.onVisitsLoadFailure(Throwable("Can't get visits from snapshot result"))
                }
            } else {
                presenter.onVisitsLoadFailure(Throwable("Visits snapshot is failure or result is null"))
            }
        }
    }

    override fun uploadViewer(viewer: Viewer) {
        firestore.document("viewers/${viewer.uid}").set(viewer)
    }

    override fun updateVisits(visits: List<Visit>) {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val month = Calendar.getInstance().get(Calendar.MONTH) + 1

        val firestoreVisits = mutableListOf<FirestoreVisit>()

        visits.forEach {
            firestoreVisits.add(FirestoreVisit(firestore.document("viewers/${it.viewer.uid}"), it.date))
        }

        val result = mutableMapOf<String, List<FirestoreVisit>>()
        result["visits"] = firestoreVisits

        firestore.document("visits/$day.$month").set(result, SetOptions.merge())
    }

    private data class FirestoreVisit(val viewer: DocumentReference, val timestamp: Timestamp)
}