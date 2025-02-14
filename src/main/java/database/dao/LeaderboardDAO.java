package database.dao;

import database.datasource.SqlJpaConn;
import database.entity.Leaderboard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import model.ModeType;

import java.util.ArrayList;


/**
 * LeaderboardDAO class for the game
 * Contains methods for saving and retrieving scores from the database
 *
 * @author Eetu Soronen
 * @version 1
 */
public class LeaderboardDAO implements ILeaderboardDAO {


    /**
     * Saves a score to the database
     *
     * @param lb Leaderboard-object to be saved
     * @return true if successful, false if not
     */
    @Override
    public boolean saveScore(Leaderboard lb) {
        if (SqlJpaConn.getInstance() == null) {
            return false;
        }

        EntityManager em = SqlJpaConn.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(lb);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }


    /**
     * selects top 100 scores
     * sorted by points, then time
     *
     * @param accountid account id
     * @return ArrayList of Leaderboard-objects
     */
    @Override
    public ArrayList<Leaderboard> getAccountScores(Long accountid) {
        if (SqlJpaConn.getInstance() == null) {
            return null;
        }

        EntityManager em = SqlJpaConn.getInstance();
        // why is accountid typed twice? ¯\_(ツ)_/¯
        Query query = em.createQuery("SELECT l FROM Leaderboard l WHERE l.accountid.accountid = :accountid ORDER BY points desc, time asc limit 100");
        query.setParameter("accountid", accountid);
        return (ArrayList<Leaderboard>) query.getResultList();
    }

    /**
     * selects top 100 scores of select account and difficulty
     *
     * @param accountid  account id
     * @param difficulty difficulty
     * @return ArrayList of Leaderboard-objects
     */
    @Override
    public ArrayList<Leaderboard> getAccountScoresByDifficulty(Long accountid, ModeType difficulty) {
        if (SqlJpaConn.getInstance() == null) {
            return null;
        }

        EntityManager em = SqlJpaConn.getInstance();
        // why is accountid typed twice? ¯\_(ツ)_/¯
        Query query = em.createQuery("SELECT l FROM Leaderboard l WHERE l.accountid.accountid = :accountid AND l.difficulty = :difficulty ORDER BY points desc, time asc limit 100");
        query.setParameter("accountid", accountid);
        query.setParameter("difficulty", difficulty);
        return (ArrayList<Leaderboard>) query.getResultList();
    }

    /**
     * selects top 100 scores of select difficulty
     *
     * @return ArrayList of Leaderboard-objects
     */
    @Override
    public ArrayList<Leaderboard> readWorldScores(ModeType difficulty) {
        if (SqlJpaConn.getInstance() == null) {
            return null;
        }

        EntityManager em = SqlJpaConn.getInstance();
        // why is accountid typed twice? ¯\_(ツ)_/¯
        Query query = em.createQuery("SELECT l FROM Leaderboard l WHERE l.difficulty = :difficulty ORDER BY points desc, time asc limit 100");
        query.setParameter("difficulty", difficulty);
        return (ArrayList<Leaderboard>) query.getResultList();
    }

    /**
     * deletes a score by id
     *
     * @param scoreid score id
     * @return true if successful
     */
    @Override
    public boolean deleteScore(Long scoreid) {
        if (SqlJpaConn.getInstance() == null) {
            return false;
        }

        EntityManager em = SqlJpaConn.getInstance();
        em.getTransaction().begin();
        Leaderboard score = em.find(Leaderboard.class, scoreid);
        if (score != null) {
            em.remove(score);
            em.getTransaction().commit();
            return true;
        }
        em.getTransaction().commit();
        return false;
    }

    /**
     * deletes all scores of select account
     *
     * @param accountid account id
     */
    @Override
    public void deleteAllScores(Long accountid) {
        if (SqlJpaConn.getInstance() == null) {
            return;
        }

        EntityManager em = SqlJpaConn.getInstance();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("DELETE FROM Leaderboard l WHERE l.accountid.accountid = :accountid");
            query.setParameter("accountid", accountid);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}