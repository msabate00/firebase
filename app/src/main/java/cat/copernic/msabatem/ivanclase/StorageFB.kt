package cat.copernic.msabatem.ivanclase

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class StorageFB : AppCompatActivity() {

    private lateinit var ivBaixada: ImageView;
    private lateinit var btnGuardarImagen: Button;
    private lateinit var storageRef: StorageReference;
    private lateinit var tvMostra: TextView;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage_fb)

        ivBaixada = findViewById(R.id.ivBaixada);
        btnGuardarImagen = findViewById(R.id.btnGuardaImg);
        tvMostra = findViewById(R.id.tvMostra);

        var storage = FirebaseStorage.getInstance("gs://ivanclase.appspot.com");

        storageRef = storage.reference;
        val pathReference = storageRef.child("images/nuevoFondo.jpg")
        val im = pathReference.getBytes(500000);

        im.addOnSuccessListener {
            var bitmap = BitmapFactory.decodeByteArray(it,0, it.size);
            ivBaixada.setImageBitmap(bitmap);
        }


        btnGuardarImagen.setOnClickListener{
            guardarImage();
        }



    }

    private fun guardarImage() {

        var bitmaps = ivBaixada.drawable.toBitmap(ivBaixada.width, ivBaixada.height);
        var outba = ByteArrayOutputStream();
        bitmaps.compress(Bitmap.CompressFormat.JPEG, 50, outba);
        val dadesbytes = outba.toByteArray();
        val pathReferenceSubir = storageRef.child("imagesnova/nova.jpg")
        pathReferenceSubir.putBytes(dadesbytes);
        tvMostra.text = "SE HA SUBIDO";


    }
}