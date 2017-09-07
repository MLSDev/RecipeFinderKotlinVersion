package com.mlsdev.recipefinder.kotlinversion.view.utils

class ParamsHelper {

    companion object {
        fun formatLabel(label: String?): String {
            if (label == null)
                throw IllegalArgumentException("'label' can't be 'null'")

            var formattedLabel = label.toLowerCase()
            formattedLabel = formattedLabel.replace(" ", "-")
            return formattedLabel
        }
    }

}
