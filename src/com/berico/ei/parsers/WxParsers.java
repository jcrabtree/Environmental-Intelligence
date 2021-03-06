package com.berico.ei.parsers;

import java.util.ArrayList;
import java.util.List;

import com.berico.ei.Observation;
import com.berico.ei.parsers.EncodedWxStringParseContext.ContextHandler;

public class WxParsers {

	private static List<EncodedWxElementParser> metarParserChain = setupParserChain();
	
	public static Observation parseMetar(String metarString){
		
		EncodedWxStringParseContext context = new EncodedWxStringParseContext(metarString);
		
		context.foreachElement(new ContextHandler(){

			public void handleElement(EncodedWxStringParseContext context) {
				
				for(EncodedWxElementParser parser : metarParserChain){
					
					if(parser.canParseCurrentElement(context)){
						
						try { 
							parser.performParse(context);
							
						} catch (EncodedWxElementParseException ex){
							
							ex.printStackTrace();
						}
					}
				}
			}
		});
		
		return context.getObservation();
	}


	private static List<EncodedWxElementParser> setupParserChain() {
		List<EncodedWxElementParser> parserChain = new ArrayList<EncodedWxElementParser>();
		
		parserChain.add(new ObservationTypeParser());
		parserChain.add(new StationIdentificationParser());
		parserChain.add(new ObservationTimeParser());
		parserChain.add(new WindsParser());
		parserChain.add(new PrevailingVisibilityParser());
		parserChain.add(new CloudLayerParser());
		parserChain.add(new TemperatureAndDewpointParser());
		parserChain.add(new AltimeterParser());
		parserChain.add(new SeaLevelPressureParser());
		parserChain.add(new TemperatureAndDewpointGroupParser());
		parserChain.add(new ThreeSixHourMaxMinTemperatureParser());
		parserChain.add(new TwentyFourHourMaxMinTemperatureParser());
		parserChain.add(new PrecipGroupParser());
		parserChain.add(new SnowDepthGroupParser());
		parserChain.add(new LiquidEquivalentOfSnowGroupParser());
		parserChain.add(new CloudClassificationGroupParser());
		parserChain.add(new PressureTendencyGroupParser());
		
		return parserChain;
	}
	
}
