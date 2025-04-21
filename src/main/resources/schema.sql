--  Utilisateurs
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       nom VARCHAR(100),
                       prenom VARCHAR(100),
                       role VARCHAR(50) NOT NULL -- "membre" ou "gestionnaire"
);

--  Projets
CREATE TABLE projet (
                         id SERIAL PRIMARY KEY,
                         nom VARCHAR(150) NOT NULL,
                         description TEXT,
                         date_debut DATE,
                         date_fin DATE,
                         user_id INTEGER REFERENCES users(id) ON DELETE CASCADE -- créateur du projet
);

--  Projets <-> Membres (relation ManyToMany)
CREATE TABLE projet_membres (
                                projet_id INTEGER NOT NULL,
                                membre_id INTEGER NOT NULL,
                                PRIMARY KEY (projet_id, membre_id),
                                CONSTRAINT fk_projet FOREIGN KEY (projet_id) REFERENCES projet(id) ON DELETE CASCADE,
                                CONSTRAINT fk_membre FOREIGN KEY (membre_id) REFERENCES users(id) ON DELETE CASCADE
);

--  Tâches
CREATE TABLE tache (
                       id SERIAL PRIMARY KEY,
                       titre VARCHAR(150) NOT NULL,
                       description TEXT,
                       statut VARCHAR(50) DEFAULT 'en_attente', -- 'en_cours', 'termine'
                       date_limite DATE,
                       projet_id INTEGER REFERENCES projet(id) ON DELETE CASCADE, -- Clé étrangère vers la table projet
                       assignee_email VARCHAR(255), -- Email de l'utilisateur assigné
                       FOREIGN KEY (assignee_email) REFERENCES users(email) ON DELETE SET NULL -- Clé étrangère vers l'email de l'utilisateur, ON DELETE SET NULL
                       fichier VARCHAR(255)
);


CREATE TABLE notifications (
    id SERIAL PRIMARY KEY,  -- Identifiant unique de la notification
    message TEXT NOT NULL,  -- Le message de la notification
    type VARCHAR(100) NOT NULL,  -- Le type de notification (par exemple, "Tâche", "Projet")
    is_read BOOLEAN DEFAULT FALSE,  -- Statut de lecture (false par défaut)
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Date de création de la notification
    assignee_email VARCHAR(255) NOT NULL  -- Email de l'utilisateur assigné à la notification
);
);

CREATE TABLE commentaires (
                              id SERIAL PRIMARY KEY,
                              tache_id BIGINT NOT NULL,
                              commentaire TEXT NOT NULL,
                              FOREIGN KEY (tache_id) REFERENCES tache(id) ON DELETE CASCADE
);

