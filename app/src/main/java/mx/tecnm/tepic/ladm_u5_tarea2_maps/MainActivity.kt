package mx.tecnm.tepic.ladm_u5_tarea2_maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.rpc.context.AttributeContext
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity : AppCompatActivity(){
    var baseRemota = FirebaseFirestore.getInstance()
    var position = ArrayList<Data>()


    lateinit var locacion : LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),7)
        }

        baseRemota.collection("locations")
            .addSnapshotListener { querySnapshot, error ->
                if (error!=null){
                    txt_locations.setText("Error: ${error.message}")
                    return@addSnapshotListener
                }

                var resultado = ""
                position.clear()
                for (document in querySnapshot!!){
                    var data = Data()
                    data.nombre = document.getString("nombre").toString()
                    resultado += document.getString("nombre").toString()+"\n"
                    data.posicion1 = document.getGeoPoint("posicion1")!!
                    resultado += document.getGeoPoint("posicion1")!!.toString()+"\n"
                    data.posicion2 = document.getGeoPoint("posicion2")!!
                    resultado += document.getGeoPoint("posicion2")!!.toString()+"\n"
                    data.informacion = document.getString("informacion").toString()
                    resultado += "-------------\n"
                    //resultado += data.toString()+"-------------\n"
                    position.add(data)
                }
                txt_locations.setText(resultado)
            }


        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, oyente)
        /*btn_locations.setOnClickListener {
            locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            var oyente = Oyente(this)
            locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 01f, oyente)
        }*/

        btn_integrantes.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Integrantes del equipo")
                .setMessage("Edwin Antonio Arellano Mata\nMaria Elena Montanez Herrera\nM Michelle Salinas Tirado\nPaul Neftaly Campos Contreras")
                .setPositiveButton("OK"){p,q -> }
                .show()
        }

        btn_buscar.setOnClickListener {
            val intentoAbrir = Intent(this, MainActivity2::class.java)
            startActivity(intentoAbrir)
        }
        imageForum.setImageResource(R.drawable.forum1)
    }
}


class Oyente(puntero:MainActivity) : LocationListener {
    var p = puntero

    override fun onLocationChanged(locationOne: Location) {
        p.txt_coordenada.setText("Ubicacion actual:\n${locationOne.latitude}, ${locationOne.longitude}")
        var geoPosicionGPS = GeoPoint(locationOne.latitude, locationOne.longitude)

        for (item in p.position) {
            if (item.estoyEn(geoPosicionGPS)) {
                p.txt_actual.setText("Te encuentras en: ${item.nombre}")
                imagenes(item.nombre, item.informacion)
            }
        }
    }//onLocationChanged

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    fun imagenes(img : String, info : String){
        when(img){
            "Liverpool"->{
                p.imageView1.setImageResource(R.drawable.liv1)
                p.imageView5.setImageResource(R.drawable.liv3)
                p.imageView6.setImageResource(R.drawable.live2)
                p.txt_infoMain.setText(info)
                p.imageForum.setImageResource(R.drawable.liverpool1)
            }
            "KFC"->{
                p.imageView1.setImageResource(R.drawable.kfc1)
                p.imageView5.setImageResource(R.drawable.kfc2)
                p.imageView6.setImageResource(R.drawable.kfc3)
                p.txt_infoMain.setText(info)
                p.imageForum.setImageResource(R.drawable.kfcforum)
            }
            "C&A"->{
                p.imageView1.setImageResource(R.drawable.canda1)
                p.imageView5.setImageResource(R.drawable.canda2)
                p.imageView6.setImageResource(R.drawable.canda3)
                p.txt_infoMain.setText(info)
                p.imageForum.setImageResource(R.drawable.candaforum)
            }
            "Cinemex"->{
                p.imageView1.setImageResource(R.drawable.cine1)
                p.imageView5.setImageResource(R.drawable.cine2)
                p.imageView6.setImageResource(R.drawable.cine3)
                p.txt_infoMain.setText(info)
                p.imageForum.setImageResource(R.drawable.cinemexforum)
            }
        }
    }

}//Oyente



































