package fi.masaz.celatum;

public class Tools {
    public static int getIcon(String iconStr) {
        int icon = R.drawable.baseline_note_24;

        if (iconStr != null) {
            switch (iconStr) {
                case "credit_card":
                    icon = R.drawable.baseline_credit_card_24;
                    break;
                case "key":
                    icon = R.drawable.baseline_key_24;
                    break;
                case "person":
                    icon = R.drawable.baseline_person_24;
                    break;
                case "link":
                    icon = R.drawable.baseline_link_24;
                    break;
            }
        }

        return icon;
    }
}
