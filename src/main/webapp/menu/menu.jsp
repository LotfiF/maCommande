<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="menu">
    <p><a href="<c:url value="/client/creationClient"/>">Créer un nouveau client</a></p>
    <p><a href="<c:url value="/commande/creationCommande"/>">Créer une nouvelle commande</a></p>
    <p><a href="<c:url value="/client/listerClients"/>">Voir les clients existants</a></p>
    <p><a href="<c:url value="/commande/listerCommandes"/>">Voir les commandes existants</a></p>
    <p><a href="<c:url value="/deconnexion"/>">Se déconnecter</a></p>
</div>
