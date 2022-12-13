package com.upm.refreshme.backend;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.auth.UserRecord.CreateRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Servlet {

	private static UserRecord userRecord;
	private static FirebaseAuth defaultAuth;
	private static FirebaseDatabase defaultDatabase;
	private static DatabaseReference databaseReference;
	private static ArrayList<WebPage> userWebPages = new ArrayList<WebPage>();

	final static String rmEmail = "refreshmeapp@gmail.com";
	final static String token = "jprafjwywgsijwci";

	public static void main(String[] args) throws IOException{
		firebaseConnection();
		String password = "123456";
		
		if (password.length() < 6) {
			System.out.println("Por favor, introduzca  una contraseña de al menos 6 caracteres.");
		} else {
			try {
				createUser("John Doe", "jorcordd@gmail.com", password);
			} catch (FirebaseAuthException e) {
				switch(e.getMessage()) {
				case "The user with the provided email already exists (EMAIL_EXISTS).":
					System.out.println("Correo ya registrado, por favor, introduzca un correo no existente en RefreshMe.");
					break;
				default:
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	private static void firebaseConnection() throws IOException {
		FileInputStream serviceAccount = new FileInputStream("./ServiceAccountKey.json");

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
	}
	
	private static void setUserDatabaseReference() {
		databaseReference = defaultDatabase.getReference("users/" + getUser() + "/web-pages");
		getData();
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
	
	private static String getUser() {
		// TODO
		return "1";
	}
	
	private static void getData() {
		System.out.println("getData(): userWebPages:" + userWebPages);
		databaseReference.orderByValue().addChildEventListener(new ChildEventListener() {
				@Override
				public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
					System.out.println("The " + dataSnapshot.getKey() + " web page is " + dataSnapshot.getValue() + dataSnapshot.getValue().getClass());
					@SuppressWarnings("unchecked")
					WebPage webPage = new WebPage(
							((HashMap<String, String>)dataSnapshot.getValue()).get("nombre"),
							((HashMap<String, String>)dataSnapshot.getValue()).get("url"),
							((HashMap<String, String>)dataSnapshot.getValue()).get("categoria"),
							new Date() // FIX
					);
					System.out.println("web page: " + webPage);
					userWebPages.add(webPage);
				}

				@Override
				public void onChildChanged(DataSnapshot snapshot, String previousChildName) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onChildRemoved(DataSnapshot snapshot) {
					// TODO Auto-generated method stub
				}
	
				@Override
				public void onChildMoved(DataSnapshot snapshot, String previousChildName) {
					// TODO Auto-generated method stub
					
				}
	
				@Override
				public void onCancelled(DatabaseError error) {
					// TODO Auto-generated method stub
					
				}
			});

	}

	public static void saveNewWebPage(WebPage webPage) throws IOException { 
		if (defaultDatabase == null)
			firebaseConnection();
		setUserDatabaseReference();
		DatabaseReference reference = defaultDatabase.getReference("users");
		DatabaseReference userUrls = reference.child(getUser()).child("web-pages");
		userUrls.push().setValue(webPage, null);
	}

	public static ArrayList<WebPage> getWebPages() throws IOException { 
		if (defaultDatabase == null)
			firebaseConnection();
		setUserDatabaseReference();		
		return userWebPages;
	}

}
