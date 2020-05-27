package hu.inf.unideb.oldgold.results;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Az adatbázis műveletekért felelős osztály.
 */
public class GameResultDao {

    private EntityManager entityManager;

    /**
     * Növeli a játékosok pontszámát {@code score} értékkel, a játékos  {@code nameOfPlayer} alapján.
     *
     * @param gameResult A módosítani kívánt játékállás példánya.
     */
    public void increaseScoreByGameResult(GameResult gameResult) {
        entityManager.getTransaction().begin();
        gameResult.setScore(gameResult.getScore()+1);
        entityManager.merge(gameResult);
        entityManager.getTransaction().commit();
    }

    /**
     * Visszatér játékállások listájával, pont szerint csökkenő sorrendben.
     *
     * @return {@code GameResult} listája.
     */
    public List<GameResult> findAllOrderByScoreAsc() {
        TypedQuery<GameResult> query = entityManager.createQuery("SELECT gr FROM GameResult gr " +
                "ORDER BY gr.score DESC", GameResult.class);
        return query.getResultList();
    }

    /**
     * A játékállás példány rögzítése az adatbázisban.
     *
     * @param gameResult Játékállás példánya.
     */
    public void persist(GameResult gameResult){
        entityManager.getTransaction().begin();
        entityManager.persist(gameResult);
        entityManager.getTransaction().commit();
    }

    /**
     * Megkeresi a játékállást az adatbázisban egy a játékos neve szerint és visszaadja,
     * ha nincs akkor null-t ad vissza.
     *
     * @param name Játékos neve
     * @return {@code GameResult} Játékállás vagy {@code null} ha nem talált a névhez játékállást.
     */
    public GameResult findByName(String name){
        TypedQuery<GameResult> query = entityManager.createQuery("select gr from GameResult gr where gr.nameOfPlayer=:name",GameResult.class);
        query.setParameter("name",name);
        try{
            return query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * Entitymanager setter.
     * @param entityManager EntityManager példány.
     */
    @Inject
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
