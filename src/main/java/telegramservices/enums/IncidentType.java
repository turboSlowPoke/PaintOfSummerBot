package telegramservices.enums;

public enum IncidentType {
    //create crash
    ELECTRO("нет электричества"),
    COLDWATER("нет горячей воды"),
    HOTWATER("нет холодной воды"),
    ALLWATER("нет холодной и горячей воды"),
    LIFT("не работает лифт"),
    FLOODING("потоп"),
    HEATING("нет отопления"),
    OTHER("Другая")
    ;

    private String text;

    IncidentType(String text) {
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public static IncidentType getTYPE(String s){

        IncidentType type = OTHER;
        for (IncidentType tempTYPE : IncidentType.values()){
            if (s.equals(tempTYPE.getText()))
                type = tempTYPE;
        }
        return type;
    }
}
