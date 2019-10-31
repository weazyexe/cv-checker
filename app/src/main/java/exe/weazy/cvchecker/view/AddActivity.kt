package exe.weazy.cvchecker.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import com.google.android.material.textfield.TextInputEditText
import exe.weazy.cvchecker.R
import exe.weazy.cvchecker.util.ADD_ACTIVITY_REQUEST_CODE
import exe.weazy.cvchecker.util.ADD_ACTIVITY_RESULT_SUCCESS
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
    }

    override fun onStart() {
        super.onStart()

        button_done.setOnClickListener {
            onDoneClick()
        }

        text_email.setOnEditorActionListener { _, actionId, _ ->
            when(actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    onDoneClick()
                    return@setOnEditorActionListener true
                }
                else -> {
                    return@setOnEditorActionListener false
                }
            }
        }

        text_surname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!isNameCorrect(p0.toString())) {
                    textLayout_surname.error = getString(R.string.uncorrect)
                } else {
                    textLayout_surname.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        text_name.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!isNameCorrect(p0.toString())) {
                    textLayout_name.error = getString(R.string.uncorrect)
                } else {
                    textLayout_name.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        text_email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (!isNameCorrect(p0.toString())) {
                    textLayout_email.error = getString(R.string.uncorrect)
                } else {
                    textLayout_email.isErrorEnabled = false
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun onDoneClick() {
        val intent = Intent()

        val surname = text_surname.text.toString().trim()
        val name = text_name.text.toString().trim()
        val patronymic = text_patronymic.text.toString().trim()
        val email = text_email.text.toString().trim()

        if (!isNameCorrect(surname)) {
            textLayout_surname.error = getString(R.string.uncorrect)
        } else if (!isNameCorrect(name)) {
            textLayout_name.error = getString(R.string.uncorrect)
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textLayout_email.error = getString(R.string.uncorrect)
        } else {
            intent.putExtra("surname", surname)
            intent.putExtra("name", name)
            intent.putExtra("patronymic", patronymic)
            intent.putExtra("email", email)

            setResult(ADD_ACTIVITY_RESULT_SUCCESS, intent)
            finish()
        }



    }

    private fun isNameCorrect(name : String)
            = name.isNotBlank() && name.length > 1 && !name.contains(Regex("[0-9]"))
}
