package telegramservices.enums;

public enum IncidentData {
    ONEHOUR("Менее часа"),
    THREEHOUR("Менее трёх часов"),
    ENDOFDAY("К вечеру"),
    ONEDAY("Один день"),
    TWODAY("Два дня"),
    WERYLONG("Больше двух дней "),
    FAIL("Неверные данные");
    private String text;

    IncidentData(String text) {
        this.text=text;
    }

    public String getText() {
        return text;
    }

    public static IncidentData getTYPE(String s){

        IncidentData type = FAIL;
        for (IncidentData tempTYPE : IncidentData.values()){
            if (s.equals(tempTYPE.getText()))
                type = tempTYPE;
        }
        return type;
    }
}
