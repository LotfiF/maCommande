package com.sdzee.tp.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sdzee.tp.entities.Utilisateur;

import com.sdzee.tp.dao.DAOException;
import com.sdzee.tp.dao.UtilisateurDao;


public final class ConnexionForm {

    private static final String CHAMP_EMAIL  = "email";
    private static final String CHAMP_PASS   = "motdepasse";
    
    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();
    private UtilisateurDao      utilisateurDao;
    
    public ConnexionForm( UtilisateurDao utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public Utilisateur connecterUtilisateur( HttpServletRequest request ) {
        
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
                       
        Utilisateur utilisateur = new Utilisateur();
        
        traiterEmail( email, utilisateur );
        traiterMotDePasse( motDePasse, utilisateur );
        
        try {
            if ( erreurs.isEmpty() ) {
            	utilisateurDao.trouver( email );
                resultat = "Succès de la connexion.";
            } else {
            	resultat = "Échec de la connexion.";
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la connexion." );
            resultat = "Échec de la connexion : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }

        /* Validation du champ email. */   
        private void traiterEmail( String email, Utilisateur utilisateur ) {
        try {
            validationEmail( email );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_EMAIL, e.getMessage() );
        }
        utilisateur.setEmail( email );
        }
        
        /* Validation du champ motDePasse. */   
        private void traiterMotDePasse( String motDePasse, Utilisateur utilisateur ) {
        try {
        	validationMotDePasse( motDePasse );
        } catch ( FormValidationException e ) {
            setErreur( CHAMP_PASS, e.getMessage() );
        }
        utilisateur.setMotDePasse( motDePasse );
        }
        
    /* Validateur de l'adresse email */
    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null && !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            }
        }
    
    /* Validateur du mot de passe.*/     
    private void validationMotDePasse( String motDePasse ) throws FormValidationException {
        if ( motDePasse != null ) {
            if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Le mot de passe doit contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir votre mot de passe." );
        }
    }
    
    /* Ajoute un message correspondant au champ spécifié à la map des erreurs. */
    private void setErreur( String champ, String message ) {
        erreurs.put( champ, message );
    }

    /* Méthode utilitaire qui retourne null si un champ est vide, et son contenu sinon.*/
    private static String getValeurChamp( HttpServletRequest request, String nomChamp ) {
        String valeur = request.getParameter( nomChamp );
        if ( valeur == null || valeur.trim().length() == 0 ) {
            return null;
        } else {
            return valeur;
        }
    }
          
}
