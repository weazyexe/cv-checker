package exe.weazy.cvchecker.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import exe.weazy.cvchecker.R
import exe.weazy.cvchecker.entity.Rank
import exe.weazy.cvchecker.entity.Viewer
import java.text.SimpleDateFormat
import java.util.*

fun buildViewerDialog(viewer: Viewer, activity: Activity) : AlertDialog {

    val builder: AlertDialog.Builder = activity.let {
        AlertDialog.Builder(it)
    }

    val msg = StringBuilder()


    msg.append("${viewer.surname} ${viewer.name} ${viewer.patronymic}\n")


    val rank = StringBuilder("Статус: ")
    rank.append(getViewerRankTitle(viewer))
    msg.append("${rank}\n")


    val date = getDateByUid(viewer)
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    msg.append("Дата регистрации: ${formatter.format(date)}")


    builder.setMessage(msg.toString())
        .setTitle(R.string.viewer_info)
        .setPositiveButton("OK") { _, _ ->

        }

    return builder.create()
}

fun getDateByUid(viewer: Viewer) = Date(viewer.uid.toLong(16) * 1000)

fun getViewerRankTitle(viewer: Viewer) : String {
    return when(viewer.rank) {
        Rank.VIEWER -> "зритель"
        Rank.STAFF -> "персонал"
        Rank.ORGS -> "организатор"
        Rank.PLAYER -> "игрок"
        Rank.STANDIN -> "стенд-ин (замена)"
        Rank.PEDIK -> "Лёша педик"
    }
}

fun hideKeyboard(view: View?, context: Context?) {
    val inputManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(
        view?.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}