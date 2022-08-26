package scraper.db;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

/** Examples of references to a collection, document in a collection and subcollection. */
public class References {

    private final Firestore db;

    public References(Firestore db) {
        this.db = db;
    }

    /**
     * Return a reference to collection.
     *
     * @return collection reference
     */
    public CollectionReference getCollectionRef() {
        // [START fs_collection_ref]
        // [START firestore_data_reference_collection]
        // Reference to the collection "users"
        CollectionReference collection = db.collection("bitcoin");
        // [END firestore_data_reference_collection]
        // [END fs_collection_ref]
        return collection;
    }

    /**
     * Return a reference to a document.
     *
     * @return document reference
     */
    public DocumentReference getDocumentRef() {
        // [START fs_document_ref]
        // [START firestore_data_reference_document]
        // Reference to a document with id "alovelace" in the collection "users"
        DocumentReference document = db.collection("bitcoin").document("testing1");
        // [END firestore_data_reference_document]
        // [END fs_document_ref]
        return document;
    }

    /**
     * Return a reference to a document using path.
     *
     * @return document reference
     */
    public DocumentReference getDocumentRefUsingPath() {
        // [START fs_document_path_ref]
        // [START firestore_data_reference_document_path]
        // Reference to a document with id "alovelace" in the collection "users"
        DocumentReference document = db.document("bitcoin/testing1");
        // [END firestore_data_reference_document_path]
        // [END fs_document_path_ref]
        return document;
    }

    /**
     * Return a reference to a document in a sub-collection.
     *
     * @return document reference in a subcollection
     */
//    public DocumentReference getSubCollectionDocumentRef() {
//        // [START fs_subcollection_ref]
//        // [START firestore_data_reference_subcollection]
//        // Reference to a document in subcollection "messages"
//        DocumentReference document =
//                db.collection("rooms").document("roomA").collection("messages").document("message1");
//        // [END firestore_data_reference_subcollection]
//        // [END fs_subcollection_ref]
//        return document;
//    }
}