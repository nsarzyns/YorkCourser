package edu.ycp.cs320.sme.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ycp.cs320.sme.model.User;

public class UserTest {
	@Test
	public void UserIDtest(){
		User testUser = new User();
		for(int i=0;i<1000;i++){
			int p =testUser.generateUniqueID();
			System.out.println(p);
			assertEquals(p > 0,true);
			assertEquals(p < 1000000000,true);
		}
	}
}
