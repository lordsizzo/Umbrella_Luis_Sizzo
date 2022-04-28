package luis.sizzo.umbrella.common

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import luis.sizzo.umbrella.R
import luis.sizzo.umbrella.view.adapter.WeatherAdapter

fun View.snackbar(message:String, duration: Int = Snackbar.LENGTH_LONG){
    Snackbar.make(this, message, duration).show()
}

fun Context.toast(message:String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

fun View.click(listener: (View) -> Unit){
    this.setOnClickListener{
        listener(it)
    }
}

fun RecyclerView.configRecycler(adapter: WeatherAdapter){
    this.layoutManager =
        GridLayoutManager(this.context, 3)
    this.adapter = adapter
}

fun Spinner.itemSelected(listener: (Int) -> Unit){
    this.onItemSelectedListener =
        object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: android.widget.AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                listener(position)
            }
        }
}
fun Activity.settings(){
    this.window.navigationBarColor = resources.getColor(R.color.blue)
    this.window.statusBarColor = resources.getColor(R.color.blue)
}

fun Activity.changeBarColor(color: Int){
    this.window.navigationBarColor = resources.getColor(R.color.blue)
    this.window.statusBarColor = resources.getColor(color)
}
