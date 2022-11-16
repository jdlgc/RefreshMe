package com.upm.refreshme.backend;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Servlet {
	
	public static void main(String[] args) throws IOException{
		
		FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");
		
		FirebaseOptions options = FirebaseOptions.builder()
			    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
			    .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
			    .build();

			FirebaseApp.initializeApp(options);
			
			Firestore db = FirestoreClient.getFirestore();
			
	}
}
