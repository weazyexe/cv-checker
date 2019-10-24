package exe.weazy.cvchecker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import exe.weazy.cvchecker.presenter.MainPresenter

class MainViewModel : ViewModel() {

    private lateinit var presenter : MutableLiveData<MainPresenter>


    fun getPresenter() : LiveData<MainPresenter> {
        if (!::presenter.isInitialized) {
            presenter = MutableLiveData()
            presenter.postValue(MainPresenter())
        }

        return presenter
    }
}