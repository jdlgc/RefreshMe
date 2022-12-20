package com.upm.refreshme.backend;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;

public class Servlet {

	private static UserRecord userRecord;
	private static FirebaseAuth defaultAuth;
	private static FirebaseDatabase defaultDatabase;
	private static Firestore db;

	final static String rmEmail = "refreshmeapp@gmail.com";
	final static String token = "jprafjwywgsijwci";
	
	public Servlet() {
		db = initiateFirebase();
	}
	
	private static Firestore initiateFirebase() {
		FileInputStream serviceAccount;
		try {
			serviceAccount = new FileInputStream("./ServiceAccountKey.json");
			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://refreshme-8db78-default-rtdb.europe-west1.firebasedatabase.app/")
					.build();
			
			// Initialize the default app
			FirebaseApp defaultApp = FirebaseApp.initializeApp(options);
			
			
			System.out.println(defaultApp.getName());  // "[DEFAULT]"
			
			// Retrieve services by passing the defaultApp variable...
			defaultAuth = FirebaseAuth.getInstance(defaultApp);
			defaultDatabase = FirebaseDatabase.getInstance(defaultApp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return FirestoreClient.getFirestore();
	}

	private static void createUser(String userName, String email, String password) throws FirebaseAuthException {
		CreateRequest request = new CreateRequest()
				.setEmail(email)
				.setEmailVerified(false)
				.setPassword(password)
				.setDisplayName(userName)
				.setDisabled(false);

		userRecord = FirebaseAuth.getInstance().createUser(request);
		System.out.println("Successfully created new user: " + userRecord.getUid());
		sendEmailVerification(email, userName);
	}

	private static void sendEmailVerification(String email, String userName) {
		String link = "";
		try {

			link = FirebaseAuth.getInstance()
					.generateEmailVerificationLink(
							email);
			// Construct email verification template, embed the link and send
			// using custom SMTP server.
			//		  sendCustomEmail(email, userName, link);
			//		  sendCustomEmail(email, password);
			System.out.println("Este es el link: " + link);
		} catch (FirebaseAuthException e) {
			System.out.println("Error generating email link: " + e.getMessage());
		}



		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true"); //TLS

		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(rmEmail, token);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("refreshmeapp@gmail.com"));
			message.setRecipients(
					Message.RecipientType.TO,
					InternetAddress.parse(email)
					);
			message.setSubject("RefreshMe Authentication");
			message.setText("Hola " + userName + ",\r\n"
					+ "\r\n"
					+ "¡Bienvenido a RefreshMe!\r\n"
					+ "\r\n"
					+ "Para poder validar tu cuenta necesitamos que hagas click en el siguiente enlace:\r\n"
					+ link
					+ "\r\n"
					+ "\r\n"
					+ "Si no has solicitado crearte una cuenta, puedes ignorar este mensaje.\r\n"
					+ "\r\n"
					+ "Gracias,\r\n"
					+ "\r\n"
					+ "Tu equipo de RefreshMe");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	public static String getUser() {
		// TODO
		return "1";
	}
	
	public boolean addNewWebPage(WebPage webPage) {
		DocumentReference docRef = db.collection("users").document(getUser())
				.collection("web-pages").document(UUID.randomUUID().toString());

		Map<String, Object> data = new HashMap<>();
        data.put("nombre", webPage.getNombre());
        data.put("categoria", webPage.getCategoria());
        data.put("url", webPage.getUrl());
        data.put("ultimosCambios", webPage.getUltimosCambios());
        
        ApiFuture<WriteResult> result = docRef.set(data);

        try {
			System.out.println("Update time : " + result.get().getUpdateTime());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}
	
	public ArrayList<WebPage> getWebPagesList() {
		ArrayList<WebPage> webPages = new ArrayList<WebPage>();
		try {
			ApiFuture<QuerySnapshot> future = db.collection("users").document(getUser())
					.collection("web-pages").get();
			List<QueryDocumentSnapshot> documents = future.get().getDocuments();
			for (QueryDocumentSnapshot document : documents) {
				WebPage webPage = new WebPage(document.getData().get("nombre").toString(), 
						document.getData().get("url").toString(), 
						document.getData().get("categoria").toString(), 
						document.getData().get("ultimosCambios").toString());
				webPages.add(webPage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return webPages;
	}


}
