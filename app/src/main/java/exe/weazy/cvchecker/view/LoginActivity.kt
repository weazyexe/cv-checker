package exe.weazy.cvchecker.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import exe.weazy.cvchecker.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        updateUi(auth.currentUser)
    }

    override fun onStart() {
        super.onStart()

        button_login.setOnClickListener {
            auth.signInWithEmailAndPassword(text_login.text.toString(), text_password.text.toString()).addOnCompleteListener {
                if (it.isSuccessful) {
                    updateUi(auth.currentUser)
                } else {
                    Snackbar.make(layout_main, R.string.login_error, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateUi(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}