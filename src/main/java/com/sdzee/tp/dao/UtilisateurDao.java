package com.sdzee.tp.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sdzee.tp.entities.Utilisateur;



@Stateless
public class UtilisateurDao implements Serializable {
	
    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "tp_sdzee_PU" )
    private EntityManager em;
    
    public Utilisateur trouver( String email ) throws DAOException {
        try {
        	Query query = em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email");
        	query.setParameter("email", email);
        	/* return (Utilisateur) query.getSingleResult(); */ 
        	/* return (Utilisateur) query.getResultList().get(0);  */ 
        	return query.getResultList().size() > 0 ? (Utilisateur) query.getResultList().get(0) : null;
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }
    
    public void creer( Utilisateur utilisateur ) throws DAOException {
        try {
            em.persist( utilisateur );
        } catch ( Exception e ) {
            throw new DAOException( e );
        }
    }

    
}
