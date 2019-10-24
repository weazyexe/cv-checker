package exe.weazy.cvchecker.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import exe.weazy.cvchecker.R
import exe.weazy.cvchecker.util.hideKeyboard
import exe.weazy.cvchecker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_viewers.*

class MainActivity : AppCompatActivity() {

    private lateinit var scanFragment: ScanFragment
    private lateinit var viewersFragment: ViewersFragment
    private var active = Fragment()

    private lateinit var viewModel : MainViewModel

    private var newPosition = 1
    private var startingPosition = 1


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_scan -> {
                newPosition = 0
                if (startingPosition != newPosition) {
                    changeFragment(scanFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_viewers -> {
                newPosition = 1
                if (startingPosition != newPosition) {
                    changeFragment(viewersFragment)
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        loadFragments()

        active = viewersFragment

        if (savedInstanceState != null) {
            newPosition = savedInstanceState["new_position"] as Int
            startingPosition = savedInstanceState["starting_position"] as Int

            when (startingPosition) {
                0 -> {
                    changeFragment(scanFragment)
                    bottom_navigation.selectedItemId = R.id.navigation_scan
                }
                1 -> {
                    changeFragment(viewersFragment)
                    bottom_navigation.selectedItemId = R.id.navigation_viewers
                }
            }
        }
    }

    override fun onBackPressed() {
        if (!text_search.text.isNullOrBlank()) {
            text_search.setText("", TextView.BufferType.EDITABLE)
            hideKeyboard(layout_main, this)
            text_search.clearFocus()
        } else {
            super.onBackPressed()
        }
    }

    private fun loadFragments() {
        scanFragment = ScanFragment()
        viewersFragment = ViewersFragment()

        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, scanFragment).hide(scanFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.layout_fragment, viewersFragment).commit()

        bottom_navigation.selectedItemId = R.id.navigation_viewers
    }

    private fun changeFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()/*.setCustomAnimations(R.anim.top_level_in, R.anim.top_level_out)*/.show(fragment).hide(active).commit()
        startingPosition = newPosition
        active = fragment
    }
}
