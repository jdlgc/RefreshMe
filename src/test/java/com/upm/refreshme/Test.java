package com.upm.refreshme;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.mockito.stubbing.Answer;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.upm.refreshme.backend.Servlet;
import com.upm.refreshme.backend.WebPage;

public class Test extends Servlet {


	/*@org.junit.Test
	public void test() {
		Servlet s = mock(Servlet.class);
		CollectionReference cf = mock(CollectionReference.class);
		DocumentSnapshot dsn = mock(DocumentSnapshot.class);
		ApiFuture<QuerySnapshot> af = mock(ApiFuture.class);
		Firestore f = mock(Firestore.class);
		FirestoreClient fcl = mock(FirestoreClient.class);
		
		try {
			
			when(fcl.getFirestore().collection("users/1/web-pages/")).thenReturn(null);
			when(f.collection("users/1/web-pages/")).thenReturn(null);
			when(af.get()).thenReturn(null);
			when(dsn.getId()).thenReturn("0");
			when(dsn.getString("categoria")).thenReturn("user");
			when(dsn.getString("nombre")).thenReturn("usuario");
			when(dsn.getString("ultimosCambios")).thenReturn("");
			when(dsn.getString("url")).thenReturn("url");
			
			WebPage webpage = new WebPage("0","user","usuario","","url");
			
			s.UserWebPages.add(webpage);
			
			s.getClientPages();
			
			assertTrue(s.UserWebPages.size() > 0);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}*/

}
