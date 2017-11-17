package telegramservices;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import telegramservices.enums.IncidentType;
import telegramservices.enums.KeybordCommand;

import java.util.ArrayList;
import java.util.List;

public class MenuCreator {
    public ReplyKeyboard createMainMenu() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton(KeybordCommand.CURRENTINCIDENT.getText()));
        keyboardRow1.add(new KeyboardButton(KeybordCommand.NEWINCIDENT.getText()));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton(KeybordCommand.INFO.getText()));
        keyboardRow2.add(new KeyboardButton(KeybordCommand.CHAT.getText()));
        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton(KeybordCommand.NEWS.getText()));
        keyboardRow3.add(new KeyboardButton(KeybordCommand.MY_ACCOUNT.getText()));
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);
        keyboardRows.add(keyboardRow3);
        keyboardMarkup.setKeyboard(keyboardRows);
        keyboardMarkup.setResizeKeyboard(true);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup createSelectIncidentTypeMenu() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        for (IncidentType incindentType : IncidentType.values()){
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText(incindentType.getText()).setCallbackData(incindentType.getText()));
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        System.out.println("Создан SelectIncidentTypeMenu");
        return markupInline;
    }

    public InlineKeyboardMarkup createTimeToSolveMenu(IncidentType incidentType) {

        return null;
    }
}
