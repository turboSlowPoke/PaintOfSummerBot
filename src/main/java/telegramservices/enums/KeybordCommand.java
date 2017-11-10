package telegramservices.enums;

public enum  KeybordCommand {
    //mainmenu
    CURRENTCRASH("Текущие аварии"),
    NEWCRASH("Сообщить об аварии"),
    INFO("Полезная информация"),
    NEWS("Новости"),
    CHAT("Чат"),
    MY_ACCOUNT("Мои данные"),
    //info menu
    REFERENCEBOOK("Справочник"),
    FAQ("Частые впоросы"),
    HELP("Помощь"),
    //time crash
    CLARYFI("Исправить срок устранения"),
    ONEHOUR("Менее часа"),
    THREEHOUR("Менее трёх часов"),
    ENDOFDAY("К вечеру"),
    ONEDAY("Один день"),
    TWODAY("Два дня"),
    WERYLONG("Больше двух дней "),
    //create crash
    ELECTRO("нет электричества"),
    WATER("нет воды"),
    COLDWATER("горячей"),
    HOTWATER("холодной"),
    ALLWATER("холодной и горячей"),
    LIFT("Не работает лифт"),
    IMFLOODING("Я затапливаю"),
    FLOODING("Меня топят"),
    FAIL("Неизвестная команда"),
    HEATING("Нет отопления")
            ;

    KeybordCommand(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }

    public static KeybordCommand getTYPE(String s){
        KeybordCommand type = FAIL;
        for (KeybordCommand tempTYPE : KeybordCommand.values()){
            if (s.equals(tempTYPE.getText()))
                type = tempTYPE;
        }
        return type;
    }
}
