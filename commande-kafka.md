### **1. Accédez au conteneur Kafka**

#### Listez les conteneurs pour trouver le nom du conteneur Kafka :
```bash
docker ps
```

#### Exemple de sortie :
```plaintext
CONTAINER ID   IMAGE                      COMMAND                  CREATED       STATUS       PORTS                                      NAMES
a1b2c3d4e5f6   confluentinc/cp-kafka:7.4  "/etc/confluent/dock…"   5 hours ago   Up 5 hours   0.0.0.0:9092->9092/tcp, 0.0.0.0:2181->2181/tcp   kafka
```

Le nom du conteneur Kafka est probablement quelque chose comme `kafka`.

#### Connectez-vous au conteneur Kafka :
```bash
docker exec -it kafka bash
```

---

### **2. Créez un topic dans Kafka**

#### Commande pour créer un topic :
```bash
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic account-events
```

#### Détails des options :
- `--create` : Indique que vous voulez créer un nouveau topic.
- `--bootstrap-server localhost:9092` : Adresse du serveur Kafka (dans le conteneur, utilisez `localhost` pour Kafka).
- `--replication-factor 1` : Nombre de répliques pour chaque partition du topic (ici, pas de répliques).
- `--partitions 1` : Nombre de partitions pour le topic.
- `--topic account-events` : Nom du topic à créer (par exemple, `account-events`).

---

### **3. Vérifiez si le topic a été créé**

Pour lister tous les topics disponibles :
```bash
kafka-topics --list --bootstrap-server localhost:9092
```

---

### **4. Produisez un message pour tester le topic**

Utilisez le **console producer** pour envoyer un message au topic :
```bash
kafka-console-producer --bootstrap-server localhost:9092 --topic account-events
```

Vous pouvez ensuite taper un message comme :
```plaintext
{"event":"ACCOUNT_DELETED","accountId":12345}
```
Appuyez sur **Entrée** pour l'envoyer.

---

### **5. Consommez un message pour vérifier**

Utilisez le **console consumer** pour lire les messages du topic :
```bash
kafka-console-consumer --bootstrap-server localhost:9092 --topic account-events --from-beginning
```

Cela affichera les messages envoyés au topic, par exemple :
```plaintext
{"event":"ACCOUNT_DELETED","accountId":12345}
```

---

### **6. Quittez le conteneur**

Pour quitter le conteneur Kafka, utilisez :
```bash
exit
```

---

### **7. Commandes récapitulatives**

1. Liste des conteneurs :
   ```bash
   docker ps
   ```

2. Accéder au conteneur Kafka :
   ```bash
   docker exec -it kafka bash
   ```

3. Créer un topic :
   ```bash
   kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic account-events
   ```

4. Lister les topics :
   ```bash
   kafka-topics --list --bootstrap-server localhost:9092
   ```

5. Produire un message :
   ```bash
   kafka-console-producer --bootstrap-server localhost:9092 --topic account-events
   ```

6. Consommer un message :
   ```bash
   kafka-console-consumer --bootstrap-server localhost:9092 --topic account-events --from-beginning
   ```

---

### **8. Notes supplémentaires**

- Si votre Kafka est exposé via un **réseau Docker**, le `--bootstrap-server` doit pointer vers l'adresse externe ou le nom de service dans le réseau Docker, par exemple `kafka:9092` au lieu de `localhost:9092`.
- Si des problèmes persistent, vérifiez les logs du conteneur Kafka :
  ```bash
  docker logs kafka
  ```

