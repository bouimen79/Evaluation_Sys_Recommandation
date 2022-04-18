package Data_Split;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Connection_Mongodb {
  public static MongoDatabase database;

    public static  void connection_mongo(String Base_Name){
        Logger logger = Logger.getLogger("org.mongodb.driver");
        logger.setLevel(Level.SEVERE);
        //  Connexion avec Mongod
        MongoClient mongo=new MongoClient("Localhost",27017);
        // Accéder a la base de Donnée
        database = mongo.getDatabase(Base_Name);


    }


    public static void ADD_Document_Item( String domain,
                                            String Created_at, long Item_Id, String recom,
                                            String Url, String title, String category, String text){
        //Preparing a document
        //Domain,CreatedAt,ItemID,Recommendable,URL,Title,category,text
        Document document = new Document();

        document.append("Domain", domain);
        document.append("CreatedAt",Created_at );
        document.append("ItemID",Item_Id);
        document.append("Recommendable",recom);
        document.append("URL",Url);
        document.append("Title",title);
        document.append("category",category);
        document.append("text",text);
        //Inserting the document into the collection
        database.getCollection("Item").insertOne(document);
        // System.out.println("Document inserted successfully");
    }
    public static void ADD_Document_Event(long Id_event, String actiontype, long item_id_event, long user_id_event, String time_event, long geoUser, long deviceType){
        //Preparing a document
        //Id_event,actionType,ItemID,UserId,TimeStamp,geoUser,deviceType
        Document document = new Document();
        document.append("Id_event",Id_event);
        document.append("actionType", actiontype);
        document.append("ItemID",item_id_event);
        document.append("UserId",user_id_event);
        document.append("TimeStamp",time_event);
        document.append("geoUser",geoUser);
        document.append("deviceType",deviceType);
        //Inserting the document into the collection
        database.getCollection("Event").insertOne(document);
        //System.out.println("Document inserted successfully Event");

    }


    public static Document Get_Document(MongoDatabase database, String Id_Name,long Id_ , String Collection_Name){
        try {
            MongoCollection<Document> collection_Item = database.getCollection(Collection_Name);
            //System.out.println("Collection myCollection selected successfully");
            BasicDBObject Query = new BasicDBObject();
            Query.put(Id_Name, Id_);
            MongoCursor<Document> cursor = collection_Item.find(Query).iterator();
            while (cursor.hasNext()) {
                return cursor.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
