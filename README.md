# MyBestLocation-App-Mobile

**Version :** 1.0  
**Auteur :** [Yasmine Souissi]  

---

## 📌 Description  
MyBestLocation est une application Android interactive utilisant l'API Google Maps pour offrir une expérience de localisation multi-utilisateurs. L'application permet :  
- D'afficher une carte interactive.  
- D'enregistrer et de partager ses positions géographiques.  
- De consulter les positions d'autres utilisateurs.  

Elle repose sur une architecture client-serveur avec une base de données MySQL et des services PHP pour gérer les utilisateurs et les données de localisation.

---

## ✨ Fonctionnalités principales  
1. **Connexion et gestion des comptes utilisateurs**  
   - Inscription et connexion sécurisées.  
   - Gestion des informations de profil.  

2. **Carte interactive (API Google Maps)**  
   - Affichage des positions personnelles sur une carte.  
   - Enregistrement des localisations.  
   - Visualisation des positions des autres utilisateurs en temps réel.  

3. **Backend centralisé**  
   - Base de données MySQL pour stocker les utilisateurs et les positions.  
   - Services PHP pour gérer les interactions entre l'application et la base de données.

---

## 🛠️ Structure du projet  

### 1. **Frontend : Application Android**  
- **Langage :** Java.  
- **Développement :** Android Studio.  
- **Activités principales :**  
  - `MainActivity` : Navigation principale.  
  - `Login` & `SignUp` : Authentification des utilisateurs.  
  - `MapsActivity` : Carte interactive avec enregistrement et consultation des positions.  
  - `Contacts` : Visualisation des utilisateurs connectés.  
  - `Locations` : Liste des positions enregistrées.  
  - `Account` : Gestion du profil utilisateur.

### 2. **Backend : Services PHP**  
- Gestion des utilisateurs (connexion, inscription).  
- Sauvegarde et récupération des positions.  

### 3. **Base de données : MySQL**  
- Tables principales :  
  - `users` : Informations des utilisateurs (nom, email, mot de passe, etc.).  
  - `locations` : Coordonnées géographiques et identifiant utilisateur.

---

## ⚙️ Installation et configuration  

### Prérequis  
1. **Android Studio :** Pour exécuter ou modifier l'application.  
2. **XAMPP :** Pour héberger les fichiers PHP et la base MySQL.  
3. **Clé API Google Maps :** Nécessaire pour activer les fonctionnalités de carte.  

### Étapes d'installation  
1. **Cloner le projet GitHub :**  
   ```bash
   git clone https://github.com/your-repo/MyBestLocation.git
Configurer XAMPP :

2. **Placez les fichiers PHP dans le dossier htdocs.**
Créez une base de données MySQL et importez le fichier database/schema.sql.
3. **Ajouter votre clé API Google Maps :**

Ouvrez AndroidManifest.xml.
Remplacez la valeur de :
xml
Copier le code
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="VOTRE_CLE_API_ICI" />
4. **Lancer l'application :**

Importez le projet dans Android Studio.
Configurez un émulateur ou connectez un appareil physique.
## 🚀 Fonctionnement
1. **Inscription et connexion :**

Les utilisateurs créent un compte ou se connectent via les activités SignUp et Login.
2. **Carte interactive :**

Affiche la carte Google Maps.
Permet d'enregistrer des positions (cliquez pour ajouter un marqueur).
3. **Enregistrement des données :**

Les positions sont sauvegardées dans la base MySQL via des services PHP.
Partage des positions :

Les positions des autres utilisateurs sont récupérées et affichées en temps réel.
## 🌐 Technologies utilisées
- Frontend : Java, Android SDK.
- Backend : PHP, MySQL (via XAMPP).
-API : Google Maps API.
## 📅 Prochaines améliorations
- Intégration de notifications en temps réel pour les nouvelles positions.
- Ajout d’un système de messagerie entre utilisateurs.
- Amélioration de l'interface utilisateur avec des animations.
## 🤝 Contributeurs
[Yasmine Souissi] ([(https://github.com/YasmineSouissi/)])

