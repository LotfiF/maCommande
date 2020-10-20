package com.sdzee.tp.forms;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.util.password.ConfigurablePasswordEncryptor;

import com.sdzee.tp.entities.Utilisateur;
import com.sdzee.tp.dao.UtilisateurDao;
import com.sdzee.tp.forms.FormValidationException;
import com.sdzee.tp.dao.DAOException;

public final class InscriptionForm {

    private static final String CHAMP_EMAIL      = "email";
    private static final String CHAMP_PASS       = "motdepasse";
    private static final String CHAMP_CONF       = "confirmation";
    private static final String CHAMP_NOM        = "nom";
    
    private static final String ALGO_CHIFFREMENT = "SHA-256";   

    private String              resultat;
    private Map<String, String> erreurs          = new HashMap<String, String>();
    private UtilisateurDao      utilisateurDao;
    
    public InscriptionForm( UtilisateurDao utilisateurDao ) {
        this.utilisateurDao = utilisateurDao;
    }

    public Map<String, String> getErreurs() {
        return erreurs;
    }

    public String getResultat() {
        return resultat;
    }

    public Utilisateur inscrireUtilisateur( HttpServletRequest request ) {
        
        String email = getValeurChamp( request, CHAMP_EMAIL );
        String motDePasse = getValeurChamp( request, CHAMP_PASS );
        String confirmation = getValeurChamp( request, CHAMP_CONF );
        String nom = getValeurChamp( request, CHAMP_NOM );
        
        java.util.Date date= new java.util.Date();
        Timestamp stamp = new Timestamp(date.getTime()); 
                       
        Utilisateur utilisateur = new Utilisateur();
        
        try {
        utilisateur.setDateInscription(stamp);
        
        traiterEmail( email, utilisateur );
        traiterMotsDePasse( motDePasse, confirmation, utilisateur );
        traiterNom( nom, utilisateur );
        
        
            if ( erreurs.isEmpty() ) {
            	utilisateurDao.creer( utilisateur );
            	resultat = "Succès de l'inscription. Vous pouvez vous connecter.";
            } else {
            	resultat = "échec de l'inscription.";
            }
        } catch ( DAOException e ) {
            setErreur( "imprévu", "Erreur imprévue lors de la création." );
            resultat = "échec de l'inscription : une erreur imprévue est survenue, merci de réessayer dans quelques instants.";
            e.printStackTrace();
        }

        return utilisateur;
    }
    
        /* Appel à  la validation de l'adresse email reçue et initialisation de la propriété email du bean */
        private void traiterEmail( String email, Utilisateur utilisateur ) {
            try {
                validationEmail( email );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_EMAIL, e.getMessage() );
            }
            utilisateur.setEmail( email );
        }
        
        /* Appel à  la validation du nom reçu et initialisation de la propriété nom du bean */
        private void traiterNom( String nom, Utilisateur utilisateur ) {
            try {
                validationNom( nom );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_NOM, e.getMessage() );
            }
            utilisateur.setNom( nom );
        }
        
        /* Appel à  la validation des mots de passe reçus, chiffrement du mot de
           passe et initialisation de la propriété motDePasse du bean */         
        private void traiterMotsDePasse( String motDePasse, String confirmation, Utilisateur utilisateur ) {
            try {
                validationMotsDePasse( motDePasse, confirmation );
            } catch ( FormValidationException e ) {
                setErreur( CHAMP_PASS, e.getMessage() );
                setErreur( CHAMP_CONF, null );
            }
            /* Utilisation de la bibliothèque Jasypt pour chiffrer le mot de passe efficacement.*/
            ConfigurablePasswordEncryptor passwordEncryptor = new ConfigurablePasswordEncryptor();
            passwordEncryptor.setAlgorithm( ALGO_CHIFFREMENT );
            passwordEncryptor.setPlainDigest( false );
            String motDePasseChiffre = passwordEncryptor.encryptPassword( motDePasse );

            utilisateur.setMotDePasse( motDePasseChiffre );
        }
        
    /* Validation de l'adresse email */
    private void validationEmail( String email ) throws FormValidationException {
        if ( email != null ) {
            if ( !email.matches( "([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)" ) ) {
                throw new FormValidationException( "Merci de saisir une adresse mail valide." );
            } else if ( utilisateurDao.trouver( email ) != null ) {
                throw new FormValidationException( "Cette adresse email est déjà  utilisée, merci d'en choisir une autre." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir une adresse mail." );
        }
    }
    
    /* Validation des mots de passe */
    private void validationMotsDePasse( String motDePasse, String confirmation ) throws FormValidationException {
        if ( motDePasse != null && confirmation != null ) {
            if ( !motDePasse.equals( confirmation ) ) {
                throw new FormValidationException( "Les mots de passe entrés sont différents, merci de les saisir à  nouveau." );
            } else if ( motDePasse.length() < 3 ) {
                throw new FormValidationException( "Les mots de passe doivent contenir au moins 3 caractères." );
            }
        } else {
            throw new FormValidationException( "Merci de saisir et confirmer votre mot de passe." );
        }
    }
    
    /* Validation du nom */
    private void validationNom( String nom ) throws FormValidationException {
        if ( nom != null && nom.length() < 3 ) {
            throw new FormValidationException( "Le nom d'utilisateur doit contenir au moins 3 caractères." );
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