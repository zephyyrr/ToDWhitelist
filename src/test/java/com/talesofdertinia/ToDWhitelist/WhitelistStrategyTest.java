package com.talesofdertinia.ToDWhitelist;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.TestCase;

import org.junit.Test;

public class WhitelistStrategyTest extends TestCase {
	
	Strategy ws;
	
	@Override
	public void setUp() {
		ws = new WhitelistStrategy();
	}

	@Test
	public void test_isAllowed_whitelisted() {
		User u = new User(UUID.randomUUID(), true, false);
		
		if (!ws.isAllowed(u)) {
			fail("whitelisted user should be allowed.");
		}
	}
	
	@Test
	public void test_isAllowed_blacklisted() {
		User u = new User(UUID.randomUUID(), false, true);
		
		if (ws.isAllowed(u)) {
			fail("Not whitelisted user should not be allowed.");
		}
	}
	
	@Test
	public void test_isAllowed_blackwhitelisted() {
		User u = new User(UUID.randomUUID(), true, true);
		
		if (!ws.isAllowed(u)) {
			fail("whitelisted user should be allowed.");
		}
	}
	
	@Test
	public void test_isAllowed_neither() {
		User u = new User(UUID.randomUUID(), false, false);
		
		if (ws.isAllowed(u)) {
			fail("Not whitelisted user should not be allowed.");
		}
	}

}
