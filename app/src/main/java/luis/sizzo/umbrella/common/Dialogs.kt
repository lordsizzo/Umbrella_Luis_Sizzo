package luis.sizzo.umbrella.common

import android.R
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialog
import luis.sizzo.umbrella.databinding.BottomSheetSettingsBinding
import luis.sizzo.umbrella.model.local.WeatherLocal


class Dialogs {

    private var units_temperature: Array<String> = arrayOf(
        "Select Unit","C°", "F°","K°"
    )
    private var units_temperature_values: Array<String> = arrayOf(
        "","metric", "imperial",""
    )

    private var country_code: Array<String> = arrayOf(
        "Select Country Code", "US", "MX", "CA", "AU"
    )
    @RequiresApi(Build.VERSION_CODES.M)
    fun bottomSheetDialogSettings(
        context: Context,
        flag: String,
        switch: Boolean,
        clickMe: () -> Unit
    ) {
        try {
                var unit = ""
                var zipCode = ""
                var zipCountry = ""
                val dialogSignatureView: BottomSheetSettingsBinding = BottomSheetSettingsBinding.inflate(
                    LayoutInflater.from(context)
                )

                val dialogBSDAddUser = BottomSheetDialog(context)
                dialogBSDAddUser.setCancelable(false)

                dialogSignatureView.bsdSpinnerUnits.adapter = ArrayAdapter<Any?>(
                    context,
                    R.layout.simple_spinner_dropdown_item,
                    units_temperature
                )
                dialogSignatureView.bsdSpinnerUnits.itemSelected{   position ->
                    unit = if (position != 0) {
                        units_temperature_values[position]
                    }else{
                        ""
                    }
                }


                WeatherLocal().getSettingColor(context)?.let {color ->
                    dialogSignatureView.bottomSheetHeader.setBackgroundColor(context.getColor(
                        color
                    ))
                }?: run {
                    dialogSignatureView.bottomSheetHeader.setBackgroundColor(context.resources.getColor(R.color.darker_gray))
               }

                dialogSignatureView.bsdSpinnerCountryCode.adapter = ArrayAdapter<Any?>(
                    context,
                    R.layout.simple_spinner_dropdown_item,
                    country_code
                )

                dialogSignatureView.bsdSpinnerCountryCode.itemSelected{   position ->
                    if (position != 0) {
                        zipCode = country_code[position]
                        zipCountry = dialogSignatureView.etZipCodeSetting.text.toString()
                    }else{
                        ""
                    }
                }


               if (flag == "zip_code"){
                    dialogSignatureView.bsdTitle.text = "Write your zip Code"
                    dialogSignatureView.bsdLayoutZipCode.visibility = View.VISIBLE
                    dialogSignatureView.bsdSpinnerUnits.visibility = View.GONE
                }else{
                    dialogSignatureView.bsdTitle.text = "Select your Units"
                    dialogSignatureView.bsdLayoutZipCode.visibility = View.GONE
                    dialogSignatureView.bsdSpinnerUnits.visibility = View.VISIBLE
                }
                dialogSignatureView.btnCancel.click {

                    if(switch){
                        dialogBSDAddUser.dismiss()
                        (context as Activity).finish()
                    }else{
                        dialogBSDAddUser.dismiss()

                    }
                }

                dialogSignatureView.btnSave.click {
                    if (flag == "zip_code"){
                        if(zipCode!="" && zipCountry!="") {
                            WeatherLocal().insertSettingsZip(context, zipCode, zipCountry)
                            clickMe()
                            dialogBSDAddUser.dismiss()
                        }
                        else {
                            context.toast("Please fill all the fields")
                        }
                    }else{
                        WeatherLocal().insertSettingsUnits(context, unit)
                        clickMe()
                        dialogBSDAddUser.dismiss()
                    }

                }


                dialogBSDAddUser.setContentView(dialogSignatureView.root)
                dialogBSDAddUser.show()

        } catch (e: Exception) {
            Log.e("DialogsData", "bottomSheetDialogSettings: $e")
            context.toast(e.toString(), Toast.LENGTH_LONG)
        }
    }
}