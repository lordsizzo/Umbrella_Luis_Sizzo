package luis.sizzo.umbrella.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import luis.sizzo.umbrella.common.Utilities
import luis.sizzo.umbrella.common.toast
import luis.sizzo.umbrella.databinding.ItemWeatherBinding
import luis.sizzo.umbrella.model.remote.Weather
import kotlin.math.roundToInt


class WeatherAdapter(private val items: List<Weather>, val context: Context) :
    RecyclerView.Adapter<WeatherAdapter.BooksViewHolder>() {

    class BooksViewHolder(val binding: ItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        try {
            holder.binding.tvTemp.text = items[position].main.temp.roundToInt().toString() + "°"
            Picasso.get().load("https://openweathermap.org/img/wn/" + items[position].weather[0].icon + ".png").into(holder.binding.ivWeather)
            holder.binding.tvTime.text = Utilities().formatHour(items[position].dt_txt)?.substring(11)

        } catch (e: Exception) {
            context.toast(e.toString())
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}