package com.talesofdertinia.ToDWhitelist.db;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.Test;

public class MySqlDatabaseTest {
	
	@Test
	public void test_format() {
		String s = MySqlDatabase.format(UUID.fromString("a3192ee8-4577-4261-af62-fc118d00a20d"));
		assertEquals("a3192ee845774261af62fc118d00a20d", s);
	}
	
	@Test
	public void test_deformat() {
		UUID uuid = MySqlDatabase.deformat("a3192ee845774261af62fc118d00a20d");
		assertEquals(UUID.fromString("a3192ee8-4577-4261-af62-fc118d00a20d"), uuid);
	}

}
