package com.berico.ei.parsers.tests;

import static com.berico.ei.ConversionUtils.toC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.berico.ei.parsers.EncodedWxElementParser;
import com.berico.ei.parsers.EncodedWxStringParseContext;
import com.berico.ei.parsers.TemperatureAndDewpointParser;

public class TemperatureAndDewpointParserTest extends
		EncodedWxElementParserBaseTestCase {

	@Override
	protected EncodedWxElementParser createParserInstance() {
		
		return new TemperatureAndDewpointParser();
	}

	@Test
	public void temperature_and_dewpoint_element_is_correctly_identified_by_parser(){
		
		assertTrue(
				getParser()
					.canParseCurrentElement(
							this.createContext("10/01")));
		
		assertTrue(
				getParser()
					.canParseCurrentElement(
							this.createContext("10/M01")));
		
		assertTrue(
				getParser()
					.canParseCurrentElement(
							this.createContext("M10/M11")));
		
		assertFalse(
				getParser()
					.canParseCurrentElement(
							this.createContext("1010")));
		
		assertFalse(
				getParser()
					.canParseCurrentElement(
							this.createContext("M123")));
	}
	
	public void assertTempAndDewpoint(double expectedTemp, double expectedDewpoint, String encodedElement){
		
		EncodedWxStringParseContext context = assertParse(encodedElement);
		
		assertEquals(expectedTemp, 
				toC(context
					.getObservation()
					.getTemperatures()
					.getAmbientAirTemperature()), 0d);
		
		assertEquals(expectedDewpoint, 
				toC(context
					.getObservation()
					.getTemperatures()
					.getDewpoint()), 0d);
	}
	
	@Test
	public void parser_correctly_parses_non_negative_temp_and_dewpoint(){
		
		assertTempAndDewpoint(10d, 1d, "10/01");
	}

	@Test
	public void parser_correctly_parses_non_negative_temp_and_a_negative_dewpoint(){
		
		assertTempAndDewpoint(5d, -1d, "05/M01");
	}
	
	@Test
	public void parser_correctly_parses_negative_temp_and_dewpoint(){
		
		assertTempAndDewpoint(-3d, -16d, "M03/M16");
	}
	
}
