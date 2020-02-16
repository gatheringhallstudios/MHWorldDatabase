package com.gatheringhallstudios.mhworlddatabase.features.workshop.detail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.gatheringhallstudios.mhworlddatabase.R
import android.widget.EditText




class RenameSetDialog: DialogFragment() {
    var resultName: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_rename_set, null))
                    // Add action buttons
                    .setPositiveButton(R.string.user_equipment_apply)
                            { dialog, id ->
                                val f = dialog as Dialog
                                val inputTemp = f.findViewById<View>(R.id.new_set_name) as EditText
                                resultName = inputTemp.text.toString()
                                listener.onDialogPositiveClick(this)
                            }
                    .setNegativeButton(R.string.user_equipment_cancel)
                            { dialog, id ->
                                listener.onDialogNegativeClick(this)
                            }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    internal lateinit var listener: RenameDialogListener

    interface RenameDialogListener {
        fun onDialogPositiveClick(dialog: RenameSetDialog)
        fun onDialogNegativeClick(dialog: RenameSetDialog)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (targetFragment != null) {
            listener = targetFragment as RenameDialogListener
        } else {
            listener = activity as RenameDialogListener
        }
    }
}