package mx.tecnm.tepic.ladm_u5_tarea2_maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var baseRemota2 = FirebaseFirestore.getInstance()
    var datos = ArrayList<Data>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        baseRemota2.collection("locations")
            .addSnapshotListener { querySnapshot, error ->
                if (error!=null){
                    txt_locations.setText("Error: ${error.message}")
                    return@addSnapshotListener
                }

                datos.clear()
                for (document in querySnapshot!!){
                    var data = Data()
                    data.nombre = document.getString("nombre").toString()
                    data.posicion1 = document.getGeoPoint("posicion1")!!
                    data.posicion2 = document.getGeoPoint("posicion2")!!
                    data.informacion = document.getString("informacion").toString()
                    datos.add(data)
                }
            }

        btn_buscarNombres.setOnClickListener{
            for (items in datos) {
                if(edt_buscar.text.toString().equals(items.nombre,true)){
                    informacion(edt_buscar.text.toString().toLowerCase(), items.informacion)
                }
            }
        }
        btn_regresarMain.setOnClickListener {
            finish()
        }
    }

    private fun informacion(info: String, cont : String) {
        when(info){
            "liverpool"->{
                imageView2.setImageResource(R.drawable.liv1)
                imageView3.setImageResource(R.drawable.liv3)
                imageView4.setImageResource(R.drawable.live2)
                txt_info.setText(cont)
                imageBusquedaForum.setImageResource(R.drawable.liverpool1)
            }
            "kfc"->{
                imageView2.setImageResource(R.drawable.kfc1)
                imageView3.setImageResource(R.drawable.kfc2)
                imageView4.setImageResource(R.drawable.kfc3)
                txt_info.setText(cont)
                imageBusquedaForum.setImageResource(R.drawable.kfcforum)
            }
            "c&a"->{
                imageView2.setImageResource(R.drawable.canda1)
                imageView3.setImageResource(R.drawable.canda2)
                imageView4.setImageResource(R.drawable.canda3)
                txt_info.setText(cont)
                imageBusquedaForum.setImageResource(R.drawable.candaforum)
            }
            "cinemex"->{
                imageView2.setImageResource(R.drawable.cine1)
                imageView3.setImageResource(R.drawable.cine2)
                imageView4.setImageResource(R.drawable.cine3)
                txt_info.setText(cont)
                imageBusquedaForum.setImageResource(R.drawable.cinemexforum)
            }
        }
    }
}