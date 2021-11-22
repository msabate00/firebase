package cat.copernic.msabatem.ivanclase

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class Rtdatabase : AppCompatActivity() {


    private lateinit var database: FirebaseDatabase
    private lateinit var btnmodificar: Button
    private lateinit var etmodificar: EditText
    private lateinit var tvmodificar: TextView
    private lateinit var btnobjecte: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rtdatabase)
        // Write a message to the database
        database = FirebaseDatabase.getInstance("https://ivanclase-default-rtdb.europe-west1.firebasedatabase.app/")

        val myRef = database.getReference("message")

        btnmodificar = findViewById<Button>(R.id.btmodificar);
        etmodificar = findViewById<EditText>(R.id.etmodificar);
        tvmodificar = findViewById<TextView>(R.id.tvtextomodificado);
        btnobjecte = findViewById<Button>(R.id.bt_insertarobjeto);

        //myRef.setValue("Hello, World! yeeeee")

        btnmodificar.setOnClickListener {
            val myRef = database.getReference("message")

            myRef.setValue(etmodificar.text.toString());
            btnmodificar.isEnabled = false;
        }

        myRef.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<String>();
                tvmodificar.text = value;
                btnmodificar.isEnabled = true;
            }


            override fun onCancelled(error: DatabaseError) {
                tvmodificar.text = "Error de lectura"
                btnmodificar.isEnabled = true;
            }





        })

        btnobjecte.setOnClickListener {
            posarDadesUsuari();
        }
        obtenirDadesUsuari();

    }


    data class DatosUsuari(
        var nom: String? = "",
        var cognoms: String? = "",
        var cont: Int = 1,
        var list: List<Int> = mutableListOf(0),
        var listSt: List<String> = mutableListOf("")
    )
    fun posarDadesUsuari(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid;
        if( uid == null ) return

        val usuari =  DatosUsuari( "Marti", "Sabaté", 1, mutableListOf(1,2,3), mutableListOf("hola", "bon dia", "que tal estem")  )

        val myRef = database.getReference("/usuarios/$uid")
        myRef.setValue(usuari)
    }

    fun obtenirDadesUsuari(){
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if( uid == null ) return
        val myRef = database.getReference("/usuarios/$uid")
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<DatosUsuari>()
                Log.d(TAG, "Value is: $value")
                val mostra = "${value?.nom?:""} ${value?.cognoms?:""} ${value?.cont?: ""}"
                tvmodificar.text = mostra
                etmodificar.setText( mostra )
                //btModifica.isEnabled = true
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
                tvmodificar.text = "error de lectura"
                //btModifica.isEnabled = true
            }
        })
    }
}