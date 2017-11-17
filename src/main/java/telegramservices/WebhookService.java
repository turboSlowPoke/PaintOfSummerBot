package telegramservices;

import dbservices.DbService;
import dbservices.entyties.Address;
import dbservices.entyties.User;
import main.Config;
import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import telegramservices.enums.CommandsWaitParameters;
import telegramservices.enums.IncidentType;
import telegramservices.enums.KeybordCommand;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebhookService extends TelegramWebhookBot{
    private static final Logger log = Logger.getLogger(WebhookService.class);
    @NotNull
    private MenuCreator menuCreator;

    public WebhookService() {
        this.menuCreator = new MenuCreator();
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
                System.out.println("юзера нет в бд");
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
        System.out.println("commandContext");
        String command = incomingMessage.getText();
        SendMessage response = new SendMessage(incomingMessage.getChatId(),"Неверная комманда");
        if (command.startsWith("/квартира")){
            Integer apartmentNumber = getNumber(incomingMessage.getText());
            if (apartmentNumber==null) {
                response.setText("Вы ввели некорректный номер квартиры");
            }else if (DbService.getInstance().dbHasApartment(apartmentNumber)){
                response.setText("Пользователь с таким номером квартиры уже существует");
                log.error("юзер "+user+" ввел номер чужой квартиры");
            }else {
                Address address = user.getAddress();
                address.setApartment(apartmentNumber);
                DbService.getInstance().updateUser(user);
                response.setText("Ваш этаж "+address.getFloor()+", ваш строительный номер "+address.getBuildNumber());
            }
        }else if (command.equals("/start")){
            System.out.println("Создаем ответ на /start");
            response.setText("Главное меню");
            response.setReplyMarkup(menuCreator.createMainMenu());
            System.out.println("ответ на /start создан");
        }
        return response;
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
        long telegramChatId = incomingMessage.getChatId();
        SendMessage response = new SendMessage(incomingMessage.getChatId(), "Что-то пошло не так, сообщите @i_kuteynikov");
        //новый пользователь должен ввести номер квартиры
        if (DbService.getInstance().getWaitParametersMap().containsKey(telegramChatId)) {
            Integer apartmentNumber = getNumber(incomingMessage.getText());
            if (apartmentNumber==null) {
                response.setText("Вы ввели некорректный номер квартиры!"+"\nВведите номер квартиры:");
            }else if (DbService.getInstance().dbHasApartment(apartmentNumber)){
                response.setText("Пользователь с таким номером квартиры уже существует, разобраться в ситуации поможет админ");
                log.error("юзер ввел номер чужой квартиры chatid="+incomingMessage.getChatId());
            }else {
                String firstName = validName(incomingMessage.getChat().getFirstName());
                String lastName = validName(incomingMessage.getChat().getLastName());
                String telegramUsername = incomingMessage.getChat().getUserName();
                User user = new User(telegramChatId, firstName, lastName, telegramUsername);
                System.out.println("Создан юзер " + user);
                Address address = user.getAddress();
                address.setApartment(apartmentNumber);
                DbService.getInstance().addNewUser(user);
                response.setText("Ваш этаж "+address.getFloor()+", ваш строительный номер "+address.getBuildNumber());
            }

        }else {
            DbService.getInstance().getWaitParametersMap().put(telegramChatId, CommandsWaitParameters.FLAT);
            response.setText("Добро пожаловать!" +
                    "\n\n*Введите номер квартиры:*");
            response.setReplyMarkup(menuCreator.createMainMenu());
            response.enableMarkdown(true);
            System.out.println("Ждем номер квартиры от " + incomingMessage.getChatId());
        }
        return response;
    }

    private BotApiMethod mainContext(User user, Message incomingMessage) {
        System.out.println("mainContext");
        SendMessage response = new SendMessage(incomingMessage.getChatId(),"Неизвестная команда!");
        KeybordCommand command = KeybordCommand.getTYPE(incomingMessage.getText());
        switch (command){
            case CURRENTINCIDENT:
                response.setText("Текущие аварии..");
                break;
            case NEWINCIDENT:
                response.setText("Выберите тип аварии:");
                response.setReplyMarkup(menuCreator.createSelectIncidentTypeMenu());
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
        EditMessageText response = new EditMessageText()
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setChatId(callbackQuery.getMessage().getChatId());

        String requestText = callbackQuery.getMessage().getText();
        IncidentType incidentType = IncidentType.getTYPE(requestText);
        switch (incidentType){
            case ELECTRO:
                response.setText("Свяжитесь пожалуйста с УК, чтобы уточнить время решения:"+
                        "\nТелефон УК, электрик..."+
                        "\nУкажите через сколько устранят: ");
                response.setReplyMarkup(menuCreator.createTimeToSolveMenu(IncidentType.ELECTRO));
                break;
                
        }

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
        return Config.botname;
    }

    @Override
    public String getBotToken() {
        return Config.token;
    }

    @Override
    public String getBotPath() {
        return Config.botPath;
    }
}
