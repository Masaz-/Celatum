package fi.masaz.celatum

object Tools {
    fun getIcon(iconStr: String?): Int {
        var icon = R.drawable.baseline_note_24

        if (iconStr != null) {
            when (iconStr) {
                "credit_card" -> icon = R.drawable.baseline_credit_card_24
                "key" -> icon = R.drawable.baseline_key_24
                "person" -> icon = R.drawable.baseline_person_24
                "link" -> icon = R.drawable.baseline_link_24
            }
        }

        return icon
    }
}
