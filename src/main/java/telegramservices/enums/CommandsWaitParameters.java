package telegramservices.enums;

public enum CommandsWaitParameters {
    FLAT("Квартира"),
    FAIL("FAIL");
    CommandsWaitParameters(String text) {
        this.text = text;
    }

    private String text;

    public String getText() {
        return text;
    }

    public static CommandsWaitParameters getTYPE(String s){
        CommandsWaitParameters type = FAIL;
        for (CommandsWaitParameters tempTYPE : CommandsWaitParameters.values()){
            if (s.equals(tempTYPE.getText()))
                type = tempTYPE;
        }
        return type;
    }
}
