package mx.tecnm.tepic.ladm_u1_practica2

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import java.io.*
import java.lang.Exception
import java.nio.Buffer

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }

        abrir.setOnClickListener {
            if(radio()){
                abrirFile()
            }else{
                abrirExt()
            }

        }
        guardar.setOnClickListener {
            if(radio()){
                guardarInterna()
            }else{
                guardarExt()
            }

        }

    }
    private fun guardarExt():Boolean{
      try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No hay memoria sd insertada ")
                        .setPositiveButton("ok") { d, i ->
                            d.dismiss()
                        }
                        .show()
                return false;
           }
           // var ruta= mContext.getExternalFilesDir(null)//Environment.getExternalStorageDirectory()
          //lo cambie porque a partir de la api 29 o android 6 no me deja usar el environment
            var archivoSD= File(getExternalFilesDir(null),archivo.text.toString())//se cambio por el context de getExternalFilesDir
            var flujoSalida=OutputStreamWriter(FileOutputStream(archivoSD))

            flujoSalida.write(textarea.text.toString())
            flujoSalida.flush()
            flujoSalida.close()


       }catch(io:IOException){
            AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo guardar")
                    .setPositiveButton("ok"){
                        d,i->d.dismiss()
                    }
                    .show()
            return false
        }
        AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Se guardo en la sd")
                .setPositiveButton("ok"){
                    d,i->d.dismiss()
                }
                .show()
        return true
    }
    private fun guardarInterna():Boolean{
        try{
            var flujosalida=OutputStreamWriter(openFileOutput(archivo.text.toString(),Context.MODE_PRIVATE))
            var data=textarea.text.toString()
            flujosalida.write(data)
            flujosalida.flush()
            flujosalida.close()
        }catch(io:IOException){
            AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo guardar, revise los campos")
                    .setPositiveButton("ok"){
                        d,i->d.dismiss()
                    }
                    .show()
            return false
        }
        return true
    }
    private fun abrirFile():String{//abrir interna
        var cont=""
        try{
            var flujoentrada=BufferedReader(InputStreamReader(openFileInput(archivo.text.toString())))
            cont=flujoentrada.readText()
            flujoentrada.close()
            textarea.setText(cont)
        }catch(io:IOException){
            AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se abrir, introduzca un nombre válido")
                    .setPositiveButton("ok"){
                        d,i->d.dismiss()
                    }
                    .show()
            return ""
        }
        return cont;
    }
    private fun abrirExt():String{
        var cont=""
        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("No hay memoria sd insertada ")
                        .setPositiveButton("ok") { d, i ->
                            d.dismiss()
                        }
                        .show()
                return "";
            }
            //var ruta = Environment.getExternalStorageDirectory()
            var archivoSD= File(getExternalFilesDir(null),archivo.text.toString())
            var flujoEntrada=InputStreamReader(FileInputStream(archivoSD))
            cont=flujoEntrada.readText()
            textarea.setText(cont)



        }catch(io:Exception){
            AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("No se pudo leer")
                    .setPositiveButton("ok"){
                        d,i->d.dismiss()
                    }
                    .show()
            return ""
        }
        AlertDialog.Builder(this)
                .setTitle("Atencion")
                .setMessage("Se leyo de la sd")
                .setPositiveButton("ok"){
                    d,i->d.dismiss()
                }
                .show()
        return cont
    }
    private fun radio():Boolean{
        if(interno.isChecked){
        return true
        }else if(externa.isChecked){
        return false
        }
        return true
    }





}