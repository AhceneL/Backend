-- Création de la table des utilisateurs
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nom VARCHAR(100),
                       prenom VARCHAR(100),
                       role VARCHAR(50) NOT NULL -- "membre" ou "gestionnaire"
);

-- Création de la table des projets
CREATE TABLE projets (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(150) NOT NULL,
                         description TEXT,
                         date_debut DATE,
                         date_fin DATE,
                         user_id INTEGER REFERENCES users(id) ON DELETE CASCADE -- créateur du projet
);

-- Création de la table des tâches
CREATE TABLE taches (
                        id SERIAL PRIMARY KEY,
                        titre VARCHAR(150) NOT NULL,
                        description TEXT,
                        statut VARCHAR(50) DEFAULT 'en_attente', -- ou 'en_cours', 'termine'
                        date_limite DATE,
                        projet_id INTEGER REFERENCES projets(id) ON DELETE CASCADE,
                        assignee_id INTEGER REFERENCES users(id) ON DELETE SET NULL -- assignée à un utilisateur
);

-- Création de la table des notifications
CREATE TABLE notifications (
                               id SERIAL PRIMARY KEY,
                               contenu TEXT NOT NULL,
                               date_envoi TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               lu BOOLEAN DEFAULT FALSE,
                               user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);
