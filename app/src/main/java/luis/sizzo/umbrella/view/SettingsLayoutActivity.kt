package luis.sizzo.umbrella.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import luis.sizzo.umbrella.R
import luis.sizzo.umbrella.common.*
import luis.sizzo.umbrella.databinding.SettingsLayoutBinding
import luis.sizzo.umbrella.model.local.WeatherLocal

class SettingsLayoutActivity : AppCompatActivity() {

    private lateinit var bindingSettings: SettingsLayoutBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSettings = SettingsLayoutBinding.inflate(layoutInflater)
        setContentView(bindingSettings.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Umbrella"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        WeatherLocal().getSettingColor(this)?.let { color ->
            changeBarColor(color)
            toolbar.setBackgroundColor(
                getColor(
                    color
                )
            )
        } ?: run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                changeBarColor(getColor(R.color.amber))
            }
            toolbar.setBackgroundColor(getColor(R.color.amber))
        }

        bindingSettings.settingZip.click {
            Dialogs().bottomSheetDialogSettings(this, "zip_code", false) { loadData() }
        }

        bindingSettings.settingUnits.click {
            Dialogs().bottomSheetDialogSettings(this, "units", false) { loadData() }
        }

        bindingSettings.swipeRefreshLayoutSettings.setOnRefreshListener {
            loadData()
        }

        loadData()

    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        WeatherLocal().getSettingZipCode(this)?.let { zip ->
            WeatherLocal().getSettingZipCountryCode(this)?.let { countryCode ->
                bindingSettings.settingZipText.text = "$countryCode, $zip"
            }
        }
        bindingSettings.settingUnitsText.text = Utilities().getTempFormat(this)
        bindingSettings.swipeRefreshLayoutSettings.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                Intent(this, MainActivity::class.java).apply {
                    startActivity(this)
                    finish()
                }
                return true
            }
        }
        return false
    }
}