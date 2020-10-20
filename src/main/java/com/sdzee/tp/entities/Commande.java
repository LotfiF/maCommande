package com.sdzee.tp.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.eclipse.persistence.annotations.Convert;
import org.eclipse.persistence.annotations.Converter;
import org.joda.time.DateTime;

import com.sdzee.tp.tools.JodaDateTimeConverter;

@Entity
public class Commande  implements Serializable {
	
	
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long      id;
    
    @ManyToOne
    @JoinColumn( name = "id_client" )
    private Client    client;
    
    @Column( columnDefinition = "TIMESTAMP" )
    @Converter( name = "dateTimeConverter", converterClass = JodaDateTimeConverter.class )
    @Convert( "dateTimeConverter" )
    private DateTime date;
    
	private Double    montant;
	@Column( name = "mode_paiement" )
    private String    modePaiement;
	@Column( name = "statut_paiement" )
    private String    statutPaiement;
	@Column( name = "mode_livraison" )
    private String    modeLivraison;
	@Column( name = "statut_livraison" )
    private String    statutLivraison;
    

    
    
    public void setId( Long id ) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }   

    public void setClient( Client client ) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
    
    public void setDate( DateTime date) {
        this.date = date;
    }
    
    public DateTime getDate() {
        return date;
    }
    
    public void setMontant( Double montant ) {
        this.montant = montant;
    }
    public Double getMontant() {
        return montant;
    }

    public void setModePaiement( String modePaiement ) {
        this.modePaiement = modePaiement;
    }
    public String getModePaiement() {
        return modePaiement;
    }

    public void setStatutPaiement( String statutPaiement ) {
        this.statutPaiement = statutPaiement;
    }
    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setModeLivraison( String modeLivraison ) {
        this.modeLivraison = modeLivraison;
    }
    public String getModeLivraison() {
        return modeLivraison;
    }

    public void setStatutLivraison( String statutLivraison ) {
        this.statutLivraison = statutLivraison;
    }
    public String getStatutLivraison() {
        return statutLivraison;
      
    }
	
}
