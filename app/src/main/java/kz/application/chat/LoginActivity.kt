package kz.application.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kz.application.chat.model.Chat
import kz.application.chat.model.User

class LoginActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewSetup()
    }

    override fun onResume() {
        super.onResume()
        viewSetup()
    }

    private fun viewSetup() {

        if (Firebase.auth.currentUser?.email != null) {
            Log.d("ffff", Firebase.auth.currentUser?.email + " login")
            startActivity(
                Intent(
                    this,
                    ChatsActivity::class.java
                )
            )
            finish()
        }

        sign_in_button.setOnClickListener { signIn() }
        sign_up_link.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegistrationActivity::class.java
                )
            )
            finish()
        }
    }

    private fun signIn() {
        val email = email_edit_text.text.toString()
        val password = password_edit_text.text.toString()


        Firebase.auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this, ChatsActivity::class.java))
                finish()
                return@addOnCompleteListener
            }

            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
        }

    }
}
