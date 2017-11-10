package telegramservices;

import dbservices.DbService;
import dbservices.entyties.Address;
import dbservices.entyties.User;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import telegramservices.enums.KeybordCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebhookService extends TelegramWebhookBot{
    private static final Logger log = Logger.getLogger(WebhookService.class);
    private MenuCreator menuCreator;

    public WebhookService() {
        this.menuCreator = menuCreator;
    }


    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        System.out.println("Пришел update");
        BotApiMethod response=null;
        if (update.hasCallbackQuery()){
            response = callBackContext(update.getCallbackQuery());
        } else if (update.hasMessage()&&!update.getMessage().isGroupMessage()&&update.getMessage().hasText()) {
            Message incomingMessage = update.getMessage();
            User user = DbService.getInstance().findUser(incomingMessage.getChatId());
            if (user==null){
                response = startContext(incomingMessage);
            }else {
                if (incomingMessage.getText().startsWith("/")){
                    response = commandContext(user,incomingMessage);
                }else {
                    response = mainContext(user, incomingMessage);
                }
            }
        }
        System.out.println("response отправлен");
        return response;
    }

    private BotApiMethod commandContext(User user, Message incomingMessage) {
        String command = incomingMessage.getText();
        SendMessage response = new SendMessage(incomingMessage.getChatId(),"Не верная комманда");
        if (command.startsWith("/квартира")){
            Integer apartmentNumber = getNumber(incomingMessage.getText());
            if (apartmentNumber==null) {
                response.setText("Вы ввели некорректный номер квартиры");
            }else if (DbService.getInstance().dbHasApartment(apartmentNumber)){
                response.setText("Пользователь с таким номером уже существует");
                log.error("юзер "+user+" ввел номер чужой квартиры");
            }else {
                Address address = user.getAddress();
                address.setApartment(apartmentNumber);
                DbService.getInstance().updateUser(user);
                response.setText("");
            }
        }else if (command.equals("/start")){
            response.setText("Главное меню");
            response.setReplyMarkup(menuCreator.createMainMenu());
        }
        return null;
    }

    private Integer getNumber(String text) {
        Integer number = null;
        try{
            number = Integer.parseInt(text.substring(10));
            if (number!=null&&number<1||number>400){
                number=null;
                log.error("Номер квартиры <1 или >400");
            }
        }catch (Exception e){
            number=null;
            log.error("Некорректный номер квартиры");
        }
        return number;
    }

    private BotApiMethod startContext(Message incomingMessage) {
        System.out.println("Создаём нового юзера");
        long telegramChatId = incomingMessage.getChatId();
        String firstName = validName(incomingMessage.getChat().getFirstName());
        String lastName = validName(incomingMessage.getChat().getLastName());
        String telegramUsername = incomingMessage.getChat().getUserName();
        User user = new User(telegramChatId,firstName,lastName,telegramUsername);
        boolean userIsAdded= DbService.getInstance().addNewUser(user);
        SendMessage response = new SendMessage(incomingMessage.getChatId(),"что-то пошло не так, сообщите @i_kuteynikov");
        if (userIsAdded) {
            String welcomeText = "Добро пожаловать" + firstName != null ? "," + firstName : "!";
            response.setText(welcomeText +
                    "\nОтправьте: " +
                    "\n\"/квартира *номер вашей квартиры*\"" +
                    "\n Краткая инструкция");
            response.setReplyMarkup(menuCreator.createMainMenu());
            response.enableHtml(true);
        }
        return response;
    }

    private BotApiMethod mainContext(User user, Message incomingMessage) {
        SendMessage response = new SendMessage(incomingMessage.getChatId(),"Неизвестная команда!");
        KeybordCommand command = KeybordCommand.getTYPE(incomingMessage.getText());
        switch (command){
            case CURRENTCRASH:
                response.setText("Текущие аварии..");
                break;
            case NEWCRASH:
                response.setText("Создать новую аварию");
                break;
            case INFO:
                response.setText("Информация");
                break;
            case CHAT:
                response.setText("Ссылка на чат");
                break;
            case NEWS:
                response.setText("Новости");
                break;
            case MY_ACCOUNT:
                response.setText("Настройка данных");
                break;
        }
        return response;
    }

    private BotApiMethod callBackContext(CallbackQuery callbackQuery) {
        return null;
    }

    public String validName(String name) {
        String validName="-";
        String p = "([\\w]|.)*";
        Pattern pattern = Pattern.compile(p,Pattern.UNICODE_CHARACTER_CLASS);
        if (name!=null) {
            try {
                Matcher matcher = pattern.matcher(name);
                if (matcher.matches()) {
                    validName = name;
                }
            } catch (Exception e) {
                log.info("Не прошло проверку имя "+name);
            }
        }
        return name;
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return null;
    }

    @Override
    public String getBotPath() {
        return null;
    }
}
