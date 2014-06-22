package com.lge.warehouse.common.test;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lge.warehouse.util.WidgetInfo;

public class WidgetInfoTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		HashMap<WidgetInfo, Integer> map = new HashMap<WidgetInfo, Integer>();
		WidgetInfo w1 = new WidgetInfo(1, "Widget1",1000);
		WidgetInfo w2 = new WidgetInfo(1, "Widget1",1000);
		WidgetInfo w3 = new WidgetInfo(1, "Widget1", 500);
		
		map.put(w1,100);
		map.put(w2,300);
		map.put(w3, 1000);
		assertEquals(map.size(), 2);
		assertEquals(w1, w2);
		assertEquals((int)map.get(w1), 300);
	}

}
