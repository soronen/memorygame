package database.dao;

import database.datasource.SqlJpaConn;
import database.entity.Account;
import database.entity.Leaderboard;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.ArrayList;

public class LeaderboardDAO implements ILeaderboardDAO {


    @Override
    public boolean saveScores(Leaderboard lb) {
        try {
            EntityManager em = SqlJpaConn.getInstance();
            em.getTransaction().begin();
            em.persist(lb);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            System.out.println("error saving a score to db.." + e);
        }
        return false;
    }


    // does work
    @Override
    public ArrayList<Leaderboard> getAccountScores(Long accountid) {
        EntityManager em = SqlJpaConn.getInstance();
        // why is accountid typed twice? ¯\_(ツ)_/¯
        Query query = em.createQuery("SELECT l FROM Leaderboard l WHERE l.accountid.accountid = :accountid ORDER BY score DESC limit 100");
        query.setParameter("accountid", accountid);
        ArrayList<Leaderboard> scores = (ArrayList<Leaderboard>) query.getResultList();
        return scores;
    }


    /**
     * selects top 100 scores
     * @return
     */
    @Override
    public ArrayList<Leaderboard> readWorldScores() {
        EntityManager em = SqlJpaConn.getInstance();
        // why is accountid typed twice? ¯\_(ツ)_/¯
        Query query = em.createQuery("SELECT l FROM Leaderboard l ORDER BY score DESC limit 100");
        ArrayList<Leaderboard> scores = (ArrayList<Leaderboard>) query.getResultList();
        return scores;
    }

    @Override
    public boolean deleteScore(Long scoreid) {
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
}
