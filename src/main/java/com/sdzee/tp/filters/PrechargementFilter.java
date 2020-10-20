package com.sdzee.tp.filters;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sdzee.tp.entities.Client;
import com.sdzee.tp.entities.Commande;
import com.sdzee.tp.dao.ClientDao;
import com.sdzee.tp.dao.CommandeDao;
import com.sdzee.tp.dao.UtilisateurDao;

@WebFilter(urlPatterns = { "/client/*","/commande/*","/menu/*"}) 
public class PrechargementFilter implements Filter {
    
	public static final String ATT_SESSION_CLIENTS   = "clients";
    public static final String ATT_SESSION_COMMANDES = "commandes";
    public static final String ATT_SESSION_USER = "sessionUtilisateur";
    public static final String ACCES_CONNEXION  = "/connexion";
    
    @EJB
    private ClientDao clientDao;
    @EJB
    private CommandeDao commandeDao;    
    @EJB
    private UtilisateurDao utilisateurDao;

    public void init( FilterConfig filterconfig ) throws ServletException {}
 
    public void doFilter( ServletRequest req, ServletResponse res, FilterChain chain ) throws IOException, ServletException {
    	
        /* Cast des objets request et response */
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        /* Non-filtrage des ressources statiques */
        String chemin = request.getRequestURI().substring( request.getContextPath().length() );
        if ( chemin.startsWith( "/inc" ) ) {
            chain.doFilter( request, response );
            return;
        }
        
        /* Récupération de la session depuis la requête */
        HttpSession session = request.getSession();
        
        /* Si l'objet utilisateur n'existe pas dans la session en cours, alors
        l'utilisateur n'est pas connecté. */         
       if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
           /* Redirection vers la page publique */
           request.getRequestDispatcher( ACCES_CONNEXION ).forward( request, response );
       } else {
    	   /* Si la map des clients n'existe pas en session, alors l'utilisateur se
            * connecte pour la première fois et nous devons précharger en session
            * les infos contenues dans la BDD.*/         
           if ( session.getAttribute( ATT_SESSION_CLIENTS ) == null ) {
               
           	/* Récupération de la liste des clients existants, et enregistrement en session */       
               List<Client> listeClients = clientDao.lister();
               Map<Long, Client> mapClients = new HashMap<Long, Client>();
               for ( Client client : listeClients ) {
                   mapClients.put( client.getId(), client );
               }
               session.setAttribute( ATT_SESSION_CLIENTS, mapClients );
           }

          /* De même pour la map des commandes */
          if ( session.getAttribute( ATT_SESSION_COMMANDES ) == null ) {
               
       	   /* Récupération de la liste des commandes existantes, et enregistrement en session  */                 
               List<Commande> listeCommandes = commandeDao.lister();
               Map<Long, Commande> mapCommandes = new HashMap<Long, Commande>();
               for ( Commande commande : listeCommandes ) {
                   mapCommandes.put( commande.getId(), commande );
               }
               session.setAttribute( ATT_SESSION_COMMANDES, mapCommandes );
           }

          /* Pour terminer, poursuite de la requête en cours */
           chain.doFilter( request, response );
       }
   }
    
    public void destroy() {
    }
}
