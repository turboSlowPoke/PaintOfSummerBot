package dbservices;

import dbservices.entyties.Address;
import dbservices.entyties.User;
import org.apache.log4j.Logger;

import javax.persistence.*;

public class DbService {
    private static final Logger log = Logger.getLogger(DbService.class);
    private static DbService dbService;
    private final EntityManagerFactory entityManagerFactory;

    public DbService() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("eclipsMysql");
    }

    public synchronized static DbService getInstance(){
        if (dbService==null)
            dbService=new DbService();
        return dbService;
    }

    public synchronized User findUser(Long chatId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.telegramChatId=:id",User.class)
                .setParameter("id",chatId);
        User user = null;
        try {
            user = query.getSingleResult();
        }catch (Exception e){
            System.out.println("ошибка при поиске юзера chatid="+chatId);
            log.error("ошибка при поиске юзера chatid="+chatId);
        }finally {
            em.clear();
            em.close();
            return user;
        }
    }

    public synchronized boolean addNewUser(User user) {
        boolean check = false;
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        try {
            em.persist(user);
            tr.commit();
            check=true;
            log.info("Добавлен новый юзер "+user);
        }catch (Exception e){
            System.out.println("Ошибка при сохранении юзера "+user);
            log.error("Ошибка при сохранении юзера "+user);
            if (tr.isActive())
                tr.rollback();
        }finally {
            em.clear();
            em.close();
        }
        return check;
    }

    public synchronized boolean dbHasApartment(int apartmentNumber) {
        boolean check = false;
        EntityManager em = entityManagerFactory.createEntityManager();
        TypedQuery<Address> query = em.createQuery("SELECT a FROM Address a WHERE a.apartment=:n",Address.class)
                .setParameter("n",apartmentNumber);
        try {
            Address address = query.getSingleResult();
            if (address!=null){
                check=true;
            }
        }catch (Exception e){
            System.out.println("Ошибка при проверке номера квартиры");
            log.error("Ошибка при проверке номера квартиры");
        }finally {
            em.clear();
            em.close();
            return check;
        }
    }

    public synchronized void updateUser(User user) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tr = em.getTransaction();
        tr.begin();
        try {
            em.merge(user);
            tr.commit();
        }catch (Exception e){
            log.error("Ошибка при обновлении юзера");
            System.out.println("Ошибка при обновлении юзера");
            if (tr.isActive())
                tr.rollback();
        }finally {
            em.clear();
            em.close();
        }
    }
}
