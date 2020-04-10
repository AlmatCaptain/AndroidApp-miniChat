package kz.application.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        run()
    }

    fun run() {
        sign_up_button.setOnClickListener { signUp() }
    }

    private fun signUp() {

        val email = email_edit_text.editText?.text.toString()
        val username = username_edit_text.editText?.text.toString()
        val password = password_edit_text.editText?.text.toString()

        Log.d("gggg", email + " " + password)

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid

                    val user = hashMapOf(
                        "email" to email,
                        "username" to username,
                        "uid" to uid
                    )
                    Firebase.database
                        .collection("users")
                        .document()
                        .set(user).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                startActivity(
                                    Intent(
                                        this,
                                        LoginActivity::class.java
                                    )
                                )
                                return@addOnCompleteListener
                            }
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()

                    return@addOnCompleteListener
                }

                Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
            }
    }
}
