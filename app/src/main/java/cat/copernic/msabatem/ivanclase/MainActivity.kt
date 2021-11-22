package cat.copernic.msabatem.ivanclase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



class MainActivity : AppCompatActivity() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    private lateinit var tvText: TextView
    private lateinit var btAutentifica: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btAlta: Button
    private lateinit var btsalir: Button
    private lateinit var btRtDatabase: Button;
    private lateinit var btStorageFB: Button;

    // [END declare_auth]


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        btAutentifica = findViewById(R.id.button);
        tvText = findViewById(R.id.textview)
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPass);
        btAlta = findViewById(R.id.btAlta)
        btsalir = findViewById(R.id.salir)
        btRtDatabase = findViewById(R.id.btRtDatabase);
        btStorageFB = findViewById(R.id.btnStorageFB);


        btAutentifica.setOnClickListener {

            btAutentifica.isEnabled = false;
            signIn(etEmail.text.toString().trim(), etPassword.text.toString().trim())
            //signIn("sabatemiranda@gmail.com", "123456")
        }
        btAlta.setOnClickListener {
            btAlta.isEnabled = false;
            createAccount(etEmail.text.toString().trim(), etPassword.text.toString().trim())
            //signIn("sabatemiranda@gmail.com", "123456")
        }
        btsalir.setOnClickListener {

            auth.signOut();
            reload();

        }

        btRtDatabase.setOnClickListener {
            val intent = Intent(this, Rtdatabase::class.java);
            startActivity(intent);
        }
        btStorageFB.setOnClickListener {
            val intent = Intent(this, StorageFB::class.java);
            startActivity(intent);
        }



    }

    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()


        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){
            reload();
        }
    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btAlta.isEnabled = true;
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                btAutentifica.isEnabled = true
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
        if( user != null ) {
            tvText.text = "Email usuari: ${user.email}"
        }else{
            tvText.text = "No s'ha identificat"
        }
    }

    private fun reload() {
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    companion object {
        private const val TAG = "EmailPassword"
    }
}