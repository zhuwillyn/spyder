package spyder;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBTest {

	private MongoClient client;

	@Before
	public void init() {
		client = new MongoClient("localhost", 27017);
	}

	@Test
	public void testInsert() {
		MongoDatabase db = client.getDatabase("local");
		MongoCollection<Document> collection = db.getCollection("student");
		Document doc = new Document();
		doc.append("name", "zhuweilin");
		doc.append("age", 25);
		doc.append("email", "zhuweilin@datapark.cn");
		collection.insertOne(doc);
	}

	@Test
	public void test1() {
		MongoDatabase db = client.getDatabase("local");

		MongoCollection<Document> collection = db.getCollection("student");
		FindIterable<Document> findIterable = collection.find();
		MongoCursor<Document> iterator = findIterable.iterator();
		while (iterator.hasNext()) {
			Document document = iterator.next();
			System.out.println(document);
		}

	}

}
