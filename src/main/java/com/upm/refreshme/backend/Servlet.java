package com.upm.refreshme.backend;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Servlet {

	public static void main(String[] args) throws IOException{

		FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");

		FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.setDatabaseUrl("https://refreshme-8db78-default-rtdb.europe-west1.firebasedatabase.app/")
				.build();

		// Initialize the default app
		FirebaseApp defaultApp = FirebaseApp.initializeApp(options);


		System.out.println(defaultApp.getName());  // "[DEFAULT]"

		// Retrieve services by passing the defaultApp variable...
		FirebaseAuth defaultAuth = FirebaseAuth.getInstance(defaultApp);
		FirebaseDatabase defaultDatabase = FirebaseDatabase.getInstance(defaultApp);

	}
		
}
